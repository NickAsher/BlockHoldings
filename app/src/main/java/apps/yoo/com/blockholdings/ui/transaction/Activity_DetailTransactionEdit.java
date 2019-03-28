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
import android.widget.ImageView;
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
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;

public class Activity_DetailTransactionEdit extends AppCompatActivity implements MyListener.DialogFragments_to_ActivityTransaction{
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
    ImageView btn_DeleteTransaction ;

    Object_TransactionFullData currentTransactionFD ;
    Table<String, String, String> table_ExchangePairData ;
    String coinNewImageLink ;
    String singleCoinPrice_Currency ;
    String tradingPairPrice_Currency ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction_edit);
        context = this ;
        fragmentManager = ((FragmentActivity)context).getSupportFragmentManager() ;
        requestQueue = Volley.newRequestQueue(context) ;
        db = AppDatabase.getInstance(getApplicationContext()) ;

        if(!getIntent().hasExtra("transactionId")) {
            Message.display(context, "Didnt receive the transactionId");
            return;
        }
        currentTransactionFD = db.transactionDao().getTransactionFullData_ById(getIntent().getIntExtra("transactionId", -1)) ;


        getCoinData();
        getReferences() ;
        setBasicUi();
        setDeleteTransactionBtn() ;



    }


    private void getReferences(){
        relLt_Exchange = findViewById(R.id.activityTransactionEdit_RelLt_ExchangeContainer) ;
        relLt_TradingPair = findViewById(R.id.activityTransactionEdit_RelLt_ContainerTradingPair) ;
        relLt_BtnAddTransaction = findViewById(R.id.activityTransactionEdit_relLt_BtnAddTransaction) ;


        textView_CoinName = findViewById(R.id.activityTransactionEdit_TextView_CoinName) ;
        textView_Exchange = findViewById(R.id.activityTransactionEdit_TextView_ValueExchange) ;
        textView_TradingPair = findViewById(R.id.activityTransactionEdit_TextView_ValueTradingPair) ;
        textView_Date = findViewById(R.id.activityTransactionEdit_TextView_ValueDate) ;
        textView_Time = findViewById(R.id.activityTransactionEdit_TextView_ValueTime) ;

        editText_SingleCoinPrice = findViewById(R.id.activityTransactionEdit_EditText_ValueSingleCoinPrice) ;
        editText_Quantity = findViewById(R.id.activityTransactionEdit_EditText_ValueQuantity) ;
        editText_Note= findViewById(R.id.activityTransactionEdit_EditText_ValueNote) ;

        radioButton_Buy = findViewById(R.id.activityTransactionEdit_RadioButton_Buy) ;
        radioButton_Sell = findViewById(R.id.activityTransactionEdit_RadioButton_Sell) ;
        radioGroup_BuySell = findViewById(R.id.activityTransactionEdit_RadioGroup_BuySell) ;

        btn_DeleteTransaction = findViewById(R.id.activityTransactionEdit_ImageView_DeleteTxnBtn) ;



    }

    private void setBasicUi(){
        textView_CoinName.setText(currentTransactionFD.getCoinObject().getName());

        if(currentTransactionFD.getTransactionObject().getType() == Object_Transaction.TYPE_BUY){
            radioButton_Buy.setChecked(true);
            radioButton_Sell.setChecked(false);

        } else if(currentTransactionFD.getTransactionObject().getType() == Object_Transaction.TYPE_BUY){
            radioButton_Buy.setChecked(false);
            radioButton_Sell.setChecked(true);

        }

        textView_Exchange.setText(currentTransactionFD.getExchangeObject().getName()) ;
        textView_TradingPair.setText(currentTransactionFD.getCoinObject().getSymbol() + "/" + currentTransactionFD.getTransactionObject().getTradingPair());
        editText_SingleCoinPrice.setText(currentTransactionFD.getTransactionObject().getSingleCoinPrice_TradingPair());
        editText_Quantity.setText(currentTransactionFD.getTransactionObject().getNoOfCoins());
        editText_Note.setText(currentTransactionFD.getTransactionObject().getNote());
        textView_Date.setText(new SimpleDateFormat("MMMM dd yyyy").format(currentTransactionFD.getTransactionObject().getTransactionDateTime()));
        textView_Time.setText(new SimpleDateFormat("hh:mm a").format(currentTransactionFD.getTransactionObject().getTransactionDateTime()));



        setupTransactionDateTime();

    }


    private void getCoinData(){
        String url = Constants.UTL_APICALL_TRANSACTIONDATA_PREFIX + currentTransactionFD.getCoinObject().getId() + Constants.UTL_APICALL_TRANSACTIONDATA_SUFFIX ;
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
            if(!responseObject.has("name") || !responseObject.getString("name").equalsIgnoreCase(currentTransactionFD.getCoinObject().getName())){
                return;
            }

            table_ExchangePairData = Helper_Transaction.getExchangePairDataForCoin(responseObject.getJSONArray("tickers")) ;
            singleCoinPrice_Currency = responseObject.getJSONObject("market_data").getJSONObject("current_price").getString(MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()).getCurrencyId()) ;


        }catch (JSONException e){

        }



        setupExhangeDialogFragment();


    }

    private  void setDeleteTransactionBtn(){
        btn_DeleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        db.transactionDao().deleteTransaction_ById(currentTransactionFD.getTransactionObject().getTransactionNo()) ;
                        MyGlobals.refreshPortfolioValue(db);
                        finish();
                    }
                });

            }
        });
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
        textView_TradingPair.setText("");
        editText_SingleCoinPrice.setText("");

    }

    @Override
    public void onSelectingExchange_GlobalAverage(String price) {

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
            Log.e(LOG_TAG, "error dude " + e.toString()) ;
            return null ;
        }
    }


    private void makeTransaction_with_TradingPairPrice(String tradingPairPrice_Currency){

        if(tradingPairPrice_Currency == null){
            // do nothing to singleCoinPrice_Currency
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



        // we need the current value 24price%. so we dont need to change its value
        // same is the situation for singleCoinPrice_CurrencyCurrent. We are only
        // changing the original value i.e buy/sell value at time of transaction
        // the current value is not to be disturbed ;
//        currentTransactionFD.getTransactionObject().setPrice24hChange(p24hChange);
        currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyOriginal(singleCoinPrice_Currency);
//        currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyCurrent(singleCoinPrice_Currency);



        currentTransactionFD.getTransactionObject().setSingleCoinPrice_TradingPair(editText_SingleCoinPrice.getText().toString());
        currentTransactionFD.getTransactionObject().setNoOfCoins(editText_Quantity.getText().toString());

        String totalValue = new BigDecimal(singleCoinPrice_Currency).multiply(new BigDecimal(editText_Quantity.getText().toString())).toString() ;
        currentTransactionFD.getTransactionObject().setTotalValue_Original(totalValue);

        // As we said, we dont change the current value of singleCoinPrice_CurrencyCurrent
        // But if we change the Quantity of Coins , then we need to update the current value. so
        String totalValueCurrent = new BigDecimal(currentTransactionFD.getTransactionObject().getTotalValue_Current())
                .multiply(new BigDecimal(editText_Quantity.getText().toString())).toPlainString() ;
        currentTransactionFD.getTransactionObject().setTotalValue_Current(totalValueCurrent);


        currentTransactionFD.getTransactionObject().setPortFolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.transactionDao().updateTransaction(currentTransactionFD.getTransactionObject());
                MyGlobals.refreshPortfolioValue(db);

                Log.e(LOG_TAG, Helper_Transaction.getTransactionFullDataObject().getTransactionObject().toString()) ;
                Helper_Transaction.makeNewTransactionFullDataObejct();
                finish();
            }
        });


    }



}
