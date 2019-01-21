package apps.yoo.com.blockholdings.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;


import apps.yoo.com.blockholdings.data.Objects.Object_Coin;
import apps.yoo.com.blockholdings.data.Objects.Object_Exchange;
import apps.yoo.com.blockholdings.data.Objects.Object_NewsSite;
import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;
import apps.yoo.com.blockholdings.data.dao.Dao_Coin;
import apps.yoo.com.blockholdings.data.dao.Dao_Exchange;
import apps.yoo.com.blockholdings.data.dao.Dao_NewsSite;
import apps.yoo.com.blockholdings.data.dao.Dao_Transaction;

@Database(entities = {Object_NewsSite.class, Object_Coin.class, Object_Exchange.class, Object_Transaction.class}, version = 10, exportSchema = false)
@TypeConverters({MyTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName() + "--> ";
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "yolo_blockholdings.db";
    private static AppDatabase instance ;

    public static AppDatabase getInstance(Context appContext){
        if(instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating a new Database Instance") ;

                instance = Room.databaseBuilder(appContext.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build() ;
            }
        }
        Log.d(LOG_TAG, "Getting the database instance") ;
        return instance ;
    }




    public abstract Dao_NewsSite newSiteDao();
    public abstract Dao_Coin coinDao();
    public abstract Dao_Exchange exchangeDao() ;
    public abstract Dao_Transaction transactionDao() ;




}
