package apps.yoo.com.blockholdings.ui.transaction;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.Objects.Object_Exchange;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_Exchanges extends DialogFragment implements MyListener.RVAdapterExchange_to_DialogFragmentExchange {
    Context context ;
    String LOG_TAG = "DialogFragment_Exchanges -->" ;
    AppDatabase db ;
    FragmentManager fragmentManager ;

    RecyclerView rv ;
    RVAdapter_Exchanges adapter ;
    SearchView searchView_Main ;

    List<String> listOfExchangeIds ;
    List<Object_Exchange> listOfExchanges ;
    MyListener.DialogFragments_to_ActivityTransaction listener_ActivityTransaction ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_exchanges, null) ;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        getDialog().getWindow().setLayout((9*width)/10, WindowManager.LayoutParams.WRAP_CONTENT);
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setCancelable(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setCancelable(true);

        context = getActivity() ;
        fragmentManager = ((FragmentActivity)context).getSupportFragmentManager() ;
        listener_ActivityTransaction = (Activity_Transaction)getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        listOfExchanges = new ArrayList<>() ;



        getReferences(view);
        getListOfExchanges_From_ExhangeIds() ;
        setupSearchView();
        setupRecyclerView();




    }


    private void getReferences(View view){
        searchView_Main = (SearchView) view.findViewById(R.id.dialogFragmentExchanges_SearchView_Main) ;
        rv = (RecyclerView) view.findViewById(R.id.dialogFragmentExchanges_RecyclerView_CoinList) ;
    }

    private void getListOfExchanges_From_ExhangeIds(){
        listOfExchangeIds = getArguments().getStringArrayList("exchangesList") ;
        listOfExchanges = db.exchangeDao().getListOfExchanges_WithIds(listOfExchangeIds) ;

    }

    private void setupSearchView(){
        searchView_Main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Object_Exchange> listOfSelectedExchanges = db.exchangeDao().getListOExchanges_WithIds_WhoseNameStartWithAlphabet(listOfExchangeIds, newText) ;
                Log.e(LOG_TAG, listOfSelectedExchanges.toString()) ;
                adapter.refreshData(listOfSelectedExchanges);
                return true;
            }
        });



    }


    private void setupRecyclerView(){
        adapter = new RVAdapter_Exchanges(context, listOfExchanges, this) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }


    @Override
    public void closeDialog() {
        dismiss();
        listener_ActivityTransaction.onSelectingExchange();
    }
}
