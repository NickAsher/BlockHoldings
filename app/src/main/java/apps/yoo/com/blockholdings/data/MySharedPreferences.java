package apps.yoo.com.blockholdings.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;

import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.util.Helper_Colors;

public class MySharedPreferences {

    public static final String PREFS_KEY_THEME = "pref_key_theme" ;
    public static final int PREFS_VALUE_THEME_LIGHT = 1;
    public static final int PREFS_VALUE_THEME_DARK = 2;

    public static final String KEY_SORTMETHOD = "pref_key_sort" ;
    public static final int VALUE_SORT_NAME_ASC = 1 ;
    public static final int VALUE_SORT_NAME_DESC = 2 ;
    public static final int VALUE_SORT_PERCENTAGE_ASC = 3 ;
    public static final int VALUE_SORT_PERCENTAGE_DESC = 4 ;
    public static final int VALUE_SORT_HOLDINGS_ASC = 5 ;
    public static final int VALUE_SORT_HOLDINGS_DESC = 6 ;

    public static final String KEY_CURRENCY_ID = "key_currency_id" ;
    public static final String KEY_CURRENCY_NAME = "key_currency_name" ;
    public static final String KEY_CURRENCY_SYMBOL = "key_currency_symbol" ;

    public static final int VALUE_THEME_0 = 0 ;
    public static final int VALUE_THEME_1 = 1 ;
    public static final int VALUE_THEME_2 = 2 ;
    public static final int VALUE_THEME_3 = 3 ;
    public static final int VALUE_THEME_4 = 4 ;
    public static final int VALUE_THEME_5 = 5 ;
    public static final int VALUE_THEME_6 = 6 ;
    public static final int VALUE_THEME_7 = 7 ;
    public static final int VALUE_THEME_8 = 8 ;
    public static final int VALUE_THEME_9 = 9 ;
    public static final int VALUE_THEME_10 = 10 ;
    public static final int VALUE_THEME_11 = 11 ;
    public static final int VALUE_THEME_12 = 12 ;

    public static final String KEY_CURRENT_PORTFOLIO = "key_current_portfolio" ;
    public static final String PREFIX_KEY_WIDGETCOINPRICE = "prefix_key_coinId_" ;




    public static int getAppThemeFromPreference(Context applicationContext){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        return sp.getInt(MySharedPreferences.PREFS_KEY_THEME, MySharedPreferences.VALUE_THEME_0);

    }

    public static void setAppThemeInPreferences(Context applicationContext, int themeValue){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putInt(MySharedPreferences.PREFS_KEY_THEME, themeValue) ;
        editor.commit() ;
    }


    public static GradientDrawable getAppThemeGradientDrawableOnPreference(Context applicationContext ){
        int theme = getAppThemeFromPreference(applicationContext) ;

        int startColor = Helper_Colors.getListOfColorStart().get(theme) ;
        int endColor = Helper_Colors.getListOfColorEnd().get(theme) ;

        GradientDrawable gd =  new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {startColor,endColor});
        gd.setCornerRadius(0f);

        return gd ;
    }



    /* ***************************** Portfolio Sorting  **************************** */
    public static int getPortfolioSortMethod_FromPreference(Context applicationContext){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        return sp.getInt(MySharedPreferences.KEY_SORTMETHOD, MySharedPreferences.VALUE_SORT_NAME_ASC);

    }

    public static void setPortfolioSortMode_InPreferences(Context applicationContext, int sortMode){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putInt(MySharedPreferences.KEY_SORTMETHOD, sortMode) ;
        editor.commit() ;
    }



    /* ****************************** Currency ************************************* */

    public static void setCurrency_InPreferences(Context applicationContext, Object_Currency currencyObj){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putString(MySharedPreferences.KEY_CURRENCY_ID, currencyObj.getCurrencyId()) ;
        editor.putString(MySharedPreferences.KEY_CURRENCY_NAME, currencyObj.getCurrencyName()) ;
        editor.putString(MySharedPreferences.KEY_CURRENCY_SYMBOL, currencyObj.getCurrencySymbol()) ;
        editor.commit() ;
    }

    public static Object_Currency getCurrencyObj_FromPreference(Context applicationContext){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        return new Object_Currency(sp.getString(KEY_CURRENCY_ID, "usd"),
                sp.getString(KEY_CURRENCY_NAME, "United States Dollar"),
                sp.getString(KEY_CURRENCY_SYMBOL, "$")) ;

    }



    /* *************************************** Portfolio ID *******************************/
    public static void setPortfolioId_InPreferences(Context applicationContext, int portfolioId){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putInt(MySharedPreferences.KEY_CURRENT_PORTFOLIO, portfolioId) ;
        editor.commit() ;
    }

    public static int getPortfolioId_FromPreference(Context applicationContext){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        return sp.getInt(KEY_CURRENT_PORTFOLIO, 1) ;

    }


    /* *************************************** Widget CoinPrice ********************************* */


    public static void setWidgetCoinPriceInfo_InPreferences(Context applicationContext, int widgetId, String widgetInfo){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putString(MySharedPreferences.PREFIX_KEY_WIDGETCOINPRICE + widgetId, widgetInfo) ;
        editor.commit() ;
    }

    public static String getWidgetCoinPriceInfo_FromPreference(Context applicationContext, int widgetId){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        return sp.getString(PREFIX_KEY_WIDGETCOINPRICE + widgetId, "") ;

    }


    public static void deleteWidgetCoinPriceInfo_FromPreferences(Context applicationContext, int widgetId){
        SharedPreferences sp = applicationContext.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sp.edit() ;
        editor.remove(MySharedPreferences.KEY_CURRENT_PORTFOLIO + widgetId) ;
        editor.commit() ;
    }







}
