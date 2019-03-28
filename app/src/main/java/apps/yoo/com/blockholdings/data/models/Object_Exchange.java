package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.common.base.Predicate;

@Entity(tableName = "table_exchange")
public class Object_Exchange {

    @PrimaryKey
    @ColumnInfo(name = "exchange_Id")
    @NonNull
    String id ;

    @ColumnInfo(name = "exchangeName")
    String name ;

    @ColumnInfo(name = "exchangeImageLink")
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


    @Ignore
    public Object_Exchange() { }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }


    @Override
    public String toString() {
        return "-id:" + id + "  -name:" + name + "  -imageLink:" + imageLink;
    }


    public static Predicate<Object_Exchange> getPredicateFilter_Name_containsString(final String text){
        Predicate predicate = new Predicate<Object_Exchange>(){
            @Override
            public boolean apply(Object_Exchange input) {

                if(input.getName().toLowerCase().contains(text.toLowerCase())){
                    return true ;
                }
                return false;
            }
        } ;
        return predicate ;
    }

    public static Object_Exchange getGlobalAverage(){
        return new Object_Exchange("0000", "Global Average", "") ;
    }
}
