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

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_VsSimpleCurrency;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;

public class Worker_CurrencyUpdater_SingleCoinPriceOriginal extends Worker {
    Context context ;
    private static final String LOG_TAG = "Worker_CurrencyUpdater_SingleCoinPriceOriginal --> " ;
    AppDatabase db ;
    RequestQueue requestQueue ;


    List<Object_Transaction> listOfTransactions  ;
    Object_Currency newCurrency ;

    final int NO_EXCHANGE = 1 ;
    final int ONLY_EXCHANGE = 2 ;
    final int EXCHANGE_AND_TRADINGPAIR = 3 ;

    public Worker_CurrencyUpdater_SingleCoinPriceOriginal(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        listOfTransactions = db.transactionDao().getListOfAllTransactions() ;

        updateSingleCoinPrice_Original_InDB() ;














        return Result.success() ;
    }


    private void updateSingleCoinPrice_Original_InDB(){
        for (Object_Transaction transactionObj : listOfTransactions){

            // case 1
            // there is no exchange and thus no trading pair
            // so we fetch the coin price directly from server for specified date
            // put coinPrice_currencyOriginal in database
            if(transactionObj.getExchangeId().equalsIgnoreCase("0000")){
                fetchFromServer_CoinPrice(transactionObj);
            }

            else{
                // check if trading pair is in list of vs_simple_currencies
                boolean tradingPairIsInVsSimpleCurrenciesList = false ;
                String tradingPairSymbol = transactionObj.getTradingPair() ;
                List<Object_VsSimpleCurrency> list = db.vsSimpleCurrencyDao().getListOfAllVsSimpleCurrencies() ;

                for(Object_VsSimpleCurrency obj : list){
                    if(obj.getCurrencyId().equalsIgnoreCase(tradingPairSymbol)){
                        tradingPairIsInVsSimpleCurrenciesList = true ;
                        break;
                    }
                }

                // case 2
                // an exchange and trading pair is selected
                // the selected trading pair is NOT in list of vs_simple_currencies list
                // then we again simply fetch the price of our coin from server for specified date
                // put that price in coinPrice_currencyOriginal in database
                if(!tradingPairIsInVsSimpleCurrenciesList){
                    fetchFromServer_CoinPrice(transactionObj);
                } else {
                    // case 3
                    // an exchange and trading pair is selected
                    // the selected trading pair is in the list of vs_simple_currencies
                    // it means that the value of coin/trading pair saved in our db is a valid value for the date
                    // so we just fetch the price of trading pair in the specified currency for the specified date
                    // and multiple the price of trading pair with   coin/trading pair value specified in the db
                    Object_Coin coinOfTradingPair = db.coinDao().getCoinBySymbol(tradingPairSymbol) ;
                    fetchFromServer_TradingPairPrice(transactionObj, coinOfTradingPair.getId());
                }
            }
        }
    }
    private void fetchFromServer_CoinPrice(final Object_Transaction transactionObj){
        final String coinId = transactionObj.getCoinId() ;
        final String dateString  = new SimpleDateFormat("dd-MM-yyyy").format(transactionObj.getTransactionDateTime());


        final String currentDateString = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) ;
        if(dateString.equalsIgnoreCase(currentDateString)){
            // special case date of transaction is current date
            //  so the api call of getting historical prices will fail
            // we have to fetch the simple price here
            String url = Constants.getURL_APICALL_SIMPLEPRICES(coinId, newCurrency.getCurrencyId());
            Log.e(LOG_TAG, url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(LOG_TAG, "Response from server is " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String price = jsonObject.getJSONObject(coinId).get(newCurrency.getCurrencyId()).toString();
                                transactionObj.setSingleCoinPrice_CurrencyOriginal(price);
                                String totalPrice_Original = new BigDecimal(price).multiply(new BigDecimal(transactionObj.getNoOfCoins())).toPlainString();
                                transactionObj.setTotalValue_Original(totalPrice_Original);
                                db.transactionDao().updateTransaction(transactionObj);
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, e.toString() + "date is " + dateString);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString());
                }
            });

            requestQueue.add(stringRequest);
        } else {
            String url = Constants.getURL_APICALL_HISTORICAL_PRICE(coinId, dateString);
            Log.e(LOG_TAG, url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(LOG_TAG, "Response from server is " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String price = jsonObject.getJSONObject("market_data").getJSONObject("current_price").get(newCurrency.getCurrencyId()).toString();
                                transactionObj.setSingleCoinPrice_CurrencyOriginal(price);
                                String totalPrice_Original = new BigDecimal(price).multiply(new BigDecimal(transactionObj.getNoOfCoins())).toPlainString();
                                transactionObj.setTotalValue_Original(totalPrice_Original);
                                db.transactionDao().updateTransaction(transactionObj);
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, e.toString() + "date is " + dateString);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString());
                }
            });

            requestQueue.add(stringRequest);
        }
    }

    private void fetchFromServer_TradingPairPrice(final Object_Transaction transactionObj, final String tradingPairId){

        String dateString  = new SimpleDateFormat("dd-MM-yyyy").format(transactionObj.getTransactionDateTime());

        final String currentDateString = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) ;
        if(dateString.equalsIgnoreCase(currentDateString)){
            // special case date of transaction is current date
            //  so the api call of getting historical prices will fail
            // we have to fetch the simple price here
            String url = Constants.getURL_APICALL_SIMPLEPRICES(tradingPairId, newCurrency.getCurrencyId());
            Log.e(LOG_TAG, url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(LOG_TAG, "Response from server is " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String tradingPairPriceInCurrency = jsonObject.getJSONObject(tradingPairId).get(newCurrency.getCurrencyId()).toString();
                                BigDecimal coinPrice_Original = new BigDecimal(tradingPairPriceInCurrency).multiply(new BigDecimal(transactionObj.getSingleCoinPrice_TradingPair()));
                                transactionObj.setSingleCoinPrice_CurrencyOriginal(coinPrice_Original.toPlainString());
                                String totalPrice_Original = coinPrice_Original.multiply(new BigDecimal(transactionObj.getNoOfCoins())).toPlainString();
                                transactionObj.setTotalValue_Original(totalPrice_Original);
                                db.transactionDao().updateTransaction(transactionObj);
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString());
                }
            });

            requestQueue.add(stringRequest);
        } else {
            String url = Constants.getURL_APICALL_HISTORICAL_PRICE(tradingPairId, dateString);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(LOG_TAG, "Response from server is " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String tradingPairPriceInCurrency = jsonObject.getJSONObject("market_data").getJSONObject("current_price").get(newCurrency.getCurrencyId()).toString();
                                BigDecimal coinPrice_Original = new BigDecimal(tradingPairPriceInCurrency).multiply(new BigDecimal(transactionObj.getSingleCoinPrice_TradingPair()));
                                transactionObj.setSingleCoinPrice_CurrencyOriginal(coinPrice_Original.toPlainString());
                                String totalPrice_Original = coinPrice_Original.multiply(new BigDecimal(transactionObj.getNoOfCoins())).toPlainString();
                                transactionObj.setTotalValue_Original(totalPrice_Original);
                                db.transactionDao().updateTransaction(transactionObj);
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, e.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Message.display(context, "Error in making volley request");
                    Log.e(LOG_TAG, error.toString());
                }
            });

            requestQueue.add(stringRequest);
        }
    }










}
