package apps.yoo.com.blockholdings.ui.home;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Collections2;

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
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.ui.home.portfolio.DialogFragment_Portfolio;
import apps.yoo.com.blockholdings.ui.home.portfolio.DialogFragment_UpdateLog;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.ui.transaction.Activity_Transaction2;
import apps.yoo.com.blockholdings.ui.watchlist.Activity_WatchlistContainer;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;
import apps.yoo.com.blockholdings.util.Utils;


public class Activity_Portfolio extends AppCompatActivity implements MyListener.DialogFragmentPortfolios_to_ActivityPortfolio{
    Context context ;
    String LOG_TAG = "Activity_Portfolio --> " ;
    AppDatabase db;
    FragmentManager fragmentManager ;


    BottomNavigationView btmNavigationView ;
    RecyclerView rv ;
    TextView textView_NameSort, textView_PercentageSort, textView_HoldingsSort, textView_SingleCoinPriceSort,
            textView_Holding24HSort, textView_Holding7DSort, textView_HoldingMaxSort, textView_PortfolioName, textView_PortfolioBalance ;
    RelativeLayout layout_MainContainer ;
    Button btn_PortfolioChange ;

    Map<String, BigDecimal> holdingsChangeValueSet ;

    Object_Portfolio currentPortfolioObj ;
    RVAdapter_Transactions adapter ;
    List<Object_TransactionFullData> listOfTransactions, listOfAllTransaction_Unsummed, listOfPortfolioTransactions_Unsummed, listOfPortfolioTransactions_Summed ;
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

    final int SORT_CHANGE_1D_ASC = 11 ;
    final int SORT_CHANGE_1D_DESC = 12 ;
    final int SORT_CHANGE_7D_ASC = 13 ;
    final int SORT_CHANGE_7D_DESC = 14 ;
    final int SORT_CHANGE_MAX_ASC = 15 ;
    final int SORT_CHANGE_MAX_DESC = 16 ;
    int currentSortMode  = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        fragmentManager = getSupportFragmentManager() ;

        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()) ;
        listOfPortfolios = db.portfolioDao().getListOfAllPortfolios() ;
        listOfTransactions = new ArrayList<>() ;

        listOfAllTransaction_Unsummed = new ArrayList<>() ;
        listOfPortfolioTransactions_Unsummed = new ArrayList<>() ;
        listOfPortfolioTransactions_Summed = new ArrayList<>() ;

        holdingsChangeValueSet = new HashMap<>() ;
        getReferences();
        layout_MainContainer.setBackground(MySharedPreferences.getAppThemeGradientDrawableOnPreference(getApplicationContext()));

        setupBottomNavigationView();
        setupListOfTransactions();
        setupRecyclerView();
        setupBasicUI() ;
