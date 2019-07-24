package apps.yoo.com.blockholdings.ui.settings;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.data.models.Object_Coin;

public class Helper_Settings {
    private static final String LOG_TAG = "Helper_Settings --> " ;

    public static ArrayList<Object_Coin> getListOfCoins_FromApi(String response){
        ArrayList<Object_Coin> listOfCoins = new ArrayList<>() ;

        try {
            JSONArray responseArray = new JSONArray(response);


            int length = responseArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject_Coin = responseArray.getJSONObject(i);


                listOfCoins.add(new Object_Coin(
                        jsonObject_Coin.getString("id"),
                        jsonObject_Coin.getString("symbol"),
                        jsonObject_Coin.getString("name"),
                        false
                ));


        }

        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage()) ;
        }

        return listOfCoins ;
    }


    public static ArrayList<Object_Exchange> getListOfExchanges_FromApi(String response){
        ArrayList<Object_Exchange> listOfExchanges = new ArrayList<>() ;
        listOfExchanges.add(Object_Exchange.getGlobalAverage()) ;

        try {
            JSONArray responseArray = new JSONArray(response);


            int length = responseArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject_Exchange = responseArray.getJSONObject(i);


                listOfExchanges.add(new Object_Exchange(
                        jsonObject_Exchange.getString("id"),
                        jsonObject_Exchange.getString("name"),
                        jsonObject_Exchange.getString("image")
                ));


            }

        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage()) ;
        }

        return listOfExchanges ;
    }
}
