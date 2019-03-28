package apps.yoo.com.blockholdings.network;

import apps.yoo.com.blockholdings.ui.watchlist.all.DataSource_AllCoins;



public class ApiManager {

    public static String getUrl_FullListOfCoins_withMarketNSparklineData(int paginationNo, String currencyId){
        int noOfItems = DataSource_AllCoins.NO_OF_COINS_PER_PAGE ;
        //      https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d
        return "https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + currencyId + "&order=market_cap_desc&per_page=" + noOfItems + "&page=" + paginationNo + "&sparkline=true&price_change_percentage=1h%2C24h%2C7d" ;

    }
}
