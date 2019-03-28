package apps.yoo.com.blockholdings.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private Context context ;
    private RequestQueue requestQueue ;
    private static VolleySingleton instance ;


    private VolleySingleton(Context context){
        this.context = context ;
        this.requestQueue = getRequestQueue() ;

    }

    public static VolleySingleton getInstance(Context context){
        if(instance == null){
            synchronized (VolleySingleton.class){
                if(instance == null){
                    instance = new VolleySingleton(context) ;
                }
            }
        }
        return instance ;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context) ;
        }
        return requestQueue ;
    }


    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request) ;
    }


















}
