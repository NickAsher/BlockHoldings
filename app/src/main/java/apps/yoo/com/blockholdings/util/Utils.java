package apps.yoo.com.blockholdings.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.math.RoundingMode;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;

public class Utils {


    public static String showHumanDecimals(String string_W_LotsOfDecimals){
        BigDecimal no = new BigDecimal(string_W_LotsOfDecimals) ;

        // when no. is less than 0
        if(no.compareTo(new BigDecimal(1)) == -1  ){
            return no.toString() ;
        } else {
            return new BigDecimal(string_W_LotsOfDecimals).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() ;
        }
    }






}
