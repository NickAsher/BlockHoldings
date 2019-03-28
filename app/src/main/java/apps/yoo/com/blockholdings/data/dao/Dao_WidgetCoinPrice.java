package apps.yoo.com.blockholdings.data.dao;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_WidgetCoinPrice;

@Dao
public interface Dao_WidgetCoinPrice {


    @Query("SELECT * FROM table_widget_coinprice ORDER BY srNo ASC")
    List<Object_WidgetCoinPrice> getListOfAllIds();

    @Query("SELECT table_coin.* FROM table_coin, table_widget_coinprice WHERE table_widget_coinprice.coinId = table_coin.coin_Id ORDER BY table_widget_coinprice.srNo ASC")
    List<Object_Coin> getListOfCoinObjects();

    @Query("SELECT * FROM table_widget_coinprice WHERE coinId = :coinId ORDER BY srNo ASC")
    LiveData<Object_WidgetCoinPrice> getWidget_ByCoinId_LiveData(String coinId);



    @Insert
    long insertWidget(Object_WidgetCoinPrice widgetObj);

    @Insert
    void insertManyWidgets(ArrayList<Object_WidgetCoinPrice> widgetObjIterator) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWidget(Object_WidgetCoinPrice widgetObj);

    @Delete
    void deleteWidget(Object_WidgetCoinPrice widgetObj);

    @Query("DELETE FROM table_widget_coinprice WHERE srNo = :srNo")
    public void deleteWidget_BySrNo(int srNo);

    @Query("DELETE FROM table_widget_coinprice WHERE widgetId = :widgetId")
    public void deleteWidget_ByWidgetId(int widgetId);

    @Query("DELETE FROM table_widget_coinprice WHERE coinId = :coinId")
    public void deleteWidget_ByCoinId(String coinId);

    @Query("DELETE FROM table_widget_coinprice")
    public void deleteWholeTable();
}
