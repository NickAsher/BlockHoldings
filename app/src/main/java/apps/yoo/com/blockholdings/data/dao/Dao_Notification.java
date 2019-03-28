package apps.yoo.com.blockholdings.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_NotificationCoinId;

@Dao
public interface Dao_Notification {

    @Query("SELECT * FROM table_notificationCoinId ORDER BY srNo ASC")
    LiveData<List<Object_NotificationCoinId>> getListOfAllIds_LiveData();

    @Query("SELECT * FROM table_notificationCoinId ORDER BY srNo ASC")
    List<Object_NotificationCoinId> getListOfAllIds();

    @Query("SELECT table_coin.* FROM table_coin, table_notificationCoinId WHERE table_notificationCoinId.coinId = table_coin.coin_Id ORDER BY table_notificationCoinId.srNo ASC")
    List<Object_Coin> getListOfCoinObjects();

    @Query("SELECT * FROM table_notificationCoinId WHERE coinId = :coinId ORDER BY srNo ASC")
    LiveData<List<Object_NotificationCoinId>> getListOfNotification_ById_LiveData(String coinId);


    @Insert
    long insertCoin(Object_NotificationCoinId coin);

    @Insert
    void insertManyCoins(ArrayList<Object_NotificationCoinId> coinIterator) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCoin(Object_NotificationCoinId coin);

    @Delete
    void deleteWatchlistCoin(Object_NotificationCoinId watchlistCoinId);

    @Query("DELETE FROM table_notificationCoinId WHERE srNo = :srNo")
    public void deleteNotification_BySrNo(int srNo);

    @Query("DELETE FROM table_notificationCoinId WHERE coinId = :coinId")
    public void deleteNotification_ByCoinId(String coinId);

    @Query("DELETE FROM table_notificationCoinId")
    public void deleteWholeTable();
}
