package apps.yoo.com.blockholdings.ui.watchlist.all;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
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
import com.google.common.collect.Collections2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.ui.watchlist.RVAdapter_WatchlistAllCoins;
import apps.yoo.com.blockholdings.util.Message;


public class Fragment_WatchlistAllCoins extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_WatchlistAllCoins --> " ;
    AppDatabase db;
    RequestQueue requestQueue ;


    RecyclerView rv, rv_SearchSuggestions ; ;
    SearchView searchView_Coins ;

    List<Object_Coin> listOfCoins, listOfSearchSuggestionCoins ;
    RVAdapter_WatchlistAllCoins adapter ;
    RVAdapter_SearchSuggestions adapter_SearchSuggestions ;


    public Fragment_WatchlistAllCoins() {
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
        return inflater.inflate(R.layout.frg_watchlist_allcoins, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        requestQueue  = Volley.newRequestQueue(context) ;

        listOfCoins = new ArrayList<>() ;
        listOfSearchSuggestionCoins = db.coinDao().getListOfAllCoins() ;
        getReferences(view) ;
        getFromServer_ListOfAllCoins() ;
        setupRecyclerView() ;
        setupRecyclerView_SearchSuggestions();
        setupSearchView() ;
    }

    private void getReferences(View v){
        rv = v.findViewById(R.id.fragWatchlistAllCoins_RV_AllCoins) ;
        searchView_Coins = v.findViewById(R.id.frgWatchlistAllCoins_SearchView_Coins) ;
        rv_SearchSuggestions = v.findViewById(R.id.fragWatchlistAllCoins_RV_SearchSuggestions) ;

    }

    private void getFromServer_ListOfAllCoins(){
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d" ;
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

            for (int i = 0; i<responseArray.length(); i++){
                JSONObject jsonObject = responseArray.getJSONObject(i) ;
                Object_Coin coinObj = new Object_Coin(
                        jsonObject.getString("id"),
                        jsonObject.getString("symbol"),
                        jsonObject.getString("name")
                ) ;

                coinObj.setImageLogoLink(jsonObject.getString("image"));
                JSONObject marketData = new JSONObject() ;
                marketData.put("current_price",jsonObject.get("current_price").toString() ) ;
                marketData.put("market_cap",jsonObject.get("market_cap").toString() ) ;
                marketData.put("market_cap_rank",jsonObject.getInt("market_cap_rank")) ;
                marketData.put("price_change_percentage_7d_in_currency",jsonObject.getString("price_change_percentage_7d_in_currency") ) ;
                marketData.put("sparkline_in_7d",jsonObject.getJSONObject("sparkline_in_7d").getJSONArray("price")) ;

                coinObj.setCachedData(marketData.toString());

                listOfCoins.add(coinObj) ;
            }
            adapter.refreshData(listOfCoins);


        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
            Message.display(context, e.toString());
        }

    }

    private void setupRecyclerView(){
        adapter = new RVAdapter_WatchlistAllCoins(context, listOfCoins) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

    }


    private void setupSearchView(){




        searchView_Coins.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    adapter_SearchSuggestions.refreshData(new ArrayList<Object_Coin>()) ;

                } else {
                    adapter_SearchSuggestions.refreshData(new ArrayList<Object_Coin>(Collections2.filter(listOfSearchSuggestionCoins, Object_Coin.getPredicateFilter_NameSymbol_startsWithAlphabet(newText)))) ;
                }
                return true;
            }
        });

        searchView_Coins.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter_SearchSuggestions.refreshData(new ArrayList<Object_Coin>());
                return true;
            }
        });
    }



    private void setupRecyclerView_SearchSuggestions(){
        adapter_SearchSuggestions = new RVAdapter_SearchSuggestions(context, new ArrayList<Object_Coin>()) ;
        GridLayoutManager gd = new GridLayoutManager(context, 2) ;
        gd.setAutoMeasureEnabled(true);
        rv_SearchSuggestions.setLayoutManager(gd);
        rv_SearchSuggestions.setNestedScrollingEnabled(false);
        rv_SearchSuggestions.setAdapter(adapter_SearchSuggestions);

    }
}
