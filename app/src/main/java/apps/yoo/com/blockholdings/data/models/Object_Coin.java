package apps.yoo.com.blockholdings.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.google.common.base.Predicate;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.AppExecutors;
import apps.yoo.com.blockholdings.ui.detail.Object_ProjectLink;
import apps.yoo.com.blockholdings.util.Message;

@Entity(tableName = "table_coin")
public class Object_Coin {

    @PrimaryKey
    @ColumnInfo(name = "coin_Id")
    @NonNull String id ;

    @ColumnInfo(name = "coinSrNo")
    int coinSrNo ;

    @ColumnInfo(name = "coinSymbol")
    String symbol ;

    @ColumnInfo(name = "coinName")
    String name;

    @ColumnInfo(name = "coinImageLogoLink")
    String imageLogoLink ;

    @ColumnInfo(name = "coinDescription")
    String coinDescription ;

    @ColumnInfo(name = "link_Website")
    String link_Website ;

    @ColumnInfo(name = "link_BlockExplorer")
    String link_BlockExplorer ;

    @ColumnInfo(name = "link_Blog")
    String link_Blog ;

    @ColumnInfo(name = "link_Reddit")
    String link_Reddit ;

    @ColumnInfo(name = "link_Twitter")
    String link_Twitter ;

    @ColumnInfo(name = "link_Facebook")
    String link_Facebook ;

    @ColumnInfo(name = "link_Telegram")
    String link_Telegram ;

    @ColumnInfo(name = "link_Github")
    String link_Github ;

    @ColumnInfo(name = "link_Slack")
    String link_Slack ;

    @ColumnInfo(name = "link_WhitePaper")
    String link_WhitePaper ;

    @ColumnInfo(name = "link_LinkedIn")
    String link_LinkedIn ;



    @ColumnInfo(name = "market_Rank")
    int rank ;

    @ColumnInfo(name = "market_MarketCap")
    String marketCap ;

    @ColumnInfo(name = "market_TotalVolume")
    String totalVolume ;

    @ColumnInfo(name = "market_PercentageChange_24H")
    String percentageChange_24H ;

    @ColumnInfo(name = "market_PercentageChange_1W")
    String percentageChange_1W ;

    @ColumnInfo(name = "market_High24H")
    String high24H ;

    @ColumnInfo(name = "market_Llow24H")
    String low24H ;

    @ColumnInfo(name = "market_Total_supply")
    String totalSupply ;

    @ColumnInfo(name = "market_Circulating_supply")
    String circulatingSupply ;

    @ColumnInfo(name = "market_Sparkline")
    String sparklineData ;

    @ColumnInfo(name = "price_data")
    String priceData ;

    public static final String LOG_TAG = "Object_Coin --> " ;

    public static final int PRICE_1D = 1 ;
    public static final int PRICE_1W = 2 ;
    public static final int PRICE_1M = 3 ;
    public static final int PRICE_1Y = 4 ;
    public static final int PRICE_MAX = 5 ;

    public static final int TIMEFRAME_1DAY = 1 ;
    public static final int TIMEFRAME_1WEEK = 2 ;
    public static final int TIMEFRAME_1MONTH = 3 ;
    public static final int TIMEFRAME_1YEAR = 4 ;
    public static final int TIMEFRAME_MAX = 5 ;

    public Object_Coin(String id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.priceData = "[]" ;
    }





    @Ignore
    public Object_Coin(JSONObject jsonObject){
        try {
            this.id = jsonObject.getString("id");
            this.symbol = jsonObject.getString("symbol");
            this.name = jsonObject.getString("name");
            this.imageLogoLink = jsonObject.getString("imagelogo");
            this.priceData = "[]" ;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }


    @Ignore
    public Object_Coin(String id, String symbol, String name, int coinSrNo, String imageLogoLink) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.coinSrNo = coinSrNo ;
        this.imageLogoLink = imageLogoLink ;
        this.priceData = "[]" ;
    }



