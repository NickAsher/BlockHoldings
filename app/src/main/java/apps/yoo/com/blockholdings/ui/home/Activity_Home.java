package apps.yoo.com.blockholdings.ui.home;

import android.app.Activity;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import apps.yoo.com.blockholdings.data.models.Object_TransactionGroup;
import apps.yoo.com.blockholdings.ui.background.Worker_UpdateCoinCurrentPrice;
import apps.yoo.com.blockholdings.ui.portfolio.Fragment_PortfolioBrief;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.ui.transaction.Activity_Transaction3;
import apps.yoo.com.blockholdings.ui.watchlist.Activity_WatchlistContainer;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.MyGlobals;
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
    Map<String, BigDecimal> holdingsChangeValueSet_TotalDollarDifference;
    Table<String, Integer, BigDecimal> table_PriceChange_TotalDollarDifference ;

    Object_Portfolio currentPortfolioObj ;
    List<Object_TransactionFullData>  listOfPortfolioTransactions ;
    ArrayList<Object_TransactionGroup> listOfTransactionGroups ;
    List<Object_Portfolio> listOfPortfolios ;
    Object_Currency currentCurrency ;
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

        holdingsChangeValueSet_TotalDollarDifference = new HashMap<>() ;
        table_PriceChange_TotalDollarDifference = HashBasedTable.create() ;

        getReferences();
        layout_MainContainer.setBackground(MySharedPreferences.getAppThemeGradientDrawableOnPreference(getApplicationContext()));

        setupBottomNavigationView();
        setupListOfTransactions() ;
        setupRecyclerView();
        setupBasicUI() ;

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


    private void setupListOfTransactions(){

        db.transactionDao().getListOfAllTransactionFD_OfPortfolio_LiveData(MyGlobals.getCurrentPortfolioObj().getPortfolioId()).observe(this, new Observer<List<Object_TransactionFullData>>() {
            @Override
            public void onChanged(List<Object_TransactionFullData> newList) {


                if(!initialLoad){
                    fragment_portfolioBrief.refreshPortfolio();

                    if(listOfPortfolioTransactions.size() != newList.size()){
                        //Whenever a atransaction is added or deleted the size of the list is changed
                        // And then we refresh the data using a background service
                        WorkManager.getInstance().enqueue(new OneTimeWorkRequest.Builder(Worker_UpdateCoinCurrentPrice.class)
                                .addTag("Worker_UpdateCoinCurrentPrice")
                                .build()) ;
                    }
                }

                listOfPortfolioTransactions = newList ;
                listOfTransactionGroups = Helper_Home.getListOfTransactionGroups(listOfPortfolioTransactions) ;
                adapter_transactionsExpandable.refreshAdapter(listOfTransactionGroups);
                initialLoad = false ;


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





    @Override
    protected void onResume() {
        super.onResume();

    }
}
