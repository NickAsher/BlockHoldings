package apps.yoo.com.blockholdings.ui.transaction;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.AppExecutors;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;

public class Activity_Transaction extends AppCompatActivity implements MyListener.DialogFragments_to_ActivityTransaction{
    Context context ;
    private static final String LOG_TAG = "Activity_Transaction --> " ;
    AppDatabase db ;
    FragmentManager fragmentManager ;
    RequestQueue requestQueue ;

    RelativeLayout relLt_Exchange, relLt_TradingPair, relLt_BtnAddTransaction ;
    TextView textView_CoinName, textView_Exchange, textView_TradingPair, textView_Date, textView_Time ;
    EditText editText_SingleCoinPrice, editText_Quantity, editText_Note ;
    RadioButton radioButton_Buy, radioButton_Sell ;
    RadioGroup radioGroup_BuySell ;

    Object_Coin currentCoin ;
    Object_TransactionFullData currentTransactionFD ;
    Object_Currency currencyObj ;
    Table<String, String, String> table_ExchangePairData ;
    String p24hChange ;
    String singleCoinPrice_Currency ;
    String tradingPairPrice_Currency ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        context = this ;
        fragmentManager = ((FragmentActivity)context).getSupportFragmentManager() ;
        requestQueue = Volley.newRequestQueue(context) ;
        db = AppDatabase.getInstance(getApplicationContext()) ;

        if(getIntent().getStringExtra("coin") != null){
            try {
                Helper_Transaction.makeNewTransactionFullDataObejct();
                currentCoin = new Object_Coin(new JSONObject(getIntent().getStringExtra("coin")));
                currentTransactionFD = new Object_TransactionFullData() ;
                currentTransactionFD.setCoinObject(currentCoin);
                currentTransactionFD.getTransactionObject().setCoinId(currentCoin.getId());
                currencyObj = MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()) ;
                getCoinData();
                getReferences() ;
                setBasicUi();




            }catch (JSONException e){
                Message.display(context, "Unable to get the coin");
                Log.e(LOG_TAG, e.toString()) ;
            }
        }

    }


    private void getReferences(){
        relLt_Exchange = findViewById(R.id.activityTransaction_RelLt_ExchangeContainer) ;
        relLt_TradingPair = findViewById(R.id.activityTransaction_RelLt_ContainerTradingPair) ;
        relLt_BtnAddTransaction = findViewById(R.id.activityTransaction_relLt_BtnAddTransaction) ;


        textView_CoinName = findViewById(R.id.activityTransaction_TextView_CoinName) ;
        textView_Exchange = findViewById(R.id.activityTransaction_TextView_ValueExchange) ;
        textView_TradingPair = findViewById(R.id.activityTransaction_TextView_ValueTradingPair) ;
        textView_Date = findViewById(R.id.activityTransaction_TextView_ValueDate) ;
        textView_Time = findViewById(R.id.activityTransaction_TextView_ValueTime) ;

        editText_SingleCoinPrice = findViewById(R.id.activityTransaction_EditText_ValueSingleCoinPrice) ;
        editText_Quantity = findViewById(R.id.activityTransaction_EditText_ValueQuantity) ;
        editText_Note= findViewById(R.id.activityTransaction_EditText_ValueNote) ;

        radioButton_Buy = findViewById(R.id.activityTransaction_RadioButton_Buy) ;
        radioButton_Sell = findViewById(R.id.activityTransaction_RadioButton_Sell) ;
        radioGroup_BuySell = findViewById(R.id.activityTransaction_RadioGroup_BuySell) ;



    }

    private void getCoinData(){
        String url = Constants.UTL_APICALL_TRANSACTIONDATA_PREFIX + currentCoin.getId() + Constants.UTL_APICALL_TRANSACTIONDATA_SUFFIX ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                Log.e(LOG_TAG, "Response from server is " + response) ;
                                processData(response);
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

    private void processData(String respoonse) {

//        Helper_Transaction.getTransactionObject().setCoinName(currentCoin.getName());
//        Helper_Transaction.getTransactionObject().setCoinSymbol(currentCoin.getSymbol());

        try{
            JSONObject responseObject = new JSONObject(respoonse) ;
            if(!responseObject.has("name") || !responseObject.getString("name").equalsIgnoreCase(currentCoin.getName())){
                return;
            }



            table_ExchangePairData = Helper_Transaction.getExchangePairDataForCoin(responseObject.getJSONArray("tickers")) ;
            p24hChange = responseObject.getJSONObject("market_data").getString("price_change_percentage_24h") ;
            singleCoinPrice_Currency = responseObject.getJSONObject("market_data").getJSONObject("current_price").getString(MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()).getCurrencyId()) ;


        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString());
        }

