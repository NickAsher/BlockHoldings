package apps.yoo.com.blockholdings.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import apps.yoo.com.blockholdings.data.models.Object_Portfolio;

@Dao
public interface Dao_Portfolio {

    @Query("SELECT * FROM table_portfolio ORDER BY portfolio_id ASC")
    List<Object_Portfolio> getListOfAllPortfolios();

    @Query("SELECT * FROM table_portfolio ORDER BY portfolio_id ASC")
    LiveData<List<Object_Portfolio>> getListOfAllPortfolios_LiveData();

    @Query("SELECT * FROM table_portfolio WHERE portfolio_id = :portfolioId")
    Object_Portfolio getPortfolioById(int portfolioId);

    @Query("SELECT * FROM table_portfolio WHERE portfolio_id = :portfolioId")
    LiveData<Object_Portfolio> getPortfolioById_LiveData(int portfolioId);

    @Insert
    void insertPortfolio(Object_Portfolio portfolio);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePortfolio(Object_Portfolio portfolio);


    @Query("UPDATE table_portfolio SET portfolio_value = :newTotalValue WHERE portfolio_id = :portfolioId")
    void updatePortfolio_TotalValue(int portfolioId, String newTotalValue) ;

    @Query("UPDATE table_portfolio SET portfolio_name = :newPortfolioName WHERE portfolio_id = :portfolioId")
    void updatePortfolio_Name(int portfolioId, String newPortfolioName) ;

    @Query("UPDATE table_portfolio SET portfolio_value = :newTotalValue, portfolio_UpdateLog = :newUpdateLog WHERE portfolio_id = :portfolioId")
    void updatePortfolio_UpdateLog(int portfolioId, String newTotalValue, String newUpdateLog) ;

    @Delete
    void deletePortfolio(Object_Portfolio newsSite);

    @Query("DELETE FROM  table_portfolio  WHERE portfolio_id = :portfolioId")
    void deletePortfolioById(int portfolioId) ;

    @Query("DELETE FROM table_portfolio")
    public void deleteWholeTable();
}
