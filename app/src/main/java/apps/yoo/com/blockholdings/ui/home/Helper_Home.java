package apps.yoo.com.blockholdings.ui.home;

import android.util.Log;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.data.models.Object_TransactionGroup;

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



    public static ArrayList<Object_TransactionGroup> getListOfTransactionGroups (List<Object_TransactionFullData> list){
        // Firstly we will create a multimap with pair of <String,Object_TransactionGroup>
        // Here the string will be used as key to group the transactions
        // IN our case the string is coinId, since we are grouping the transaction based on the Coin

        //1. Put all the transaction of the list in the multimap with each transaction mapped to the coin ID
        //2.
        ArrayList<Object_TransactionGroup> listOfTransactionGroups = new ArrayList<>() ;
        Multimap<String, Object_TransactionFullData> transactionMultimap = HashMultimap.create() ;

        for (Object_TransactionFullData transaction: list) {
            transactionMultimap.put(transaction.getCoinObject().getId(), transaction) ;
        } ;


        for (String coinId : transactionMultimap.keySet()){
            Object_TransactionGroup txGrp = new Object_TransactionGroup(transactionMultimap.get(coinId).iterator().next()) ;

            for (Object_TransactionFullData childTransaction: transactionMultimap.get(coinId)) {
                txGrp.addChildTransactionFD(childTransaction);

                // see the formula for SummedPriceChange
                if(childTransaction.getTransactionObject().getType() == Object_Transaction.TYPE_BUY){
                    txGrp.setNoOfCoins(txGrp.getNoOfCoins()
                            .add(new BigDecimal(childTransaction.getTransactionObject().getNoOfCoins())));

                    txGrp.setSummedPriceChange(txGrp.getSummedPriceChange()
                            .add(new BigDecimal(childTransaction.getTransactionObject().getTotalValue_Current()))
                            .subtract(new BigDecimal(childTransaction.getTransactionObject().getTotalValue_OriginalwFees())));


                } else if(childTransaction.getTransactionObject().getType() == Object_Transaction.TYPE_SELL){
                    txGrp.setNoOfCoins(txGrp.getNoOfCoins()
                            .subtract(new BigDecimal(childTransaction.getTransactionObject().getNoOfCoins())));

                    txGrp.setSummedPriceChange(txGrp.getSummedPriceChange()
                            .add(new BigDecimal(childTransaction.getTransactionObject().getTotalValue_Original()))
                            .subtract(new BigDecimal(childTransaction.getTransactionObject().getTotalValue_Current()))) ;
                }
            }

            listOfTransactionGroups.add(txGrp) ;
        }


        return listOfTransactionGroups ;
    }
}
