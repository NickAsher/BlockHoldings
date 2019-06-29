package apps.yoo.com.blockholdings.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class NetworkRepository {

    private static VolleySingleton volleySingleton ;
    public static NetworkRepository instance ;
    private Context context ;

    private NetworkRepository(Context context){
        this.context = context ;
        volleySingleton = VolleySingleton.getInstance(context) ;
    }

    public static NetworkRepository getInstance(Context context){
        if(instance == null){
            synchronized (NetworkRepository.class){
                if(instance == null){
                    instance = new NetworkRepository(context.getApplicationContext()) ;
                }
            }
        }
        return instance ;
    }



    public  void getFullListOfCoins_withMarketSparklineData(int paginationNo, String currencyId, final MyNetworkResponse myNetworkResponse){
        String url = ApiManager.getUrl_FullListOfCoins_withMarketNSparklineData(paginationNo, currencyId) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myNetworkResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myNetworkResponse.onErrorResponse(error);
            }
        }) ;

        volleySingleton.addToRequestQueue(stringRequest);
    }


    public void getAllTickersOfCoins(String coinId, final MyNetworkResponse myNetworkResponse){

        String url = ApiManager.getUrl_TickersOfCoin(1, coinId) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myNetworkResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myNetworkResponse.onErrorResponse(error);
            }
        }) ;
        volleySingleton.addToRequestQueue(stringRequest);
    }




}
