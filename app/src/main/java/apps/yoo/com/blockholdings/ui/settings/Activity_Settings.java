package apps.yoo.com.blockholdings.ui.settings;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.ui.background.Worker_UpdatePreviousPricesLog;
import apps.yoo.com.blockholdings.ui.general.BackgroundDataUpdater;
import apps.yoo.com.blockholdings.ui.home.Activity_Portfolio;
import apps.yoo.com.blockholdings.ui.background.Worker_CurrencyUpdater_SingleCoinPriceOriginal;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.ui.watchlist.Activity_WatchlistContainer;
import apps.yoo.com.blockholdings.util.MyListener;


public class Activity_Settings extends AppCompatActivity implements MyListener.DialogFragmentCurrency_to_ActivitySettings {
    Context context ;
    private static final String LOG_TAG = "Activity_settings -->" ;
    AppDatabase db ;
    int theme ;
    RequestQueue requestQueue ;

    BottomNavigationView btmNavigationView ;
    Switch switch_ThemeChanger ;
    TextView textView_ThemeValue, textView_currencyValue ;
    Button btn_RefreshAllCoins ;
    RelativeLayout relLt_ContainerCurrency ;
    FragmentManager fragmentManager ;

    RelativeLayout layout_MainContainer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        context = this;

        db = AppDatabase.getInstance(getApplicationContext()) ;
        requestQueue = Volley.newRequestQueue(context) ;
        fragmentManager = getSupportFragmentManager() ;

        getReferences();
        setupBottomNavigationView();
        setupThemeChangingSwitchButton();
        setupRefreshDataButton() ;
        setupCurrencyButton();

        layout_MainContainer.setBackground(MySharedPreferences.getAppThemeGradientDrawableOnPreference(getApplicationContext()));
    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activitySettings_BottomNavigationView_Main) ;
        switch_ThemeChanger = (Switch)findViewById(R.id.activitySetting_Switch_ThemeChanger) ;
        textView_ThemeValue = (TextView)findViewById(R.id.activitySetting_TextView_ValueTheme) ;
        textView_currencyValue = findViewById(R.id.activitySetting_TextView_ValueCurrency) ;
        btn_RefreshAllCoins = (Button)findViewById(R.id.activitySettings_Button_RefreshData) ;
        relLt_ContainerCurrency = findViewById(R.id.activitySettings_RelLt_ContainerCurrency) ;

        layout_MainContainer = findViewById(R.id.activitySettings_RelLt_MainContainer) ;
    }
    private void setupBottomNavigationView(){

        btmNavigationView.setSelectedItemId(R.id.menuBottomNavigation_Item_Settings);

        btmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuBottomNavigation_Item_Home :
                            Intent intent = new Intent(context, Activity_Portfolio.class) ;
                            startActivity(intent);
                            finish();
                        break;

                    case R.id.menuBottomNavigation_Item_Watchlist :
                        Intent intentW = new Intent(context, Activity_WatchlistContainer.class) ;
                        startActivity(intentW);
                        finish();
                        break;

                    case R.id.menuBottomNavigation_Item_News :
                            Intent intent2 = new Intent(context, Activity_News.class) ;
                            startActivity(intent2);
                            finish();

                        break;

                    case R.id.menuBottomNavigation_Item_Settings :
                        break;

                    default:
                            Intent intent4 = new Intent(context, Activity_Portfolio.class) ;
                            startActivity(intent4);
                            finish();

                        break;
                }
                return true;
            }
        });
    }



    public void setupThemeChangingSwitchButton(){

        if(theme == MySharedPreferences.PREFS_VALUE_THEME_DARK){
            textView_ThemeValue.setText("Dark");
            switch_ThemeChanger.setChecked(true);

        } else {
            textView_ThemeValue.setText("Light");
            switch_ThemeChanger.setChecked(false);
        }

        switch_ThemeChanger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textView_ThemeValue.setText("Dark");
                    MySharedPreferences.setAppThemeInPreferences(getApplicationContext(), MySharedPreferences.PREFS_VALUE_THEME_DARK);
//                    switch_ThemeChanger.setChecked(true);
                } else {
                    textView_ThemeValue.setText("Light");
                    MySharedPreferences.setAppThemeInPreferences(getApplicationContext(), MySharedPreferences.PREFS_VALUE_THEME_LIGHT);
                }
            }
        });

    }

    private void setupCurrencyButton(){
        Object_Currency newCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context) ;
        textView_currencyValue.setText(newCurrency.getCurrencyName() + " (" + newCurrency.getCurrencySymbol() + ")");

        relLt_ContainerCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_Currency dfCurrency = new DialogFragment_Currency() ;
                dfCurrency.show(fragmentManager, "dfCurrency");
            }
        });

    }

    @Override
    public void onCurrencySelected(Object_Currency currencyObj) {
        MySharedPreferences.setCurrency_InPreferences((context.getApplicationContext()), currencyObj);
        textView_currencyValue.setText(currencyObj.getCurrencyName() + " (" + currencyObj.getCurrencySymbol() + ")");


        OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(BackgroundDataUpdater.class)
                .addTag("BackgroundDataUpdater")
                .build() ;


        OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(Worker_CurrencyUpdater_SingleCoinPriceOriginal.class)
                .addTag("Worker_CurrencyUpdater_SingleCoinPriceOriginal") // used just like findFragmentByTag
                .build() ;

        OneTimeWorkRequest workRequest3 = new OneTimeWorkRequest.Builder(Worker_UpdatePreviousPricesLog.class)
                .addTag("Worker_UpdatePreviousPricesLog") // used just like findFragmentByTag
                .build() ;




        WorkManager.getInstance().enqueue(workRequest1) ;
        WorkManager.getInstance().enqueue(workRequest2) ;
        WorkManager.getInstance().enqueue(workRequest3) ;


    }

    private void setupRefreshDataButton(){

    }




    private void changeAppTheme(){

    }


}