//        setupSortingTextViews() ;
//        getFreshDataFromServer() ;
        setupPortfolioDialogFragment();
        setPortfolioUpdateLog() ;

    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activityHome_BottomNavigationView_Main) ;
        rv = findViewById(R.id.activityPortfolio_RecyclerView_transactions) ;
        layout_MainContainer= findViewById(R.id.activityPortfolio_RelLt_MainContainer) ;
        textView_PortfolioName = findViewById(R.id.activityPortfolio_TextView_PortfolioName) ;
        textView_PortfolioBalance= findViewById(R.id.activityPortfolio_TextView_PortfolioBalance) ;

        textView_NameSort = findViewById(R.id.activityPortfolio_TextView_SortName) ;
        textView_SingleCoinPriceSort = findViewById(R.id.activityPortfolio_TextView_SortSingleCoinPrice) ;
        textView_PercentageSort = findViewById(R.id.activityPortfolio_TextView_SortPercentage) ;
        textView_HoldingsSort = findViewById(R.id.activityPortfolio_TextView_SortTotalHoldings) ;

        textView_Holding24HSort = findViewById(R.id.activityPortfolio_TextView_SortChange24H) ;
        textView_Holding7DSort = findViewById(R.id.activityPortfolio_TextView_SortChange7D) ;
        textView_HoldingMaxSort = findViewById(R.id.activityPortfolio_TextView_SortChangeAll) ;

        btn_PortfolioChange = findViewById(R.id.activityPortfolio_Btn_PortfolioChange) ;
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
                        Intent intent = new Intent(context, Activity_Portfolio.class) ;
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }


    private void setupBasicUI(){
        setupPortfolioTextViews();
        FloatingActionButton fab_AddCoin = findViewById(R.id.activityHome_FAB_AddCoin) ;
        fab_AddCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_CoinSelector.class) ;
                startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_COIN);
            }
        });
    }

    private void setupPortfolioTextViews(){
        textView_PortfolioName.setText(MyGlobals.getCurrentPortfolioObj().getPortfolioName());
        textView_PortfolioBalance.setText(Utils.showHumanDecimals(MyGlobals.getCurrentPortfolioObj().getPortfolioValue()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE_ADD_COIN) {
            if(resultCode == Activity.RESULT_OK){
                String coinId=data.getStringExtra("coinId");

                Intent intent = new Intent(context, Activity_Transaction2.class) ;
                intent.putExtra("coin", db.coinDao().getCoinById(coinId).toJson().toString()) ;
                context.startActivity(intent);
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void setupListOfTransactions(){
        db.transactionDao().getListOfAllTransaction_FullData_All().observe(this, new Observer<List<Object_TransactionFullData>>() {
            @Override
            public void onChanged(@Nullable List<Object_TransactionFullData> list) {
                Log.e(LOG_TAG, "Inside the live data onChangedMethod") ;
//                Log.e(LOG_TAG, list.toString()) ;

                if(listOfAllTransaction_Unsummed.size() == list.size()){
                    // there hasn't been any new insert
                    // It means the data is just updated from the server
                    // 1. Just refresh the adapter
                    Log.e(LOG_TAG, "Inside the live data onChangedMethod Updation") ;
//
                    listOfAllTransaction_Unsummed = list ;
                    listOfPortfolioTransactions_Unsummed = new ArrayList<Object_TransactionFullData>(Collections2.filter(list, Object_TransactionFullData.getPredicateFilter_PortfolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId()))) ;
                    listOfPortfolioTransactions_Summed = Helper_Home.getSummedTransactions(listOfPortfolioTransactions_Unsummed) ;
                    adapter.refreshHoldingsChange(listOfPortfolioTransactions_Summed, getListOfPrices_TimeAgo(Object_Coin.PRICE_MAX));

                } else {
                    listOfAllTransaction_Unsummed = list ;
//                    Log.e(LOG_TAG, listOfAllTransaction_Unsummed.toString()) ;

                    Log.e(LOG_TAG, "Inside the live data onChangedMethod Insertion") ;
                    if(initialLoad){
                        Log.e(LOG_TAG, "this is just the initial Load") ;
                        listOfPortfolioTransactions_Unsummed = new ArrayList<Object_TransactionFullData>(Collections2.filter(list, Object_TransactionFullData.getPredicateFilter_PortfolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId()))) ;
                        listOfPortfolioTransactions_Summed = Helper_Home.getSummedTransactions(listOfPortfolioTransactions_Unsummed) ;
//                        Log.e(LOG_TAG, listOfAllTransaction_Unsummed.toString()) ;

                        adapter.refreshHoldingsChange(listOfPortfolioTransactions_Summed, getListOfPrices_TimeAgo(Object_Coin.PRICE_MAX));
                        initialLoad = false ;

                    }
//                    else {
                        // there has been a new insert in the database
                        // 1. get the fresh data form server,
                        // 2. that data will be updated here in db and this method will be triggered again
                        getFreshDataFromServer();
//                    }

                    //
                }

            }
        }); ;
//        listOfTransactions = new ArrayList<>(Collections2.filter(listOfTransactions, Object_TransactionFullData.getPredicateFilter_PortfolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId())));
//        Helper_Home.getSummedTransactions(listOfTransactions) ;
    }

    private void setupRecyclerView(){

//        listOfTransactions = db.transactionDao().getListOfAllTransaction_FullData_Summed(MyGlobals.getCurrentPortfolioObj().getPortfolioId()) ;
//        setupPreferenceSorting_OfList() ;
        adapter = new RVAdapter_Transactions(context, new ArrayList<Object_TransactionFullData>(), getListOfPrices_TimeAgo(Object_Coin.PRICE_MAX)) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        DividerItemDecoration plainHorizontalLines = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL) ;
        rv.addItemDecoration(plainHorizontalLines);


    }






    public void setupSortingTextViewsNew(View v){
        switch (v.getId()){
            case R.id.activityPortfolio_TextView_SortName :
                if(currentSortMode == SORT_NAME_ASC){
                    setupSorting(SORT_NAME_DESC);
                } else if(currentSortMode == SORT_NAME_DESC){
                    setupSorting(SORT_NAME_ASC);
                } else {
                    setupSorting(SORT_NAME_ASC) ;
                }
                break;
            case R.id.activityPortfolio_TextView_SortSingleCoinPrice :
                if(currentSortMode == SORT_COINPRICE_ASC){
                    setupSorting(SORT_COINPRICE_DESC);
                } else if(currentSortMode == SORT_COINPRICE_DESC){
                    setupSorting(SORT_COINPRICE_ASC);
                } else {
                    setupSorting(SORT_COINPRICE_ASC) ;
                }

                break;

            case R.id.activityPortfolio_TextView_SortTotalHoldings :
                if(currentSortMode == SORT_HOLDINGS_ASC){
                    setupSorting(SORT_HOLDINGS_DESC);
                } else if(currentSortMode == SORT_HOLDINGS_DESC){
                    setupSorting(SORT_HOLDINGS_ASC);
                } else {
                    setupSorting(SORT_HOLDINGS_ASC) ;
                }
                break;

            case R.id.activityPortfolio_TextView_SortPercentage :
                if(currentSortMode == SORT_CHANGE_ASC){
                    setupSorting(SORT_CHANGE_DESC);
                } else if(currentSortMode == SORT_CHANGE_DESC){
                    setupSorting(SORT_CHANGE_ASC);
                } else {
                    setupSorting(SORT_CHANGE_ASC) ;
                }
                break;
            case R.id.activityPortfolio_TextView_SortChange24H :
                adapter.refreshHoldingsChange(listOfPortfolioTransactions_Summed, getListOfPrices_TimeAgo(Object_Coin.PRICE_1D));

                break;
            case R.id.activityPortfolio_TextView_SortChange7D :
                adapter.refreshHoldingsChange(listOfPortfolioTransactions_Summed, getListOfPrices_TimeAgo(Object_Coin.PRICE_1W));

                break;
            case R.id.activityPortfolio_TextView_SortChangeAll :
                adapter.refreshHoldingsChange(listOfPortfolioTransactions_Summed, getListOfPrices_TimeAgo(Object_Coin.PRICE_MAX));

                break;









        }

    }

    private void setupSorting(int sortMethod){
        refreshAllSortingTextViews();

        switch (sortMethod){
            case SORT_NAME_ASC :
                textView_NameSort.append(" \u2191");
                listOfPortfolioTransactions_Summed.sort(Object_TransactionFullData.NameComparator);
//                Collections.reverse(list);
                break;
            case SORT_NAME_DESC :
                textView_NameSort.append(" \u2193");
                listOfPortfolioTransactions_Summed.sort(Object_TransactionFullData.NameComparator);
                Collections.reverse(listOfPortfolioTransactions_Summed); // it is ultaa for names

                break;
            case SORT_COINPRICE_ASC :
                textView_SingleCoinPriceSort.append(" \u2191");
                listOfPortfolioTransactions_Summed.sort(Object_TransactionFullData.singleCoinPrice);
                Collections.reverse(listOfPortfolioTransactions_Summed);
                break;
            case SORT_COINPRICE_DESC :
                textView_SingleCoinPriceSort.append(" \u2193");
                listOfPortfolioTransactions_Summed.sort(Object_TransactionFullData.singleCoinPrice);
                break;
            case SORT_HOLDINGS_ASC :
                textView_HoldingsSort.append(" \u2191");
                listOfPortfolioTransactions_Summed.sort(Object_TransactionFullData.HoldingComparator);
                Collections.reverse(listOfPortfolioTransactions_Summed);
                break;
            case SORT_HOLDINGS_DESC :
                textView_HoldingsSort.append(" \u2193");
                listOfPortfolioTransactions_Summed.sort(Object_TransactionFullData.HoldingComparator);
                break;
            case SORT_CHANGE_ASC:
                textView_PercentageSort.append(" \u2191");
                Collections.sort(listOfPortfolioTransactions_Summed, new Comparator<Object_TransactionFullData>() {
                    @Override
                    public int compare(Object_TransactionFullData o1, Object_TransactionFullData o2) {
                        return holdingsChangeValueSet.get(o1.getCoinObject().getId()).compareTo(holdingsChangeValueSet.get(o2.getCoinObject().getId())) ;
                    }
                });
                Collections.reverse(listOfPortfolioTransactions_Summed);
                break;
            case SORT_CHANGE_DESC :
                textView_PercentageSort.append(" \u2193");
                Collections.sort(listOfPortfolioTransactions_Summed, new Comparator<Object_TransactionFullData>() {
                    @Override
                    public int compare(Object_TransactionFullData o1, Object_TransactionFullData o2) {
                        return holdingsChangeValueSet.get(o1.getCoinObject().getId()).compareTo(holdingsChangeValueSet.get(o2.getCoinObject().getId())) ;
                    }
                });
                break;
            default:

                break;
        }
        currentSortMode = sortMethod ;

//        adapter.notifyDataSetChanged();
        adapter.refreshHoldingsChange(listOfPortfolioTransactions_Summed, holdingsChangeValueSet);

        // save the sort method in shared preferences
    }

    private void refreshAllSortingTextViews(){
        textView_NameSort.setText("Name");
        textView_SingleCoinPriceSort.setText("CoinPrice");
        textView_HoldingsSort.setText("Holdings");
        textView_PercentageSort.setText("change");
    }




    private Map<String, BigDecimal> getListOfPrices_TimeAgo(int caseTimeAgo){
        for(Object_TransactionFullData transactionObjFD : listOfPortfolioTransactions_Summed){
            holdingsChangeValueSet.put(transactionObjFD.getCoinObject().getId(), new BigDecimal(0)) ;
        }
        Long currentTimeinLong = Calendar.getInstance().getTimeInMillis() ;
        List<String> listOfPricesTimeAgo = new ArrayList<>() ;

        switch (caseTimeAgo){
            case Object_Coin.PRICE_1D :

                for(Object_TransactionFullData transactionFullData : listOfPortfolioTransactions_Unsummed){
                    String priceTimeAgo = Object_Coin.getPriceOfCoin_FromPriceLog_1DAgo(transactionFullData.getTransactionObject(), transactionFullData.getCoinObject(), currentTimeinLong) ;
                    Log.e(LOG_TAG, "Price time ago is " + priceTimeAgo) ;
                    BigDecimal priceChange = new BigDecimal(transactionFullData.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()).subtract(new BigDecimal(priceTimeAgo)) ;
                    Log.e(LOG_TAG, "Price Change per coin is " + priceChange) ;

                    BigDecimal totalPriceChange = priceChange.multiply(new BigDecimal(transactionFullData.getTransactionObject().getNoOfCoins())) ;
                    Log.e(LOG_TAG, "Price Change Total is " + totalPriceChange) ;

                    BigDecimal OriginalValueOfHoldingChange = holdingsChangeValueSet.get(transactionFullData.getCoinObject().getId()) ;
                    BigDecimal newValue = OriginalValueOfHoldingChange.add(totalPriceChange) ;
                    holdingsChangeValueSet.replace(transactionFullData.getCoinObject().getId(), newValue) ;
                }



//                for(Object_TransactionFullData transactionObjFD : listOfPortfolioTransactions_Summed){
//                    Object_Coin coinObj = transactionObjFD.getCoinObject() ;
//                    listOfPricesTimeAgo.add(Object_Coin.getPriceOfCoin_FromPriceLog_1DAgo(transactionObjFD.getTransactionObject(), coinObj, currentTimeinLong)) ;
//                }
                break;
            case Object_Coin.PRICE_1W :
                for(Object_TransactionFullData transactionFullData : listOfPortfolioTransactions_Unsummed){
                    String priceTimeAgo = Object_Coin.getPriceOfCoin_FromPriceLog_1WAgo(transactionFullData.getTransactionObject(), transactionFullData.getCoinObject(), currentTimeinLong) ;
                    BigDecimal priceChange = new BigDecimal(transactionFullData.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()).subtract(new BigDecimal(priceTimeAgo)) ;
                    BigDecimal totalPriceChange = priceChange.multiply(new BigDecimal(transactionFullData.getTransactionObject().getNoOfCoins())) ;

                    BigDecimal OriginalValueOfHoldingChange = holdingsChangeValueSet.get(transactionFullData.getCoinObject().getId()) ;
                    BigDecimal newValue = OriginalValueOfHoldingChange.add(totalPriceChange) ;
                    holdingsChangeValueSet.replace(transactionFullData.getCoinObject().getId(), newValue) ;
                }
//                for(Object_TransactionFullData transactionObjFD : listOfPortfolioTransactions_Summed){
//                    Object_Coin coinObj = transactionObjFD.getCoinObject() ;
//                    listOfPricesTimeAgo.add(Object_Coin.getPriceOfCoin_FromPriceLog_1WAgo(transactionObjFD.getTransactionObject(), coinObj, currentTimeinLong)) ;
//                }
                break;
            case Object_Coin.PRICE_MAX :
                Log.e(LOG_TAG, "Max is being called") ;

                for(Object_TransactionFullData transactionFullData : listOfPortfolioTransactions_Unsummed){
                    String priceTimeAgo = Object_Coin.getPriceOfCoin_FromPriceLog_MaxAgo(transactionFullData.getTransactionObject()) ;
                    BigDecimal priceChange = new BigDecimal(transactionFullData.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()).subtract(new BigDecimal(priceTimeAgo)) ;
                    BigDecimal totalPriceChange = priceChange.multiply(new BigDecimal(transactionFullData.getTransactionObject().getNoOfCoins())) ;

                    BigDecimal OriginalValueOfHoldingChange = holdingsChangeValueSet.get(transactionFullData.getCoinObject().getId()) ;
                    BigDecimal newValue = OriginalValueOfHoldingChange.add(totalPriceChange) ;
                    holdingsChangeValueSet.replace(transactionFullData.getCoinObject().getId(), newValue) ;
                }
//                for(Object_TransactionFullData transactionObjFD : listOfPortfolioTransactions_Summed){
//                    Object_Coin coinObj = transactionObjFD.getCoinObject() ;
//                    listOfPricesTimeAgo.add(Object_Coin.getPriceOfCoin_FromPriceLog_MaxAgo(transactionObjFD.getTransactionObject())) ;
//                }
                break;
            default:
                Log.e(LOG_TAG, "Default is being called") ;
                for(Object_TransactionFullData transactionFullData : listOfPortfolioTransactions_Unsummed){
                    String priceTimeAgo = Object_Coin.getPriceOfCoin_FromPriceLog_MaxAgo(transactionFullData.getTransactionObject()) ;
                    BigDecimal priceChange = new BigDecimal(transactionFullData.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()).subtract(new BigDecimal(priceTimeAgo)) ;
                    BigDecimal totalPriceChange = priceChange.multiply(new BigDecimal(transactionFullData.getTransactionObject().getNoOfCoins())) ;

                    BigDecimal OriginalValueOfHoldingChange = holdingsChangeValueSet.get(transactionFullData.getCoinObject().getId()) ;
                    BigDecimal newValue = OriginalValueOfHoldingChange.add(totalPriceChange) ;
                    holdingsChangeValueSet.replace(transactionFullData.getCoinObject().getId(), newValue) ;
                }
//                for(Object_TransactionFullData transactionObjFD : listOfPortfolioTransactions_Summed){
//                    Object_Coin coinObj = transactionObjFD.getCoinObject() ;
//                    listOfPricesTimeAgo.add(Object_Coin.getPriceOfCoin_FromPriceLog_MaxAgo(transactionObjFD.getTransactionObject())) ;
//                }
                break;
        }

        Log.e(LOG_TAG, "value set is "  + holdingsChangeValueSet.toString()) ;

        return  holdingsChangeValueSet ;
    }


    private void getFreshDataFromServer(){
//        Log.e(LOG_TAG, listOfAllTransaction_Unsummed.toString()) ;  receiving the 300 noOfCoins ;
        if (listOfAllTransaction_Unsummed.size() == 0){
            Log.e(LOG_TAG, "Size of listOfAllTransaction_Unsummed = 0") ;
            return;
        }

        string_NewDataURL = Constants.URL_APICALL_SIMPLEPRICES ;

        for (Object_TransactionFullData object_transactionFullData : listOfAllTransaction_Unsummed){
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
            setupPortfolioTextViews() ;
//            adapter.refreshData(listOfTransactions);
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }

    private void setupPortfolioDialogFragment(){
        btn_PortfolioChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_Portfolio dfPortfolio = new DialogFragment_Portfolio() ;
                dfPortfolio.show(fragmentManager, "dfPortfolio ");
            }
        });
    }

    @Override
    public void onChangingPortfolio() {
        // TODO do something about the portfolio change
        Message.display(context, "Portfolio has been changed to" + MyGlobals.getCurrentPortfolioObj().getPortfolioName());
        textView_PortfolioName.setText(MyGlobals.getCurrentPortfolioObj().getPortfolioName());
        textView_PortfolioBalance.setText(Utils.showHumanDecimals(MyGlobals.getCurrentPortfolioObj().getPortfolioValue()));
        listOfPortfolioTransactions_Unsummed = new ArrayList<Object_TransactionFullData>(Collections2.filter(listOfAllTransaction_Unsummed, Object_TransactionFullData.getPredicateFilter_PortfolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId()))) ;
        listOfPortfolioTransactions_Summed = Helper_Home.getSummedTransactions(listOfPortfolioTransactions_Unsummed) ;
        adapter.refreshData(listOfPortfolioTransactions_Summed);
        getFreshDataFromServer();



        //        db.transactionDao().getListOfAllTransaction_FullData_Summed(MyGlobals.getCurrentPortfolioObj().getPortfolioId()).observe(this, new Observer<List<Object_TransactionFullData>>() {
