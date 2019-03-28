package apps.yoo.com.blockholdings.ui.general;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_WidgetCoinPrice;
import apps.yoo.com.blockholdings.ui.home.Activity_CoinSelector;
import apps.yoo.com.blockholdings.ui.settings.DialogFragment_Currency;
import apps.yoo.com.blockholdings.util.MyListener;

/**
 * The configuration screen for the {@link Widget_CoinPrice Widget_CoinPrice} AppWidget.
 */
public class Activity_ConfigureWidget_CoinPrice extends AppCompatActivity implements MyListener.DialogFragmentCurrency_to_ActivitySettings{
    Context context ;
    String LOG_TAg = "Activity_ConfigureWidget_CoinPrice --> " ;
    AppDatabase db ;
    FragmentManager fragmentManager ;
    AppWidgetManager appWidgetManager ;



    Button btn_ChooseCoin, btn_ChooseCurrency, btn_SaveConfig ;
    TextView textView_selectedCurrency, textView_selectedCoin ;

    String coinId ;
    Object_Currency selectedCurrencyObj ;
    final int INTENT_REQUEST_CODE_CHOOSE_COIN = 1 ;

    int widgetId ;



    public Activity_ConfigureWidget_CoinPrice() {
        super();
    }



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_OK);


        setContentView(R.layout.activity_configurewidget_coinprice);
        context  = this ;
        fragmentManager = getSupportFragmentManager() ;
        appWidgetManager = AppWidgetManager.getInstance(context);
        db = AppDatabase.getInstance(context.getApplicationContext()) ;
        selectedCurrencyObj = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;




        setWidgetId() ;
        getReferences() ;
        setCurrencyTextView() ;
        setupChooseCoinButton();
        setupSaveConfigButton();
    }


    private void setWidgetId(){
        // by default lets set the widget id to a invalid thing
        widgetId= AppWidgetManager.INVALID_APPWIDGET_ID;

        Bundle bundle = getIntent().getExtras() ;
        if (bundle != null) {
            widgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            // If this activity was started with an intent without an app widget ID, finish with an error.
            finish();
        }
    }


    private void getReferences(){

        btn_ChooseCoin = findViewById(R.id.activityWidgetCoinPrice_Btn_ChooseCoin) ;
        btn_ChooseCurrency = findViewById(R.id.activityWidgetCoinPrice_Btn_ChooseCurrency) ;
        btn_SaveConfig = findViewById(R.id.activityWidgetCoinPrice_Btn_SaveConfig) ;
//        textView_selectedCurrency = findViewById(R.id.activityWidgetCoinPrice_TV_ValueCurrency) ;
//        textView_selectedCoin = findViewById(R.id.activityWidgetCoinPrice_TV_ValueCoin) ;
    }


    private void setCurrencyTextView(){
//        textView_selectedCurrency.setText(selectedCurrencyObj.getCurrencyName() + " " + selectedCurrencyObj.getCurrencySymbol());
        btn_ChooseCurrency.setText(selectedCurrencyObj.getCurrencyName() + " " + selectedCurrencyObj.getCurrencySymbol());


    }

    private void setupChooseCoinButton(){

        btn_ChooseCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_CoinSelector.class) ;
                intent.putExtra("exclude_widget", true) ;
                startActivityForResult(intent, INTENT_REQUEST_CODE_CHOOSE_COIN);
            }
        });

        btn_ChooseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_Currency dfCurrency = new DialogFragment_Currency() ;
                dfCurrency.show(fragmentManager, "dfCurrency");
            }
        });

    }


    private void setupSaveConfigButton(){
        btn_SaveConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.widgetCoinPriceDao().insertWidget( new Object_WidgetCoinPrice(widgetId, coinId, selectedCurrencyObj.getCurrencyId(), Calendar.getInstance().getTime() + "")) ;

                Object_Coin coinObj  = db.coinDao().getCoinById(coinId) ;
                Widget_CoinPrice.createMyAppWidget(context, appWidgetManager, widgetId, coinObj.getId(), coinObj.getName());

                // Make sure we pass back the original widgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);

                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundDataUpdater.class)
                        .addTag("idTag_BackgroundWorker_3")
                        .build() ;
                WorkManager.getInstance().enqueue(workRequest) ;
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE_CHOOSE_COIN) {
            if(resultCode == Activity.RESULT_OK){
                coinId = data.getStringExtra("coinId");
                btn_ChooseCoin.setText(coinId);
            }

            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }


    @Override
    public void onCurrencySelected(Object_Currency currencyObj) {
        selectedCurrencyObj = currencyObj ;
        setCurrencyTextView();
    }
}

