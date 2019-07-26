package apps.yoo.com.blockholdings.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.AppExecutors;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.network.NetworkRepository;
import apps.yoo.com.blockholdings.ui.background.Worker_UpdatePricesLog_1Coin;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;

public class Activity_Transaction3 extends AppCompatActivity implements MyListener.DialogFragments_to_ActivityTransaction{
    Context context ;
    private static final String LOG_TAG = "Activity_Transaction3 --> " ;
    AppDatabase db ;
    FragmentManager fragmentManager ;
    RequestQueue requestQueue ;
    NetworkRepository networkRepository ;

    RelativeLayout relLt_Exchange, relLt_TradingPair, relLt_BtnAddTransaction ;
    TextView textView_CoinName, textView_Exchange, textView_TradingPair, textView_Date, textView_SingleCoinPriceLabel,
            textView_AddButtonText, textView_TransactionTotalValue ;
    EditText editText_SingleCoinPrice, editText_Quantity, editText_Note, editText_Fee ;
    RadioButton radioButton_Buy, radioButton_Sell ;
    RadioGroup radioGroup_BuySell ;
    ProgressBar progressBarAddBtn ;
    Spinner spinner_FeeType ;
    Switch switch_DeductFromBase ;

    String coinId ;
    Object_Coin currentCoin ;
    Object_TransactionFullData currentTransactionFD ;
    Object_Transaction complementTransaction  ;
    Object_Currency currencyObj ;
    Table<String, String, String> table_ExchangePairData ;
    String p24hChange ;
    String singleCoinPrice_Currency ;
    String tradingPairPrice_Currency ;
    int feeType ;

