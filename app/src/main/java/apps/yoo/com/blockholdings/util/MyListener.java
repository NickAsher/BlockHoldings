package apps.yoo.com.blockholdings.util;

import java.math.BigDecimal;

public class MyListener {

    public interface RVAdapterExchange_to_DialogFragmentExchange{
        public void closeDialog() ;
    }

    public interface RVAdapterTradingPair_to_DialogFragmentTradingPair{
        public void closeDialog() ;
    }

    public interface DialogFragments_to_ActivityTransaction{
        public void onSelectingExchange() ;
        public void onSelectingTradingPair() ;
    }
}
