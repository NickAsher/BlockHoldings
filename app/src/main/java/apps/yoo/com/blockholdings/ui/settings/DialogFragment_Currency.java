package apps.yoo.com.blockholdings.ui.settings;


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
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_Currency extends DialogFragment implements MyListener.RVAdapterCurrency_to_DialogFragmentCurrency {
    Context context ;
    String LOG_TAG = "DialogFragment_Currency -->" ;
    FragmentManager fragmentManager ;
    AppDatabase db ;



    RecyclerView rv ;
    SearchView searchView_Main ;

    List<Object_Currency> listOfCurrencies  ;
    RVAdapter_Currency adapter ;
    MyListener.DialogFragmentCurrency_to_ActivitySettings listener ;


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
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        listener =  (MyListener.DialogFragmentCurrency_to_ActivitySettings)getActivity() ;
        setupRecyclerView(view);

    }



    private void setupRecyclerView(View view){
        listOfCurrencies = db.currencyDao().getListOfAllCurrencies() ;
        rv = (RecyclerView) view.findViewById(R.id.dialogFragmentExchanges_RecyclerView_CoinList) ;
        adapter = new RVAdapter_Currency(context, listOfCurrencies, this) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }


    @Override
    public void onCurrencySelected(Object_Currency currencyObj) {
        dismiss();
        listener.onCurrencySelected(currencyObj);
    }
}