//        Object_Coin.insertIntoDB_FullCoinData(respoonse, context, db, MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencyId(),  currentCoin);


        setupExhangeDialogFragment();


    }

    private void setBasicUi(){
        textView_CoinName.setText(currentCoin.getName());
        setupBuySellRadioButton();
        setupTransactionDateTime();

    }

    private void setupBuySellRadioButton(){
        radioButton_Buy.setChecked(true);
        radioButton_Sell.setChecked(false);
        Helper_Transaction.getTransactionFullDataObject().getTransactionObject().setType(Object_Transaction.TYPE_BUY);
    }


    private void setupExhangeDialogFragment(){
        relLt_Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_Exchanges dfExchanges = new DialogFragment_Exchanges() ;
                Bundle bundle = new Bundle() ;
                bundle.putStringArrayList("exchangesList", new ArrayList<>(table_ExchangePairData.rowKeySet()));
                dfExchanges.setArguments(bundle);
                dfExchanges.show(fragmentManager, "dfExchanges");
            }
        });
    }




    @Override
    public void onSelectingExchange(Object_Exchange exchangeObj) {
        // TODO make the trading pair text view unselectable before and make it selectable now
        Message.display(context, "Exchange is now selected ");
        currentTransactionFD.setExchangeObject(exchangeObj);
        currentTransactionFD.getTransactionObject().setExchangeId(exchangeObj.getId());
        textView_Exchange.setText(exchangeObj.getName());
        setupTradingPairDialogFragment() ;
    }

    @Override
    public void onSelectingExchange_GlobalAverage(String price) {
        Message.display(context, "Exchange is now selected ");
        Object_Exchange selectedExchange = Object_Exchange.getGlobalAverage() ;
        currentTransactionFD.setExchangeObject(selectedExchange);
        currentTransactionFD.getTransactionObject().setExchangeId(selectedExchange.getId());
        textView_Exchange.setText(selectedExchange.getName());


        relLt_TradingPair.setClickable(false);
        currentTransactionFD.getTransactionObject().setTradingPair(null);
        textView_TradingPair.setText("N/A");
        editText_SingleCoinPrice.setText(currencyObj.getCurrencySymbol() + price);


    }

    private void setupTradingPairDialogFragment() {
        relLt_TradingPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_TradingPair dfTradingPair = new DialogFragment_TradingPair() ;
                Bundle bundle = new Bundle() ;
                List<String> tradingPairKeysList = new ArrayList<>(table_ExchangePairData.row(currentTransactionFD.getTransactionObject().getExchangeId()).keySet()) ;
                bundle.putStringArrayList("tradingPairList", new ArrayList<>(tradingPairKeysList));
                bundle.putString("coinSymbol", currentTransactionFD.getCoinObject().getSymbol());
                dfTradingPair.setArguments(bundle);
                dfTradingPair.show(fragmentManager, "dfTradingPair");


            }
        });
    }

    @Override
    public void onSelectingTradingPair(String tradingPairSymbol) {
        currentTransactionFD.getTransactionObject().setTradingPair(tradingPairSymbol);
        Message.display(context, "Trading Pair is now selected + ");
        textView_TradingPair.setText(currentTransactionFD.getCoinObject().getSymbol() + "/" + tradingPairSymbol);
        setupCoinPrice();
        setupAddTransactionButton();
    }

    @Override
    public void onSelectingDateTime(long timeInLong) {


    }

    private void setupCoinPrice(){
        String singleCoinPrice = table_ExchangePairData.get(currentTransactionFD.getTransactionObject().getExchangeId(), currentTransactionFD.getTransactionObject().getTradingPair()) ;
        editText_SingleCoinPrice.setText(singleCoinPrice);
    }

    private void setupTransactionDateTime(){
        Date currentDate = Calendar.getInstance().getTime() ;

        final SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM dd yyyy") ;
        String currentDateString = sdf1.format(currentDate) ;



        textView_Date.setText(currentDateString);

        textView_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

//                        Date date = new Date(year, monthOfYear, dayOfMonth) ;
//                        Calendar cal = Calendar.getInstance() ;
                                try {
                                    // So we firstly write the Date in String form as dd-mm-YYYY
                                    // Then we use the SimpleDateFormat3 Object and use it to get parse our String date
                                    // Then in our TextViewDate , we format the date using our initially used SimpleDateFormat1
                                    String resultDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
                                    Date formattedDate = sdf3.parse(resultDate) ;

                                    textView_Date.setText(sdf1.format(formattedDate));
                                }catch (Exception e){
                                    Log.e(LOG_TAG, e.toString()) ;
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        final SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a") ;
        String currentTimeString = sdf2.format(currentDate) ;

        textView_Time.setText(currentTimeString);

        textView_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                try {
                                    String resultTime = hourOfDay + ":" + minute;
                                    SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");
                                    Date formattedTime = sdf4.parse(resultTime) ;

                                    textView_Time.setText(sdf2.format(formattedTime));
                                }catch (Exception e){
                                    Log.e(LOG_TAG, e.toString()) ;
                                }

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

    }




    private void setupAddTransactionButton(){
        relLt_BtnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relLt_BtnAddTransaction.setClickable(false);

                if(currentTransactionFD.getTransactionObject().getTradingPair() == null){
                    // this means that global average is selected .
                    // so transaction is added directly without fetching price
                    makeTransaction_with_TradingPairPrice( null) ;
                    return;
                }

                Object_Coin tradingPairCoinObject = db.coinDao().getCoinBySymbol(currentTransactionFD.getTransactionObject().getTradingPair().toLowerCase()) ;
                if(tradingPairCoinObject == null){
                    // this means that this trading pair is not a coin on the website
                    // so we do not need to make a url call for the price of this coin

                    makeTransaction_with_TradingPairPrice( null) ;
                    return;
                }
                String tradingPairId = tradingPairCoinObject.getId() ;
                Log.e(LOG_TAG, tradingPairId) ;

                getCurrencyPriceForTradingPair(tradingPairId);
            }
        });
    }



    private void getCurrencyPriceForTradingPair(String tradingPairId){

        String dateString  = "";
        try {
            dateString = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("MMMM dd yyyy").parse(textView_Date.getText().toString()));
        }catch (Exception e){
            Log.e(LOG_TAG, "Problem in parsing date" + e.toString()) ;
        }


        String url = Constants.getURL_APICALL_HISTORICAL_PRICE(tradingPairId, dateString) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tradingPairPrice_Currency = processCurrencyPriceForTradingPair(response);
                        makeTransaction_with_TradingPairPrice(tradingPairPrice_Currency);
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

    private String processCurrencyPriceForTradingPair(String response){
        try {
            JSONObject responseObject = new JSONObject(response);
            if(!responseObject.has("id")){
                return null ;
            }
            return responseObject.getJSONObject("market_data").getJSONObject("current_price").getString(MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()).getCurrencyId()) ;
        }catch (Exception e){
            Log.e(LOG_TAG, "error dude " + e.toString() + "\n \n the response from server was" + response) ;

            return null ;
        }
    }


    private void makeTransaction_with_TradingPairPrice(String tradingPairPrice_Currency){

        if(tradingPairPrice_Currency == null){
            singleCoinPrice_Currency = singleCoinPrice_Currency ;
        } else {
            Log.e(LOG_TAG, "Original singleCoinPrice_Currency : " + singleCoinPrice_Currency) ;
            singleCoinPrice_Currency = new BigDecimal(editText_SingleCoinPrice.getText().toString())
                    .multiply(new BigDecimal(tradingPairPrice_Currency))
                    .toPlainString() ;
            Log.e(LOG_TAG, "New singleCoinPrice_Currency : " + singleCoinPrice_Currency) ;
        }
        int selectedRadioButtonId = radioGroup_BuySell.getCheckedRadioButtonId() ;
        switch (selectedRadioButtonId){
            case R.id.activityTransaction_RadioButton_Buy :
                currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
                break;
            case R.id.activityTransaction_RadioButton_Sell :
                currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_SELL);
                break;
            default:
                currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
                break;
        }

        String resultantDateString = textView_Date.getText() + " " + textView_Time.getText() ;
        SimpleDateFormat sdf5 = new SimpleDateFormat("MMMM dd yyyy hh:mm a") ;
        try {
            currentTransactionFD.getTransactionObject().setTransactionDateTime(sdf5.parse(resultantDateString));
        } catch (Exception e){
            Log.e(LOG_TAG, "Unable to store date time in transactionObject : " + e.toString()) ;
        }
        currentTransactionFD.getTransactionObject().setNote(editText_Note.getText().toString());




        currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyOriginal(singleCoinPrice_Currency);
        currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyCurrent(singleCoinPrice_Currency);



        currentTransactionFD.getTransactionObject().setSingleCoinPrice_TradingPair(editText_SingleCoinPrice.getText().toString());
        currentTransactionFD.getTransactionObject().setNoOfCoins(editText_Quantity.getText().toString());

        String totalValue = new BigDecimal(singleCoinPrice_Currency).multiply(new BigDecimal(editText_Quantity.getText().toString())).toString() ;
        currentTransactionFD.getTransactionObject().setTotalValue_Original(totalValue);
        currentTransactionFD.getTransactionObject().setTotalValue_Current(totalValue);
        currentTransactionFD.getTransactionObject().setPortFolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId());

        Log.e(LOG_TAG, Helper_Transaction.getTransactionFullDataObject().getTransactionObject().toString()) ;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.transactionDao().insertTransaction(currentTransactionFD.getTransactionObject());
                MyGlobals.refreshPortfolioValue(db);
                Log.v(LOG_TAG, "Transaction is added : " + currentTransactionFD) ;
                Helper_Transaction.makeNewTransactionFullDataObejct();
                finish();
            }
        });
    }



}
