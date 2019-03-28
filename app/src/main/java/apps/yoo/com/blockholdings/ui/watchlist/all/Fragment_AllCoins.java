package apps.yoo.com.blockholdings.ui.watchlist.all;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Collections2;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.models.Object_Coin;


public class Fragment_AllCoins extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_AllCoins --> " ;
    ViewModel_FragmentAllCoins viewModel ;

    RecyclerView rv, rv_SearchSuggestions ; ;
    SearchView searchView_Coins ;

    RVPagedAdapter_AllCoins adapter_AllCoins;
    RVAdapter_SearchSuggestions adapter_SearchSuggestions ;


    public Fragment_AllCoins() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frg_watchlist_allcoins, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity() ;

        getReferences(view) ;
        setupViewModel() ;

        setupRecyclerView_AllCoins() ;
        setupRecyclerView_SearchSuggestions();
        setupSearchView() ;
    }

    private void getReferences(View v){
        rv = v.findViewById(R.id.fragWatchlistAllCoins_RV_AllCoins) ;
        searchView_Coins = v.findViewById(R.id.frgWatchlistAllCoins_SearchView_Coins) ;
        rv_SearchSuggestions = v.findViewById(R.id.fragWatchlistAllCoins_RV_SearchSuggestions) ;

    }

    private void setupViewModel(){
        ViewModel_FragmentAllCoins.Factory viewModelFactory_pagingAllCoins = new ViewModel_FragmentAllCoins.Factory(context.getApplicationContext()) ;
        viewModel = ViewModelProviders.of(this, viewModelFactory_pagingAllCoins).get(ViewModel_FragmentAllCoins.class) ;

        viewModel.getLiveData_PagedList().observe(this, new Observer<PagedList<Object_Coin>>() {
            @Override
            public void onChanged(PagedList<Object_Coin> pagedList) {
                adapter_AllCoins.submitList(pagedList);
            }
        });
    }


    private void setupRecyclerView_AllCoins(){
        adapter_AllCoins = new RVPagedAdapter_AllCoins(context) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter_AllCoins);

    }


    private void setupSearchView(){
        searchView_Coins.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    adapter_SearchSuggestions.refreshData(new ArrayList<Object_Coin>()) ;

                } else {
                    adapter_SearchSuggestions.refreshData(new ArrayList<Object_Coin>(Collections2.filter(
                            viewModel.getListOfSearchSuggestedCoins(),
                            Object_Coin.getPredicateFilter_NameSymbol_startsWithAlphabet(newText)
                    ))) ;
                }
                return true;
            }
        });

        searchView_Coins.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter_SearchSuggestions.refreshData(new ArrayList<Object_Coin>());
                return true;
            }
        });
    }



    private void setupRecyclerView_SearchSuggestions(){
        adapter_SearchSuggestions = new RVAdapter_SearchSuggestions(context, new ArrayList<Object_Coin>()) ;
        GridLayoutManager gd = new GridLayoutManager(context, 2) ;
        gd.setAutoMeasureEnabled(true);
        rv_SearchSuggestions.setLayoutManager(gd);
        rv_SearchSuggestions.setNestedScrollingEnabled(false);
        rv_SearchSuggestions.setAdapter(adapter_SearchSuggestions);

    }
}
