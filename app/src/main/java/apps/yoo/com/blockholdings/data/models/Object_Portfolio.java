package apps.yoo.com.blockholdings.data.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import apps.yoo.com.blockholdings.data.AppDatabase;

@Entity(tableName = "table_portfolio")
public class Object_Portfolio {
    private static final String LOG_TAG = "Object_Portfolio --> " ;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "portfolio_id")
    int portfolioId;

    @ColumnInfo(name = "portfolio_name")
    String portfolioName;

    @ColumnInfo(name = "portfolio_value")
    String portfolioValue;

    @ColumnInfo(name = "portfolio_UpdateLog")
    String portfolioUpdateLog ;

    @ColumnInfo (name = "portfolio_freeCurrency")
    String freeCurrency ;



    public Object_Portfolio(int portfolioId, String portfolioName, String portfolioValue) {
        this.portfolioId = portfolioId ;
        this.portfolioName = portfolioName;
        this.portfolioValue = portfolioValue;
        this.portfolioUpdateLog = "[]" ;
        this.freeCurrency = "0" ;
    }

    @Ignore
    public Object_Portfolio(String portfolioName, String portfolioValue) {
        this.portfolioName = portfolioName;
        this.portfolioValue = portfolioValue;
        this.portfolioUpdateLog = "[]" ;
        this.freeCurrency = "0" ;



    }


    public int getPortfolioId() {
        return portfolioId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public String getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(String portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    public String getPortfolioUpdateLog() {
        return portfolioUpdateLog;
    }

    public void setPortfolioUpdateLog(String portfolioUpdateLog) {
        this.portfolioUpdateLog = portfolioUpdateLog;
    }

    public String getFreeCurrency() {
        return freeCurrency;
    }

    public void setFreeCurrency(String freeCurrency) {
        this.freeCurrency = freeCurrency;
    }

    public static void addInDb_portfolioUpdateLog(AppDatabase db, int portfolioId, String newTotalValue){
        try{
            Object_Portfolio portfolioObj = db.portfolioDao().getPortfolioById(portfolioId) ;
            JSONArray updateLog = new JSONArray(portfolioObj.getPortfolioUpdateLog()) ;
            String timeInLong = Calendar.getInstance().getTimeInMillis() + "" ;
            JSONArray newJsonArray = new JSONArray().put(newTotalValue).put(timeInLong).put("") ;
            db.portfolioDao().updatePortfolio_UpdateLog(portfolioObj.getPortfolioId(), newTotalValue, updateLog.put(newJsonArray).toString());
            Log.e(LOG_TAG, "Update log of portfolio is changed") ;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }

    @Override
    public String toString() {
        return portfolioId + " - " + portfolioName + " - " +  portfolioValue + " - " + freeCurrency  + "\n";
    }
}
