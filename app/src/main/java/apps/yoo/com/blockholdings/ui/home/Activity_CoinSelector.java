package apps.yoo.com.blockholdings.ui.home;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Coin;

public class Activity_CoinSelector extends AppCompatActivity {
    Context context ;
    private static final String LOG_TAG = "Activity_CoinSelector -->" ;
    AppDatabase db ;

    SearchView searchViewCoin ;
    RecyclerView rv ;
    RVAdapter_CoinSelector adapter ;

    List<Object_Coin> listOfSelectedCoins ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_selector);

        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        if(getIntent().hasExtra("exclude_watchlist")){
            listOfSelectedCoins = db.coinDao().getListOfAllCoins_ExcludeWatchlist() ;
        } else if(getIntent().hasExtra("exclude_widget")){
            listOfSelectedCoins = db.coinDao().getListOfAllCoins_ExcludeWidgetCoins() ;
        } else {
            listOfSelectedCoins = db.coinDao().getListOfAllCoins() ;
        }
        getReferences() ;
        setupSearchView() ;
        setupRecyclerView();



    }

    private void getReferences(){
        searchViewCoin = findViewById(R.id.activityCoinSelector_SearchView_Main) ;
        rv = (RecyclerView)findViewById(R.id.activityCoinSelector_RecyclerView_CoinList) ;

    }


    private void setupSearchView(){
        searchViewCoin.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.isEmpty()){
                    Log.v(LOG_TAG, "Filtering with predicate") ;
                    adapter.refreshData(new ArrayList<Object_Coin>(Collections2.filter(listOfSelectedCoins, Object_Coin.getPredicateFilter_NameSymbol_startsWithAlphabet(newText))));
                }
                 return true;
            }
        });
    }

    private void setupRecyclerView(){

        adapter = new RVAdapter_CoinSelector(context, listOfSelectedCoins) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }


}
