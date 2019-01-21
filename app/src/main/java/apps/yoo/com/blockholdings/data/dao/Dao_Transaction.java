package apps.yoo.com.blockholdings.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;
@Dao
public interface Dao_Transaction {
    @Query("SELECT * FROM table_transaction ORDER BY coinId ASC")
    List<Object_Transaction> getListOfAllTransactions();


    @Query("SELECT * FROM table_transaction WHERE coinId = :coinId")
    Object_Transaction getTransactionByCoinId(int coinId);

    @Insert
    void insertTransaction(Object_Transaction transaction);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTransaction(Object_Transaction transaction);


    @Delete
    void deleteTransaction(Object_Transaction transaction);

    @Query("DELETE FROM table_transaction")
    public void deleteWholeTable();
}
