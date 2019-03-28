package apps.yoo.com.blockholdings.data.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import apps.yoo.com.blockholdings.data.models.Object_VsSimpleCurrency;

@Dao
public interface Dao_VsSimpleCurrency {
    @Query("SELECT * FROM table_vs_simple_currency ORDER BY vs_currency_id ASC")
    List<Object_VsSimpleCurrency> getListOfAllVsSimpleCurrencies();

    @Insert
    void insertManyVsSimpleCurrencies(List<Object_VsSimpleCurrency> listOfCurrencies) ;

    @Query("DELETE FROM table_vs_simple_currency")
    public void deleteWholeTable();
}
