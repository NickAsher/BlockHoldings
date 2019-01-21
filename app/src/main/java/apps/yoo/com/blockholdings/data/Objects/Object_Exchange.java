package apps.yoo.com.blockholdings.data.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "table_exchange")
public class Object_Exchange {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    String id ;

    @ColumnInfo(name = "name")
    String name ;

    @ColumnInfo(name = "image")
    String imageLink ;

    public Object_Exchange(String id, String name, String imageLink) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
    }

    @Ignore
    public Object_Exchange(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }
}
