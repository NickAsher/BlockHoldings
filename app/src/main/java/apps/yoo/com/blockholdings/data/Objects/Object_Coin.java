package apps.yoo.com.blockholdings.data.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "table_coin")
public class Object_Coin {

    @PrimaryKey
    @ColumnInfo(name = "coinId")
    @NonNull String id ;

    @ColumnInfo(name = "coinSymbol")
    String symbol ;

    @ColumnInfo(name = "coinName")
    String name;

    @ColumnInfo(name = "coinImageLogoLink")
    String imageLogoLink ;

    @ColumnInfo(name = "coinTwitterLink")
    String twitterLink ;

    public static final String LOG_TAG = "Object_Coin --> " ;

    public Object_Coin(String id, String symbol, String name, String imageLogoLink, String twitterLink) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.imageLogoLink = imageLogoLink;
        this.twitterLink = twitterLink;
    }


    public Object_Coin(JSONObject jsonObject){
        try {
            this.id = jsonObject.getString("id");
            this.symbol = jsonObject.getString("symbol");
            this.name = jsonObject.getString("name");
            this.imageLogoLink = jsonObject.getString("imagelogo");
            this.twitterLink = jsonObject.getString("twitter");
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }



    }


    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getImageLogoLink() {
        return imageLogoLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }


    @Override
    public String toString() {
        return id + " - " +  symbol + " - " + name + " - " + imageLogoLink + " - " + twitterLink  + " \n" ;
    }


    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject() ;
        try{
            jsonObject.put("id", id) ;
            jsonObject.put("symbol", symbol) ;
            jsonObject.put("name", name) ;
            jsonObject.put("imagelogo", imageLogoLink) ;
            jsonObject.put("twitter", twitterLink) ;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
        return jsonObject ;
    }
}
