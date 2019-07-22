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
import apps.yoo.com.blockholdings.util.Constants;
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

    @ColumnInfo(name = "price_data")
    String priceData ;

    @ColumnInfo(name = "cached_data")
    String cachedData ;


    String sparklineData ;


    public static final String LOG_TAG = "Object_Coin --> " ;


    public Object_Coin(String id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.priceData = "[]" ;
        this.cachedData = "{}" ;
    }





    @Ignore
    public Object_Coin(JSONObject jsonObject){
        try {
            this.id = jsonObject.getString("id");
            this.symbol = jsonObject.getString("symbol");
            this.name = jsonObject.getString("name");
            this.imageLogoLink = jsonObject.getString("imagelogo");
            this.priceData = "[]" ;
            this.cachedData = "{}" ;

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
        this.cachedData = "{}" ;

    }



    @Ignore
    public Object_Coin() {
        this.priceData = "[]" ;
        this.cachedData = "{}" ;

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

    public void setImageLogoLink(String imageLogoLink) {
        this.imageLogoLink = imageLogoLink;
    }

    public int getCoinSrNo() {
        return coinSrNo;
    }

    public void setCoinSrNo(int coinSrNo) {
        this.coinSrNo = coinSrNo;
    }

    public String getPriceData() {
        return priceData;
    }

    public void setPriceData(String priceData) {
        this.priceData = priceData;
    }



    public String getCachedData() {
        return cachedData;
    }

    public void setCachedData(String cachedData) {
        this.cachedData = cachedData;
    }


    public String getSparklineData() {
        return sparklineData;
    }

    public void setSparklineData(String sparklineData) {
        this.sparklineData = sparklineData;
    }

    @Override
    public String toString() {
        return id + " - " +  symbol + " - " + name + " - " + imageLogoLink  + " \n" ;
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


    public static JSONObject getMarketData(String coinDetails, String currencyId){
        String link_Website, link_blockExporer, link_blog, link_reddit, link_twitter, link_telegram, link_facebook,
                link_github, link_linkedIn, link_Slack, link_WhitePaper  ;

        JSONObject returnObject = new JSONObject() ;

        try{
            returnObject.put("market_cap_rank", "") ;
            returnObject.put("market_cap", "") ;
            returnObject.put("total_volume", "") ;
            returnObject.put("price_change_percentage_24h", "") ;
            returnObject.put("price_change_percentage_7d", "") ;
            returnObject.put("high_24h", "") ;
            returnObject.put("low_24h", "") ;
            returnObject.put("total_supply", "") ;
            returnObject.put("circulating_supply", "") ;
            returnObject.put("current_price", "") ;
            returnObject.put("hyperlinks", "[]") ;


            JSONObject responseObject = new JSONObject(coinDetails) ;
            if(!responseObject.has("name")){
                throw new JSONException("Response is bekaar ") ;
            }




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
                    link_telegram = "https://t.me/" + link_telegram ;
                }
                link_facebook = responseObject.getJSONObject("links").getString("facebook_username");
                if(!link_facebook.isEmpty()){
                    link_facebook = "https://www.facebook.com/" + link_facebook ;
                }

                link_Slack = "" ;
                link_linkedIn = "" ;
                link_WhitePaper = "" ;





            }


            JSONArray hyperlinksArray = new JSONArray() ;

            hyperlinksArray.put(0, new JSONArray(new String[]{"Website", link_Website})) ;
            hyperlinksArray.put(1, new JSONArray(new String[]{"Reddit", link_reddit})) ;
            hyperlinksArray.put(2, new JSONArray(new String[]{"Github", link_github})) ;
            hyperlinksArray.put(3, new JSONArray(new String[]{"Telegram", link_telegram})) ;
            hyperlinksArray.put(4, new JSONArray(new String[]{"Blog", link_blog})) ;
            hyperlinksArray.put(5, new JSONArray(new String[]{"Twitter", link_twitter})) ;
            hyperlinksArray.put(6, new JSONArray(new String[]{"Facebook", link_facebook})) ;
            hyperlinksArray.put(7, new JSONArray(new String[]{"Block Explorer", link_blockExporer})) ;
            hyperlinksArray.put(8, new JSONArray(new String[]{"Slack", link_Slack})) ;
            hyperlinksArray.put(9, new JSONArray(new String[]{"Linked In", link_linkedIn})) ;
            hyperlinksArray.put(10, new JSONArray(new String[]{"WhitePaper", link_WhitePaper})) ;

            returnObject.put("hyperlinks", hyperlinksArray) ;

            returnObject.put("description", responseObject.getJSONObject("description").getString("en"));


            JSONObject marketObj = responseObject.getJSONObject("market_data") ;


            returnObject.put("market_cap_rank", marketObj.getInt("market_cap_rank") );
            returnObject.put("market_cap", String.valueOf(marketObj.getJSONObject("market_cap").getLong(currencyId))) ;
            returnObject.put("total_volume", marketObj.getJSONObject("total_volume").getString(currencyId)) ;
            returnObject.put("price_change_percentage_24h", marketObj.getString("price_change_percentage_24h")) ;
            returnObject.put("price_change_percentage_7d", marketObj.getString("price_change_percentage_7d")) ;
            returnObject.put("high_24h", marketObj.getJSONObject("high_24h").getString(currencyId)) ;
            returnObject.put("low_24h", marketObj.getJSONObject("low_24h").getString(currencyId)) ;
            returnObject.put("total_supply", marketObj.get("total_supply").toString()) ;
            returnObject.put("circulating_supply", marketObj.getString("circulating_supply")) ;
            returnObject.put("current_price", marketObj.getJSONObject("current_price").get(currencyId).toString()) ;


        }catch (Exception e){
            Log.e(LOG_TAG, e.toString()) ;
        }

        return returnObject ;
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
            case Constants.TIMEFRAME_1DAY :
                startingDateTime = currentDateTime.minusDays(1).getMillis() ;
                break;
            case Constants.TIMEFRAME_1WEEK :
                startingDateTime = currentDateTime.minusWeeks(1).getMillis() ;
                break;
            case Constants.TIMEFRAME_1MONTH :
                startingDateTime = currentDateTime.minusMonths(1).getMillis() ;
                break;
            case Constants.TIMEFRAME_1YEAR :
                startingDateTime = currentDateTime.minusYears(1).getMillis() ;
                break;
            case Constants.TIMEFRAME_MAX :
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
//            return transactionObj.getSingleCoinPrice_CurrencyOriginal() ;
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
