package apps.yoo.com.blockholdings.ui;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.data.models.Object_NewsSite;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_VsSimpleCurrency;
import apps.yoo.com.blockholdings.ui.general.BackgroundDataUpdater;
import apps.yoo.com.blockholdings.ui.home.Activity_Home;
import apps.yoo.com.blockholdings.ui.settings.Helper_Settings;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.Message;


public class MainActivity extends AppCompatActivity {
    Context context ;
    private static final String LOG_TAG = "MainActivity -->" ;
    AppDatabase db ;
    RequestQueue requestQueue ;
    int totalNoOfCoins ;
    int checkerNo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        requestQueue = Volley.newRequestQueue(context) ;






        initOneTimeSetup() ;
        setupCurrentPortfolio();


    }


    private void initOneTimeSetup(){
        checkSomething() ;
        addCoin_inDB() ;
        addExchanges_inDB() ;
        addCurrencies_inDB() ;
        addPortfolio_inDB() ;
        addListOfNewSites_inDB() ;
//        startActivityPortfolio();
        startBackgroundUpdationWorker() ;
//        startUpdatingMarketRanks() ;

    }

    private void startActivityPortfolio(){
//        if(checkerNo == 5){

        Intent intent = new Intent(this, Activity_Home.class) ;
        startActivity(intent);
        finish();


//        }
    }


    private void checkSomething(){

    }

    private void startBackgroundUpdationWorker(){
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(BackgroundDataUpdater.class, 31, TimeUnit.MINUTES)
                .addTag("idTag_myYolo1") // used just like findFragmentByTag
//                .setInputData(inputWorkData)
//                .setConstraints()
                .build() ;

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                "background_updater",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest) ;



//       we caould also use the following line if we want to get the workInfo using the tag
//        It will return a list oF workInfo Objects. Just get the list.get(0) object
//        WorkManager.getInstance().getWorkInfosByTag("idTag_myYolo1").observe ...

//        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(@Nullable WorkInfo workInfo) {
//                if(workInfo.getState().isFinished()){
//                    Data outputWorkData = workInfo.getOutputData() ;
//                    Log.e(LOG_TAG, "Result of sum of two no is " + outputWorkData.getInt("result", -1)) ;
//                }
//
//            }
//        });
    }

    private void addListOfNewSites_inDB(){
        List<Object_NewsSite> listOfItems = db.newSiteDao().getListOfNewsSites() ;
        if(listOfItems.size() == 0) {

            db.newSiteDao().insertFoodItem(new Object_NewsSite(1, "CoinDesk", "http://feeds.feedburner.com/CoinDesk", false));
            db.newSiteDao().insertFoodItem(new Object_NewsSite(2, "Coin Telegraph", "https://cointelegraph.com/rss", true));
            db.newSiteDao().insertFoodItem(new Object_NewsSite(3, "Bitcoin.com", "https://news.bitcoin.com/feed/", true));


        }
    }


    private void addCoin_inDB() {
//        db.coinDao().deleteWholeTable();
        Message.display_w_Log(context, LOG_TAG, "currenctly in addCon method");
        List<Object_Coin> listOfCoins = db.coinDao().getListOfAllCoins() ;
        if(listOfCoins.size() == 0){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_APICALL_ALLCOINS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            totalNoOfCoins = Object_Coin.getTOTAL_NO_OF_COINS(response) ;
                            Message.display_w_Log(context, LOG_TAG, "Inside the the response method" + totalNoOfCoins);

//                                Log.e(LOG_TAG, "Response from server is " + response) ;
//                            ArrayList<Object_Coin> listOfCoins = Helper_Settings.getListOfCoins_FromApi(response) ;
////                                Log.e(LOG_TAG, listOfCoins.toString()) ;
//                            db.coinDao().deleteWholeTable() ;
//                            db.coinDao().insertManyCoins(listOfCoins);
//                            totalNoOfCoins = listOfCoins.size() ;
//                            Log.e(LOG_TAG, "Size of listof coins = " + totalNoOfCoins) ;
//                            Message.display(context, "Resresh of All Coins is complete");
                            if(totalNoOfCoins != -1){
//                                db.coinDao().deleteWholeTable();

                                startUpdatingMarketRanks() ;

                            } else {
                                Message.display_w_Log(context, LOG_TAG, "Unable to get total no of coins" + totalNoOfCoins);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString() ) ;
                }
            }) ;


            requestQueue.add(stringRequest) ;
        } else {
            startActivityPortfolio();
        }

    }


    private void addExchanges_inDB(){
        final List<Object_Exchange> listOfExchagnes = db.exchangeDao().getListOfAllExchanges() ;
        if(listOfExchagnes.size() == 0){
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, Constants.URL_APICALL_EXCHANGES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<Object_Exchange> listOfExchanges = Helper_Settings.getListOfExchanges_FromApi(response) ;
                            db.exchangeDao().deleteWholeTable() ;
                            db.exchangeDao().insertManyExchanges(listOfExchanges);
                            Message.display(context, "Resresh of All Exchanges is complete");
                            checkerNo ++ ;
                            startActivityPortfolio();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString() ) ;
                }
            }) ;

            requestQueue.add(stringRequest2) ;

            Object_VsSimpleCurrency.insertAllVsSimpleCurrencies(context, requestQueue, db);
        }
    }


    private void addCurrencies_inDB(){
        List<Object_Currency> listOfCurrencies = db.currencyDao().getListOfAllCurrencies() ;
        if(listOfCurrencies.size() == 0){
            db.currencyDao().deleteWholeTable();
            db.currencyDao().insertManyCurrencies(Object_Currency.getListOfAllCurrencies()); ;


        }

    }


    private void addPortfolio_inDB(){
        List<Object_Portfolio> listOfPortfolios = db.portfolioDao().getListOfAllPortfolios() ;
        if(listOfPortfolios.size() == 0){
            db.portfolioDao().insertPortfolio(new Object_Portfolio(1,"Main Portfolio", "0" )) ;
            db.portfolioDao().insertPortfolio(new Object_Portfolio(2,"Secondary Portfolio", "0")) ;


        }
    }


    private void startUpdatingMarketRanks(){
        Log.e(LOG_TAG, "Inside the start updating market ranks method") ;

        int noOfRequests = totalNoOfCoins/250 + 1  ;
        Log.e(LOG_TAG, "no of requests is " + noOfRequests) ;

//        int noOfRequests = 1 ;

        for (int i = 1 ; i <noOfRequests + 1; i++){
//            Log.e(LOG_TAG, "Doing iteration for " + i) ;
            String url = Constants.getURL_APICALL_ALLCOINDATA(i) ;
            Log.e(LOG_TAG, url) ;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            db.coinDao().insertManyCoins(Object_Coin.getListOfCoinsFromJsonArray(response));
//

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString() ) ;
                }
            }) ;
            requestQueue.add(stringRequest) ;


        }
        checkerNo ++ ;
        Message.display_w_Log(context, LOG_TAG, "Refresh of all coins is complete");
//        startActivityPortfolio();
    }





    private void setupCurrentPortfolio(){
        int portFolioId = MySharedPreferences.getPortfolioId_FromPreference(getApplicationContext()) ;
        Object_Portfolio portfolio = db.portfolioDao().getPortfolioById(portFolioId) ;
        MyGlobals.setupCurrentPortfolioObj(portfolio);
    }



}
