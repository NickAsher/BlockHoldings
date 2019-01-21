package apps.yoo.com.blockholdings.ui.home;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.Objects.Object_Coin;

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
        listOfSelectedCoins = db.coinDao().getListOfAllCoins() ;
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
            public boolean onQueryTextChange(String newText) {
                List<Object_Coin> listOfSelectedCoins = db.coinDao().getListOfCoins_WhoseNameStartWithAlphabet(newText) ;
                Log.e(LOG_TAG, listOfSelectedCoins.toString()) ;
                adapter.refreshData(listOfSelectedCoins);
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
