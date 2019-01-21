package apps.yoo.com.blockholdings.data.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import apps.yoo.com.blockholdings.R;

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
