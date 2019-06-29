package apps.yoo.com.blockholdings.util;

import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;

public class MyListener {

    public interface RVAdapterExchange_to_DialogFragmentExchange{
        public void onSelectingExchange(Object_Exchange exchangeObj) ;
    }

    public interface RVAdapterTradingPair_to_DialogFragmentTradingPair{
        public void onSelectingTradingPair(String tradingPairSymbol) ;
    }

    public interface DialogFragments_to_ActivityTransaction{
        public void onSelectingExchange(Object_Exchange exchangeObj) ;
        public void onSelectingExchange_GlobalAverage(String price) ;
        public void onSelectingTradingPair(String tradingPairSymbol) ;
        public void onSelectingDateTime(long timeInLong);

    }

    public interface RVAdapterCurrency_to_DialogFragmentCurrency{
        public void onCurrencySelected(Object_Currency currencyObj) ;
    }

    public interface DialogFragmentCurrency_to_ActivitySettings{
        public void onCurrencySelected(Object_Currency currencyObj) ;
    }


    public interface DialogFragmentPortfolios_to_ActivityPortfolio{
        public void onChangingPortfolio() ;
//        public void onAddingNewPortfolio() ;
//        public void onEditingPortfolioName() ;
    }

    public interface RVAdapter_PortfolioNames_to_DialogFragmentPortfolio{
        public void onSelectingPortfolio(int newPortfolioId) ;
        public void onDeletingPortfolio(int portfolioId) ;

    }

    public interface dlgFrg_AddNewPortfolio_to_DlgFrg_Portfolio{
        public void onAddingNewPortfolio() ;
    }

    public interface dlgFrg_EditPortfolioName_to_DlgFrg_Portfolio{
        public void onEditingPortfolioName() ;
    }






}
