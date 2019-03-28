package apps.yoo.com.blockholdings.ui.general;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import android.util.Log;

import apps.yoo.com.blockholdings.data.AppDatabase;

public class Service_CloseNotification extends IntentService {
    Context context ;
    String LOG_TAG = "Service_CloseNotification --> " ;
    NotificationManager notificationManager ;
    int notificationId ;
    AppDatabase db ;


    public Service_CloseNotification() {
        super("Service_CloseNotification");
        Log.e(LOG_TAG, "Constructor is called") ;

    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(LOG_TAG, "Service is created") ;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        context = this ;
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE) ;
        db = AppDatabase.getInstance(context.getApplicationContext()) ;

        int notificationId = (int)intent.getExtras().get("notificationId") ;
        Log.e(LOG_TAG, "NotificationId is " + notificationId) ;
        notificationManager.cancel(notificationId);
        db.notificationDao().deleteNotification_BySrNo(notificationId);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "Service is now destroyed") ;
    }
}
