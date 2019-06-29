package apps.yoo.com.blockholdings.ui.transaction;

import android.content.Context;
import android.provider.ContactsContract;

import com.google.common.collect.Table;

import androidx.lifecycle.ViewModel;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.DataRepository;
import apps.yoo.com.blockholdings.network.NetworkRepository;

public class ViewModel_ActivityTransaction extends ViewModel {
    Context context ;
    private static final String LOG_TAG = "ViewModel_ActivityTransaction --> " ;
    AppDatabase db;
    NetworkRepository networkRepository ;


    String coinGlobalPrice_Currency ;
    String coinHistoricalPrice_Currency ;
    Table<String, String, String> table_ExchangePairData ;


    public ViewModel_ActivityTransaction(Context context) {
        this.context = context ;
        this.networkRepository = NetworkRepository.getInstance(context) ;


    }





}
