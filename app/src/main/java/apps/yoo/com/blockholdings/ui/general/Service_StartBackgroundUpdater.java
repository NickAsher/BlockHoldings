package apps.yoo.com.blockholdings.ui.general;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import apps.yoo.com.blockholdings.data.AppDatabase;

public class Service_StartBackgroundUpdater extends IntentService {
    Context context ;
    String LOG_TAG = "Service_StartBackgroundUpdater --> " ;
    NotificationManager notificationManager ;
    int notificationId ;
    AppDatabase db ;


    public Service_StartBackgroundUpdater() {
        super("Service_StartBackgroundUpdater");
        Log.e(LOG_TAG, "Constructor is called") ;

    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(LOG_TAG, "Service is created") ;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundDataUpdater.class)
                .addTag("idTag_BackgroundWorker_4")
                .build() ;
        WorkManager.getInstance().enqueue(workRequest) ;


        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "Service is now destroyed") ;
    }
}
