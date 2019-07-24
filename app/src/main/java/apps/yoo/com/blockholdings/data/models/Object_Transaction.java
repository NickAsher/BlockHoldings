package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "table_transaction")
public class Object_Transaction implements Cloneable{

    public static final int TYPE_BUY = 1 ;
    public static final int TYPE_SELL = 2 ;





    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    int transactionId;


    @ColumnInfo(name = "portfolioId")
    int portFolioId ;


    @ColumnInfo(name = "coinId")
    String coinId ;

    @ColumnInfo(name = "isBaseCoinFiat")
    boolean isBaseCoinFiat;


    @ColumnInfo(name = "transactionType")
    int type ;

    @ColumnInfo(name = "exchangeId")
    String exchangeId ;


    // the pair using which the coin was bought . for ex Neo bought using BTC so NEO/BTC or NEO/USDT or NEO/ETH
    @ColumnInfo(name = "tradingPair")
    String tradingPair ;

    //This is coin price i.e. buy or sell price per coin relating to the exchange pair used
    @ColumnInfo(name = "coinPrice_TradingPair")
    String singleCoinPrice_TradingPair ;

    @ColumnInfo(name = "noOfCoins")
    String noOfCoins ;

    @ColumnInfo(name = "coinPrice_CurrencyOriginal")
    String singleCoinPrice_CurrencyOriginal ;

    @ColumnInfo(name = "coinPrice_CurrencyCurrent")
    String singleCoinPrice_CurrencyCurrent ;



    @ColumnInfo(name = "transactionDateTime")
    Date transactionDateTime ;

    @ColumnInfo(name = "totalValue_Original")
    String totalValue_Original ;

    @ColumnInfo(name = "totalValue_Current")
    String totalValue_Current ;

    @ColumnInfo(name = "totalValue_OriginalwFees")
    String totalValue_OriginalwFees ;

    @ColumnInfo(name = "note")
    String note ;



    @ColumnInfo(name = "updateLog")
    String updateLog ;

    @ColumnInfo(name = "feeCoinId")
    String feeCoinId ; // the coin id of fees ex : BNB

    @ColumnInfo(name = "isFeeCoinFiat")
    boolean isFeeCoinFiat ;

    @ColumnInfo(name = "feeNoOfCoins")
    String feeNoOfCoins ; // The No of coins of feeCoin ex :  0.1 here means fees is 0.1 BNB

    @ColumnInfo(name = "feeInDollar")
    String feeInDollar ; // Total value of fee in user Currency ex : convert 0.1 BNB to USD

    @ColumnInfo(name = "deductFromQuoteCoin")
    boolean deductFromQuoteCoin ;

    @ColumnInfo(name = "complementTransactionNo")
    int complementTransactionNo ;

    @ColumnInfo(name = "isTransactionComplement")
    boolean isTransactionComplement ;







    public Object_Transaction(int transactionId) {
        this.transactionId = transactionId ;
        this.updateLog = "[]" ;
    }

    @Ignore
    public Object_Transaction(){
        this.updateLog = "[]" ;
    }

    public int getTransactionId() {
        return transactionId;
    }


    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public boolean isBaseCoinFiat() {
        return isBaseCoinFiat;
    }

