package apps.yoo.com.blockholdings.util;

import java.math.BigDecimal;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.ui.home.portfolio.Helper_Portfolio;

public class MyGlobals {
    private static Object_Portfolio currentPortfolioObj ;

    public static void setupCurrentPortfolioObj(Object_Portfolio portfolioObj){
        currentPortfolioObj = portfolioObj;
    }

    public static Object_Portfolio getCurrentPortfolioObj(){
        return currentPortfolioObj ;
    }


    public static void refreshPortfolioValue(AppDatabase db){
        // Refreshes the value of portfolio total cost in the
        // 1. DB
        // 2. Global Instance

        BigDecimal newPortfolioCost = Helper_Portfolio.recomputePortfolioValue(db, currentPortfolioObj.getPortfolioId()) ;
        Object_Portfolio.addInDb_portfolioUpdateLog(db, currentPortfolioObj.getPortfolioId(), newPortfolioCost.toPlainString());
//        db.portfolioDao().updatePortfolio_TotalValue(currentPortfolioObj.getPortfolioId(), newPortfolioCost.toPlainString());
        currentPortfolioObj.setPortfolioValue(newPortfolioCost.toPlainString());

    }
}
