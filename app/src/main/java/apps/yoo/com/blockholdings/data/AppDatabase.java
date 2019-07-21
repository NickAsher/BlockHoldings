package apps.yoo.com.blockholdings.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.util.Log;


import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.data.models.Object_NewsSite;
import apps.yoo.com.blockholdings.data.models.Object_NotificationCoinId;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_VsSimpleCurrency;
import apps.yoo.com.blockholdings.data.models.Object_WatchlistCoinId;
import apps.yoo.com.blockholdings.data.models.Object_WidgetCoinPrice;
import apps.yoo.com.blockholdings.data.dao.Dao_Coin;
import apps.yoo.com.blockholdings.data.dao.Dao_Currency;
import apps.yoo.com.blockholdings.data.dao.Dao_Exchange;
import apps.yoo.com.blockholdings.data.dao.Dao_NewsSite;
import apps.yoo.com.blockholdings.data.dao.Dao_Notification;
import apps.yoo.com.blockholdings.data.dao.Dao_Portfolio;
import apps.yoo.com.blockholdings.data.dao.Dao_Transaction;
import apps.yoo.com.blockholdings.data.dao.Dao_VsSimpleCurrency;
import apps.yoo.com.blockholdings.data.dao.Dao_Watchlist;
import apps.yoo.com.blockholdings.data.dao.Dao_WidgetCoinPrice;

@Database(entities = {Object_NewsSite.class, Object_Coin.class, Object_Exchange.class, Object_Transaction.class,
        Object_Currency.class, Object_Portfolio.class, Object_WatchlistCoinId.class, Object_NotificationCoinId.class,
        Object_WidgetCoinPrice.class, Object_VsSimpleCurrency.class}, version = 35, exportSchema = false)
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
    public abstract Dao_Currency currencyDao() ;
    public abstract Dao_Portfolio portfolioDao() ;
    public abstract Dao_Watchlist watchlistDao() ;
    public abstract Dao_Notification notificationDao() ;
    public abstract Dao_WidgetCoinPrice widgetCoinPriceDao() ;
    public abstract Dao_VsSimpleCurrency vsSimpleCurrencyDao() ;



}
