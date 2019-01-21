package apps.yoo.com.blockholdings.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import apps.yoo.com.blockholdings.data.Objects.Object_Coin;

@Dao
public interface Dao_Coin {

    @Query("SELECT * FROM table_coin ORDER BY coinId ASC")
    List<Object_Coin> getListOfAllCoins();

    @Query("SELECT * FROM table_coin WHERE coinName LIKE :alphabet || '%' OR coinSymbol LIKE :alphabet || '%' ORDER BY coinSymbol ASC")
    List<Object_Coin> getListOfCoins_WhoseNameStartWithAlphabet(String alphabet);

    @Query("SELECT * FROM table_coin WHERE coinId = :coinId")
    Object_Coin getCoinById(String coinId);

    @Insert
    void insertCoin(Object_Coin coin);

    @Insert
    void insertManyCoins(ArrayList<Object_Coin> coinIterator) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCoin(Object_Coin coin);

    @Query("UPDATE table_coin SET coinImageLogoLink = :imageLink WHERE coinId = :coinId")
    void updateCoin_Image(String coinId, String imageLink) ;


    @Delete
    void deleteCoin(Object_Coin newsSite);

    @Query("DELETE FROM table_coin")
    public void deleteWholeTable();

}