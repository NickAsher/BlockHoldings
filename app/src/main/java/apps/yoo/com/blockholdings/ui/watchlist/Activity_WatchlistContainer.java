package apps.yoo.com.blockholdings.ui.watchlist;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.ui.home.Activity_Home;
import apps.yoo.com.blockholdings.ui.news.Activity_News;
import apps.yoo.com.blockholdings.ui.settings.Activity_Settings;

public class Activity_WatchlistContainer extends AppCompatActivity {
    Context context ;
    String LOG_TAG = "Activity_WatchlistContainer --> " ;
    AppDatabase db;
    FragmentManager fragmentManager ;

    ViewPager viewPager ;
    TabLayout tabLayout ;
    BottomNavigationView btmNavigationView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_container);

        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        fragmentManager = getSupportFragmentManager() ;

        getReferences() ;
        setupViewPager() ;
        setupBottomNavigationView();
    }

    private void getReferences(){
        viewPager = findViewById(R.id.activityWatchlistContainer_ViewPager_Main) ;
        tabLayout = findViewById(R.id.activityWatchlistContainer_TabLayout_Main) ;
        btmNavigationView = findViewById(R.id.activityWatchlist_BottomNavigationView_Main) ;
    }


    private void setupViewPager(){
        PagerAdapter_WatchlistContainer adapter_detail = new PagerAdapter_WatchlistContainer(fragmentManager, context) ;
        viewPager.setAdapter(adapter_detail);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupBottomNavigationView(){
        btmNavigationView.setSelectedItemId(R.id.menuBottomNavigation_Item_Watchlist);

        btmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuBottomNavigation_Item_Home :
                        Intent intent = new Intent(context, Activity_Home.class) ;
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.menuBottomNavigation_Item_Watchlist :
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
                        Intent intent4 = new Intent(context, Activity_Home.class) ;
                        startActivity(intent4);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

}
