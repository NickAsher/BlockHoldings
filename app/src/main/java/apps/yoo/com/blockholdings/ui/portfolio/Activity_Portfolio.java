package apps.yoo.com.blockholdings.ui.portfolio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.ui.detail.ChartMarkerView;
import apps.yoo.com.blockholdings.ui.detail.ChartValueFormatter_XAxis;
import apps.yoo.com.blockholdings.ui.home.Activity_Home;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;
import apps.yoo.com.blockholdings.ui.watchlist.Activity_WatchlistContainer;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;


public class Activity_Portfolio extends AppCompatActivity implements MyListener.dlgFrg_AddNewPortfolio_to_DlgFrg_Portfolio, MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio, MyListener.dlgFrg_EditPortfolioName_to_DlgFrg_Portfolio{
    Context context ;
    String LOG_TAG = "Activity_Portfolio --> " ;
    AppDatabase db;
    FragmentManager fragmentManager ;

    BottomNavigationView btmNavigationView ;
    RecyclerView rv ;
    RelativeLayout layout_MainContainer ;
    Fragment_PortfolioBrief fragment_portfolioBrief ;


    Object_Portfolio currentPortfolioObj ;
    RVAdapter_Portfolio adapter ;
    List<Object_Portfolio> listOfPortfolios ;
    Object_Currency currentCurrency ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        fragmentManager = getSupportFragmentManager() ;

        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(getApplicationContext()) ;
        listOfPortfolios = db.portfolioDao().getListOfAllPortfolios() ;
        currentPortfolioObj = db.portfolioDao().getPortfolioById(MySharedPreferences.getPortfolioId_FromPreference(getApplicationContext())) ;


        getReferences();
        setupBottomNavigationView();
        setupRecyclerView();
        setupAddNewPortfolio() ;


    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activityHome_BottomNavigationView_Main) ;
        rv = findViewById(R.id.activityPortfolio_RecyclerView_transactions) ;
        layout_MainContainer = findViewById(R.id.activityPortfolio_RelLt_MainContainer) ;
        fragment_portfolioBrief = (Fragment_PortfolioBrief) fragmentManager.findFragmentById(R.id.activityPortfolio_Fragment_PortfolioBriefInfo) ;
    }



    private void setupBottomNavigationView(){
        btmNavigationView.setSelectedItemId(R.id.menuBottomNavigation_Item_Home);

        btmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuBottomNavigation_Item_Home :
                        Intent intenti = new Intent(context, Activity_Home.class) ;
                        startActivity(intenti);
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
                        Intent intent3 = new Intent(context, Activity_Settings.class) ;
                        startActivity(intent3);
                        finish();
                        break;

                    default:
                        Intent intent = new Intent(context, Activity_Home.class) ;
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }


    private void setupAddNewPortfolio(){
        FloatingActionButton fab_AddCoin = findViewById(R.id.activityPortfolio_FAB_AddCoin) ;
        fab_AddCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_AddNewPortfolio dfAddNewPortfolio = new DialogFragment_AddNewPortfolio() ;
                dfAddNewPortfolio.show(fragmentManager, "dfAddNewPortfolio");
            }
        });
    }

    @Override
    public void onAddingNewPortfolio() {
        adapter.refreshData(db.portfolioDao().getListOfAllPortfolios());
    }



    private void setupRecyclerView(){
        adapter = new RVAdapter_Portfolio(context, listOfPortfolios, this) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        DividerItemDecoration plainHorizontalLines = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL) ;
        rv.addItemDecoration(plainHorizontalLines);
    }





    @Override
    public void onSelectingPortfolio(int newPortfolioId) {
        Object_Portfolio newPortfolioObj = db.portfolioDao().getPortfolioById(newPortfolioId) ;
        MyGlobals.setupCurrentPortfolioObj(newPortfolioObj);
        MySharedPreferences.setPortfolioId_InPreferences(context.getApplicationContext(), newPortfolioId);
        //TODO set the effect of the change to the fragment portfolio
        fragment_portfolioBrief.onSelectingNewPortfolio(newPortfolioId);
        adapter.notifyDataSetChanged();
        Message.display(context, "Portfolio has been changed");






    }

    @Override
    public void onEditingPortfolioName() {
        adapter.refreshData(db.portfolioDao().getListOfAllPortfolios());

    }

    @Override
    public void onDeletingPortfolio(int portfolioId) {
        db.transactionDao().deleteTransactions_OfPortfolio(portfolioId);
        db.portfolioDao().deletePortfolioById(portfolioId);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, Activity_Home.class) ;
        startActivity(intent);
        finish();
    }
}
