package apps.yoo.com.blockholdings.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.Objects.Object_NewsSite;
import apps.yoo.com.blockholdings.ui.home.Activity_Portfolio;


public class MainActivity extends AppCompatActivity {
    AppDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(getApplicationContext()) ;

        initOneTimeSetup() ;

        Intent intent = new Intent(this, Activity_Portfolio.class) ;
        startActivity(intent);
        finish();
    }


    private void initOneTimeSetup(){
        List<Object_NewsSite> listOfItems = db.newSiteDao().getListOfNewsSites() ;
        if(listOfItems.size() == 0) {

            db.newSiteDao().insertFoodItem(new Object_NewsSite(1, "CoinDesk", "http://feeds.feedburner.com/CoinDesk", false));
            db.newSiteDao().insertFoodItem(new Object_NewsSite(2, "Coin Telegraph", "https://cointelegraph.com/rss", true));
            db.newSiteDao().insertFoodItem(new Object_NewsSite(3, "Bitcoin.com", "https://news.bitcoin.com/feed/", true));

        }
    }

}