    @Ignore
    public Object_Coin() {
        this.priceData = "[]" ;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getImageLogoLink() {
        return imageLogoLink;
    }

    public int getCoinSrNo() {
        return coinSrNo;
    }

    public void setCoinSrNo(int coinSrNo) {
        this.coinSrNo = coinSrNo;
    }

    public void setImageLogoLink(String imageLogoLink) {
        this.imageLogoLink = imageLogoLink;
    }

    public String getCoinDescription() {
        return coinDescription;
    }

    public void setCoinDescription(String coinDescription) {
        this.coinDescription = coinDescription;
    }

    public String getLink_Website() {
        return link_Website;
    }

    public void setLink_Website(String link_Website) {
        this.link_Website = link_Website;
    }

    public String getLink_BlockExplorer() {
        return link_BlockExplorer;
    }

    public void setLink_BlockExplorer(String link_BlockExplorer) {
        this.link_BlockExplorer = link_BlockExplorer;
    }

    public String getLink_Blog() {
        return link_Blog;
    }

    public void setLink_Blog(String link_Blog) {
        this.link_Blog = link_Blog;
    }

    public String getLink_Reddit() {
        return link_Reddit;
    }

    public void setLink_Reddit(String link_Reddit) {
        this.link_Reddit = link_Reddit;
    }

    public String getLink_Twitter() {
        return link_Twitter;
    }

    public void setLink_Twitter(String link_Twitter) {
        this.link_Twitter = link_Twitter;
    }

    public String getLink_Facebook() {
        return link_Facebook;
    }

    public void setLink_Facebook(String link_Facebook) {
        this.link_Facebook = link_Facebook;
    }

    public String getLink_Telegram() {
        return link_Telegram;
    }

    public void setLink_Telegram(String link_Telegram) {
        this.link_Telegram = link_Telegram;
    }

    public String getLink_Github() {
        return link_Github;
    }

    public void setLink_Github(String link_Github) {
        this.link_Github = link_Github;
    }


    public String getLink_Slack() {
        return link_Slack;
    }

    public void setLink_Slack(String link_Slack) {
        this.link_Slack = link_Slack;
    }

    public String getLink_WhitePaper() {
        return link_WhitePaper;
    }

    public void setLink_WhitePaper(String link_WhitePaper) {
        this.link_WhitePaper = link_WhitePaper;
    }

    public String getLink_LinkedIn() {
        return link_LinkedIn;
    }

    public void setLink_LinkedIn(String link_LinkedIn) {
        this.link_LinkedIn = link_LinkedIn;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getPercentageChange_24H() {
        return percentageChange_24H;
    }

    public void setPercentageChange_24H(String percentageChange_24H) {
        this.percentageChange_24H = percentageChange_24H;
    }

    public String getPercentageChange_1W() {
        return percentageChange_1W;
    }

    public void setPercentageChange_1W(String percentageChange_1W) {
        this.percentageChange_1W = percentageChange_1W;
    }

    public String getHigh24H() {
        return high24H;
    }

    public void setHigh24H(String high24H) {
        this.high24H = high24H;
    }

    public String getLow24H() {
        return low24H;
    }

    public void setLow24H(String low24H) {
        this.low24H = low24H;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(String circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public String getSparklineData() {
        return sparklineData;
    }

    public void setSparklineData(String sparklineData) {
        this.sparklineData = sparklineData;
    }

    public String getPriceData() {
        return priceData;
    }

    public JSONArray getPriceData_LastItem() throws JSONException{
        JSONArray jsonArray = new JSONArray(priceData) ;
        int size = jsonArray.length() ;
        if(size > 0){
            return jsonArray.getJSONArray(size-1) ;
        }else {
            throw new JSONException("Dude the array is empty") ;
        }
    }

    public void setPriceData(String priceData) {
        this.priceData = priceData;
    }

    public static void addToPriceData(AppDatabase db, String coinId, String price, long timeInLong, String updater){
        try{
            Object_Coin coinObj = db.coinDao().getCoinById(coinId) ;
            JSONArray priceDataArray = new JSONArray(coinObj.getPriceData()) ;
            JSONArray jsonArray = new JSONArray() ;
            jsonArray.put(price).put(timeInLong).put(updater) ;
             priceDataArray.put(jsonArray) ;
            db.coinDao().updateCoin_PriceData(coinId, priceDataArray.toString());
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }

    @Override
    public String toString() {
        return id + " - " +  symbol + " - " + name + " - " + imageLogoLink  + " \n" ;
    }


    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject() ;
        try{
            jsonObject.put("id", id) ;
            jsonObject.put("symbol", symbol) ;
            jsonObject.put("name", name) ;
            jsonObject.put("imagelogo", imageLogoLink) ;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
        return jsonObject ;
    }


    public static void insertIntoDB_FullCoinData(String coinDetails, Context context, final AppDatabase db, String currencyId, final Object_Coin coinObj){
        String link_Website, link_blockExporer, link_blog, link_reddit, link_twitter, link_telegram, link_facebook, link_github, link_linkedIn, link_Slack, link_WhitePaper  ;

        try{
            JSONObject responseObject = new JSONObject(coinDetails) ;
            if(!responseObject.has("name")){
                throw new JSONException("Response is bekaar ") ;
            }

            String coinImage = responseObject.getJSONObject("image").getString("small") ;
            String coinDescription = responseObject.getJSONObject("description").getString("en");



            // dont need to check website and blockexplorer json array as their array is always a minimum of three empty strings in json array
            link_Website = responseObject.getJSONObject("links").getJSONArray("homepage").getString(0);
            link_blockExporer = responseObject.getJSONObject("links").getJSONArray("blockchain_site").getString(0);
            link_reddit = responseObject.getJSONObject("links").getString("subreddit_url");
            link_blog = responseObject.getJSONObject("links").getJSONArray("announcement_url").getString(0) ;


            if(responseObject.has("ico_data")){
                JSONObject linksObject = responseObject.getJSONObject("ico_data").getJSONObject("links") ;
                if(responseObject.getJSONObject("links").getJSONArray("homepage").getString(0).isEmpty()){
                    if(linksObject.has("web")){
                        link_Website = linksObject.getString("web");
                    }
                }

                if(responseObject.getJSONObject("links").getJSONArray("announcement_url").getString(0).isEmpty()){
                    if(linksObject.has("blog")){
                        link_blog = linksObject.getString("blog");
                    }
                }
                if(linksObject.has("slack")){
                    link_Slack = linksObject.getString("slack");
                }else{
                    link_Slack = "" ;

                }
                if(linksObject.has("github")){
                    link_github = linksObject.getString("github");
                }else{
                    link_github = "" ;

                }

                if(linksObject.has("twitter")){
                    link_twitter = linksObject.getString("twitter");
                }else{
                    link_twitter = "" ;
                }

                if(linksObject.has("facebook")){
                    link_facebook = linksObject.getString("facebook");
                }else{
                    link_facebook = "" ;
                }

                if(linksObject.has("linkedin")){
                    link_linkedIn = linksObject.getString("linkedin");
                }else{
                    link_linkedIn = "" ;

                }

                if(linksObject.has("telegram")){
                    link_telegram = linksObject.getString("telegram");
                }else{
                    link_telegram = "" ;

                }

                if(linksObject.has("whitepaper")){
                    link_WhitePaper = linksObject.getString("whitepaper");
                }else{
                    link_WhitePaper = "" ;
                }


            } else {



                if(responseObject.getJSONObject("links").getJSONObject("repos_url").getJSONArray("github").length()>0){
                    link_github = responseObject.getJSONObject("links").getJSONObject("repos_url").getJSONArray("github").getString(0) ;
                } else {
                    link_github = "" ;
                }

                link_twitter = responseObject.getJSONObject("links").getString("twitter_screen_name");
                if(!link_twitter.isEmpty()){
                    link_twitter = "https://twitter.com/" + link_twitter ;
                }


                link_telegram = responseObject.getJSONObject("links").getString("telegram_channel_identifier");
                if(!link_telegram.isEmpty()){
                    link_telegram = "https://www.facebook.com/" + link_telegram ;
                }
                link_facebook = responseObject.getJSONObject("links").getString("facebook_username");
                if(!link_facebook.isEmpty()){
                    link_facebook = "https://www.facebook.com/" + link_facebook ;
                }

                link_Slack = "" ;
                link_linkedIn = "" ;
                link_WhitePaper = "" ;

            }



            JSONObject marketObj = responseObject.getJSONObject("market_data") ;


            int marketRank = marketObj.getInt("market_cap_rank") ;
            String marketCap = String.valueOf(marketObj.getJSONObject("market_cap").getLong(currencyId)) ;
            String totalVolume = marketObj.getJSONObject("total_volume").getString(currencyId) ;
            String percentChange24H = marketObj.getString("price_change_percentage_24h") ;
            String percentChange1W = marketObj.getString("price_change_percentage_7d") ;
            String high24H = marketObj.getJSONObject("high_24h").getString(currencyId) ;
            String low24H = marketObj.getJSONObject("low_24h").getString(currencyId) ;

            String totalSupply = marketObj.get("total_supply").toString() ;
            String circulatingSupply = marketObj.getString("circulating_supply") ;
            String sparklineArray = marketObj.getJSONObject("sparkline_7d").getJSONArray("price").toString() ;

            String currentPrice = marketObj.getJSONObject("current_price").get("usd").toString() ;
            String timeInLong = Calendar.getInstance().getTime() + "" ;


            coinObj.setCoinDescription(coinDescription);
            coinObj.setImageLogoLink(coinImage);

            coinObj.setLink_Website(link_Website);
            coinObj.setLink_BlockExplorer(link_blockExporer);
            coinObj.setLink_Blog(link_blog);
            coinObj.setLink_Facebook(link_facebook);
            coinObj.setLink_Github(link_github);
            coinObj.setLink_Reddit(link_reddit);
            coinObj.setLink_Telegram(link_telegram);
            coinObj.setLink_Twitter(link_twitter);
            coinObj.setLink_Slack(link_Slack);
            coinObj.setLink_LinkedIn(link_linkedIn);
            coinObj.setLink_WhitePaper(link_WhitePaper);


            coinObj.setMarketCap(marketCap);
            coinObj.setRank(marketRank);
            coinObj.setTotalVolume(totalVolume);
            coinObj.setPercentageChange_24H(percentChange24H);
            coinObj.setPercentageChange_1W(percentChange1W);
            coinObj.setHigh24H(high24H);
            coinObj.setLow24H(low24H);
            coinObj.setTotalSupply(totalSupply);
            coinObj.setCirculatingSupply(circulatingSupply);
            coinObj.setSparklineData(sparklineArray);



            JSONArray priceDataArray = new JSONArray(coinObj.getPriceData()) ;
            JSONArray jsonArray = new JSONArray() ;
            jsonArray.put(currentPrice).put(timeInLong).put("") ;
            priceDataArray.put(jsonArray) ;


            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    db.coinDao().updateCoin(coinObj);
                }
            });



        } catch (Exception e){
            Log.e(LOG_TAG, "Unable to insert coin data into db : " +e.toString()) ;
            Message.display(context, "Unable to insert coin data into db" +e.toString());
        }
    }


    public List<Object_ProjectLink> getlistOfLinks(){
        List<Object_ProjectLink> listOfLinks = new ArrayList<>() ;

        if(link_Website != null && !link_Website.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_WEBSITE, link_Website)) ;
        }

        if(link_BlockExplorer != null && !link_BlockExplorer.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_BLOCK_EXPLORER, link_BlockExplorer)) ;
        }

        if(link_WhitePaper != null && !link_WhitePaper.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_WHITEPAPER, link_WhitePaper)) ;
        }

        if(link_Blog != null && !link_Blog.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_BLOG, link_Blog)) ;
        }

        if(link_Reddit != null && !link_Reddit.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_REDDIT, link_Reddit)) ;
        }

        if(link_Github != null && !link_Github.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_GITHUB, link_Github)) ;
        }


        if(link_Facebook != null && !link_Facebook.isEmpty()){
            String link = "" ;
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_FACEBOOK, link_Facebook)) ;
        }

        if(link_Twitter != null && !link_Twitter.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_TWITTER, link_Twitter)) ;
        }


        if(link_Telegram != null && !link_Telegram.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_TELEGRAM, link_Telegram)) ;
        }


        if(link_Slack != null && !link_Slack.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_SLACK, link_Slack)) ;
        }


        if(link_LinkedIn != null && !link_LinkedIn.isEmpty()){
            listOfLinks.add(new Object_ProjectLink(Object_ProjectLink.TYPE_LINKEDIN, link_LinkedIn)) ;
        }






        return listOfLinks ;
    }




    public static List<Object_Coin> getListOfCoinsFromJsonArray(String jsonArrayString){
        List<Object_Coin> listOfCoins = new ArrayList<>() ;
        try{
            JSONArray jsonArray = new JSONArray(jsonArrayString) ;

            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i) ;
//                Log.e(LOG_TAG, jsonObject.toString()) ;

                if(jsonObject.get("market_cap_rank").toString().equalsIgnoreCase("null")){
                    Object_Coin coin = new Object_Coin(
                            jsonObject.getString("id"),
                            jsonObject.getString("symbol"),
                            jsonObject.getString("name"),
                            Integer.MAX_VALUE,
                            jsonObject.getString("image")
                    ) ;
                    Log.e(LOG_TAG, "Null Coin inserted with id : " + jsonObject.getString("id") );
                    listOfCoins.add(coin);
                } else {
                        Object_Coin coin = new Object_Coin(
                                jsonObject.getString("id"),
                                jsonObject.getString("symbol"),
                                jsonObject.getString("name"),
                                jsonObject.getInt("market_cap_rank"),
                                jsonObject.getString("image")
                        );
                        listOfCoins.add(coin);

                }




            }
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }

        return listOfCoins ;
    }


    public static int getTOTAL_NO_OF_COINS(String responseJsonArray){
        int no ;

        try{
            JSONArray jsonArray = new JSONArray(responseJsonArray) ;
            Log.e(LOG_TAG, jsonArray.toString()) ;

            no = jsonArray.length() ;
            Log.e(LOG_TAG, "" + jsonArray.length()) ;

            return no ;

        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
            return -1;
        }
    }





    public static String getPriceOfCoin_FromPriceLog_1DAgo(Object_Transaction transactionObj, Object_Coin coinObj, Long currentTimeInLong){
        Log.e(LOG_TAG, "difference in time is " + TimeUnit.MILLISECONDS.toHours(currentTimeInLong-transactionObj.getTransactionDateTime().getTime() )) ;

        if(currentTimeInLong-transactionObj.getTransactionDateTime().getTime() < TimeUnit.HOURS.toMillis(22)){
            Log.e(LOG_TAG, "Time difference is less and this case is called") ;
            return transactionObj.getSingleCoinPrice_CurrencyOriginal() ;
        }
        try{
            JSONArray jsonArray = new JSONArray(coinObj.getPriceData()) ;
            for(int i = jsonArray.length()-1; i >= 0; i--){
                // traversing the list in reverse order
                JSONArray currentObject = jsonArray.getJSONArray(i) ;
                String price = currentObject.getString(0) ;
                Long time = Long.valueOf(currentObject.getString(1)) ;




                // if time difference is between 22hrs  and  26hrs
                long timeDifference = currentTimeInLong-time ;
                if (timeDifference >= TimeUnit.HOURS.toMillis(22) && timeDifference <=  TimeUnit.HOURS.toMillis(26)){
                    return price ;
                }

            }
            return "0" ;
        }catch (JSONException e){
            Log.e(LOG_TAG, "not a json array yet brother " + e.toString()) ;
        }
        return null ;
    }


    public static String  getPriceOfCoin_FromPriceLog_1WAgo(Object_Transaction transactionObj, Object_Coin coinObj, Long currentTimeInLong){
        if(currentTimeInLong-transactionObj.getTransactionDateTime().getTime() < TimeUnit.DAYS.toMillis(6)){
            return transactionObj.getSingleCoinPrice_CurrencyOriginal() ;
        }
        try{
            JSONArray jsonArray = new JSONArray(coinObj.getPriceData()) ;
            for(int i = jsonArray.length()-1; i >= 0; i--){
                // traversing the list in reverse order
                JSONArray currentObject = jsonArray.getJSONArray(i) ;
                String price = currentObject.getString(0) ;
                Long time = Long.valueOf(currentObject.getString(1)) ;


                // if time difference is between 6days and 8days
                long timeDifference = currentTimeInLong-time ;
                if (timeDifference >= TimeUnit.DAYS.toMillis(6) && timeDifference <=  TimeUnit.DAYS.toMillis(8)){
                    return price ;
                }

            }
            return "" ;
        }catch (JSONException e){
            Log.e(LOG_TAG, "not a json array yet brother " + e.toString()) ;
        }
        return null ;
    }

    public static String getPriceOfCoin_FromPriceLog_TimeAgo(Object_Transaction transactionObj, Object_Coin coinObj, int timeFrame){
        Long transactionBuyTime = transactionObj.getTransactionDateTime().getTime() ;
        DateTime currentDateTime = new DateTime(Calendar.getInstance().getTimeInMillis()) ;
        long startingDateTime ;
        switch (timeFrame){
            case TIMEFRAME_1DAY :
                startingDateTime = currentDateTime.minusDays(1).getMillis() ;
                break;
            case TIMEFRAME_1WEEK :
                startingDateTime = currentDateTime.minusWeeks(1).getMillis() ;
                break;
            case TIMEFRAME_1MONTH :
                startingDateTime = currentDateTime.minusMonths(1).getMillis() ;
                break;
            case TIMEFRAME_1YEAR :
                startingDateTime = currentDateTime.minusYears(1).getMillis() ;
                break;
            case TIMEFRAME_MAX :
                startingDateTime = 0 ;
                break;
            default:
                startingDateTime = 0 ;
                break;
        }

        // The following code is for the case when the date of our transaction is less than our startingDateTime
        // Ex:  Let's say that we bought coins of September 5 . So our transactionBuyTime = September_5
        // Let's say we want price of coin from date January 1 :  So our startingDateTime = January_1
        // Now In this case we want the price only from September 5. So in Milliseconds September_5 is greater than January_1
        // In that case we simply return the price from transaction entry, we don't have to look up the log table
        if(transactionBuyTime > startingDateTime){
            return transactionObj.getSingleCoinPrice_CurrencyOriginal() ;
        }




        // Now that we have the startingDateTime : i.e. the date value for which we want the price
        // what we do is loop through the update log of the coin price data from the beginning
        // And the first date value that is larger than our startingDateTime
        // we return the price for that value
        // Example : Let's say we are looking for price of January_7
        // In our update log we have the data starting from January 1
        // SO we start the data searching and looping from January 1
        // And search  January 2, January 3, January 4, January 5, January 6, January 7, January 8
        // Here see that January 8 is greater than January 7
        // so we will return the price of January 8

        // NOTE : we wanted data of January 7 but we got data of January 8
        // It depends if the millisecond long value of the date
        // we might be able to get the january 7 also , if the millisecond value of it in the update log is > than startingDateTime
        try{
            JSONArray priceArray = new JSONArray(coinObj.getPriceData()) ;
            for(int i = 0 ; i< priceArray.length() ; i++){
                JSONArray insideArray = priceArray.getJSONArray(i) ;
                String price = insideArray.getString(0) ;
                String date = insideArray.getString(1) ;
                if (Float.valueOf(date) > startingDateTime){
                    return price ;
                }
            }
            Log.e(LOG_TAG, "No price value found for the coin" + coinObj.getName() + " for the required date") ;
            return "0" ;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
            return "0" ;
        }



    }

    public static String  getPriceOfCoin_FromPriceLog_MaxAgo(Object_Transaction transactionObj){

            return transactionObj.getSingleCoinPrice_CurrencyOriginal() ;

    }


    public static Predicate<Object_Coin> getPredicateFilter_NameSymbol_startsWithAlphabet(final String text){
        Predicate predicate = new Predicate<Object_Coin>(){
            @Override
            public boolean apply(Object_Coin input) {

                if(input.getSymbol().toLowerCase().contains(text.toLowerCase())
                        || input.getName().toLowerCase().contains(text.toLowerCase())){
                    return true ;
                }
                return false;
            }
        } ;
        return predicate ;
    }



    public static Comparator<JSONArray> updateLogDateComparator = new Comparator<JSONArray>() {
        @Override
        public int compare(JSONArray o1, JSONArray o2)            {

            try {
                Long dateTime1 = new Long(o1.getLong(1));
                Long dateTime2 = new Long(o2.getLong(1));
                return dateTime1.compareTo(dateTime2);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.toString()) ;
            }
            return 0 ;
        }
    } ;
}
