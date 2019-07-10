package apps.yoo.com.blockholdings.data.models;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Object_TransactionGroup {
    ArrayList<Object_TransactionFullData> listOfChildTransactionsFD;
    Map<Integer, BigDecimal> mapOfPriceChange ;
    BigDecimal summedPriceChange ;
    BigDecimal noOfCoins ;
    BigDecimal singleCoinPrice_CurrencyCurrent;


//    public Object_TransactionGroup(Object_TransactionFullData baseTransaction){
//        this.summedTransaction.setCoinObject(baseTransaction.getCoinObject());
//
//
//    }

    public Object_TransactionGroup(Object_TransactionFullData anyChildTransactionFullData) {
        this.listOfChildTransactionsFD = new ArrayList<>() ;
        mapOfPriceChange = new HashMap<>() ;
        summedPriceChange = new BigDecimal(0) ;
        noOfCoins = new BigDecimal(0) ;
        singleCoinPrice_CurrencyCurrent = new BigDecimal(anyChildTransactionFullData.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()) ;
    }




    public ArrayList<Object_TransactionFullData> getListOfChildTransactionsFD() {
        return listOfChildTransactionsFD;
    }

    public Map<Integer, BigDecimal> getMapOfPriceChange() {
        return mapOfPriceChange;
    }

    public BigDecimal getSummedPriceChange() {
        return summedPriceChange;
    }

    public void setSummedPriceChange(BigDecimal summedPriceChange) {
        this.summedPriceChange = summedPriceChange;
    }

    public void addChildTransactionFD(Object_TransactionFullData childTransactionFD){
        this.listOfChildTransactionsFD.add(childTransactionFD) ;
        mapOfPriceChange.put(childTransactionFD.getTransactionObject().getTransactionNo(), new BigDecimal(0)) ;
    }

    public BigDecimal getNoOfCoins() {
        return noOfCoins;
    }

    public void setNoOfCoins(BigDecimal noOfCoins) {
        this.noOfCoins = noOfCoins;
    }

    public BigDecimal getSingleCoinPrice_CurrencyCurrent() {
        return singleCoinPrice_CurrencyCurrent;
    }

    public void setSingleCoinPrice_CurrencyCurrent(BigDecimal singleCoinPrice_CurrencyCurrent) {
        this.singleCoinPrice_CurrencyCurrent = singleCoinPrice_CurrencyCurrent;
    }



    public static Comparator<Object_TransactionGroup> NameComparator = new Comparator<Object_TransactionGroup>() {
        @Override
        public int compare(Object_TransactionGroup o1, Object_TransactionGroup o2) {
            String nameo1 = o1.getListOfChildTransactionsFD().iterator().next().getCoinObject().getName() ;
            String nameo2 = o2.getListOfChildTransactionsFD().iterator().next().getCoinObject().getName() ;
            return nameo1.compareTo(nameo2) ;

        }
    } ;

    public static Comparator<Object_TransactionGroup> HoldingComparator = new Comparator<Object_TransactionGroup>() {
        @Override
        public int compare(Object_TransactionGroup o1, Object_TransactionGroup o2) {
            BigDecimal holdingO1 = o1.getNoOfCoins().multiply(o1.getSingleCoinPrice_CurrencyCurrent());
            BigDecimal holdingO2 = o2.getNoOfCoins().multiply(o2.getSingleCoinPrice_CurrencyCurrent());

            return holdingO1.compareTo(holdingO2) ;
        }
    } ;

    public static Comparator<Object_TransactionGroup> singleCoinPrice = new Comparator<Object_TransactionGroup>() {
        @Override
        public int compare(Object_TransactionGroup o1, Object_TransactionGroup o2) {
            BigDecimal val1 = o1.getSingleCoinPrice_CurrencyCurrent() ;
            BigDecimal val2 = o2.getSingleCoinPrice_CurrencyCurrent() ;

            return val1.compareTo(val2) ;
        }
    } ;

    @NonNull
    @Override
    public String toString() {
        return "ChildTransactions : " + listOfChildTransactionsFD + " - NoOfCoins : " + noOfCoins.toString() + " - SummedPriceChange : " + summedPriceChange + " - map : " + mapOfPriceChange + "\n\n\n" ;
    }
}
