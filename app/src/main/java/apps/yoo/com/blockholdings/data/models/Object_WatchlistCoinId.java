package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity (tableName = "table_watchlist")
public class Object_WatchlistCoinId {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "watchlist_srNo")
    int srNo ;

    @ColumnInfo(name = "watchlist_coinId")
    String coinId ;

    public Object_WatchlistCoinId(int srNo, String coinId) {
        this.srNo = srNo;
        this.coinId = coinId;
    }

    @Ignore
    public Object_WatchlistCoinId(String coinId){
        this.coinId = coinId ;
    }


    public String getCoinId() {
        return coinId;
    }


    public int getSrNo() {
        return srNo;
    }


    public static String getListOfIdString(List<Object_WatchlistCoinId> listOfIds){
        if (listOfIds.size() == 0) {
            return null ;
        }
        StringBuffer returnBuffer = new StringBuffer("") ;
        for (Object_WatchlistCoinId watchlistObj : listOfIds){
            returnBuffer.append(watchlistObj.getCoinId() + ",") ;
        }
        returnBuffer.deleteCharAt(returnBuffer.length()-1) ;
        return returnBuffer.toString() ;
    }
}
