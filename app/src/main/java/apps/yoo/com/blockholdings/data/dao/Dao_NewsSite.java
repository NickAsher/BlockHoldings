package apps.yoo.com.blockholdings.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import apps.yoo.com.blockholdings.data.Objects.Object_NewsSite;

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
