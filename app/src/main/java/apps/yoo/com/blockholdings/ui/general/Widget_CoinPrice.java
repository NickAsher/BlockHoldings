package apps.yoo.com.blockholdings.ui.general;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_WidgetCoinPrice;
import apps.yoo.com.blockholdings.ui.detail.Activity_Detail;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link Activity_ConfigureWidget_CoinPrice Widget_CoinPriceConfigureActivity}
 */
public class Widget_CoinPrice extends AppWidgetProvider {

    public static final String LOG_TAG = "Widget_CoinPrice --> " ;
    AppDatabase db ;

    static void createMyAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String coinId, String coinName){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget__coin_price);

        Intent intent = new Intent(context, Activity_Detail.class) ;
        intent.putExtra("coinId", coinId) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT) ;
        views.setOnClickPendingIntent(R.id.widgetCoinPrice_Lt_Parent, pendingIntent);

        views.setTextViewText(R.id.widgetCoinPriceInitial_TV_CoinName, coinName);
        views.setImageViewBitmap(R.id.widgetCoinPriceInitial_ImageView_CoinImage,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_circle_white));


    }



    public static void updateMyWidget(Context context, AppWidgetManager appWidgetManager,
                                 int widgetId, String coinId, String coiName, String imageLogoLink, String coinPrice, String lastUpdatedAt, Object_Currency currencyObj){

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget__coin_price);

        Intent intent = new Intent(context, Activity_Detail.class) ;
        intent.putExtra("coinId", coinId) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT) ;
        views.setOnClickPendingIntent(R.id.widgetCoinPrice_Lt_Parent, pendingIntent);

        String currentTime ;
        if(!lastUpdatedAt.isEmpty()){
            currentTime = new SimpleDateFormat("MMM-dd hh:mm:ss a").format(new Date(Long.valueOf(lastUpdatedAt))) ;
        } else {
            currentTime = "huh" ;
        }
        views.setTextViewText(R.id.widgetCoinPrice_TV_CoinName, coiName + " : " + currencyObj.getCurrencySymbol() + coinPrice);
        views.setTextViewText(R.id.widgetCoinPrice_TV_LastUpdated, currentTime);




        AppWidgetTarget awt = new AppWidgetTarget(context, R.id.widgetCoinPrice_ImageView_CoinImage, views, widgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        RequestOptions options = new RequestOptions().
                override(300, 300).placeholder(R.drawable.ic_telegram).error(R.drawable.ic_reddit) ;
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imageLogoLink)
                .apply(options)
                .into(awt);



        PendingIntent pendingIntentRefresh = PendingIntent.getService(
                context,
                widgetId,
                new Intent(context, Service_StartBackgroundUpdater.class),
                PendingIntent.FLAG_CANCEL_CURRENT
        ) ;
        views.setOnClickPendingIntent(R.id.widgetCoinPrice_ImageView_Refresh, pendingIntentRefresh);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(widgetId, views);


    }

    public static void refreshAllMyWidgets(Context context, AppWidgetManager appWidgetManager, List<Object_WidgetCoinPrice> listOfWidgets, List<Object_Coin> listOfCoins, Object_Currency currencyObj){
        int length = listOfWidgets.size() ;

        for(int i = 0 ; i< length ; i++){
            int widgetId = listOfWidgets.get(i).getWidgetId() ;
            String coinId = listOfWidgets.get(i).getCoinId() ;
            String coinName = listOfCoins.get(i).getName() ;
            String coinImage = listOfCoins.get(i).getImageLogoLink() ;
            try {
                JSONArray priceData = listOfCoins.get(i).getPriceData_LastItem() ;
                String price = priceData.getString(0) ;
                String timeInLong = priceData.getString(1) ;

                updateMyWidget(context, appWidgetManager, widgetId, coinId, coinName, coinImage, price, timeInLong, currencyObj);

            }catch (Exception e){
                Log.e(LOG_TAG, e.toString()) ;
            }

        }
    }




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//                updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MySharedPreferences.deleteWidgetCoinPriceInfo_FromPreferences(context, appWidgetId);
            db = AppDatabase.getInstance(context.getApplicationContext()) ;
            db.widgetCoinPriceDao().deleteWidget_ByWidgetId(appWidgetId);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

