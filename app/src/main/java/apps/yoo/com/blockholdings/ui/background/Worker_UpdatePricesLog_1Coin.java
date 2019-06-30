package apps.yoo.com.blockholdings.ui.background;


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
import java.util.List;
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

public class Worker_UpdatePricesLog_1Coin extends Worker {
  Context context ;
  private static final String LOG_TAG = "Worker_CurrencyUpdater_SingleCoinPriceOriginal --> " ;
  AppDatabase db ;
  RequestQueue requestQueue ;


  List<Object_Transaction> listOfTransactions  ;
  Object_Coin currentCoin ;
  Object_Portfolio portfolioObj ;
  Object_Currency newCurrency ;
  List<JSONArray> resultValues ;
  AtomicInteger requestChecker ;

  Date oldestDate ;


  public Worker_UpdatePricesLog_1Coin(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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

    String coinId = getInputData().getString("coinId") ;
    currentCoin = db.coinDao().getCoinById(coinId) ;

    listOfTransactions = db.transactionDao().getListOfTransaction_ForCoin_SortByDate(coinId) ;


    // doesn't matter if the no of already existing transactions of the coin are more than 0
    // because we basically just throw the old log away
    // so just get the transaction with oldest date in here, which is basically the first item in the list
    oldestDate = listOfTransactions.get(0).getTransactionDateTime() ;
    resultValues = new ArrayList<>() ;



    Date CurrentDate = Calendar.getInstance().getTime() ;
    long noOfDaysFromOldestDate = TimeUnit.DAYS.convert(CurrentDate.getTime()-oldestDate.getTime(), TimeUnit.MILLISECONDS);

    if(noOfDaysFromOldestDate == 0){
      // case when the day transaction was added is today
      // in that case we really only need the data of 1 day
      requestChecker = new AtomicInteger(1) ;
      fetchDataChartFromServer_ForCoin(coinId, "1") ;
    } else {
      requestChecker=  new AtomicInteger() ;
      fetchDataChartFromServer_ForCoin(coinId, "1") ;
      fetchDataChartFromServer_ForCoin(coinId, "" + noOfDaysFromOldestDate) ;
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
                    resultValues.add(updateLogEntry) ;
                  }
                  requestChecker.addAndGet(1) ;
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
    if(requestChecker.get() == 2){
      resultValues.sort(Object_Coin.updateLogDateComparator);
      AppExecutors.getInstance().diskIO().execute(new Runnable() {
        @Override
        public void run() {
          db.coinDao().updateCoin_PriceData(coinId, new JSONArray(resultValues).toString());
        }
      });
    }
  }










}
