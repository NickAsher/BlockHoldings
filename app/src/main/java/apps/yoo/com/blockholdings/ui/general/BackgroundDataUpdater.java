package apps.yoo.com.blockholdings.ui.general;

//import androidx.work.ListenableWorker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_NotificationCoinId;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_WidgetCoinPrice;
import apps.yoo.com.blockholdings.ui.detail.Activity_Detail;
import apps.yoo.com.blockholdings.ui.home.portfolio.Helper_Portfolio;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;

public class BackgroundDataUpdater extends Worker {
    Context context ;
    private static final String LOG_TAG = "BackgroundDataUpdater --> " ;
    AppDatabase db ;
    RequestQueue requestQueue ;
    NotificationManager notificationManager ;
    AppWidgetManager appWidgetManager ;


    List<Object_Transaction> listOfTransactions  ;
    List<Object_WidgetCoinPrice> listOfWidgets  ;
    List<Object_NotificationCoinId> listOfNotifications ;
    Set<String> setOfCoinIds ;
    Object_Currency currentCurrency ;
    JSONObject responseObj ;
    String timeInLong ;

    public BackgroundDataUpdater(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context ;
        Log.e(LOG_TAG, "BackgroundWorker is being constructed") ;
    }

    @NonNull
    @Override
    public Result doWork() {
        db = AppDatabase.getInstance(context.getApplicationContext()) ;
        requestQueue = Volley.newRequestQueue(context) ;
        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        notificationManager = context.getSystemService(NotificationManager.class) ;
        appWidgetManager = AppWidgetManager.getInstance(context) ;


        // firstly get the ids of all of the coins
        listOfTransactions = db.transactionDao().getListOfAllTransactions() ;
        listOfWidgets = db.widgetCoinPriceDao().getListOfAllIds()  ;
        listOfNotifications = db.notificationDao().getListOfAllIds() ;

        setOfCoinIds  = new HashSet<String>() ;

        for (Object_Transaction transactionObj : listOfTransactions){
            setOfCoinIds.add(transactionObj.getCoinId()) ;
        }

        for (Object_WidgetCoinPrice widgetObj : listOfWidgets){
            setOfCoinIds.add(widgetObj.getCoinId()) ;
        }

        for (Object_NotificationCoinId notificationObj : listOfNotifications){
            setOfCoinIds.add(notificationObj.getCoinId()) ;
        }

        String stringListOfCoinIds = TextUtils.join(",", setOfCoinIds) ;
        Log.e(LOG_TAG, "Set is   " + stringListOfCoinIds) ;



        if(setOfCoinIds.size()>0){
            getFromServer_CoinPriceData(stringListOfCoinIds);
        }









        return Result.success() ;
    }


    private void getFromServer_CoinPriceData(String coinIdsString){
        String currency = currentCurrency.getCurrencyId() ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.getURL_APICALL_SIMPLEPRICES(coinIdsString, currency),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(LOG_TAG, response) ;
                        timeInLong = Calendar.getInstance().getTimeInMillis() + "" ; ;
                        processFreshDataFromServer(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in making volley request");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        }) ;

        requestQueue.add(stringRequest) ;

    }



    private void processFreshDataFromServer(String response){

        try{
            responseObj = new JSONObject(response) ;
            for (String coinId : setOfCoinIds){
                String price = responseObj.getJSONObject(coinId).get(currentCurrency.getCurrencyId()).toString() ;
                Object_Coin.addToPriceData(db, coinId, price, Long.valueOf(timeInLong), "BackgroundUpdater");
            }

            for(Object_Transaction transactionObj : listOfTransactions){
                String price = responseObj.getJSONObject(transactionObj.getCoinId()).get(currentCurrency.getCurrencyId()).toString() ;
                String newTotalCost = new BigDecimal(price).multiply(new BigDecimal(transactionObj.getNoOfCoins())).toPlainString() ;
                db.transactionDao().updateTransactionPriceByTxnId(transactionObj.getTransactionNo(), price, newTotalCost);
            }

            Helper_Portfolio.recomputeAllPortfolios(db); ;
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString()) ;
        }


        updateWidgets() ;
        updateNotifications() ;

    }

