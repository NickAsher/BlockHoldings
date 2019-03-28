package apps.yoo.com.blockholdings.ui.watchlist.all;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Coin;

public class ViewModel_FragmentAllCoins extends ViewModel{
    private static final String LOG_TAG = "ViewModel_FragmentAllCoins --> " ;
    private Context context ;
    private AppDatabase db ;

    LiveData<PagedList<Object_Coin>> liveData_PagedList ;
    LiveData<PageKeyedDataSource<Integer, Object_Coin>> liveData_DataSource ;
    List<Object_Coin> listOfSearchSuggestedCoins ;


    public ViewModel_FragmentAllCoins(Context context){
        this.context = context ;
        db = AppDatabase.getInstance(context) ;

        DataSource_AllCoins.Factory dataSourceFactory_allCoinsList = new DataSource_AllCoins.Factory() ;
        dataSourceFactory_allCoinsList.setupContext(context);
        liveData_DataSource = dataSourceFactory_allCoinsList.getLiveData_DataSource() ;

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(DataSource_AllCoins.NO_OF_COINS_PER_PAGE)
                .build() ;

        liveData_PagedList = (new LivePagedListBuilder(dataSourceFactory_allCoinsList, config)).build() ;
        listOfSearchSuggestedCoins = db.coinDao().getListOfAllCoins() ;

    }

    public LiveData<PagedList<Object_Coin>> getLiveData_PagedList() {
        return liveData_PagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, Object_Coin>> getLiveData_DataSource() {
        return liveData_DataSource;
    }

    public List<Object_Coin> getListOfSearchSuggestedCoins() {
        return listOfSearchSuggestedCoins;
    }













    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        private Context context ;

        public Factory(Context context){
            this.context = context ;
        }


        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ViewModel_FragmentAllCoins(context) ;
        }

    }
}
