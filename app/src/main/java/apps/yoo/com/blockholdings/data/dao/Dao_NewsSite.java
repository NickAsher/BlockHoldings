package apps.yoo.com.blockholdings.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_NewsSite;

@Dao
public interface Dao_NewsSite {

    @Query("SELECT * FROM table_newssite ORDER BY siteId ASC")
    List<Object_NewsSite> getListOfNewsSites() ;

    @Query("SELECT * FROM table_newssite WHERE siteId = :siteId")
    Object_NewsSite getNewsSiteById(int siteId) ;

    @Insert
    void insertFoodItem(Object_NewsSite newsSite) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFoodItem(Object_NewsSite newsSite) ;


    @Delete
    void deleteFoodItem(Object_NewsSite newsSite) ;

}
