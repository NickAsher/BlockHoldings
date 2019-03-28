package apps.yoo.com.blockholdings.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Coin;

@Dao
public interface Dao_Coin {

    @Query("SELECT * FROM table_coin ORDER BY coinSrNo ASC")
    List<Object_Coin> getListOfAllCoins();

    @Query("SELECT * FROM table_coin ORDER BY coinSrNo ASC")
    LiveData<List<Object_Coin>> getListOfAllCoins_LiveData();

    @Query("SELECT * FROM table_coin WHERE coin_Id NOT IN (SELECT table_watchlist.watchlist_coinId as coin_Id FROM table_watchlist) ORDER BY coinSrNo ASC")
    List<Object_Coin> getListOfAllCoins_ExcludeWatchlist() ;

    @Query("SELECT * FROM table_coin WHERE coin_Id NOT IN (SELECT table_widget_coinprice.coinId as coin_Id FROM table_widget_coinprice) ORDER BY coinSrNo ASC")
    List<Object_Coin> getListOfAllCoins_ExcludeWidgetCoins() ;

    @Query("SELECT * FROM table_coin WHERE coin_Id = :coinId")
    Object_Coin getCoinById(String coinId);


    @Query("SELECT * FROM table_coin WHERE coin_Id = :coinId")
    LiveData<Object_Coin> getCoinById_LiveData(String coinId);

    @Query("SELECT * FROM table_coin WHERE coinSymbol = :coinSymbol")
    Object_Coin getCoinBySymbol(String coinSymbol);

    @Insert
    void insertCoin(Object_Coin coin);

    @Insert
    void insertManyCoins(List<Object_Coin> coinIterator) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCoin(Object_Coin coin);

    @Query("UPDATE table_coin SET coinImageLogoLink = :imageLink WHERE coin_Id = :coinId")
    void updateCoin_Image(String coinId, String imageLink) ;

    @Query("UPDATE table_coin SET price_data = :priceData WHERE coin_Id = :coinId")
    void updateCoin_PriceData(String coinId, String priceData) ;

    @Delete
    void deleteCoin(Object_Coin newsSite);

    @Query("DELETE FROM table_coin")
    public void deleteWholeTable();

}