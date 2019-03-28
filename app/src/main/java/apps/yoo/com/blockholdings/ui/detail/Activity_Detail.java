package apps.yoo.com.blockholdings.ui.detail;

import androidx.lifecycle.Observer;
import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.Utils;

public class Activity_Detail extends AppCompatActivity {
    Context context ;
    String LOG_TAG = "Activity_Detail --> " ;
    AppDatabase db;
    FragmentManager fragmentManager ;

    ViewPager viewPager ;
    TabLayout tabLayout ;
    RelativeLayout lt_CoinPortfolioDetails ;
    TextView textView_CoinName, textView_PortfolioTotalNoOfCoins, textView_PortfolioTotalPriceOriginal,
            textView_PortfolioTotalPriceCurrent ;
    ImageView imageView_CoinImage ;

    String coinId ;
    Object_Portfolio currentPortfolioObj ;
    Object_Currency currentCurrencyObj ;
    Object_Coin currentCoinObj ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__detail);

        context = this ;
        db = AppDatabase.getInstance(getApplicationContext()) ;
        fragmentManager = getSupportFragmentManager() ;


        coinId = getIntent().getStringExtra("coinId") ;
        Message.display(context, "Coin id is " + coinId);
        currentPortfolioObj = db.portfolioDao().getPortfolioById(MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext())) ;
        currentCurrencyObj = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext());
//        checkForTransactionsOfCoin();
        getReferences() ;
        setupViewPager() ;
        setBasicUI() ;
        setupPortfolioDetailsOfCoinLt();
    }

    private void getReferences(){
        viewPager = findViewById(R.id.activityDetail_ViewPager_Main) ;
        tabLayout = findViewById(R.id.activityDetail_TabLayout_Main) ;

        imageView_CoinImage = findViewById(R.id.activityDetail_ImageView_CoinImage) ;
        textView_CoinName = findViewById(R.id.activityDetail_TextView_CoinName) ;

        lt_CoinPortfolioDetails = findViewById(R.id.activityDetail_RelLt_PortfolioDataContainer) ;
        textView_PortfolioTotalNoOfCoins = findViewById(R.id.activityDetail_TextView_ValuePortfolioNoOfCoins) ;
        textView_PortfolioTotalPriceOriginal = findViewById(R.id.activityDetail_TextView_ValueTotalPriceOriginal) ;
        textView_PortfolioTotalPriceCurrent = findViewById(R.id.activityDetail_TextView_ValueTotalPriceCurrent) ;



    }


    private void setupViewPager(){
        PagerAdapter_Detail adapter_detail = new PagerAdapter_Detail(fragmentManager, context, coinId) ;
        viewPager.setAdapter(adapter_detail);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setBasicUI(){
        currentCoinObj = db.coinDao().getCoinById(coinId) ;
        Glide.with(context).load(currentCoinObj.getImageLogoLink()).into(imageView_CoinImage) ;
        textView_CoinName.setText(currentCoinObj.getName());

    }





    private void setupPortfolioDetailsOfCoinLt(){
        db.transactionDao().getListOfAllTransaction_FullData_OfCoin(currentPortfolioObj.getPortfolioId(), coinId).observe(this, new Observer<List<Object_TransactionFullData>>() {
            @Override
            public void onChanged(@Nullable List<Object_TransactionFullData> list) {


                if(list.size() == 0){
                    lt_CoinPortfolioDetails.setVisibility(View.GONE);
                } else if (list.size() > 0){

                    BigDecimal totalNoOfCoins = new BigDecimal(0),
                            totalPrice_Original = new BigDecimal(0),
                            totalPrice_Current = new BigDecimal(0) ;

                    for(Object_TransactionFullData transactionObj : list){
                        totalNoOfCoins = totalNoOfCoins.add(new BigDecimal(transactionObj.getTransactionObject().getNoOfCoins())) ;
                        totalPrice_Original = totalPrice_Original.add(new BigDecimal(transactionObj.getTransactionObject().getTotalValue_Original())) ;
                        totalPrice_Current = totalPrice_Current.add(new BigDecimal(transactionObj.getTransactionObject().getTotalValue_Current())) ;
                    }





                    lt_CoinPortfolioDetails.setVisibility(View.VISIBLE);
                    textView_PortfolioTotalNoOfCoins.setText(Utils.formatNumber_ie_Commas(totalNoOfCoins.toPlainString()) + " " + list.get(0).getCoinObject().getSymbol());
                    textView_PortfolioTotalPriceOriginal.setText(currentCurrencyObj.getCurrencySymbol() + Utils.formatNumber_ie_SingleCoinPriceCurrency(totalPrice_Original.toPlainString()));
                    textView_PortfolioTotalPriceCurrent.setText(currentCurrencyObj.getCurrencySymbol() + Utils.formatNumber_ie_SingleCoinPriceCurrency(totalPrice_Current.toPlainString()));

                }
            }
        });
    }


}
