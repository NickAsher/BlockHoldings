package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "table_notificationCoinId")
public class Object_NotificationCoinId {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    int srNo ;

    @ColumnInfo(name = "coinId")
    String coinId ;

    public Object_NotificationCoinId(int srNo, String coinId) {
        this.srNo = srNo;
        this.coinId = coinId;
    }

    @Ignore
    public Object_NotificationCoinId(String coinId){
        this.coinId = coinId ;
    }


    public String getCoinId() {
        return coinId;
    }


    public int getSrNo() {
        return srNo;
    }


    public static String getListOfIdString(List<Object_NotificationCoinId> listOfIds){
        if (listOfIds.size() == 0) {
            return null ;
        }
        StringBuffer returnBuffer = new StringBuffer("") ;
        for (Object_NotificationCoinId notificationCoinIdObj : listOfIds){
            returnBuffer.append(notificationCoinIdObj.getCoinId() + ",") ;
        }
        returnBuffer.deleteCharAt(returnBuffer.length()-1) ;
        return returnBuffer.toString() ;
    }
}

