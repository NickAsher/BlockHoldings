package apps.yoo.com.blockholdings.ui.home;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;

public class Helper_Home {
    private static final String LOG_TAG = "Helper_Home --> " ;

    public static List<Object_TransactionFullData> getSummedTransactions(final List<Object_TransactionFullData> previousList){
        Map<String, Object_TransactionFullData> map = new HashMap<>() ;
        List<Object_TransactionFullData> newList = new ArrayList<>() ;


        try {
            for (Object_TransactionFullData txObj : previousList) {
                if (map.containsKey(txObj.getTransactionObject().getCoinId())) {
                    // the object is already there in the map, so sum the noOfCoins and totalPrice
                    String previousNoOfCoins = map.get(txObj.getTransactionObject().getCoinId()).getTransactionObject().getNoOfCoins();
                    String previousTotalValueCurrency = map.get(txObj.getTransactionObject().getCoinId()).getTransactionObject().getTotalValue_Current();

                    String toAddSub_NoOfCoins = txObj.getTransactionObject().getNoOfCoins();
                    String toAddSub_TotalValueCurrency = txObj.getTransactionObject().getTotalValue_Current();


                    // setting the new NoOfCoins
                    if(txObj.getTransactionObject().getType() == Object_Transaction.TYPE_BUY){
//                        Log.e(LOG_TAG, "Transaction type is Buy" );

                        map.get(txObj.getTransactionObject().getCoinId()).getTransactionObject().setNoOfCoins(
                                new BigDecimal(previousNoOfCoins).add(new BigDecimal(toAddSub_NoOfCoins)).toPlainString());

                        // setting the new totalValueCurrency
                        map.get(txObj.getTransactionObject().getCoinId()).getTransactionObject().setTotalValue_Current(
                                new BigDecimal(previousTotalValueCurrency).add(new BigDecimal(toAddSub_TotalValueCurrency)).toPlainString());

                    }else if (txObj.getTransactionObject().getType() == Object_Transaction.TYPE_SELL){
//                        Log.e(LOG_TAG, "Transaction type is sell" );

                        map.get(txObj.getTransactionObject().getCoinId()).getTransactionObject().setNoOfCoins(
                                new BigDecimal(previousNoOfCoins).subtract(new BigDecimal(toAddSub_NoOfCoins)).toPlainString());

                        // setting the new totalValueCurrency
                        map.get(txObj.getTransactionObject().getCoinId()).getTransactionObject().setTotalValue_Current(
                                new BigDecimal(previousTotalValueCurrency).subtract(new BigDecimal(toAddSub_TotalValueCurrency)).toPlainString());

                    }


                } else {
                    // the object is not in the map, so add it to the map
                    Object_TransactionFullData tempObj = txObj;
                    if(txObj.getTransactionObject().getType() == Object_Transaction.TYPE_SELL){
//                        Log.e(LOG_TAG, "Transaction type is sell" );
                        txObj.getTransactionObject().setNoOfCoins("-" + txObj.getTransactionObject().getNoOfCoins());
                        txObj.getTransactionObject().setTotalValue_Current("-" + txObj.getTransactionObject().getTotalValue_Current());
                    } else {
//                        Log.e(LOG_TAG, "Transaction type is Buy" );

                    }
                    map.put(txObj.getTransactionObject().getCoinId(), (Object_TransactionFullData) txObj.clone());
                }
            }
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString()) ;
        }



        for (String coinId : map.keySet()){
            newList.add(map.get(coinId)) ;
        }

        return newList ;
    }
}
