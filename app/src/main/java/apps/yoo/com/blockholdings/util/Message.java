package apps.yoo.com.blockholdings.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Message {
    public static void display(Context context , String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void display_w_Log(Context context, String LOG_TAG, String msg ){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Log.e(LOG_TAG, msg) ;
    }
}
