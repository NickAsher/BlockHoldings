package apps.yoo.com.blockholdings.ui.transaction;

import android.util.Log;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;

public class Helper_Transaction {
    private static final String LOG_TAG = "Helpr_Transaction --> " ;


    private static Object_TransactionFullData transactionFullDataObject ;

    public static Object_TransactionFullData getTransactionFullDataObject(){
        if(transactionFullDataObject == null){
            transactionFullDataObject = new Object_TransactionFullData() ;
        }
        return transactionFullDataObject ;
    }

    public static void makeNewTransactionFullDataObejct(){
        transactionFullDataObject = null ;

    }



    public static Table<String, String, String> getExchangePairDataForCoin(JSONArray tickerList) throws JSONException{

        Table<String, String, String >table = HashBasedTable.create() ;

            int tickerListLength = tickerList.length() ;
            Log.e(LOG_TAG, "No of tickers is " + tickerList.length()) ;
            for(int i = 0; i< tickerListLength; i++){

                JSONObject tickerObject = tickerList.getJSONObject(i) ;

                String marketId = tickerObject.getJSONObject("market").getString("identifier") ;
                String targetName = tickerObject.getString("target") ;
                String price = tickerObject.getString("last") ;

                if(table.contains(marketId, targetName)){
                    Log.e(LOG_TAG, "Missing : "  + marketId + " -" + targetName) ;
                    continue;
                }
                table.put(marketId, targetName, price) ;

            }

            Log.e(LOG_TAG, table.toString()) ;
            return table ;


    }


    /*

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
     */

}
