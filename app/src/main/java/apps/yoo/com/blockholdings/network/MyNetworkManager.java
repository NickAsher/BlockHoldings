package apps.yoo.com.blockholdings.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import apps.yoo.com.blockholdings.util.Message;

public class MyNetworkManager {

    private static VolleySingleton volleySingleton ;
    public static MyNetworkManager instance ;
    private Context context ;

    private MyNetworkManager(Context context){
        this.context = context ;
        volleySingleton = VolleySingleton.getInstance(context) ;
    }

    public static MyNetworkManager getInstance(Context context){
        if(instance == null){
            synchronized (MyNetworkManager.class){
                if(instance == null){
                    instance = new MyNetworkManager(context) ;
                }
            }
        }
        return instance ;
    }



    public  void getFullListOfCoins_withMarketSparklineData(int paginationNo, String currencyId, final NetworkResponse networkResponse){
        String url = ApiManager.getUrl_FullListOfCoins_withMarketNSparklineData(paginationNo, currencyId) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        networkResponse.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkResponse.onErrorResponse(error);
            }
        }) ;

        volleySingleton.addToRequestQueue(stringRequest);
    }




}
