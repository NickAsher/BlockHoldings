package apps.yoo.com.blockholdings.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.util.Constants;

public class MySharedPreferences {

    public static final String PREFS_KEY_THEME = "pref_key_theme" ;
    public static final int PREFS_VALUE_THEME_LIGHT = 1;
    public static final int PREFS_VALUE_THEME_DARK = 2;

    public static int getAppThemeFromPreference(Context applicationContext){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        return sp.getInt(MySharedPreferences.PREFS_KEY_THEME, MySharedPreferences.PREFS_VALUE_THEME_LIGHT);

    }

    public static void setAppThemeInPreferences(Context applicationContext, int themeValue){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putInt(MySharedPreferences.PREFS_KEY_THEME, themeValue) ;
        editor.commit() ;
    }


    public static void setAppThemeBasedOnPreference(Activity activity, Context applicationContext){
        int theme = getAppThemeFromPreference(applicationContext) ;
        if(theme == PREFS_VALUE_THEME_DARK){
            activity.setTheme(R.style.AppTheme_Dark);
        } else {
            activity.setTheme(R.style.AppTheme_Light);
        }
    }
}
