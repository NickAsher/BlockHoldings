package apps.yoo.com.blockholdings.ui.detail;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_NotificationCoinId;
import apps.yoo.com.blockholdings.ui.general.BackgroundDataUpdater;
import apps.yoo.com.blockholdings.ui.general.Service_CloseNotification;
import apps.yoo.com.blockholdings.ui.general.Service_StartBackgroundUpdater;
import apps.yoo.com.blockholdings.ui.general.Widget_CoinPrice;
import apps.yoo.com.blockholdings.util.Message;


public class Fragment_DetailNotification extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_DetailPrice --> " ;
    AppDatabase db;
    NotificationManager notificationManager ;
    AppWidgetManager appWidgetManager  ;


    ImageView btn_AddNotification, btn_AddWidget ;
    TextView tv_NotificationContent, tv_WidgetContent, tv_AddNotification, tv_AddWidget ;
    Button btn_RemoveNotification ;
    String coinId ;
    Object_Coin currentCoin ;
    Object_Currency currentCurrency ;

    int notificationId ;


    public Fragment_DetailNotification() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coinId = getArguments().getString("coinId") ;
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frg_detail_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        notificationManager = context.getSystemService(NotificationManager.class);
        appWidgetManager = AppWidgetManager.getInstance(context) ;

        getReferences(view) ;

        currentCoin = db.coinDao().getCoinById(coinId) ;
        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;

        setupBasicUI();
        setupAddButtons();

    }

    private void getReferences(View itemView){


        btn_AddNotification = itemView.findViewById(R.id.frgDetailNotification_ImageView_AddNotificationBtn) ;
        btn_AddWidget = itemView.findViewById(R.id.frgDetailNotification_ImageView_AddWidgetBtn) ;
        tv_AddNotification = itemView.findViewById(R.id.frgDetailNotification_TV_AddNotificationText) ;
        tv_AddWidget = itemView.findViewById(R.id.frgDetailNotification_TV_AddWidgetText) ;
        tv_NotificationContent = itemView.findViewById(R.id.frgDetailNotification_TV_NotificationContent) ;
        tv_WidgetContent = itemView.findViewById(R.id.frgDetailNotification_TV_WidgetContent) ;
        btn_RemoveNotification = itemView.findViewById(R.id.frgDetailNotification_Btn_RemoveNotification) ;



    }


    private void setupBasicUI(){

        btn_RemoveNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(notificationId);
                db.notificationDao().deleteNotification_ByCoinId(coinId);
            }
        });
        db.notificationDao().getListOfNotification_ById_LiveData(coinId).observe(this, new Observer<List<Object_NotificationCoinId>>() {
            @Override
            public void onChanged(@Nullable List<Object_NotificationCoinId> list) {
                if(list.size() == 0){
                    btn_AddNotification.setVisibility(View.VISIBLE);
                    tv_AddNotification.setVisibility(View.VISIBLE);
                    tv_NotificationContent.setVisibility(View.GONE);
                    btn_RemoveNotification.setVisibility(View.GONE);
                } else
                if (list.size() > 0) {

                    currentCoin = db.coinDao().getCoinById(list.get(0).getCoinId()) ;
                    notificationId = list.get(0).getSrNo() ;

                    btn_AddNotification.setVisibility(View.GONE);
                    tv_AddNotification.setVisibility(View.GONE);
                    tv_NotificationContent.setVisibility(View.VISIBLE);
                    btn_RemoveNotification.setVisibility(View.VISIBLE);

                    String notificationContent = "CoinName: " + currentCoin.getName() ;
                    tv_NotificationContent.setText(notificationContent);

                }
            }
        }) ;





    }

    private void setupAddButtons(){
        btn_AddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notifString  = "" ;
                try {
                    JSONArray currentPriceData = currentCoin.getPriceData_LastItem();
                    String lastCoinPrice = currentPriceData.getString(0);
                    String lastUpdatedTime = currentPriceData.getString(1);
                    notifString = "CoinName : " + currentCoin.getName() + "\n Price : " + lastCoinPrice + "\n lastUpdated : " + lastUpdatedTime;

                }catch (JSONException e){
                    notifString = "CoinName : " + currentCoin.getName() + "\n Price : " + "loading" + "\n lastUpdated : " + "loading";
                    Log.e(LOG_TAG, e.toString()) ;
                }
                long notificationId = db.notificationDao().insertCoin(new Object_NotificationCoinId(coinId)) ;

                Intent openDetailScreen = new Intent(context, Activity_Detail.class) ;
                openDetailScreen.putExtra("coinId", coinId) ;

                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        (int)notificationId,
                        openDetailScreen,
                        PendingIntent.FLAG_CANCEL_CURRENT);



                Intent intent = new Intent(context, Service_CloseNotification.class) ;
                intent.putExtra("notificationId", (int)notificationId) ;


                PendingIntent pIntent_ServiceCloseNotif = PendingIntent.getService(context,
                        (int)notificationId,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT) ;

                PendingIntent pendingIntentRefresh = PendingIntent.getService(
                        context,
                        (int)notificationId,
                        new Intent(context, Service_StartBackgroundUpdater.class),
                        PendingIntent.FLAG_CANCEL_CURRENT
                ) ;




                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, coinId)
                        .setSmallIcon(R.drawable.ic_block_explorer)
                        .setContentText(notifString).setStyle(new NotificationCompat.BigTextStyle().bigText(notifString))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .addAction(new NotificationCompat.Action())
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .addAction(new NotificationCompat.Action(R.drawable.ic_remove_circle_black_24dp, "Close Notification", pIntent_ServiceCloseNotif))
                        .addAction(new NotificationCompat.Action(R.drawable.ic_refresh_black, "Refresh", pendingIntentRefresh))

                        ;

                notificationManager.notify((int)notificationId, builder.build());
                Message.display(context, "Notification refreshed");


                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundDataUpdater.class)
                        .addTag("idTag_BackgroundWorker_2") // used just like findFragmentByTag
//                .setInputData(inputWorkData)
//                .setConstraints()
                        .build() ;

                WorkManager.getInstance().enqueue(workRequest) ;

            }
        });


        btn_AddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            Widget_CoinPrice.refreshAllMyWidgets(context, appWidgetManager,
                    db.widgetCoinPriceDao().getListOfAllIds(),
                    db.widgetCoinPriceDao().getListOfCoinObjects(),
                    currentCurrency ) ;

            Message.display(context, "widgets refreshed");


            }
        });


    }


}
