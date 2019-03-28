package apps.yoo.com.blockholdings.util;

import android.content.Context;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;

public class AppSession {
    Context context ;
    private static final String LOG_TAg = "AppSession --> " ;
    AppDatabase db ;


    private Object_Currency currencyObj ;
    private Object_Portfolio portfolioObj;


    public AppSession(AppDatabase db, Context context){
        this.context = context ;
        this.db = db ;
        currencyObj = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        portfolioObj = db.portfolioDao().getPortfolioById(MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext())) ;
    }


    public Object_Currency getCurrencyObj(){
        return this.currencyObj ;
    }

    public Object_Portfolio getPortfolioOb(){
        return this.portfolioObj;
    }


    public void setCurrencyObj(Object_Currency newCurrencyObj){
        this.currencyObj = newCurrencyObj ;
    }

    public void setPortfolioObj(Object_Portfolio newPortfolioObj){
        this.portfolioObj = newPortfolioObj ;
    }










}
