package apps.yoo.com.blockholdings.ui.watchlist;

import android.app.Activity;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.AppExecutors;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_WatchlistCoinId;
import apps.yoo.com.blockholdings.ui.home.Activity_CoinSelector;
import apps.yoo.com.blockholdings.util.Message;

import  apps.yoo.com.blockholdings.util.Constants;


public class Fragment_WatchlistSelected extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_DetailPrice --> " ;
    AppDatabase db;
    RequestQueue requestQueue ;

    RecyclerView rv ;
    FloatingActionButton fab_AddNewCoin ;

    List<Object_WatchlistCoinId> listOfIds ;
    List<Object_Coin> listOfSelectedCoins ;
    RVAdapter_WatchlistAllCoins adapter ;


    public Fragment_WatchlistSelected() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frg_watchlist_selectcoins, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        requestQueue  = Volley.newRequestQueue(context) ;

        listOfSelectedCoins = new ArrayList<>() ;
        listOfIds = new ArrayList<>() ;

        getReferences(view) ;
        if(listOfIds.size() != 0){
            getFromServer_SelectedCoinsData();
        }

        setupRecyclerView() ;
        setupBtn_FABAddNewCoin();

        db.watchlistDao().getListOfAllIds().observe(this, new Observer<List<Object_WatchlistCoinId>>() {
            @Override
            public void onChanged(@Nullable List<Object_WatchlistCoinId> newListOfIds) {
                Log.v(LOG_TAG, "Live data changed for listOfIds") ;
                if(newListOfIds.size() != 0){
                    listOfIds = newListOfIds ;
                    getFromServer_SelectedCoinsData();
                }
            }
        });


    }

    private void getReferences(View v){
        rv = v.findViewById(R.id.frgWatchlistSelectedCoins_RV_Main) ;
        fab_AddNewCoin = v.findViewById(R.id.frgWatchlistSelectedCoins_FAB_AddNewCoin) ;
    }

    private void setupRecyclerView(){
        adapter = new RVAdapter_WatchlistAllCoins(context, listOfSelectedCoins) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

    }

    private void getFromServer_SelectedCoinsData(){

        String currencyId = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencyId() ;
        String coinIdsString = Object_WatchlistCoinId.getListOfIdString(listOfIds) ;

        String url = Constants.getURL_APICALL_WATCHLIST_SELECTED_COIN_DATA(coinIdsString, currencyId) ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(LOG_TAG, "Response from server is " + response) ;
                        processServerData_ListOfAllCoins(response); ;
//                        processData(response);
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


    private void processServerData_ListOfAllCoins(String response){

        try{
            JSONArray responseArray = new JSONArray(response) ;
            if(responseArray.length() == 0){
                return;
            }
            listOfSelectedCoins.clear();
            for (int i = 0; i<responseArray.length(); i++){
                JSONObject jsonObject = responseArray.getJSONObject(i) ;
                Object_Coin coinObj = new Object_Coin(
                        jsonObject.getString("id"),
                        jsonObject.getString("symbol"),
                        jsonObject.getString("name")
                ) ;

                coinObj.setImageLogoLink(jsonObject.getString("image"));
                coinObj.setTotalVolume("" + jsonObject.getDouble("current_price")); /// this is the price of coin, but stored in telegram
                coinObj.setMarketCap( jsonObject.get("market_cap").toString());
                coinObj.setRank(jsonObject.getInt("market_cap_rank"));
                coinObj.setPercentageChange_1W(jsonObject.getString("price_change_percentage_7d_in_currency"));
                coinObj.setSparklineData(jsonObject.getJSONObject("sparkline_in_7d").getJSONArray("price").toString());

                listOfSelectedCoins.add(coinObj) ;
            }
            adapter.refreshData(listOfSelectedCoins);


        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
            Message.display(context, e.toString());
        }
    }




    private void setupBtn_FABAddNewCoin(){
        fab_AddNewCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_CoinSelector.class) ;
                intent.putExtra("exclude_watchlist", true) ;
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                final String coinId = data.getStringExtra("coinId");
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        db.watchlistDao().insertCoin(new Object_WatchlistCoinId(coinId));
                    }
                });
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Message.display_w_Log(context, LOG_TAG, "No result from starting the coin Selector activity");
            }
        }

    }
}