//            @Override
//            public void onChanged(@Nullable List<Object_TransactionFullData> list) {
//                Log.e(LOG_TAG, "Live data Changed for  transaction data inside onChangingPortfolio()") ;
//                listOfTransactions = db.transactionDao().getListOfAllTransaction_FullData_Summed(MyGlobals.getCurrentPortfolioObj().getPortfolioId()) ;
//                setupPreferenceSorting_OfList() ;
//                adapter.refreshData(listOfTransactions);
//                getFreshDataFromServer();
//            }
//        }) ;

    }


    private void setPortfolioUpdateLog(){
        Button btn = findViewById(R.id.activityPortfolio_Btn_PortfolioUpdates) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_UpdateLog dfPortfolioUpdate = new DialogFragment_UpdateLog() ;
                Bundle bundle = new Bundle() ;
                bundle.putInt("portfolioId", MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext()));
                dfPortfolioUpdate.setArguments(bundle);
                dfPortfolioUpdate.show(fragmentManager, "dfPortfolioUpdate ");
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
//        listOfTransactions = db.transactionDao().getListOfAllTransaction_FullData_Summed(MyGlobals.getCurrentPortfolioObj().getPortfolioId()) ;
//        setupPreferenceSorting_OfList() ;
//        adapter.refreshData(listOfTransactions);
    }
}
