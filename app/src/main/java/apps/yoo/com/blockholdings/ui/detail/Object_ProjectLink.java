package apps.yoo.com.blockholdings.ui.detail;

import android.util.Log;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.ArrayList;

import apps.yoo.com.blockholdings.R;

public class Object_ProjectLink {
    public static final String LOG_TAG= "Object_ProjectLink ";


    public static final int TYPE_WEBSITE = 0 ;
    public static final int TYPE_REDDIT = 1 ;
    public static final int TYPE_GITHUB = 2 ;
    public static final int TYPE_TELEGRAM = 3 ;
    public static final int TYPE_BLOG = 4 ;
    public static final int TYPE_TWITTER = 5 ;
    public static final int TYPE_FACEBOOK = 6 ;
    public static final int TYPE_BLOCK_EXPLORER = 7 ;
    public static final int TYPE_SLACK = 8 ;
    public static final int TYPE_LINKEDIN = 9 ;
    public static final int TYPE_WHITEPAPER = 10 ;

    int type ;

    String name ;

    String link ;

    public Object_ProjectLink(int type, String link) {
        this.type = type;
        switch (type){
            case Object_ProjectLink.TYPE_WEBSITE :
                this.name = "Website" ;
                break ;
            case Object_ProjectLink.TYPE_BLOG :
                this.name = "Blog" ;
                break ;
            case Object_ProjectLink.TYPE_REDDIT :
                this.name = "Reddit" ;
                break ;
            case Object_ProjectLink.TYPE_TELEGRAM :
                this.name = "Telegram" ;
                break ;
            case Object_ProjectLink.TYPE_TWITTER :
                this.name = "Twitter" ;
                break ;
            case Object_ProjectLink.TYPE_FACEBOOK :
                this.name = "Facebook" ;
                break ;
            case Object_ProjectLink.TYPE_GITHUB :
                this.name = "Github" ;
                break ;
            case Object_ProjectLink.TYPE_BLOCK_EXPLORER :
                this.name = "Block Explorer" ;
                break ;

            case Object_ProjectLink.TYPE_SLACK :
                this.name = "Slack" ;
                break ;

            case Object_ProjectLink.TYPE_WHITEPAPER :
                this.name = "WhitePaper" ;
                break ;

            case Object_ProjectLink.TYPE_LINKEDIN :
                this.name = "Linked In" ;
                break ;
        }
        this.link = link;
    }

    public Object_ProjectLink(int type, String name, String link) {
        this.type = type;
        this.name = name;
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }


    public static ArrayList<Object_ProjectLink> getNonEmptyListOfProjectLinks(JSONArray jsonArray){
        ArrayList<Object_ProjectLink> listOfHyperLinks = new ArrayList<>() ;

        try{
            for(int i = 0; i<jsonArray.length(); i++){
                JSONArray innerLinkArray = jsonArray.getJSONArray(i) ;
                String name = innerLinkArray.getString(0) ;
                String link = innerLinkArray.get(1).toString() ;

                if(!link.isEmpty()){
                    listOfHyperLinks.add(new Object_ProjectLink(i, name, link )) ;
                }
            }
        } catch (Exception e){
            Log.e(LOG_TAG, e.toString()) ;
        }

        return listOfHyperLinks ;
    }
}
