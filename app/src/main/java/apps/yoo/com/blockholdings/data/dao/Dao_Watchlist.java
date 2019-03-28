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

import apps.yoo.com.blockholdings.data.models.Object_WatchlistCoinId;
@Dao
public interface Dao_Watchlist {

    @Query("SELECT * FROM table_watchlist ORDER BY watchlist_srNo ASC")
    LiveData<List<Object_WatchlistCoinId>> getListOfAllIds();



    @Insert
    void insertCoin(Object_WatchlistCoinId coin);

    @Insert
    void insertManyCoins(ArrayList<Object_WatchlistCoinId> coinIterator) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCoin(Object_WatchlistCoinId coin);

    @Delete
    void deleteWatchlistCoin(Object_WatchlistCoinId watchlistCoinId);

    @Query("DELETE FROM table_watchlist")
    public void deleteWholeTable();
}
