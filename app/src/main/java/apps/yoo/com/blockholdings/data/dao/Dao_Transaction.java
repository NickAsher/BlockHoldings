package apps.yoo.com.blockholdings.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;

@Dao
public interface Dao_Transaction {

    @Query("SELECT * FROM table_transaction")
    List<Object_Transaction> getListOfAllTransactionsa();


    @Query("SELECT * FROM table_transaction ORDER BY coinId ASC")
    List<Object_Transaction> getListOfAllTransactions();

    @Query("SELECT * FROM table_transaction WHERE portfolioId = :portfolioId ORDER BY coinId ASC")
    List<Object_Transaction> getListOfAllTransactions_OfPortfolio(int portfolioId);

    @Query("SELECT * FROM table_transaction WHERE portfolioId = :portfolioId AND transactionType = 1 ORDER BY coinId ASC")
    List<Object_Transaction> getListOfBuyTransactions_OfPortfolio(int portfolioId);

    @Query("SELECT * FROM table_transaction WHERE coinId = :coinId ORDER BY transactionDateTime ASC")
    List<Object_Transaction> getListOfTransaction_ForCoin_SortByDate(String coinId);


    @Query("SELECT * FROM table_transaction WHERE portfolioId = :portfolioId AND transactionDateTime <= :date ORDER BY coinId ASC")
    List<Object_Transaction> getListOfAllTransactions_OfPortfolio_BeforeADate(int portfolioId, long date);

    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id  ORDER BY table_transaction.transactionId")
    List<Object_TransactionFullData> getListOfAllTransactionFD() ;

    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id  ORDER BY table_transaction.transactionId")
    LiveData<List<Object_TransactionFullData>> getListOfAllTransactionFD_LiveData() ;


    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id WHERE table_transaction.portfolioId = :portfolioId ORDER BY table_coin.coinName")
    List<Object_TransactionFullData> getListOfAllTransactionFD_OfPortfolio(int portfolioId) ;

    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id WHERE table_transaction.portfolioId = :portfolioId ORDER BY table_coin.coinName")
    LiveData<List<Object_TransactionFullData>> getListOfAllTransactionFD_OfPortfolio_LiveData(int portfolioId) ;

    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id WHERE table_transaction.portfolioId = :portfolioId AND table_transaction.transactionType = 1 ORDER BY table_coin.coinName")
    List<Object_TransactionFullData> getListOfBuyTransactionFD_OfPortfolio(int portfolioId) ;


    @Query("SELECT  *, SUM(table_transaction.noOfCoins) AS noOfCoins FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id WHERE table_transaction.portfolioId = :portfolioId GROUP BY table_transaction.coinId ORDER BY table_coin.coinName")
    List<Object_TransactionFullData> getListOfAllTransaction_FullData_Summed(int portfolioId) ;


    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id WHERE table_transaction.portfolioId = :portfolioId AND table_transaction.coinId = :coinId ORDER BY table_transaction.transactionDateTime DESC")
    LiveData<List<Object_TransactionFullData>> getListOfAllTransaction_FullData_OfCoin(int portfolioId, String coinId) ;



    @Query("SELECT  * FROM table_transaction INNER JOIN table_coin ON table_transaction.coinId = table_coin.coin_Id INNER JOIN table_exchange ON table_transaction.exchangeId = table_exchange.exchange_Id WHERE table_transaction.transactionId = :txnId")
    Object_TransactionFullData getTransactionFullData_ById(int txnId) ;


    // the following method gives this
    // it returns the lists of all transactions
    // it then filters the list, such that each coin has only one entry
    // and the the entry returned is that which is the oldest date
    @Query("SELECT t1.* FROM table_transaction t1 INNER JOIN (SELECT coinId, MIN(transactionDateTime) AS minDateTime FROM table_transaction GROUP BY coinId) t2 ON t1.coinId = t2.coinId AND t1.transactionDateTime = t2.minDateTime ORDER BY t1.coinId ASC")
    List<Object_Transaction> getListOfAllTransactions_WithSingularCoins_OfOldestDate();

    @Insert
    void insertTransaction(Object_Transaction transaction);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTransaction(Object_Transaction transaction);

    @Query("UPDATE table_transaction SET coinPrice_CurrencyCurrent = :singleCoinPriceCurrent, totalValue_Current = :totalValueCurrent WHERE transactionId = :txnId" )
    void updateTransactionPriceByTxnId(int txnId, String singleCoinPriceCurrent, String totalValueCurrent);

    @Query("UPDATE table_transaction SET coinPrice_CurrencyCurrent = :singleCoinPriceCurrent, totalValue_Current = :totalValueCurrent, updateLog = :newUpdateLog WHERE transactionId = :txnId" )
    void updateTransactionPriceByTxnId(int txnId, String singleCoinPriceCurrent, String totalValueCurrent, String newUpdateLog);



    @Delete
    void deleteTransaction(Object_Transaction transaction);

    @Query("DELETE FROM  table_transaction  WHERE transactionId = :transactionId")
    void deleteTransaction_ById(int transactionId) ;

    @Query("DELETE FROM  table_transaction  WHERE portfolioId = :portfolioId")
    void deleteTransactions_OfPortfolio(int portfolioId) ;

    @Query("DELETE FROM table_transaction")
    public void deleteWholeTable();
}
