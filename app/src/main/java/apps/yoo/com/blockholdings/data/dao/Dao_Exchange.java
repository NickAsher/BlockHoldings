package apps.yoo.com.blockholdings.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Exchange;


@Dao
public interface Dao_Exchange {

    @Query("SELECT * FROM table_exchange ORDER BY exchange_Id ASC")
    List<Object_Exchange> getListOfAllExchanges();


    @Query("SELECT * FROM table_exchange WHERE exchange_Id = :exchangeId")
    Object_Exchange getExchangeById(int exchangeId);


    @Query("SELECT * FROM table_exchange WHERE exchange_Id IN(:listOfIds) ORDER BY exchange_Id ASC")
    List<Object_Exchange> getListOfExchanges_WithIds(List<String> listOfIds);


    @Insert
    void insertExchange(Object_Exchange coin);

    @Insert
    void insertManyExchanges(ArrayList<Object_Exchange> coinIterator) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExchanges(Object_Exchange coin);


    @Delete
    void deleteExchanges(Object_Exchange newsSite);

    @Query("DELETE FROM table_exchange")
    void deleteWholeTable();
}
