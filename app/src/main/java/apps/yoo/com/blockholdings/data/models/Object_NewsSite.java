package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_newssite")
public class Object_NewsSite {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "siteId")
    int siteId ;

    @ColumnInfo(name = "siteName")
    String siteName ;

    @ColumnInfo(name = "siteLink")
    String siteLink ;



    @ColumnInfo(name = "isImageSupported")
    boolean isImageSupported ;

    public Object_NewsSite(int siteId, String siteName, String siteLink, boolean isImageSupported) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.siteLink = siteLink;
        this.isImageSupported = isImageSupported;
    }

    public int getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteLink() {
        return siteLink;
    }


    public boolean isImageSupported() {
        return isImageSupported;
    }
}
