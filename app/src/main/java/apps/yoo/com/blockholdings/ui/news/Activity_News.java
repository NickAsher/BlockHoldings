package apps.yoo.com.blockholdings.ui.news;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.ui.home.Activity_Portfolio;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;
import apps.yoo.com.blockholdings.ui.watchlist.Activity_WatchlistContainer;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;

public class Activity_News extends AppCompatActivity {
    Context context ;
    String LOG_TAG = "Activity_News --> " ;

    ArrayList<Object_NewsArticle> listOfArticles ;

    BottomNavigationView btmNavigationView ;
    RecyclerView rv ;
    RVAdapter_NewsArticle adapter ;

    RelativeLayout layout_MainContainer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_news);
        context = this ;

        getReferences();
        setupBottomNavigationView();
        fetchData();
        setupRecyclerView();

        layout_MainContainer.setBackground(MySharedPreferences.getAppThemeGradientDrawableOnPreference(getApplicationContext()));
    }

    private void getReferences(){
        btmNavigationView = (BottomNavigationView)findViewById(R.id.activityNews_BottomNavigationView_Main) ;
        rv = (RecyclerView)findViewById(R.id.activityNews_RecyclerView_Content) ;
        layout_MainContainer = findViewById(R.id.activityNews_RelLt_MainContainer) ;

    }


    private void fetchData(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.RSS_LINK_COINTDESK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listOfArticles.addAll(Helper_XMLParser.parseRssFeed2(response, LOG_TAG)) ;
                        adapter.refreshData(listOfArticles);
//                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in making volley request");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        }) ;


        RequestQueue requestQueue = Volley.newRequestQueue(context) ;
        requestQueue.add(stringRequest) ;

    }

    private void setupBottomNavigationView(){
        btmNavigationView.setSelectedItemId(R.id.menuBottomNavigation_Item_News);

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
                        break;

                    case R.id.menuBottomNavigation_Item_Settings :
                            Intent intent3 = new Intent(context, Activity_Settings.class) ;
                            startActivity(intent3);
                            finish();
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




    private void setupRecyclerView(){
        listOfArticles = new ArrayList<>() ;
        adapter = new RVAdapter_NewsArticle(context, listOfArticles) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }

}
