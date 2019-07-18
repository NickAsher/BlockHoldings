package apps.yoo.com.blockholdings.ui.home;

import android.app.Activity;

import androidx.constraintlayout.solver.widgets.Helper;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.data.models.Object_TransactionGroup;
import apps.yoo.com.blockholdings.ui.portfolio.Fragment_PortfolioBrief;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.ui.transaction.Activity_Transaction3;
import apps.yoo.com.blockholdings.ui.watchlist.Activity_WatchlistContainer;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.Utils;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;


public class Activity_Home extends AppCompatActivity{
    Context context ;
    String LOG_TAG = "Activity_Home --> " ;
    AppDatabase db;
    FragmentManager fragmentManager ;


    BottomNavigationView btmNavigationView ;
    RecyclerView rv ;
    RVAdapter_TransactionsExpandable adapter_transactionsExpandable ;
    TextView textView_NameSort, textView_PercentageSort, textView_HoldingsSort, textView_SingleCoinPriceSort ;
    RelativeLayout layout_MainContainer ;
    Fragment_PortfolioBrief fragment_portfolioBrief ;
    RadioRealButtonGroup radioGrp_PriceChange ;
    Map<String, BigDecimal> holdingsChangeValueSet_TotalDollarDifference;
    Table<String, Integer, BigDecimal> table_PriceChange_TotalDollarDifference ;

    Object_Portfolio currentPortfolioObj ;
    RVAdapter_Transactions adapter ;
    List<Object_TransactionFullData> listOfAllTransaction_Unsummed;
    List<Object_TransactionFullData>  listOfPortfolioTransactions_Unsummed, listOfPortfolioTransactions_Summed, listOfPortfolioTransactions ;
    ArrayList<Object_TransactionGroup> listOfTransactionGroups ;
    List<Object_Portfolio> listOfPortfolios ;
    Object_Currency currentCurrency ;
    String string_NewDataURL ;
    final int INTENT_REQUEST_CODE_ADD_COIN = 1 ;
    boolean initialLoad = true ;
    final int SORT_NAME_ASC = 1 ;
    final int SORT_NAME_DESC = 2 ;
    final int SORT_COINPRICE_ASC = 3 ;
    final int SORT_COINPRICE_DESC = 4 ;
    final int SORT_HOLDINGS_ASC = 5 ;
    final int SORT_HOLDINGS_DESC = 6 ;
    final int SORT_CHANGE_ASC = 7 ;
    final int SORT_CHANGE_DESC = 8 ;

