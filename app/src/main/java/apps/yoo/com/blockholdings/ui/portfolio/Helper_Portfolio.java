package apps.yoo.com.blockholdings.ui.portfolio;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;

public class Helper_Portfolio {
    private static final String LOG_TAG = "Helper_Portfolio -->" ;
    public static final int TIMEFRAME_1DAY = 1 ;
    public static final int TIMEFRAME_1WEEK = 2 ;
    public static final int TIMEFRAME_1MONTH = 3 ;
    public static final int TIMEFRAME_1YEAR = 4 ;
    public static final int TIMEFRAME_MAX = 5 ;

    public static BigDecimal recomputePortfolioValue(AppDatabase db, int portfolioId){
        BigDecimal totalCost = new BigDecimal(0) ;
        List<Object_TransactionFullData> listOfTransactions = db.transactionDao().getListOfAllTransactionFD_OfPortfolio(portfolioId) ;
        Object_Portfolio portfolioObj = db.portfolioDao().getPortfolioById(portfolioId) ;
//        Log.e(LOG_TAG, listOfTransactions.toString()) ;
        for (Object_TransactionFullData obj : listOfTransactions){

            if(obj.getTransactionObject().getType() == Object_Transaction.TYPE_BUY){
                Log.v(LOG_TAG, "Total cost of portfolio is" + totalCost) ;
                Log.v(LOG_TAG, "Adding " +obj.getCoinObject().getName()  + " quantity " + obj.getTransactionObject().getNoOfCoins() + " cost " + obj.getTransactionObject().getTotalValue_Current()) ;
                totalCost = totalCost.add(new BigDecimal(obj.getTransactionObject().getTotalValue_Current())) ;
            } else {
                Log.v(LOG_TAG, "Total cost of portfolio is" + totalCost) ;
                Log.v(LOG_TAG, "Subtracting " +obj.getCoinObject().getName()  + " quantity " + obj.getTransactionObject().getNoOfCoins() + " cost " + obj.getTransactionObject().getTotalValue_Current()) ;
                totalCost = totalCost.subtract(new BigDecimal(obj.getTransactionObject().getTotalValue_Current())) ;
            }

        }
        Log.v(LOG_TAG, "Total cost of portfolio is" + totalCost) ;
        Log.v(LOG_TAG, "Adding portfolio free currency " + portfolioObj.getFreeCurrency()) ;
        totalCost = totalCost.add(new BigDecimal(portfolioObj.getFreeCurrency())) ;
        Log.v(LOG_TAG, "Total cost of portfolio is" + totalCost) ;


        return totalCost ;
    }


    public static void recomputeAllPortfolios(AppDatabase db){
        List<Object_Portfolio> listOfPortfolios = db.portfolioDao().getListOfAllPortfolios() ;
        for(Object_Portfolio portfolioObj : listOfPortfolios){
            BigDecimal newtotalValue = recomputePortfolioValue(db, portfolioObj.getPortfolioId()) ;
            Object_Portfolio.addInDb_portfolioUpdateLog(db, portfolioObj.getPortfolioId(), newtotalValue.toPlainString());
        }
    }



    public static List<Entry> getFormattedChartValues_from_UpdateLogOfPortfolio(String response){
        List<Entry> listOfItems = new ArrayList<>() ;
        try{
            JSONArray priceArray = new JSONArray(response) ;
            for(int i = 0 ; i< priceArray.length() ; i++){
                JSONArray insideArray = priceArray.getJSONArray(i) ;
                String price = insideArray.getString(0) ;
                String date = insideArray.getString(1) ;

                listOfItems.add(new Entry(Float.valueOf(date), Float.valueOf(price))) ;
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
        return listOfItems ;
    }



    public static List<Entry> getFormattedChartValues_from_UpdateLogOfPortfolio(String response, int timeFrame){
        List<Entry> listOfItems = new ArrayList<>() ;

        DateTime dateTime = new DateTime(Calendar.getInstance().getTimeInMillis()) ;
        long startingDateTime ;
        switch (timeFrame){
            case TIMEFRAME_1DAY :
                startingDateTime = dateTime.minusDays(1).getMillis() ;
                break;
            case TIMEFRAME_1WEEK :
                startingDateTime = dateTime.minusWeeks(1).getMillis() ;
                break;
            case TIMEFRAME_1MONTH :
                startingDateTime = dateTime.minusMonths(1).getMillis() ;
                break;
            case TIMEFRAME_1YEAR :
                startingDateTime = dateTime.minusYears(1).getMillis() ;
                break;
            case TIMEFRAME_MAX :
                startingDateTime = 0 ;
                break;
                default:startingDateTime = 0 ;
        }


        try{
            JSONArray priceArray = new JSONArray(response) ;
            for(int i = 0 ; i< priceArray.length() ; i++){
                JSONArray insideArray = priceArray.getJSONArray(i) ;
                String price = insideArray.getString(0) ;
                String date = insideArray.getString(1) ;
                if (Float.valueOf(date) > startingDateTime){
                    listOfItems.add(new Entry(Float.valueOf(date), Float.valueOf(price))) ;
                }
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
        return listOfItems ;
    }


    public static String getPriceTimeAgo_from_UpdateLogOfPortfolio(String response, int timeFrame){

        DateTime dateTime = new DateTime(Calendar.getInstance().getTimeInMillis()) ;
        long startingDateTime ;
        switch (timeFrame){
            case TIMEFRAME_1DAY :
                startingDateTime = dateTime.minusDays(1).getMillis() ;
                break;
            case TIMEFRAME_1WEEK :
                startingDateTime = dateTime.minusWeeks(1).getMillis() ;
                break;
            case TIMEFRAME_1MONTH :
                startingDateTime = dateTime.minusMonths(1).getMillis() ;
                break;
            case TIMEFRAME_1YEAR :
                startingDateTime = dateTime.minusYears(1).getMillis() ;
                break;
            case TIMEFRAME_MAX :
                return "0" ;
            default:
                return "0" ;

        }


        try{
            JSONArray priceArray = new JSONArray(response) ;
            for(int i = 0 ; i< priceArray.length() ; i++){
                JSONArray insideArray = priceArray.getJSONArray(i) ;
                String price = insideArray.getString(0) ;
                String date = insideArray.getString(1) ;
                if (Float.valueOf(date) > startingDateTime){
                    return price ;
                }
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
            return null ;
        }
        return null ;
    }



}
