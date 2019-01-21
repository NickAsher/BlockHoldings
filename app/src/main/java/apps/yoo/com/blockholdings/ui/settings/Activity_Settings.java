package apps.yoo.com.blockholdings.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.Objects.Object_Exchange;
import apps.yoo.com.blockholdings.data.Objects.Object_Coin;
import apps.yoo.com.blockholdings.ui.home.Activity_Portfolio;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;


public class Activity_Settings extends AppCompatActivity {
    Context context ;
    private static final String LOG_TAG = "Activity_settings -->" ;
    AppDatabase db ;
    int theme ;
    RequestQueue requestQueue ;

    BottomNavigationView btmNavigationView ;
    Switch switch_ThemeChanger ;
    TextView textView_ThemeValue ;
    Button btn_RefreshAllCoins ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theme = MySharedPreferences.getAppThemeFromPreference(getApplicationContext()) ;
        if(theme == MySharedPreferences.PREFS_VALUE_THEME_DARK){
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        setContentView(R.layout.activity_settings);
        context = this;

        db = AppDatabase.getInstance(getApplicationContext()) ;
        requestQueue = Volley.newRequestQueue(context) ;

        getReferences();
        setupBottomNavigationView();
        setupThemeChangingSwitchButton();
        setupRefreshDataButton() ;
    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activitySettings_BottomNavigationView_Main) ;
        switch_ThemeChanger = (Switch)findViewById(R.id.activitySetting_Switch_ThemeChanger) ;
        textView_ThemeValue = (TextView)findViewById(R.id.activitySetting_TextView_ValueTheme) ;
        btn_RefreshAllCoins = (Button)findViewById(R.id.activitySettings_Button_RefreshData) ;




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

    private void setupRefreshDataButton(){
        btn_RefreshAllCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_APICALL_ALLCOINS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                Log.e(LOG_TAG, "Response from server is " + response) ;
                                ArrayList<Object_Coin> listOfCoins = Helper_Settings.getListOfCoins_FromApi(response) ;
//                                Log.e(LOG_TAG, listOfCoins.toString()) ;
                                db.coinDao().deleteWholeTable() ;
                                db.coinDao().insertManyCoins(listOfCoins);
                                Message.display(context, "Resresh of All Coins is complete");


//                        processResponse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.display(context, "Error in making volley request");
                        Log.e(LOG_TAG, error.toString() ) ;
                    }
                }) ;


                requestQueue.add(stringRequest) ;


                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, Constants.URL_APICALL_EXCHANGES,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                Log.e(LOG_TAG, "Response from server is " + response) ;
                                ArrayList<Object_Exchange> listOfExchanges = Helper_Settings.getListOfExchanges_FromApi(response) ;
//                                Log.e(LOG_TAG, listOfCoins.toString()) ;
                                db.exchangeDao().deleteWholeTable() ;
                                db.exchangeDao().insertManyExchanges(listOfExchanges);
                                Message.display(context, "Resresh of All Exchanges is complete");


//                        processResponse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.display(context, "Error in making volley request");
                        Log.e(LOG_TAG, error.toString() ) ;
                    }
                }) ;

                requestQueue.add(stringRequest2) ;

            }

        });

    }

    private void changeAppTheme(){

    }

}
