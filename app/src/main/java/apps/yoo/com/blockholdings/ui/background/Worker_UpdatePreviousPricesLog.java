package apps.yoo.com.blockholdings.ui.background;

//import androidx.work.ListenableWorker;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.AppExecutors;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;

public class Worker_UpdatePreviousPricesLog extends Worker {
    Context context ;
    private static final String LOG_TAG = "Worker_CurrencyUpdater_SingleCoinPriceOriginal --> " ;
    AppDatabase db ;
    RequestQueue requestQueue ;


    List<Object_Transaction> listOfTransactions  ;
    Object_Portfolio portfolioObj ;
    Object_Currency newCurrency ;
    Map<String, List<JSONArray>> resultValues ;
    Map<String, AtomicInteger> requestChecker ;


    public Worker_UpdatePreviousPricesLog(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context ;
        Log.e(LOG_TAG, "Worker_CurrencyUpdater_SingleCoinPriceOriginal is being constructed") ;
    }

    @NonNull
    @Override
    public Result doWork() {
        db = AppDatabase.getInstance(context.getApplicationContext()) ;
        requestQueue = Volley.newRequestQueue(context) ;

        newCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;

        resultValues = new HashMap<>() ;
        requestChecker = new HashMap<>() ;

        // update the price log for all coins
        listOfTransactions = db.transactionDao().getListOfAllTransactions_WithSingularCoins_OfOldestDate() ;

        for(Object_Transaction transactionObj : listOfTransactions){
            resultValues.put(transactionObj.getCoinId(), new ArrayList<JSONArray>()) ;

            Date oldestDate = transactionObj.getTransactionDateTime() ;
            Date CurrentDate = Calendar.getInstance().getTime() ;
            long noOfDaysFromOldestDate = TimeUnit.DAYS.convert(CurrentDate.getTime()-oldestDate.getTime(), TimeUnit.MILLISECONDS);

            if(noOfDaysFromOldestDate == 0){
                // case when the day transaction was added is today
                // in that case we really only need the data of 1 day
                requestChecker.put(transactionObj.getCoinId(), new AtomicInteger(1)) ;
                fetchDataChartFromServer_ForCoin(transactionObj.getCoinId(), "1") ;
            } else {
                requestChecker.put(transactionObj.getCoinId(), new AtomicInteger()) ;
                fetchDataChartFromServer_ForCoin(transactionObj.getCoinId(), "1") ;
                fetchDataChartFromServer_ForCoin(transactionObj.getCoinId(), "" + noOfDaysFromOldestDate) ;
            }

        }




















        return Result.success() ;
    }


    private void fetchDataChartFromServer_ForCoin(final String coinId, String noOfDays){

        final String url1 = Constants.getURL_APICALL_HISTORICAL_DATACHART(coinId, noOfDays, newCurrency.getCurrencyId()) ;

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(LOG_TAG, "Url is " + url1 + "\n Response is " + response) ;
                        try{
                            JSONObject responseObj = new JSONObject(response) ;
                            JSONArray priceArray = responseObj.getJSONArray("prices") ;
                            for(int i = 0 ; i< priceArray.length() ; i++){
                                JSONArray insideArray = priceArray.getJSONArray(i) ;
                                JSONArray updateLogEntry = new JSONArray() ;
                                long timeInLong = insideArray.getLong(0) ;
                                String price = insideArray.getString(1) ;
                                updateLogEntry.put(price).put(timeInLong).put("LogWorker") ;
                                resultValues.get(coinId).add(updateLogEntry) ;
                            }
                            requestChecker.get(coinId).addAndGet(1) ;
                            sortAndSave_UpdateLogForCoin(coinId);
                        }catch (Exception e){
                            Log.e(LOG_TAG, e.toString()) ;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in making volley request");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        }) ;
        requestQueue.add(stringRequest1) ;
    }



    private void sortAndSave_UpdateLogForCoin(final String coinId){
        if(requestChecker.get(coinId).get() == 2){
            resultValues.get(coinId).sort(Object_Coin.updateLogDateComparator);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    db.coinDao().updateCoin_PriceData(coinId, new JSONArray(resultValues.get(coinId)).toString());
                }
            });
        }
    }










}
