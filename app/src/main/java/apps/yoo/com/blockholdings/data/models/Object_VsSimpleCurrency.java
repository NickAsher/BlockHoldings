package apps.yoo.com.blockholdings.data.models;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;

@Entity(tableName = "table_vs_simple_currency")
public class Object_VsSimpleCurrency {
    private static final String LOG_TAG = "Object_VsSimpleCurrency --> " ;

    @PrimaryKey
    @ColumnInfo(name = "vs_currency_id")
    @NonNull
    String currencyId ;


    public Object_VsSimpleCurrency(@NonNull String currencyId) {
        this.currencyId = currencyId;
    }

    @NonNull
    public String getCurrencyId() {
        return currencyId;
    }


    public static void insertAllVsSimpleCurrencies(final Context context, RequestQueue requestQueue, final AppDatabase db){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_APICALL_VS_SIMPLE_CURRENCIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(LOG_TAG, response) ;
                        List<Object_VsSimpleCurrency> listOfSimpleCurrencies = new ArrayList<>() ;
                        try{
                            JSONArray jsonArray = new JSONArray(response) ;
                            for (int i = 0; i<jsonArray.length() ; i++){
                                listOfSimpleCurrencies.add(new Object_VsSimpleCurrency(jsonArray.getString(i))) ;
                            }
                            db.vsSimpleCurrencyDao().insertManyVsSimpleCurrencies(listOfSimpleCurrencies);

                        }catch (JSONException e){
                            Log.e(LOG_TAG, e.toString());
                        }
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


}
