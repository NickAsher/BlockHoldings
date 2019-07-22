package apps.yoo.com.blockholdings.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Coin;

public class NetworkDataParser {
    private static final String LOG_TAG = "NetworkDataParser --> " ;


    public static List<Object_Coin> getFullListOfCoins_withMarketSparklineData(String networkResponse){
        List<Object_Coin> returnList = new ArrayList<>() ;
        try{
            JSONArray responseArray = new JSONArray(networkResponse) ;
            if(responseArray.length() == 0){
                return null;
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
                returnList.add(coinObj) ;
            }
            return returnList ;

        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
            return null ;
        }
    }
}
