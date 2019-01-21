package apps.yoo.com.blockholdings.data.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "table_transaction")
public class Object_Transaction {

    public static final int TYPE_BUY = 1 ;
    public static final int TYPE_SELL = 2 ;



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionNo")
    int transactionNo ;

    @ColumnInfo(name = "coinId")
    String coinId ;

    @ColumnInfo(name = "coinSymbol")
    String coinSymbol ;

    @ColumnInfo(name = "coinName")
    String coinName ;

    @ColumnInfo(name = "transactionType")
    int type ;

    @ColumnInfo(name = "exchangeId")
    String exchangeId ;

    @ColumnInfo(name = "exchangeName")
    String exchangeName ;

    // the pair using which the coin was bought . for ex Neo bought using BTC so NEO/BTC or NEO/USDT or NEO/ETH
    @ColumnInfo(name = "tradingPair")
    String tradingPair ;

    //This is coin price i.e. buy or sell price per coin relating to the exchange pair used
    @ColumnInfo(name = "coinPrice_TradingPair")
    String singleCoinPrice_TradingPair ;

    @ColumnInfo(name = "noOfCoins")
    String noOfCoins ;

    @ColumnInfo(name = "coinPrice_Currency")
    String singleCoinPrice_Currency ;

    @ColumnInfo(name = "price24hChange")
    String price24hChange ;


    @ColumnInfo(name = "transactionDateTime")
    Date transactionDateTime ;

    @ColumnInfo(name = "totalValue")
    String totalValue ;



    @ColumnInfo(name = "note")
    String note ;




    public Object_Transaction(int transactionNo) {

    }

    @Ignore
    public Object_Transaction(){
    }

    public int getTransactionNo() {
        return transactionNo;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
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

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
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

    public String getSingleCoinPrice_Currency() {
        return singleCoinPrice_Currency;
    }

    public void setSingleCoinPrice_Currency(String singleCoinPrice_Currency) {
        this.singleCoinPrice_Currency = singleCoinPrice_Currency;
    }

    public String getPrice24hChange() {
        return price24hChange;
    }

    public void setPrice24hChange(String price24hChange) {
        this.price24hChange = price24hChange;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        String s = "type : " + type + " - "
                 + "coinName : " +  coinName + " - "
                + "exchangeId : " +  exchangeId + " - "
                + "exchangePair : " +  tradingPair + " - "
                + "coinPrice_TradingPair : " +  singleCoinPrice_TradingPair + " - "
                + "coinPrice_Currency : " +  singleCoinPrice_Currency + " - "
                + "24h Change : " +  price24hChange + " - "
                + "NoOfCoins : " +  noOfCoins + " - "
                + "totalValue : " +  totalValue + " - "
                + "Note : " +  note + " - "
                + "transactionDateTime : " +  transactionDateTime ;
        return s ;
    }
}