    int currentSortMode  = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        fragmentManager = getSupportFragmentManager() ;

        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()) ;
        currentPortfolioObj = db.portfolioDao().getPortfolioById(MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext())) ;
        listOfPortfolios = db.portfolioDao().getListOfAllPortfolios() ;

        listOfPortfolioTransactions = new ArrayList<>() ;
        listOfTransactionGroups = new ArrayList<>() ;
        listOfAllTransaction_Unsummed = new ArrayList<>() ;
        listOfPortfolioTransactions_Unsummed = new ArrayList<>() ;
        listOfPortfolioTransactions_Summed = new ArrayList<>() ;

        holdingsChangeValueSet_TotalDollarDifference = new HashMap<>() ;
        table_PriceChange_TotalDollarDifference = HashBasedTable.create() ;

        getReferences();
        layout_MainContainer.setBackground(MySharedPreferences.getAppThemeGradientDrawableOnPreference(getApplicationContext()));

        setupBottomNavigationView();
        setupNewListOfTransactions() ;
        setupRecyclerView();
        setupBasicUI() ;
        setupRadioBtn_PriceDifference_ChangeListener() ;

        textView_HoldingsSort.performClick() ; // this will sort the items by holdings

    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activityHome_BottomNavigationView_Main) ;
        rv = findViewById(R.id.activityPortfolio_RecyclerView_transactions) ;
        layout_MainContainer= findViewById(R.id.activityPortfolio_RelLt_MainContainer) ;
        fragment_portfolioBrief = (Fragment_PortfolioBrief) fragmentManager.findFragmentById(R.id.activityPortfolio_Fragment_PortfolioBriefInfo) ;

        textView_NameSort = findViewById(R.id.activityPortfolio_TextView_SortName) ;
        textView_SingleCoinPriceSort = findViewById(R.id.activityPortfolio_TextView_SortSingleCoinPrice) ;
        textView_PercentageSort = findViewById(R.id.activityPortfolio_TextView_SortPercentage) ;
        textView_HoldingsSort = findViewById(R.id.activityPortfolio_TextView_SortTotalHoldings) ;

        radioGrp_PriceChange = findViewById(R.id.activityHome_RadioGroup_PriceChange) ;

    }

    private void setupBottomNavigationView(){
        btmNavigationView.setSelectedItemId(R.id.menuBottomNavigation_Item_Home);

        btmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuBottomNavigation_Item_Home :
                        break;


                    case R.id.menuBottomNavigation_Item_Watchlist :
                        Intent intentW = new Intent(context, Activity_WatchlistContainer.class) ;
                        startActivity(intentW);
                        finish();
                        break;

                    case R.id.menuBottomNavigation_Item_News :
                        Intent intent2 = new Intent(context, Activity_News.class) ;
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.menuBottomNavigation_Item_Settings :
                        Intent intent3 = new Intent(context, Activity_Settings.class) ;
                        startActivity(intent3);
                        finish();
                        break;

                    default:
                        Intent intent = new Intent(context, Activity_Home.class) ;
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }


    private void setupBasicUI(){
        FloatingActionButton fab_AddCoin = findViewById(R.id.activityHome_FAB_AddCoin) ;
        fab_AddCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_CoinSelector.class) ;
                startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_COIN);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE_ADD_COIN) {
            if(resultCode == Activity.RESULT_OK){
                String coinId=data.getStringExtra("coinId");


//                Intent intent = new Intent(context, Activity_Transaction2.class) ;
//                intent.putExtra("coin", db.coinDao().getCoinById(coinId).toJson().toString()) ;
                Intent intent = new Intent(context, Activity_Transaction3.class) ;

                intent.putExtra("coinId", coinId) ;

                context.startActivity(intent);
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private void setupNewListOfTransactions(){
        //This method is called when the activity is created
        // And it is also called when a new transaction is added by the user

        // So we get the list of transactions through live data and set a observer
        // Now in this observer we have two cases
        //

        // When the activity is created we simply get the list of transactions through a live data
        // and set the adapter


        db.transactionDao().getListOfAllTransactionFD_OfPortfolio_LiveData(MyGlobals.getCurrentPortfolioObj().getPortfolioId()).observe(this, new Observer<List<Object_TransactionFullData>>() {
            @Override
            public void onChanged(List<Object_TransactionFullData> newList) {

                if(initialLoad){
                    // It means that activity is just created. So we simply setup the list
                    // And also we get the fresh data from the server
                    // NOTE : whenever we get the fresh data from the server, we also reload the list again

                    Log.d(LOG_TAG, "Inside the setupNewListOfTransactions->InitialLoad") ;
                    listOfPortfolioTransactions = newList ;
                    listOfTransactionGroups = Helper_Home.getListOfTransactionGroups(listOfPortfolioTransactions) ;
                    refreshPriceCurrencyChange(Constants.TIMEFRAME_MAX);
                    adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                    initialLoad = false ;
                    getCurrentCoinPrices_FromServer();
                    return;
                }


                // Now is the case when initial Load has been finished (Activity has been created
                // The following code is called when either a new transaction is inserted or we are just refreshing values from adapter
                if(listOfPortfolioTransactions.size() == newList.size()){
                    //Case 1 : Data is just refreshed from server
                    // Simply show the updated data in our Recycler view
                    listOfPortfolioTransactions = newList ;
                    listOfTransactionGroups = Helper_Home.getListOfTransactionGroups(listOfPortfolioTransactions) ;
                    refreshPriceCurrencyChange(Constants.TIMEFRAME_MAX);
                    radioGrp_PriceChange.setPosition(4);
                    adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                    return;
                } else {

                    // Case 2 : A new Transaction is inserted
                    // In this case, we simply refresh the data from server
                    // This method will be called again by getCurrentCoinPrices_FromServer method
                    // and case 1 will be triggered again which will simply show updated data in recyclerView
                    listOfPortfolioTransactions = newList ;
                    getCurrentCoinPrices_FromServer();
                    return;
                }




            }
        });
    }


    private void setupRecyclerView(){

        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        adapter_transactionsExpandable = new RVAdapter_TransactionsExpandable(context, listOfTransactionGroups, expMgr) ;

        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(expMgr.createWrappedAdapter(adapter_transactionsExpandable));
        expMgr.attachRecyclerView(rv);

        DividerItemDecoration plainHorizontalLines = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL) ;
        rv.addItemDecoration(plainHorizontalLines);



    }






    public void setupSortingTextViewsNew(View v){
        int newSortMode = 0 ;
        switch (v.getId()){
            case R.id.activityPortfolio_TextView_SortName :
                if(currentSortMode == SORT_NAME_ASC){
                    newSortMode = SORT_NAME_DESC ;
                } else if(currentSortMode == SORT_NAME_DESC){
                    newSortMode = SORT_NAME_ASC ;
                } else {
                    newSortMode = SORT_NAME_ASC ;
                }
                break;


            case R.id.activityPortfolio_TextView_SortSingleCoinPrice :
                if(currentSortMode == SORT_COINPRICE_ASC){
                    newSortMode = SORT_COINPRICE_DESC ;
                } else if(currentSortMode == SORT_COINPRICE_DESC){
                    newSortMode = SORT_COINPRICE_ASC ;
                } else {
                    newSortMode = SORT_COINPRICE_ASC ;
                }
                break;



            case R.id.activityPortfolio_TextView_SortTotalHoldings :
                if(currentSortMode == SORT_HOLDINGS_ASC){
                    newSortMode = SORT_HOLDINGS_DESC ;
                } else if(currentSortMode == SORT_HOLDINGS_DESC){
                    newSortMode = SORT_HOLDINGS_ASC ;
                } else {
                    newSortMode = SORT_HOLDINGS_ASC ;
                }
                break;



            case R.id.activityPortfolio_TextView_SortPercentage :
                if(currentSortMode == SORT_CHANGE_ASC){
                    newSortMode = SORT_CHANGE_DESC ;
                } else if(currentSortMode == SORT_CHANGE_DESC){
                    newSortMode = SORT_CHANGE_ASC ;
                } else {
                    newSortMode = SORT_CHANGE_ASC ;
                }
                break;


        }

        textView_NameSort.setText("Name");
        textView_SingleCoinPriceSort.setText("CoinPrice");
        textView_HoldingsSort.setText("Holdings");
        textView_PercentageSort.setText("Change");



        switch (newSortMode){
            case SORT_NAME_ASC :
                textView_NameSort.append(" \u2191");
                listOfTransactionGroups.sort(Object_TransactionGroup.NameComparator);
//                Collections.reverse(list);
                break;
            case SORT_NAME_DESC :
                textView_NameSort.append(" \u2193");
                listOfTransactionGroups.sort(Object_TransactionGroup.NameComparator);
                Collections.reverse(listOfTransactionGroups); // it is opposite thing for names
                break;


            case SORT_COINPRICE_ASC :
                textView_SingleCoinPriceSort.append(" \u2191");
                listOfTransactionGroups.sort(Object_TransactionGroup.singleCoinPrice);
                Collections.reverse(listOfTransactionGroups);
                break;
            case SORT_COINPRICE_DESC :
                textView_SingleCoinPriceSort.append(" \u2193");
                listOfTransactionGroups.sort(Object_TransactionGroup.singleCoinPrice);
                break;

            case SORT_HOLDINGS_ASC :
                textView_HoldingsSort.append(" \u2191");
                listOfTransactionGroups.sort(Object_TransactionGroup.HoldingComparator);
                Collections.reverse(listOfTransactionGroups);
                break;
            case SORT_HOLDINGS_DESC :
                textView_HoldingsSort.append(" \u2193");
                listOfTransactionGroups.sort(Object_TransactionGroup.HoldingComparator);
                break;

            case SORT_CHANGE_ASC:
                textView_PercentageSort.append(" \u2191");
                Collections.sort(listOfTransactionGroups, new Comparator<Object_TransactionGroup>() {
                    @Override
                    public int compare(Object_TransactionGroup o1, Object_TransactionGroup o2) {
                        return o1.getSummedPriceChange().compareTo(o2.getSummedPriceChange()) ;
                    }
                });
                Collections.reverse(listOfTransactionGroups);
                break;
            case SORT_CHANGE_DESC :
                textView_PercentageSort.append(" \u2193");
                Collections.sort(listOfTransactionGroups, new Comparator<Object_TransactionGroup>() {
                    @Override
                    public int compare(Object_TransactionGroup o1, Object_TransactionGroup o2) {
                        return o1.getSummedPriceChange().compareTo(o2.getSummedPriceChange()) ;
                    }
                });
                break;

        }

        adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);

        currentSortMode = newSortMode ;
        //TODO save the sort method in shared preferences


    }


    public void setupRadioBtn_PriceDifference_ChangeListener(){
        radioGrp_PriceChange.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                switch (button.getId()){
                    case R.id.activityHome_RadioBtn_1DayChange :
                        refreshPriceCurrencyChange(Constants.TIMEFRAME_1DAY);
                        adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                        break;

                    case R.id.activityHome_RadioBtn_1WeekChange :
                        refreshPriceCurrencyChange(Constants.TIMEFRAME_1WEEK);
                        adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                        break;

                    case R.id.activityHome_RadioBtn_1MonthChange :
                        refreshPriceCurrencyChange(Constants.TIMEFRAME_1MONTH);
                        adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                        break;

                    case R.id.activityHome_RadioBtn_MaxChange :
                        refreshPriceCurrencyChange(Constants.TIMEFRAME_MAX);
                        adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                        break;


                }
            }
        });


    }






    private void refreshPriceCurrencyChange(int caseTimeAgo){
        for(Object_TransactionGroup trxGrp : listOfTransactionGroups){
            BigDecimal summedChange = new BigDecimal(0) ;

            for(Object_TransactionFullData childTransactionFD : trxGrp.getListOfChildTransactionsFD()){
                String priceTimeAgo = Object_Coin.getPriceOfCoin_FromPriceLog_TimeAgo(childTransactionFD.getTransactionObject(), childTransactionFD.getCoinObject(), caseTimeAgo) ;
                BigDecimal priceChange = new BigDecimal(childTransactionFD.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()).subtract(new BigDecimal(priceTimeAgo)) ;
                BigDecimal totalPriceChange = priceChange.multiply(new BigDecimal(childTransactionFD.getTransactionObject().getNoOfCoins())) ;

                trxGrp.getMapOfPriceChange().put(childTransactionFD.getTransactionObject().getTransactionNo(), totalPriceChange) ;
                summedChange = summedChange.add(totalPriceChange) ;

            }

            trxGrp.setSummedPriceChange(summedChange);
            Log.d(LOG_TAG, trxGrp.toString()) ;
        }
    }



    private void getCurrentCoinPrices_FromServer(){
//        Log.e(LOG_TAG, listOfAllTransaction_Unsummed.toString()) ;  receiving the 300 noOfCoins ;
        if (listOfPortfolioTransactions.size() == 0){
            Log.e(LOG_TAG, "Size of listOfPortfolioTransactions = 0") ;
            return;
        }

        string_NewDataURL = Constants.URL_APICALL_SIMPLEPRICES ;

        for (Object_TransactionFullData object_transactionFullData : listOfPortfolioTransactions){
            string_NewDataURL  = string_NewDataURL + object_transactionFullData.getCoinObject().getId() + "," ;
        }
        string_NewDataURL = string_NewDataURL.substring(0, string_NewDataURL.length() - 1); // removing the last comma
        // add the &vs_currencies query to url  :    &vs_currencies=usd
        string_NewDataURL = string_NewDataURL + "&vs_currencies=" + MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()).getCurrencyId() ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, string_NewDataURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(LOG_TAG, response) ;
                        processFreshDataFromServer(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in making volley request");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        }) ;

        RequestQueue requestQueue = Volley.newRequestQueue(context) ;
        requestQueue.add(stringRequest) ;
    }

    private void processFreshDataFromServer(String response){
//        Log.e(LOG_TAG, listOfAllTransaction_Unsummed.toString()) ;

        try{
            JSONObject responseObject = new JSONObject(response) ;

            for (Object_TransactionFullData object_transactionFullData : listOfAllTransaction_Unsummed ){
                int txnId = object_transactionFullData.getTransactionObject().getTransactionNo() ;
                String coinId = object_transactionFullData.getCoinObject().getId() ;
                String newCoinPrice_Currency = responseObject.getJSONObject(coinId).getString(currentCurrency.getCurrencyId()) ;
                BigDecimal noOfCoins = new BigDecimal(Utils.removeMinusSign(object_transactionFullData.getTransactionObject().getNoOfCoins())) ;
                String newTotalCost = new BigDecimal(newCoinPrice_Currency).multiply(noOfCoins).toPlainString() ;

//                Log.e(LOG_TAG,  " CoinId " + coinId + " SinglePriceCurrency " + newCoinPrice_Currency + " noOfCoins " + object_transactionFullData.getTransactionObject().getNoOfCoins() + "  total cost  " + newTotalCost) ;
                object_transactionFullData.getTransactionObject().setSingleCoinPrice_CurrencyCurrent(newCoinPrice_Currency);
                Object_Coin.addToPriceData(db, coinId, newCoinPrice_Currency, Calendar.getInstance().getTimeInMillis() , "MainActivity");
                object_transactionFullData.getTransactionObject().setTotalValue_Current(newTotalCost);
                // not doing the following work in app executor thread because this is in a loop
                // so a number of threads will be created if we do this
                db.transactionDao().updateTransactionPriceByTxnId(txnId, newCoinPrice_Currency, newTotalCost);
            }

            MyGlobals.refreshPortfolioValue(db);
            // the following line basically refreshes the Fragment_PortfolioBrief.
            // SO rather than making a new method to refresh the damn thing, i just call it
            fragment_portfolioBrief.refreshPortfolio();
//            adapter.refreshData(listOfTransactions);
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }









    @Override
    protected void onResume() {
        super.onResume();
//        listOfTransactions = db.transactionDao().getListOfAllTransaction_FullData_Summed(MyGlobals.getCurrentPortfolioObj().getPortfolioId()) ;
//        setupPreferenceSorting_OfList() ;
//        adapter.refreshData(listOfTransactions);
    }
}
