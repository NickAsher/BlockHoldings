package apps.yoo.com.blockholdings.ui.detail;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



public class PagerAdapter_Detail extends FragmentPagerAdapter {
    String coinId ;
    Context context ;


    public PagerAdapter_Detail(FragmentManager fm, Context context, String coinId) {
        super(fm);
        this.context = context ;
        this.coinId = coinId ;
    }

    private String tabTitles[] = new String[] { "Detail", "Transactions", "Notifications", "Updates"};


    @Override
    public int getCount() {
        return tabTitles.length;
    }



    @Override
    public Fragment getItem(int position) {
        /**
         * In the fragment pager adapter, we im getItem, we have to return a fragment
         * for every position. So we simply use a switch statement and in each case,
         * we return a different fragment
         */
        switch (position){
            case 0 :
                Fragment_DetailPriceChart fragment = new Fragment_DetailPriceChart();
                Bundle args = new Bundle();
                args.putString("coinId", coinId);
                fragment.setArguments(args);
                return fragment;
            case 1 :
                Fragment_DetailTransactions fragment1 = new Fragment_DetailTransactions();
                Bundle args1 = new Bundle();
                args1.putString("coinId", coinId);
                fragment1.setArguments(args1);
                return fragment1;

            case 2 :
                Fragment_DetailNotification fragment2 = new Fragment_DetailNotification();
                Bundle args2 = new Bundle();
                args2.putString("coinId", coinId);
                fragment2.setArguments(args2);
                return fragment2;

            case 3 :
                Fragment_DetailUpdates fragment3 = new Fragment_DetailUpdates();
                Bundle args3 = new Bundle();
                args3.putString("coinId", coinId);
                fragment3.setArguments(args3);
                return fragment3;


            default:
                Fragment_DetailPriceChart fragmentD = new Fragment_DetailPriceChart();
                Bundle argsD = new Bundle();
                argsD.putString("CoinId", coinId);
                fragmentD.setArguments(argsD);
                return fragmentD;

        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position] ;
    }


}
