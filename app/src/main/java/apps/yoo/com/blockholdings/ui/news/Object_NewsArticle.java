package apps.yoo.com.blockholdings.ui.news;

public class Object_NewsArticle {
    private String title;
    private String link;
    private String description;
    private String imageLink ;
    private String pubDate ;

    public Object_NewsArticle(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate ;
    }

    public Object_NewsArticle(String title, String link, String description, String pubDate, String imageLink) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate ;
        this.imageLink = imageLink ;
    }


    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String toString(){
        if(imageLink == null){
            return "title : " + title + "\n" + "description: " + description + "\n" + "link : " + link + "\n \n" ;
        } else {
            return "title : " + title + "\n" + "description: " + description + "\n" + "link : " + link + "\n" + "imagelink : " + imageLink + "\n \n";

        }
    }
}
