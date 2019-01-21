package apps.yoo.com.blockholdings.util;

import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void display(Context context , String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
