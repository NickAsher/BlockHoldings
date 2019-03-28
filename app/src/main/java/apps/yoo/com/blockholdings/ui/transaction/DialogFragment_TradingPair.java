package apps.yoo.com.blockholdings.ui.transaction;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_TradingPair extends DialogFragment implements MyListener.RVAdapterTradingPair_to_DialogFragmentTradingPair {
    Context context ;
    String LOG_TAG = "DialogFragment_TradingPair -->" ;
    FragmentManager fragmentManager ;



    RecyclerView rv ;
    RVAdapter_TradingPair adapter ;
    SearchView searchView_Main ;

    List<String> listOfTradingPairKeys  ;
    MyListener.DialogFragments_to_ActivityTransaction listener_ActivityTransaction ;
    String coinSymbol ;


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
        listener_ActivityTransaction = (MyListener.DialogFragments_to_ActivityTransaction)getActivity() ;
        listOfTradingPairKeys = getArguments().getStringArrayList("tradingPairList") ;
        coinSymbol = getArguments().getString("coinSymbol") ;

        setupRecyclerView(view);




    }



    private void setupRecyclerView(View view){
        rv = (RecyclerView) view.findViewById(R.id.dialogFragmentExchanges_RecyclerView_CoinList) ;
        adapter = new RVAdapter_TradingPair(context, listOfTradingPairKeys, coinSymbol, this) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }



    @Override
    public void onSelectingTradingPair(String tradingPairSymbol) {
        dismiss();
        listener_ActivityTransaction.onSelectingTradingPair(tradingPairSymbol);
    }
}
