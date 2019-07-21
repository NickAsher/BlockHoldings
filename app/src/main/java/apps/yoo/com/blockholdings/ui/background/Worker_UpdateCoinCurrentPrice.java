package apps.yoo.com.blockholdings.ui.background;

//import androidx.work.ListenableWorker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.data.models.Object_VsSimpleCurrency;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.Utils;

public class Worker_UpdateCoinCurrentPrice extends Worker {
    // This class updates gets the fresh coinPrice from server
    // And then updates all the coins in the transaction_table with the current price
    // After that it also refreshes the portfolio values of the portfolios

    Context context ;
    private static final String LOG_TAG = "Worker_UpdateCoinCurrentPrice --> " ;
    AppDatabase db ;
    RequestQueue requestQueue ;


    List<Object_Transaction> listOfTransactions  ;
    Object_Currency currentCurrency ;



    public Worker_UpdateCoinCurrentPrice(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context ;
    }

    @NonNull
    @Override
    public Result doWork() {
        db = AppDatabase.getInstance(context.getApplicationContext()) ;
        requestQueue = Volley.newRequestQueue(context) ;

        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        listOfTransactions = db.transactionDao().getListOfAllTransactions() ;


        getFromServer_CurrentCoinPrices();

        return Result.success() ;
    }


    private void getFromServer_CurrentCoinPrices(){
        if (listOfTransactions.size() == 0){
            Log.d(LOG_TAG, "Size of listOfTransactions = 0, so the Worker_UpdateCoinCurrentPrice will now stop") ;
            return;
        }

        StringBuilder url = new StringBuilder(Constants.URL_APICALL_SIMPLEPRICES) ;
        for (Object_Transaction object_transaction : listOfTransactions){
            url.append(object_transaction.getCoinId() + "," );
        }

        url.deleteCharAt(url.length() - 1) ; // removing the last comma
        url.append("&vs_currencies=" + currentCurrency.getCurrencyId()) ;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, response) ;
                        processServerResponse_CurrentCoinPrice(response);
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

    private void processServerResponse_CurrentCoinPrice(String response){
        try{
            JSONObject responseObject = new JSONObject(response) ;

            for (Object_Transaction object_transaction : listOfTransactions ){
                int txnId = object_transaction.getTransactionNo() ;
                String coinId = object_transaction.getCoinId() ;
                String newSingleCoinPrice_Currency = responseObject.getJSONObject(coinId).getString(currentCurrency.getCurrencyId()) ;
                BigDecimal noOfCoins = new BigDecimal(Utils.removeMinusSign(object_transaction.getNoOfCoins())) ;
                String newTotalCost = new BigDecimal(newSingleCoinPrice_Currency).multiply(noOfCoins).toPlainString() ;
                object_transaction.setSingleCoinPrice_CurrencyCurrent(newSingleCoinPrice_Currency);
                object_transaction.setTotalValue_Current(newTotalCost);

//                Log.e(LOG_TAG,  " CoinId " + coinId + " SinglePriceCurrency " + newSingleCoinPrice_Currency + " noOfCoins " + object_transaction.getNoOfCoins() + "  total cost  " + newTotalCost) ;

                // not doing the following work in app executor thread because this is in a loop
                // so a number of threads will be created if we do this
                db.transactionDao().updateTransactionPriceByTxnId(txnId, newSingleCoinPrice_Currency, newTotalCost);
                Object_Coin.addToPriceData(db, coinId, newSingleCoinPrice_Currency, Calendar.getInstance().getTimeInMillis() , "MainActivity");

            }

            MyGlobals.refreshPortfolioValue(db);
            // the following line basically refreshes the Fragment_PortfolioBrief.
            // SO rather than making a new method to refresh the damn thing, i just call it
//            fragment_portfolioBrief.refreshPortfolio();
//            adapter.refreshData(listOfTransactions);
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }












}