    public void setBaseCoinFiat(boolean baseCoinFiat) {
        isBaseCoinFiat = baseCoinFiat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getTradingPair() {
        return tradingPair;
    }

    public void setTradingPair(String tradingPair) {
        this.tradingPair = tradingPair;
    }

    public String getSingleCoinPrice_TradingPair() {
        return singleCoinPrice_TradingPair;
    }

    public void setSingleCoinPrice_TradingPair(String singlecoinPrice) {
        this.singleCoinPrice_TradingPair = singlecoinPrice;
    }

    public String getNoOfCoins() {
        return noOfCoins;
    }

    public void setNoOfCoins(String noOfCoins) {
        this.noOfCoins = noOfCoins;
    }

    public String getTotalValue_Original() {
        return totalValue_Original;
    }

    public void setTotalValue_Original(String totalValue_Original) {
        this.totalValue_Original = totalValue_Original;
    }

    public String getSingleCoinPrice_CurrencyCurrent() {
        return singleCoinPrice_CurrencyCurrent;
    }

    public void setSingleCoinPrice_CurrencyCurrent(String singleCoinPrice_Currency) {
        this.singleCoinPrice_CurrencyCurrent = singleCoinPrice_Currency;
    }


    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getSingleCoinPrice_CurrencyOriginal() {
        return singleCoinPrice_CurrencyOriginal;
    }

    public void setSingleCoinPrice_CurrencyOriginal(String singleCoinPrice_CurrencyOriginal) {
        this.singleCoinPrice_CurrencyOriginal = singleCoinPrice_CurrencyOriginal;
    }

    public String getTotalValue_Current() {
        return totalValue_Current;
    }

    public void setTotalValue_Current(String totalValue) {
        this.totalValue_Current = totalValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPortFolioId() {
        return portFolioId;
    }

    public void setPortFolioId(int portFolioId) {
        this.portFolioId = portFolioId;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }


    public String getTotalValue_OriginalwFees() {
        return totalValue_OriginalwFees;
    }

    public String getFeeCoinId() {
        return feeCoinId;
    }

    public String getFeeNoOfCoins() {
        return feeNoOfCoins;
    }

    public String getFeeInDollar() {
        return feeInDollar;
    }

    public void setTotalValue_OriginalwFees(String totalValue_CurrentwFees) {
        this.totalValue_OriginalwFees = totalValue_CurrentwFees;
    }

    public void computeTotalValue_OriginalwFees() {
        if(this.feeInDollar == null || feeInDollar.isEmpty()){
            this.totalValue_OriginalwFees = this.totalValue_Current ;
        } else {
            this.totalValue_OriginalwFees = new BigDecimal(totalValue_Current).add(new BigDecimal(feeInDollar)).toPlainString() ;
        }
    }

    public void setFeeCoinId(String feeCoinId) {
        this.feeCoinId = feeCoinId;
    }

    public void setFeeNoOfCoins(String feeNoOfCoins) {
        this.feeNoOfCoins = feeNoOfCoins;
    }

    public void setFeeInDollar(String feeInDollar) {
        this.feeInDollar = feeInDollar;
    }


    public boolean isFeeCoinFiat() {
        return isFeeCoinFiat;
    }

    public void setIsFeeCoinFiat(boolean isFeeCoinFiat) {
        this.isFeeCoinFiat = isFeeCoinFiat;
    }


    public boolean isDeductFromQuoteCoin() {
        return deductFromQuoteCoin;
    }

    public void setDeductFromQuoteCoin(boolean deductFromQuoteCoin) {
        this.deductFromQuoteCoin = deductFromQuoteCoin;
    }


    public int getComplementTransactionNo() {
        return complementTransactionNo;
    }

    public void setComplementTransactionNo(int complementTransactionNo) {
        this.complementTransactionNo = complementTransactionNo;
    }


    public boolean isTransactionComplement() {
        return isTransactionComplement;
    }

    public void setTransactionComplement(boolean transactionComplement) {
        isTransactionComplement = transactionComplement;
    }

    @Override
    public String toString() {
        String s = ""
                + "transactionId : " + transactionId + " - "
                + "type : " + type + " - "
                + "coinId : " +  coinId + " - "
                + "exchangeId : " +  exchangeId + " - "
                + "exchangePair : " +  tradingPair + " - "
                + "coinPrice_TradingPair : " +  singleCoinPrice_TradingPair + " - "
                + "coinPrice_CurrencyOriginal : " +  singleCoinPrice_CurrencyOriginal + " - "
                + "coinPrice_CurrencyCurrent : " +  singleCoinPrice_CurrencyCurrent + " - "
                + "NoOfCoins : " +  noOfCoins + " - "
                + "totalValue_Original : " +  totalValue_Original + " - "
                + "totalValue_Current : " +  totalValue_Current + " - "
                + "Note : " +  note + " - "
                + "transactionDateTime : " +  transactionDateTime ;

//        + "transactionDateTime : " +  new SimpleDateFormat("MMM-dd-yyyy hh:mm:a").format(transactionDateTime) ;
        return s ;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
