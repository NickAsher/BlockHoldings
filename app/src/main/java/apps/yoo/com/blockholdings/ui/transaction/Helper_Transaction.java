package apps.yoo.com.blockholdings.ui.transaction;

import android.util.Log;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.Objects.Object_Exchange;
import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;

public class Helper_Transaction {
    private static final String LOG_TAG = "Helpr_Transaction --> " ;


    private static Object_Transaction transactionObject ;

    public static Object_Transaction getTransactionObject(){
        if(transactionObject == null){
            transactionObject = new Object_Transaction() ;
        }
        return transactionObject ;
    }



    public static Table<String, String, String> getExchangePairDataForCoin(JSONArray tickerList, String coinName) throws JSONException{

            ImmutableTable.Builder builder = ImmutableTable.<String, String, String> builder() ;
            int tickerListLength = tickerList.length() ;
            for(int i = 0; i< tickerListLength; i++){

                JSONObject tickerObject = tickerList.getJSONObject(i) ;

                String marketId = tickerObject.getJSONObject("market").getString("identifier") ;
                String targetName = tickerObject.getString("target") ;
                String price = tickerObject.getString("last") ;

                builder.put(marketId, targetName, price) ;
            }
            Table<String, String, String >table = HashBasedTable.create(builder.build()) ;
            Log.e(LOG_TAG, table.toString()) ;
            return table ;


    }

}
