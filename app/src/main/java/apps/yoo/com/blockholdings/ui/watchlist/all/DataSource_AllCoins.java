package apps.yoo.com.blockholdings.ui.watchlist.all;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.network.NetworkRepository;
import apps.yoo.com.blockholdings.network.NetworkDataParser;
import apps.yoo.com.blockholdings.network.MyNetworkResponse;
import apps.yoo.com.blockholdings.util.Message;

public class DataSource_AllCoins extends PageKeyedDataSource<Integer, Object_Coin> {
    private static final String LOG_TAG = "DataSource_AllCoins --> " ;
    Context context ;
    public static final int NO_OF_COINS_PER_PAGE = 20 ;
    final Integer FIRST_PAGE = 1 ;
    NetworkRepository networkManager ;


    void init(Context context){
        this.context = context ;
        networkManager = NetworkRepository.getInstance(context) ;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Object_Coin> callback) {
        networkManager.getFullListOfCoins_withMarketSparklineData(FIRST_PAGE, "usd", new MyNetworkResponse() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, "Response from server is " + response) ;
                List<Object_Coin> returnList = NetworkDataParser.getFullListOfCoins_withMarketSparklineData(response) ;
                if(returnList != null){
                    callback.onResult(returnList, null, FIRST_PAGE+1);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in Connecting to server");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Object_Coin> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Object_Coin> callback) {

        networkManager.getFullListOfCoins_withMarketSparklineData(params.key, "usd", new MyNetworkResponse() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, "Response from server is " + response) ;
                List<Object_Coin> returnList = NetworkDataParser.getFullListOfCoins_withMarketSparklineData(response) ;
                if(returnList != null){
                    callback.onResult(returnList, params.key+1);
                } else {
                    Log.e(LOG_TAG, "cant fetch more items, check if reached the list end") ;
                    callback.onResult(returnList, null);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in Connecting to server");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        });

    }




















    public static class Factory extends DataSource.Factory{
        Context context ;
        MutableLiveData<PageKeyedDataSource<Integer, Object_Coin>> liveData_DataSource = new MutableLiveData<>() ;

        void setupContext(Context context){
            this.context = context ;
        }


        @NonNull
        @Override
        public DataSource create() {
            DataSource_AllCoins dataSource_allCoinsList = new DataSource_AllCoins() ;
            dataSource_allCoinsList.init(context);
            liveData_DataSource.postValue(dataSource_allCoinsList);
            return dataSource_allCoinsList ;
        }


        public MutableLiveData<PageKeyedDataSource<Integer, Object_Coin>> getLiveData_DataSource() {
            return liveData_DataSource;
        }
    }
}
