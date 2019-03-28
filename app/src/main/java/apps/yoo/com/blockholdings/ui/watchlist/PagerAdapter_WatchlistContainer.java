package apps.yoo.com.blockholdings.ui.watchlist;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import apps.yoo.com.blockholdings.ui.watchlist.all.Fragment_AllCoins;
import apps.yoo.com.blockholdings.ui.watchlist.all.Fragment_WatchlistAllCoins;


public class PagerAdapter_WatchlistContainer extends FragmentPagerAdapter {
    Context context ;


    public PagerAdapter_WatchlistContainer(FragmentManager fm, Context context) {
        super(fm);
        this.context = context ;
    }

    private String tabTitles[] = new String[] { "All Coins", "Watchlist"};


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
                return new Fragment_AllCoins();

            case 1 :
                return new Fragment_WatchlistSelected();

            default:
                return new Fragment_WatchlistAllCoins();

        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position] ;
    }


}
