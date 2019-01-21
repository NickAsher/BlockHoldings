package apps.yoo.com.blockholdings.ui.transaction;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import apps.yoo.com.blockholdings.data.Objects.Object_Coin;
import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
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
    Table<String, String, String> table_ExchangePairData ;
    String coinNewImageLink ;
    String p24hChange ;
    String singleCoinPrice_Currency ;

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
                currentCoin = new Object_Coin(new JSONObject(getIntent().getStringExtra("coin")));

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
        Helper_Transaction.getTransactionObject().setCoinId(currentCoin.getId());
        Helper_Transaction.getTransactionObject().setCoinName(currentCoin.getName());
        Helper_Transaction.getTransactionObject().setCoinSymbol(currentCoin.getSymbol());

        try{
            JSONObject responseObject = new JSONObject(respoonse) ;
            if(!responseObject.has("name") || !responseObject.getString("name").equalsIgnoreCase(currentCoin.getName())){
                return;
            }

            table_ExchangePairData = Helper_Transaction.getExchangePairDataForCoin(responseObject.getJSONArray("tickers"), currentCoin.getName()) ;
            coinNewImageLink = responseObject.getJSONObject("image").getString("small") ;
            p24hChange = responseObject.getJSONObject("market_data").getString("price_change_percentage_24h") ;
            singleCoinPrice_Currency = responseObject.getJSONObject("market_data").getJSONObject("current_price").getString("usd") ;


        }catch (JSONException e){

        }


        db.coinDao().updateCoin_Image(currentCoin.getId(), coinNewImageLink);

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
        Helper_Transaction.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
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
    public void onSelectingExchange() {
        // TODO make the trading pair text view unselectable before and make it selectable now
        Message.display(context, "Exchange is now selected ");
        Log.e(LOG_TAG, Helper_Transaction.getTransactionObject().toString()) ;

        textView_Exchange.setText(Helper_Transaction.getTransactionObject().getExchangeName());
        setupTradingPairDialogFragment() ;
    }


    private void setupTradingPairDialogFragment() {
        relLt_TradingPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_TradingPair dfTradingPair = new DialogFragment_TradingPair() ;
                Bundle bundle = new Bundle() ;
                List<String> tradingPairKeysList = new ArrayList<>(table_ExchangePairData.row(Helper_Transaction.getTransactionObject().getExchangeId()).keySet()) ;
                bundle.putStringArrayList("tradingPairList", new ArrayList<>(tradingPairKeysList));
                dfTradingPair.setArguments(bundle);
                dfTradingPair.show(fragmentManager, "dfTradingPair");


            }
        });
    }

    @Override
    public void onSelectingTradingPair() {
        Message.display(context, "Trading Pair is now selected + ");
        Log.e(LOG_TAG, Helper_Transaction.getTransactionObject().toString()) ;
        textView_TradingPair.setText(Helper_Transaction.getTransactionObject().getCoinSymbol() + "/" + Helper_Transaction.getTransactionObject().getTradingPair());
        setupCoinPrice();
        setupAddTransactionButton();

    }

    private void setupCoinPrice(){
        String singleCoinPrice = table_ExchangePairData.get(Helper_Transaction.getTransactionObject().getExchangeId(), Helper_Transaction.getTransactionObject().getTradingPair()) ;
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


                int selectedRadioButtonId = radioGroup_BuySell.getCheckedRadioButtonId() ;
                switch (selectedRadioButtonId){
                    case R.id.activityTransaction_RadioButton_Buy :
                        Helper_Transaction.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
                        break;
                    case R.id.activityTransaction_RadioButton_Sell :
                        Helper_Transaction.getTransactionObject().setType(Object_Transaction.TYPE_SELL);
                        break;
                    default:
                        Helper_Transaction.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
                        break;
                }

                String resultantDateString = textView_Date.getText() + " " + textView_Time.getText() ;
                SimpleDateFormat sdf5 = new SimpleDateFormat("MMMM dd yyyy hh:mm a") ;
                try {
                    Helper_Transaction.getTransactionObject().setTransactionDateTime(sdf5.parse(resultantDateString));
                } catch (Exception e){
                    Log.e(LOG_TAG, "Unable to store date time in transactionObject : " + e.toString()) ;
                }
                Helper_Transaction.getTransactionObject().setNote(editText_Note.getText().toString());




                Helper_Transaction.getTransactionObject().setPrice24hChange(p24hChange);
                Helper_Transaction.getTransactionObject().setSingleCoinPrice_Currency(singleCoinPrice_Currency);



                Helper_Transaction.getTransactionObject().setSingleCoinPrice_TradingPair(editText_SingleCoinPrice.getText().toString());
                Helper_Transaction.getTransactionObject().setNoOfCoins(editText_Quantity.getText().toString());
//                Helper_Transaction.getTransactionObject().setTotalValue(textView_TotalCost.getText().toString());

                String totalValue = new BigDecimal(singleCoinPrice_Currency).multiply(new BigDecimal(editText_Quantity.getText().toString())).toString() ;
                Helper_Transaction.getTransactionObject().setTotalValue(totalValue);

                Log.e(LOG_TAG, Helper_Transaction.getTransactionObject().toString()) ;

                db.transactionDao().insertTransaction(Helper_Transaction.getTransactionObject());
                Message.display(context, "Transaction is added !");
                finish();


            }
        });




    }
}
