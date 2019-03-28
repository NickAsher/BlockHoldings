package apps.yoo.com.blockholdings.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Currency;

@Dao
public interface Dao_Currency {
    @Query("SELECT * FROM table_currency ORDER BY currencyId ASC")
    List<Object_Currency> getListOfAllCurrencies();


    @Query("SELECT * FROM table_currency WHERE currencyId = :CurrencyId")
    Object_Currency getCurrencyById(String CurrencyId);

    @Insert
    void insertCurrency(Object_Currency currency);

    @Insert
    void insertManyCurrencies(List<Object_Currency> listOfCurrencies) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCurrency(Object_Currency Currency);


    @Delete
    void deleteCurrency(Object_Currency newsSite);

    @Query("DELETE FROM table_currency")
    public void deleteWholeTable();
}