    final int FEE_TYPE_FIAT_PERCENTAGE = 0 ;
    final int FEE_TYPE_FIAT_DIRECT = 1 ;
    final int FEE_TYPE_CRYPTO_TRANSACTIONCOIN_PERCENTAGE =2 ;
    final int FEE_TYPE_CRYPTO_TRANSACTIONCOIN_DIRECT = 3 ;
    final int FEE_TYPE_CRYPTO_TRADINGPAIRCOIN_DIRECT = 4 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction3);
        context = this ;
        fragmentManager = ((FragmentActivity)context).getSupportFragmentManager() ;
        requestQueue = Volley.newRequestQueue(context) ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        networkRepository = NetworkRepository.getInstance(context) ;

        if(getIntent().getStringExtra("coinId") == null) {
            Message.display(context, "Coin Not found error");
            finish();
        }


        currentCoin = db.coinDao().getCoinById(getIntent().getStringExtra("coinId")) ;
        currencyObj = MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()) ;

        getReferences() ;
        initTransaction();
        setBasicUi();
        getFromServer_CoinTickers();





    }

    private void initTransaction(){
        currentTransactionFD = new Object_TransactionFullData() ;
        currentTransactionFD.setCoinObject(currentCoin);
        currentTransactionFD.getTransactionObject().setCoinId(currentCoin.getId());
        currentTransactionFD.getTransactionObject().setPortFolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId());


    }




    private void getReferences(){
        textView_CoinName = findViewById(R.id.activityTransaction_TextView_CoinName) ;

        radioButton_Buy = findViewById(R.id.activityTransaction_RadioButton_Buy) ;
        radioButton_Sell = findViewById(R.id.activityTransaction_RadioButton_Sell) ;
        radioGroup_BuySell = findViewById(R.id.activityTransaction_RadioGroup_BuySell) ;

        relLt_Exchange = findViewById(R.id.activityTransaction_RelLt_ExchangeContainer) ;
        textView_Exchange = findViewById(R.id.activityTransaction_TextView_ValueExchange) ;

        relLt_TradingPair = findViewById(R.id.activityTransaction_RelLt_ContainerTradingPair) ;
        textView_TradingPair = findViewById(R.id.activityTransaction_TextView_ValueTradingPair) ;

        textView_SingleCoinPriceLabel = findViewById(R.id.activityTransaction_TextView_DescriptionSingleCoinPrice) ;
        editText_SingleCoinPrice = findViewById(R.id.activityTransaction_EditText_ValueSingleCoinPrice) ;

        editText_Quantity = findViewById(R.id.activityTransaction_EditText_ValueQuantity) ;

        textView_Date = findViewById(R.id.activityTransaction_TextView_ValueDate) ;

        spinner_FeeType = findViewById(R.id.activityTransaction_Spinner_ValueFeeType) ;
        editText_Fee= findViewById(R.id.activityTransaction_EditText_ValueFee) ;

        switch_DeductFromBase= findViewById(R.id.activityTransaction_Switch_ValueDeductFromBase) ;

        editText_Note= findViewById(R.id.activityTransaction_EditText_ValueNote) ;

        textView_TransactionTotalValue = findViewById(R.id.activityTransaction_TextView_TotalTransactionValue) ;
        relLt_BtnAddTransaction = findViewById(R.id.activityTransaction_relLt_BtnAddTransaction) ;
        textView_AddButtonText = findViewById(R.id.activityTransaction_TextView_AddBtnText) ;
        progressBarAddBtn = findViewById(R.id.activityTransaction_ProgressBar_AddBtnProgress) ;

    }




    private void getFromServer_CoinTickers(){

//        networkRepository.getAllTickersOfCoins(coinId, new MyNetworkResponse() {
////            @Override
////            public void onResponse(String response) {
////                Log.e(LOG_TAG, "Response from server is " + response) ;
////                processServerResponse_CoinTickers(response);
////            }
////
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Message.display(context, "Error in making volley request");
////                Log.e(LOG_TAG, error.toString() ) ;
////            }
////        });



        String url = Constants.UTL_APICALL_TRANSACTIONDATA_PREFIX + currentCoin.getId() + Constants.UTL_APICALL_TRANSACTIONDATA_SUFFIX ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                Log.e(LOG_TAG, "Response from server is " + response) ;
                                processServerResponse_CoinTickers(response);
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

    private void processServerResponse_CoinTickers(String respoonse) {

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
        setupEditTextQuantity_TextChangeListener() ;

        setupFeesSpinner(true);
        setupSwitchDeductFromBase();






    }


    private void setupFeesSpinner(boolean isTransactionGlobalAverage){
        if(isTransactionGlobalAverage){
            String[] defaultfeeTypes = {"%" + currencyObj.getCurrencyId(), currencyObj.getCurrencyId()} ;
            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, defaultfeeTypes) ;
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_FeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0 :
                            Message.display(context, "Percentage is selected");
                            break;
                        case 1  :
                            Message.display(context, "Direct is selected");
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner_FeeType.setAdapter(aa);
        } else {
            String[] feeNames = {"%" + currencyObj.getCurrencyId(), currencyObj.getCurrencyId(),
                    "%" + currentCoin.getSymbol(), currentCoin.getSymbol(),
                    currentTransactionFD.getTransactionObject().getTradingPair()
            } ;

            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, feeNames) ;
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_FeeType.setAdapter(aa);


        }

    }

    private void setupSwitchDeductFromBase(){
        //TODO do stuff if switch if already checked
        // TODO when value of quantity is changed, then check how much coin we have in our portfolio and
        // depending on that chang ethe value of switch


        switch_DeductFromBase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked is the new state
                if(isChecked){
                    currentTransactionFD.getTransactionObject().setDeductFromQuoteCoin(true);
                }else{
                    currentTransactionFD.getTransactionObject().setDeductFromQuoteCoin(false);

                }
            }
        });

    }





    private void setupBuySellRadioButton(){
        // we can't check for (getType == null) because by default int are always initialized as 0
        // so the following code checks that if the type of transaction is not initialized then by default
        // set it to buy transaction
        if(currentTransactionFD.getTransactionObject().getType() == 0){
            radioButton_Buy.setChecked(true);
            radioButton_Sell.setChecked(false);
            currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_BUY);

        }



        radioGroup_BuySell.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.activityTransaction_RadioButton_Buy :
                        Log.d(LOG_TAG, "Transaction type is selected to Buy" );
                        currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
                        break;
                    case R.id.activityTransaction_RadioButton_Sell :
                        Log.d(LOG_TAG, "Transaction type is selected to Sell" );
                        currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_SELL);
                        break;
                    default:
                        currentTransactionFD.getTransactionObject().setType(Object_Transaction.TYPE_BUY);
                        break;
                }
            }
        });
    }


    private void setupComplementTransactionTextView(){

    }

    private void setupTransactionDateTime(){
        Date currentDate = Calendar.getInstance().getTime() ;
        currentTransactionFD.getTransactionObject().setTransactionDateTime(currentDate);
        textView_Date.setText(new SimpleDateFormat("MMMM dd yyyy hh:mm a").format(currentDate)) ;
        textView_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_DateTimePicker dfDateTime = new DialogFragment_DateTimePicker() ;
                dfDateTime.show(fragmentManager, "dfDateTime");
            }
        });

    }


    @Override
    public void onSelectingDateTime(long timeInLong) {
        try {
            textView_Date.setText(new SimpleDateFormat("MMMM dd yyyy hh:mm a").format(new Date(timeInLong)));
        }catch (Exception e){
            Log.e(LOG_TAG, "dudeeee" + e.toString()) ;
        }
        currentTransactionFD.getTransactionObject().setTransactionDateTime(new Date(timeInLong));

    }

    private void setupEditTextQuantity_TextChangeListener(){
        editText_Quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(LOG_TAG, "Before text change is called") ;
                textView_TransactionTotalValue.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty() ){
                    return;
                }
                BigDecimal quantity = new BigDecimal(s.toString().trim()) ;
                BigDecimal totalTransactionValue = new BigDecimal(editText_SingleCoinPrice.getText().toString()).multiply(quantity) ;

                if(relLt_TradingPair.getVisibility() != View.GONE){
                    textView_TransactionTotalValue.setText("Total Value Of this Transaction is " +
                            totalTransactionValue + " "  +currentTransactionFD.getTransactionObject().getTradingPair());
                } else {
                    textView_TransactionTotalValue.setText("Total Value Of this Transaction is  " +
                            currencyObj.getCurrencySymbol() + totalTransactionValue );
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

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
                String date = new SimpleDateFormat("dd-MM-yyyy")
                        .format(currentTransactionFD.getTransactionObject().getTransactionDateTime() );
                bundle.putString("transactionDate",  date);
                Log.e(LOG_TAG, "Date sent to exchange is " + date) ;
                bundle.putString("coinId", currentCoin.getId());
                dfExchanges.setArguments(bundle);
                dfExchanges.show(fragmentManager, "dfExchanges");
            }
        });
    }


    @Override
    public void onSelectingExchange_GlobalAverage(String price) {
        Message.display(context, "Exchange is now selected ");
        Object_Exchange selectedExchange = Object_Exchange.getGlobalAverage() ;
        currentTransactionFD.setExchangeObject(selectedExchange);
        currentTransactionFD.getTransactionObject().setExchangeId(selectedExchange.getId());
        currentTransactionFD.getTransactionObject().setTradingPair("");
        currentTransactionFD.getTransactionObject().setSingleCoinPrice_TradingPair("");

        textView_Exchange.setText(selectedExchange.getName());
        relLt_TradingPair.setVisibility(View.GONE);
//        textView_TradingPair.setText("N/A");

        textView_SingleCoinPriceLabel.setText("Price per Coin (in " + currencyObj.getCurrencySymbol() + ")");
        editText_SingleCoinPrice.setText(price);
        makeTransaction_WithoutExchange();


    }


    private void makeTransaction_WithoutExchange(){
        relLt_BtnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relLt_BtnAddTransaction.setClickable(false);

                currentTransactionFD.getTransactionObject().setNote(editText_Note.getText().toString());
                currentTransactionFD.getTransactionObject().setNoOfCoins(editText_Quantity.getText().toString());

                singleCoinPrice_Currency = editText_SingleCoinPrice.getText().toString() ;
                currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyOriginal(singleCoinPrice_Currency);
                currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyCurrent(singleCoinPrice_Currency);

                String totalValue = new BigDecimal(singleCoinPrice_Currency).multiply(new BigDecimal(editText_Quantity.getText().toString())).toString() ;
                currentTransactionFD.getTransactionObject().setTotalValue_Original(totalValue);
                currentTransactionFD.getTransactionObject().setTotalValue_Current(totalValue);

//                setupFeesFiat(new BigDecimal(totalValue), currencyObj.getCurrencyId());
                computeTransactionFees(null);

                addTransactionToDatabase();

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

        setupFeesSpinner(false);
        setupCoinPrice_AfterSettingExchangeNTradingPair();

    }





    private void setupCoinPrice_AfterSettingExchangeNTradingPair(){
        String singleCoinPrice = table_ExchangePairData.get(currentTransactionFD.getTransactionObject().getExchangeId(), currentTransactionFD.getTransactionObject().getTradingPair()) ;
        editText_SingleCoinPrice.setText(singleCoinPrice);
        textView_SingleCoinPriceLabel.append(" (in " + currentTransactionFD.getTransactionObject().getTradingPair() + ")");
        setupAddTransactionButton_WitExchange();
    }






    private void computeTransactionFees(String tradingPairPrice_Currency){
        feeType = spinner_FeeType.getSelectedItemPosition() ;

        if(editText_Fee.getText() == null || editText_Fee.getText().toString().trim().isEmpty() ||
                editText_Fee.getText().toString().equalsIgnoreCase("0") ||
                (feeType == FEE_TYPE_CRYPTO_TRANSACTIONCOIN_DIRECT && tradingPairPrice_Currency == null) ){
            currentTransactionFD.getTransactionObject().setFeeCoinId(null);
            currentTransactionFD.getTransactionObject().setFeeInDollar(null);
            currentTransactionFD.getTransactionObject().setFeeNoOfCoins(null);
            currentTransactionFD.getTransactionObject().setIsFeeCoinFiat(false);
            currentTransactionFD.getTransactionObject().computeTotalValue_OriginalwFees();

            return;
        }


        boolean isFeeCoinFiat ;
        String feeCoinId ;
        BigDecimal feeNoOfCoins, feeValueInDollar  ;

        String input_feeNoOfCoins = editText_Fee.getText().toString().trim() ;
        switch (feeType){

            case FEE_TYPE_FIAT_PERCENTAGE :
                Log.d(LOG_TAG, "Case FEE_TYPE_FIAT_PERCENTAGE was selected") ;

                isFeeCoinFiat = true ;
                feeCoinId = currencyObj.getCurrencyId() ;

                feeNoOfCoins = new BigDecimal(currentTransactionFD.getTransactionObject().getTotalValue_Current())
                        .multiply(new BigDecimal(input_feeNoOfCoins))
                        .divide(new BigDecimal(100), 10, RoundingMode.HALF_UP) ;

                feeValueInDollar = feeNoOfCoins ;
                break;

            case FEE_TYPE_FIAT_DIRECT :
                Log.d(LOG_TAG, "Case FEE_TYPE_FIAT_DIRECT was selected") ;
                isFeeCoinFiat = true ;
                feeCoinId = currencyObj.getCurrencyId() ;
                feeNoOfCoins = new BigDecimal(input_feeNoOfCoins) ;

                feeValueInDollar = feeNoOfCoins ;

                break;

            case FEE_TYPE_CRYPTO_TRANSACTIONCOIN_PERCENTAGE :
                Log.d(LOG_TAG, "Case FEE_TYPE_CRYPTO_TRANSACTIONCOIN_PERCENTAGE was selected") ;
                isFeeCoinFiat = false ;
                feeCoinId = currentCoin.getSymbol() ;
                feeNoOfCoins = new BigDecimal(currentTransactionFD.getTransactionObject().getNoOfCoins())
                        .multiply(new BigDecimal(input_feeNoOfCoins))
                        .divide(new BigDecimal(100), 10, RoundingMode.HALF_UP) ;

                feeValueInDollar = feeNoOfCoins.multiply(
                        new BigDecimal(currentTransactionFD.getTransactionObject().getSingleCoinPrice_CurrencyOriginal())) ;

                break;

            case FEE_TYPE_CRYPTO_TRANSACTIONCOIN_DIRECT :
                Log.d(LOG_TAG, "Case FEE_TYPE_CRYPTO_TRANSACTIONCOIN_DIRECT was selected") ;
                isFeeCoinFiat = false ;
                feeCoinId = currentCoin.getSymbol() ;
                feeNoOfCoins = new BigDecimal(input_feeNoOfCoins) ;

                feeValueInDollar = feeNoOfCoins.multiply(new BigDecimal(tradingPairPrice_Currency)) ;

                break;

            case FEE_TYPE_CRYPTO_TRADINGPAIRCOIN_DIRECT :
                Log.d(LOG_TAG, "Case FEE_TYPE_CRYPTO_TRADINGPAIRCOIN_DIRECT was selected") ;
                isFeeCoinFiat = false ;
                feeCoinId = currentTransactionFD.getTransactionObject().getTradingPair();
                feeNoOfCoins = new BigDecimal(input_feeNoOfCoins) ;

                feeValueInDollar = feeNoOfCoins.multiply(
                        new BigDecimal(currentTransactionFD.getTransactionObject().getSingleCoinPrice_TradingPair())) ;
                break;

            default:
                Log.d(LOG_TAG, "Case default was selected") ;
                isFeeCoinFiat = true ;
                feeCoinId = null ;
                feeNoOfCoins = new BigDecimal(0) ;
                feeValueInDollar = new BigDecimal(0) ;
                break;

        }


        currentTransactionFD.getTransactionObject().setIsFeeCoinFiat(isFeeCoinFiat);
        currentTransactionFD.getTransactionObject().setFeeCoinId(feeCoinId);
        currentTransactionFD.getTransactionObject().setFeeNoOfCoins(feeNoOfCoins.toPlainString());
        currentTransactionFD.getTransactionObject().setFeeInDollar(feeValueInDollar.toPlainString());
        currentTransactionFD.getTransactionObject().computeTotalValue_OriginalwFees();





    }


    private void setupAddTransactionButton_WitExchange(){
        relLt_BtnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relLt_BtnAddTransaction.setClickable(false);
                textView_AddButtonText.setVisibility(View.INVISIBLE);
                progressBarAddBtn.setVisibility(View.VISIBLE);


                if(currentTransactionFD.getTransactionObject().getTradingPair() == null){
                    // this means that global average is selected .
                    // so transaction is added directly without fetching price
                    makeTransaction_withExchange( null) ;
                    return;
                }

                Object_Coin tradingPairCoinObject = db.coinDao().getCoinBySymbol(currentTransactionFD.getTransactionObject().getTradingPair().toLowerCase()) ;
                if(tradingPairCoinObject == null){
                    // this means that this trading pair is not a coin on the website
                    // so we do not need to make a url call for the price of this coin

                    makeTransaction_withExchange( null) ;
                    return;
                }
                String tradingPairId = tradingPairCoinObject.getId() ;
                Log.e(LOG_TAG, tradingPairId) ;

                getFromServer_CurrencyPriceForTradingPair(tradingPairId);
            }
        });
    }



    private void getFromServer_CurrencyPriceForTradingPair(String tradingPairId){

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
                        tradingPairPrice_Currency = processServerResponse_CurrencyPriceForTradingPair(response);
                        makeTransaction_withExchange(tradingPairPrice_Currency);
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

    private String processServerResponse_CurrencyPriceForTradingPair(String response){
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


    private void makeTransaction_withExchange(String tradingPairPrice_Currency){

        if(tradingPairPrice_Currency == null){
            singleCoinPrice_Currency = singleCoinPrice_Currency ;
        } else {
            Log.e(LOG_TAG, "Original singleCoinPrice_Currency : " + singleCoinPrice_Currency) ;
            singleCoinPrice_Currency = new BigDecimal(editText_SingleCoinPrice.getText().toString())
                    .multiply(new BigDecimal(tradingPairPrice_Currency))
                    .toPlainString() ;
            Log.e(LOG_TAG, "New singleCoinPrice_Currency : " + singleCoinPrice_Currency) ;
        }



        currentTransactionFD.getTransactionObject().setNote(editText_Note.getText().toString());




        currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyOriginal(singleCoinPrice_Currency);
        currentTransactionFD.getTransactionObject().setSingleCoinPrice_CurrencyCurrent(singleCoinPrice_Currency);



        currentTransactionFD.getTransactionObject().setSingleCoinPrice_TradingPair(editText_SingleCoinPrice.getText().toString());
        currentTransactionFD.getTransactionObject().setNoOfCoins(editText_Quantity.getText().toString());

        String totalValue = new BigDecimal(singleCoinPrice_Currency).multiply(new BigDecimal(editText_Quantity.getText().toString())).toString() ;
        currentTransactionFD.getTransactionObject().setTotalValue_Original(totalValue);
        currentTransactionFD.getTransactionObject().setTotalValue_Current(totalValue);

        computeTransactionFees(tradingPairPrice_Currency);
        makeComplementTransaction_TradingPair(tradingPairPrice_Currency) ;

        Log.e(LOG_TAG, Helper_Transaction.getTransactionFullDataObject().getTransactionObject().toString()) ;

        addTransactionToDatabase();
    }




    private void addTransactionToDatabase(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.transactionDao().insertTransaction(currentTransactionFD.getTransactionObject());
                if(currentTransactionFD.getTransactionObject().isDeductFromQuoteCoin()){
                    db.transactionDao().insertTransaction(complementTransaction);
                }
                MyGlobals.refreshPortfolioValue(db);
                Log.d(LOG_TAG, "Transaction is added : " + currentTransactionFD) ;

                OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(Worker_UpdatePricesLog_1Coin.class)
                        .addTag("Worker_UpdatePricesLog_1Coin")
                        .setInputData(new Data.Builder().putString("coinId", currentCoin.getId()).build())
                        .build() ;
                WorkManager.getInstance().enqueue(workRequest1) ;

                finish();
            }
        });

    }

    private void makeComplementTransaction_TradingPair(String tradingPairPrice_Currency){
        if(!currentTransactionFD.getTransactionObject().isDeductFromQuoteCoin()){
            return;
        }

        complementTransaction = new Object_Transaction() ;

        complementTransaction.setPortFolioId(MyGlobals.getCurrentPortfolioObj().getPortfolioId());
        complementTransaction.setCoinId(currentTransactionFD.getTransactionObject().getTradingPair().toLowerCase());
        complementTransaction.setBaseCoinFiat(false);


        if(currentTransactionFD.getTransactionObject().getType() == Object_Transaction.TYPE_BUY){
            complementTransaction.setType(Object_Transaction.TYPE_SELL);
        }else if(currentTransactionFD.getTransactionObject().getType() == Object_Transaction.TYPE_SELL){
            complementTransaction.setType(Object_Transaction.TYPE_BUY);
        }

        complementTransaction.setExchangeId(currentTransactionFD.getTransactionObject().getExchangeId());
        complementTransaction.setTradingPair(currentTransactionFD.getTransactionObject().getCoinId());

        complementTransaction.setSingleCoinPrice_TradingPair(null); //not needed  value = 1/currentTransaction.getSingleCoinPrice_TradingPair


        BigDecimal totalNoOfCoins = new BigDecimal(currentTransactionFD.getTransactionObject().getSingleCoinPrice_TradingPair())
                .multiply(new BigDecimal(currentTransactionFD.getTransactionObject().getNoOfCoins())) ;
        complementTransaction.setNoOfCoins(totalNoOfCoins.toPlainString());


        complementTransaction.setSingleCoinPrice_CurrencyOriginal(tradingPairPrice_Currency);
        Log.d(LOG_TAG, "TradingPairPrice_Currency : "  + tradingPairPrice_Currency) ;

        BigDecimal totalValue_Original = new BigDecimal(tradingPairPrice_Currency).multiply(totalNoOfCoins) ;
        Log.d(LOG_TAG, "totalValue_Original : "  + totalValue_Original) ;

        complementTransaction.setTotalValue_Original(new BigDecimal(tradingPairPrice_Currency).multiply(totalNoOfCoins).toPlainString());
        complementTransaction.setTotalValue_OriginalwFees(complementTransaction.getTotalValue_Original()); //No point of fees in compliment trx

        complementTransaction.setSingleCoinPrice_CurrencyCurrent(tradingPairPrice_Currency);
        complementTransaction.setTotalValue_Current(complementTransaction.getTotalValue_Original());

        complementTransaction.setTransactionDateTime(currentTransactionFD.getTransactionObject().getTransactionDateTime());
        complementTransaction.setNote("");

        complementTransaction.setTransactionComplement(true);











    }



}
