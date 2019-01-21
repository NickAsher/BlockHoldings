package apps.yoo.com.blockholdings.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.data.Objects.Object_Exchange;


@Dao
public interface Dao_Exchange {

    @Query("SELECT * FROM table_exchange ORDER BY id ASC")
    List<Object_Exchange> getListOfAllExchanges();


    @Query("SELECT * FROM table_exchange WHERE id = :exchangeId")
    Object_Exchange getExchangeById(int exchangeId);


    @Query("SELECT * FROM table_exchange WHERE id IN(:listOfIds) ORDER BY id ASC")
    List<Object_Exchange> getListOfExchanges_WithIds(List<String> listOfIds);

    @Query("SELECT * FROM table_exchange WHERE id IN(:listOfIds) AND name LIKE :alphabet || '%' ORDER BY id ASC")
    List<Object_Exchange> getListOExchanges_WithIds_WhoseNameStartWithAlphabet(List<String> listOfIds, String alphabet);


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
