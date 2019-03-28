package apps.yoo.com.blockholdings.ui.home.portfolio;

import android.util.Log;

import java.math.BigDecimal;
import java.util.List;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;

public class Helper_Portfolio {
    private static final String LOG_TAG = "Helper_Portfolio -->" ;

    public static BigDecimal recomputePortfolioValue(AppDatabase db, int portfolioId){
        BigDecimal totalCost = new BigDecimal(0) ;
        List<Object_TransactionFullData> listOfTransactions = db.transactionDao().getListOfAllTransaction_FullData(portfolioId) ;
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
}
