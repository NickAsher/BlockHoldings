package apps.yoo.com.blockholdings.data.models;

import androidx.room.Embedded;

import com.google.common.base.Predicate;

import java.math.BigDecimal;
import java.util.Comparator;

public class Object_TransactionFullData implements Cloneable{

  @Embedded
  Object_Coin coinObject ;

  @Embedded
  Object_Transaction transactionObject ;

  @Embedded
  Object_Exchange exchangeObject ;

  public Object_TransactionFullData(Object_Coin coinObject, Object_Transaction transactionData, Object_Exchange exchangeObject) {
    this.coinObject = coinObject;
    this.transactionObject = transactionData;
    this.exchangeObject = exchangeObject;
  }

  public Object_TransactionFullData() {
    this.coinObject = new Object_Coin();
    this.transactionObject = new Object_Transaction();
    this.exchangeObject = new Object_Exchange();
  }

  public Object_Coin getCoinObject() {
    return coinObject;
  }

  public Object_Transaction getTransactionObject() {
    return transactionObject;
  }

  public void setCoinObject(Object_Coin coinObject) {
    this.coinObject = coinObject;
  }

  public void setTransactionObject(Object_Transaction transactionData) {
    this.transactionObject = transactionData;
  }

  public Object_Exchange getExchangeObject() {
    return exchangeObject;
  }

  public void setExchangeObject(Object_Exchange exchangeObject) {
    this.exchangeObject = exchangeObject;
  }

  @Override
  public String toString() {
    return coinObject.toString() + exchangeObject.toString() + "\n" +  transactionObject.toString() + "\n \n" ;
  }


  @Override
  public Object clone() throws CloneNotSupportedException {
    Object obj = super.clone() ;
    Object_TransactionFullData clonedTx = (Object_TransactionFullData)obj ;

    clonedTx.setTransactionObject((Object_Transaction)this.getTransactionObject().clone());
    return clonedTx ;
  }

  public static Comparator<Object_TransactionFullData> NameComparator = new Comparator<Object_TransactionFullData>() {
    @Override
    public int compare(Object_TransactionFullData o1, Object_TransactionFullData o2) {
      String nameo1 = o1.getCoinObject().getName() ;
      String nameo2 = o2.getCoinObject().getName() ;
      return nameo1.compareTo(nameo2) ;

    }
  } ;

  public static Comparator<Object_TransactionFullData> HoldingComparator = new Comparator<Object_TransactionFullData>() {
    @Override
    public int compare(Object_TransactionFullData o1, Object_TransactionFullData o2) {
      BigDecimal holdingO1 = new BigDecimal(o1.getTransactionObject().getTotalValue_Current()) ;
      BigDecimal holdingO2 = new BigDecimal(o2.getTransactionObject().getTotalValue_Current()) ;

      return holdingO1.compareTo(holdingO2) ;
    }
  } ;




  public static Comparator<Object_TransactionFullData> singleCoinPrice = new Comparator<Object_TransactionFullData>() {
    @Override
    public int compare(Object_TransactionFullData o1, Object_TransactionFullData o2) {
      BigDecimal val1 = new BigDecimal(o1.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()) ;
      BigDecimal val2 = new BigDecimal(o2.getTransactionObject().getSingleCoinPrice_CurrencyCurrent()) ;

      return val1.compareTo(val2) ;
    }
  } ;



  public static Predicate<Object_TransactionFullData> getPredicateFilter_PortfolioId(final int portfolioId){
    Predicate predicate = new Predicate<Object_TransactionFullData>(){
      @Override
      public boolean apply(Object_TransactionFullData input) {

        return input.getTransactionObject().getPortFolioId() == portfolioId ;
      }
    } ;
    return predicate ;
  }
}
