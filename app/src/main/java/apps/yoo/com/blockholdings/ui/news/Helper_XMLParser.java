package apps.yoo.com.blockholdings.ui.news;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper_XMLParser {
    
    
    public static String RSS_ITEM = "item" ;
    public static String RSS_ITEM_TITLE = "title" ;
    public static String RSS_ITEM_LINK = "link" ;
    public static String RSS_ITEM_AUTHOR = "dc:creator" ;
    public static String RSS_ITEM_CATEGORY = "category" ;
    public static String RSS_ITEM_THUMBNAIL = "media:thumbnail" ;
    public static String RSS_ITEM_DESCRIPTION = "description" ;
    public static String RSS_ITEM_CONTENT = "content:encoded" ;
    public static String RSS_ITEM_PUB_DATE = "pubDate" ;
    public static String RSS_ITEM_URL = "url" ;

    public static ArrayList<Object_NewsArticle> parseRssFeed(String inputString, String LOG_TAG){

        ArrayList<Object_NewsArticle> returnList = new ArrayList<>() ;

        String title = null;
        String link = null;
        String description = null;
        String pubdate = null ;
        String imageLink = null ;
        int imageCheck = 0;

        boolean isItem = false;
//        listOfArticles = new ArrayList<>() ;
//            List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xpp.setInput(new StringReader(inputString));

            xpp.nextTag();
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xpp.getEventType();

                String name = xpp.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xpp.next() == XmlPullParser.TEXT) {
                    result = xpp.getText();
                    xpp.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                }else if (name.equalsIgnoreCase("pubdate")) {
                    // RSS date format is the following: Sat, 15 Dec 2018 12:00:32 +0000
                    // https://validator.w3.org/feed/docs/rss2.html
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH) ;
                    Date date = sdf.parse(result) ;
//                        pubdate = TimeAgo.getTimeAgo(date.getTime()) ;
                    pubdate = result ;


                }else if (name.equalsIgnoreCase("media:thumbnail") ) {
                    imageLink = xpp.getAttributeValue(null, "url");
                    imageCheck ++ ;
                }
                else if (name.equalsIgnoreCase("description")) {
                    description = result;
                    if (imageLink == null) {
                        imageLink = getImageUrl(description) ;
                        if(imageLink == null){
                            imageCheck ++ ;

                        }
                    }
                }


                if (title != null && link != null && pubdate != null && description != null && imageCheck == 2) {
                    if (isItem) {
                        Object_NewsArticle item = new Object_NewsArticle(title, link, description, pubdate, imageLink);
                        returnList.add(item);
                    }

                    else {

                    }

                    title = null;
                    link = null;
                    description = null;
                    pubdate = null ;
                    imageLink = null ;
                    isItem = false;
                }
                 else if (title != null && link != null && pubdate != null && description != null && imageCheck == 2) {
                    if (isItem) {
                        Object_NewsArticle item = new Object_NewsArticle(title, link, description, pubdate);
                        returnList.add(item);
                    }

                    else {

                    }

                    title = null;
                    link = null;
                    description = null;
                    pubdate = null ;
                    imageLink = null ;
                    isItem = false;
                }

            }


        }catch (Exception e){
            Log.e(LOG_TAG, "Error dude " + e.toString()) ;
        }
        return returnList ;

    }


//    public static String getImageFromURL() {
//
//    }


    public static ArrayList<Object_NewsArticle> parseRssFeed2(String inputString, String LOG_TAG){

        ArrayList<Object_NewsArticle> articleList = new ArrayList<>() ;

        String title = null;
        String link = null;
        String description = null;
        String pubdate = null ;
        String imageLink = null ;
        int imageCheck = 0;

        boolean isItem = false;


        try {

            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(new StringReader(inputString));

            int eventType = xmlPullParser.getEventType() ;

            // Start parsing the xml
            while (eventType != XmlPullParser.END_DOCUMENT) {

                // Start parsing the item
                if (eventType == XmlPullParser.START_TAG) {
                    if (xmlPullParser.getName().equalsIgnoreCase(RSS_ITEM)) {
                        isItem = true ;

                    } else if (xmlPullParser.getName().equalsIgnoreCase(RSS_ITEM_TITLE)) {
                        if (isItem) {
                            title = xmlPullParser.nextText();
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase(RSS_ITEM_LINK)) {
                        if (isItem) {
                            link = xmlPullParser.nextText() ;
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase(RSS_ITEM_THUMBNAIL)) {
                        if (isItem) {
                            imageLink = xmlPullParser.getAttributeValue(null, RSS_ITEM_URL) ;
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase(RSS_ITEM_DESCRIPTION)) {
                        if (isItem) {
                            description = xmlPullParser.nextText() ;
                            if (imageLink == null) {
                                imageLink = getImageUrl(description) ;
                            }
                        }

                    } else if (xmlPullParser.getName().equalsIgnoreCase(RSS_ITEM_PUB_DATE)) {
                        // RSS date format is the following: Sat, 15 Dec 2018 12:00:32 +0000
                        // https://validator.w3.org/feed/docs/rss2.html

                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH) ;
//                        Date date = sdf.parse(xmlPullParser.nextText()) ;
//                        pubdate = TimeAgo.getTimeAgo(date.getTime()) ;
                        pubdate = xmlPullParser.nextText() ;
                    }

                } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {
                    // The item is correctly parsed
                    isItem = false ;
                    articleList.add(new Object_NewsArticle(title, link, description, pubdate, imageLink)) ;

                    title = null;
                    link = null;
                    description = null;
                    pubdate = null ;
                    imageLink = null ;

                }
                eventType = xmlPullParser.next() ;
            }

        }catch (Exception e){
            Log.e(LOG_TAG, "Error dude2 " + e.toString()) ;


        }
        return articleList ;

    }


    private static String getImageUrl(String input) {

//        var url: String? = null
        String result = null ;
        Pattern patternImg = Pattern.compile("(<img .*?>)") ;
        Matcher matcherImg = patternImg.matcher(input) ;
        if (matcherImg.find()) {
            String imgTag = matcherImg.group(1) ;
            Pattern patternLink = Pattern.compile("src\\s*=\\s*\"(.+?)\"") ;
            Matcher matcherLink = patternLink.matcher(imgTag) ;
            if (matcherLink.find()) {
                result = matcherLink.group(1) ;
            }
        }
        return result ;
    }
}
