package apps.yoo.com.blockholdings.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;
import apps.yoo.com.blockholdings.ui.news.RVAdapter_NewsArticle;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;
import apps.yoo.com.blockholdings.ui.news.Activity_News;


public class Activity_Portfolio extends AppCompatActivity {
    Context context ;
    String LOG_TAG = "Activity_Portfolio --> " ;
    AppDatabase db;

    BottomNavigationView btmNavigationView ;
    RecyclerView rv ;
    RVAdapter_Portfolio adapter ;

    List<Object_Transaction> listOfTransactions ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.setAppThemeBasedOnPreference(this, getApplicationContext());
        setContentView(R.layout.activity_home);
        context = this ;

        db = AppDatabase.getInstance(getApplicationContext()) ;
        getReferences();
        setupBottomNavigationView();
        setupRecyclerView();
        setupFAB() ;
    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activityHome_BottomNavigationView_Main) ;
        rv = findViewById(R.id.activityPortfolio_RecyclerView_transactions) ;
    }

    private void setupBottomNavigationView(){
        btmNavigationView.setSelectedItemId(R.id.menuBottomNavigation_Item_Home);

        btmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuBottomNavigation_Item_Home :
                        break;

                    case R.id.menuBottomNavigation_Item_News :
                            Intent intent2 = new Intent(context, Activity_News.class) ;
                            startActivity(intent2);
                        finish();
                        break;

                    case R.id.menuBottomNavigation_Item_Settings :
                            Intent intent3 = new Intent(context, Activity_Settings.class) ;
                            startActivity(intent3);
                        finish();
                        break;

                    default:
                            Intent intent = new Intent(context, Activity_Portfolio.class) ;
                            startActivity(intent);
                            finish();
                        break;
                }
                return true;
            }
        });
    }


    private void setupFAB(){
        FloatingActionButton fab_AddCoin = findViewById(R.id.activityHome_FAB_AddCoin) ;
        fab_AddCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_CoinSelector.class) ;
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView(){
        listOfTransactions = db.transactionDao().getListOfAllTransactions() ;
        adapter = new RVAdapter_Portfolio(context, listOfTransactions) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        DividerItemDecoration plainHorizontalLines = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL) ;
        rv.addItemDecoration(plainHorizontalLines);
    }
}
