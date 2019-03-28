package apps.yoo.com.blockholdings.util;

public class Constants {




    public static final String URL_IMAGELINK_COINMARKETCAPID = "https://s2.coinmarketcap.com/static/img/coins/128x128/72.png" ;

    public static final String URL_APICALL_ALLCOINS = "https://api.coingecko.com/api/v3/coins/list" ;
    public static final String URL_PREFIX_IMAGELOGO = "https://coinlayer.com/images/assets/coinlayer_icons/" ;
    public static final String SUFFIX_IMAGELOGO = ".png" ;



    public static final String UTL_APICALL_TRANSACTIONDATA_PREFIX = "https://api.coingecko.com/api/v3/coins/" ;
    public static final String UTL_APICALL_TRANSACTIONDATA_SUFFIX = "?localization=false&tickers=true&market_data=true&community_data=false&developer_data=false&sparkline=true" ;


    public static final String URL_APICALL_EXCHANGES  =   "https://api.coingecko.com/api/v3/exchanges" ;


    public static final String URL_APICALL_SIMPLEPRICES = "https://api.coingecko.com/api/v3/simple/price?ids=" ;

    public static final String RSS_LINK_COINTELEGRAPH = "https://cointelegraph.com/rss" ;
    public static final String RSS_LINK_COINTDESK = "http://feeds.feedburner.com/CoinDesk" ;
    public static final String RSS_LINK_BITCOINdCOM = "https://news.bitcoin.com/feed/" ;

    public static final String URL_APICALL_VS_SIMPLE_CURRENCIES = "https://api.coingecko.com/api/v3/simple/supported_vs_currencies" ;

    public static String getURL_APICALL_SIMPLEPRICES(String coinIdsString, String currencyString){
        //   https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd
        return "https://api.coingecko.com/api/v3/simple/price?ids=" + coinIdsString + "&vs_currencies=" + currencyString ;
    }

    public static String getURL_APICALL_ALLCOINDATA(int paginationPageIndex){
        // https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=16&sparkline=false
        return "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=" + paginationPageIndex + "&sparkline=false" ;
    }


    public static String getURL_APICALL_COIN_DESCRIPTIONDATA(String coinId){
        return "https://api.coingecko.com/api/v3/coins/" + coinId + "?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=true" ;
    }

    public static String getURL_APICALL_HISTORICAL_PRICE(String coinId, String dateString){
        // https://api.coingecko.com/api/v3/coins/bitcoin/history?date=27-01-2019&localization=false
        return "https://api.coingecko.com/api/v3/coins/" + coinId + "/history?date=" + dateString + "&localization=false" ;
    }



    public static String getURL_APICALL_HISTORICAL_DATACHART(String coinId, String noOfDays, String currencyId){
        //   https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=1
        return "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=" + currencyId + "&days=" + noOfDays ;
    }


    public static String getURL_APICALL_WATCHLIST_SELECTED_COIN_DATA(String coinIdString, String currencyId){
        // https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=bitcoin%2Cripple&order=market_cap_desc&per_page=250&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d

        return "https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + currencyId + "&ids=" + coinIdString + "&order=market_cap_desc&per_page=250&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d" ;
    }









}
