package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_widget_coinprice")
public class Object_WidgetCoinPrice {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "srNo")
    int srNo ;

    @ColumnInfo(name = "widgetId")
    int widgetId ;

    @ColumnInfo(name = "coinId")
    String coinId ;

    @ColumnInfo(name = "currencyId")
    String currencyId ;

    @ColumnInfo(name = "lastUpdatedAt")
    String lastUpdatedAt ; // time In Long


    public Object_WidgetCoinPrice(int srNo, int widgetId, String coinId, String currencyId, String lastUpdatedAt) {
        this.srNo = srNo;
        this.widgetId = widgetId;
        this.coinId = coinId;
        this.currencyId = currencyId;
        this.lastUpdatedAt = lastUpdatedAt;
    }


    @Ignore
    public Object_WidgetCoinPrice(int widgetId, String coinId, String currencyId, String lastUpdatedAt) {
        this.widgetId = widgetId;
        this.coinId = coinId;
        this.currencyId = currencyId;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Ignore
    public Object_WidgetCoinPrice(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getSrNo() {
        return srNo;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