    private void updateWidgets(){
        Widget_CoinPrice.refreshAllMyWidgets(context, appWidgetManager,
                db.widgetCoinPriceDao().getListOfAllIds(),
                db.widgetCoinPriceDao().getListOfCoinObjects(),
                currentCurrency) ;

    }


    private void updateNotifications(){
        List<Object_Coin> listOfCoins_InNotificationTable = db.notificationDao().getListOfCoinObjects() ;
        for(int i = 0; i< listOfNotifications.size() ; i++){
            final Object_NotificationCoinId notificationObj =  listOfNotifications.get(i) ;
            Object_Coin currentCoin = listOfCoins_InNotificationTable.get(i) ;
            String price = "" ;
            try {
                price = responseObj.getJSONObject(notificationObj.getCoinId()).get(currentCurrency.getCurrencyId()).toString();
            } catch (JSONException e){
                Log.e(LOG_TAG, e.toString()) ;
            }
            String currentTime = new SimpleDateFormat("MMM-dd hh:mm:ss a").format(new Date(Long.valueOf(timeInLong))) ;

            String notificationText = "CoinName : " + currentCoin.getName() + "\n Price : " + currentCurrency.getCurrencySymbol() + price  + "\n LastUpdated  : " + currentTime;
            Intent openDetailScreen = new Intent(context, Activity_Detail.class) ;
            openDetailScreen.putExtra("coinId", notificationObj.getCoinId()) ;

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    notificationObj.getSrNo(),
                    openDetailScreen,
                    PendingIntent.FLAG_CANCEL_CURRENT);



            Intent intent = new Intent(context, Service_CloseNotification.class) ;
            intent.putExtra("notificationId", notificationObj.getSrNo()) ;


            PendingIntent pIntent_ServiceCloseNotif = PendingIntent.getService(context,
                    notificationObj.getSrNo(),
                    intent,
                    PendingIntent.FLAG_ONE_SHOT) ;

            PendingIntent pendingIntentRefresh = PendingIntent.getService(
                    context,
                    notificationObj.getSrNo(),
                    new Intent(context, Service_StartBackgroundUpdater.class),
                    PendingIntent.FLAG_ONE_SHOT
            ) ;

            RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notif_singlecoinprice);
            notificationLayout.setTextViewText(R.id.notificationCoinPrice_TV_CoinName, currentCoin.getName());
            notificationLayout.setTextViewText(R.id.notificationCoinPrice_TV_CoinPrice, currentCurrency.getCurrencySymbol() + price);
            notificationLayout.setOnClickPendingIntent(R.id.notificationCoinPrice_TV_CloseNotification, pIntent_ServiceCloseNotif);


            String priceString = "Price : " + currentCurrency.getCurrencySymbol() + price  ;
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationObj.getCoinId())
                    .setSmallIcon(R.drawable.ic_block_explorer)
                    .setContentTitle(currentCoin.getName())
                    .setColor(Color.CYAN)
                    .setContentText(priceString)
                    .setStyle(new NotificationCompat.InboxStyle().addLine(priceString))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .addAction(new NotificationCompat.Action(R.drawable.ic_remove_circle_black_24dp, "Close Notification", pIntent_ServiceCloseNotif))
                    .addAction(new NotificationCompat.Action(R.drawable.ic_refresh_black, "Refresh", pendingIntentRefresh))

                    ;

            Glide
                    .with(getApplicationContext()).asBitmap()
                    .load(currentCoin.getImageLogoLink())

                    .into(new SimpleTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Log.e(LOG_TAG, "image is now ready") ;
                           builder.setLargeIcon(resource)  ;
                            notificationManager.notify(notificationObj.getSrNo(), builder.build());

                        }
                    });


        }
    }



}
