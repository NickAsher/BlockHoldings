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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Collections2;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_Exchanges extends DialogFragment implements MyListener.RVAdapterExchange_to_DialogFragmentExchange {
    Context context ;
    String LOG_TAG = "DialogFragment_Exchanges -->" ;
    AppDatabase db ;
    FragmentManager fragmentManager ;

    RecyclerView rv ;
    RVAdapter_Exchanges adapter ;
    SearchView searchView_Main ;
    RelativeLayout lt_GlobalAverageContainer ;

    List<String> listOfExchangeIds ;
    List<Object_Exchange> listOfExchanges ;
    MyListener.DialogFragments_to_ActivityTransaction listener_ActivityTransaction ;
    String coinId ;
    Object_Currency currencyObj ;



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
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        listOfExchanges = new ArrayList<>() ;
        currencyObj = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        coinId = getArguments().getString("coinId") ;


        getReferences(view);
        getListOfExchanges_From_ExhangeIds() ;
        setupSearchView();
        setupRecyclerView();
        setupSelectingGlobalAverage();




    }


    private void getReferences(View view){
        searchView_Main = (SearchView) view.findViewById(R.id.dialogFragmentExchanges_SearchView_Main) ;
        rv = (RecyclerView) view.findViewById(R.id.dialogFragmentExchanges_RecyclerView_CoinList) ;
        lt_GlobalAverageContainer = view.findViewById(R.id.dialogFragmentExchanges_RelLt_GlobalAverageContainer) ;
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
                if(!newText.isEmpty()){
                    Log.v(LOG_TAG, "Filtering exhanges that contain some string") ;
                    adapter.refreshData(new ArrayList<>(Collections2.filter(listOfExchanges, Object_Exchange.getPredicateFilter_Name_containsString(newText))));
                }
                return true;
            }
        });



    }


    private void setupRecyclerView(){
        adapter = new RVAdapter_Exchanges(context, listOfExchanges, this) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }

    private void setupSelectingGlobalAverage2(){
        lt_GlobalAverageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.getURL_APICALL_SIMPLEPRICES(coinId, currencyObj.getCurrencyId()) ;
                Log.e(LOG_TAG, url) ;
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    Log.e(LOG_TAG, "Response from server : " + response) ;
                                    JSONObject jsonObject = new JSONObject(response) ;
                                    String price = jsonObject.getJSONObject(coinId).get(currencyObj.getCurrencyId()).toString();
                                    dismiss();
                                    listener_ActivityTransaction.onSelectingExchange_GlobalAverage(price);
                                }catch (JSONException e){
                                    Log.e(LOG_TAG, e.toString()) ;
                                    Message.display(context, "Some server error");
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.display(context, "Error in making volley request");
                        Log.e(LOG_TAG, error.toString() ) ;
                    }
                }) ;

                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext()) ;
                requestQueue.add(stringRequest2) ;
            }
        });
    }


    private void setupSelectingGlobalAverage(){

        final String transactionDate = getArguments().getString("transactionDate") ;




        lt_GlobalAverageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.getURL_APICALL_HISTORICAL_PRICE(coinId, transactionDate) ;

                Log.e(LOG_TAG, url) ;
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    Log.e(LOG_TAG, "Response from server : " + response) ;
                                    JSONObject jsonObject = new JSONObject(response) ;
                                    String price = jsonObject.getJSONObject("market_data").getJSONObject("current_price").get(currencyObj.getCurrencyId()).toString();
                                    dismiss();
                                    listener_ActivityTransaction.onSelectingExchange_GlobalAverage(price);
                                }catch (JSONException e){
                                    Log.e(LOG_TAG, e.toString()) ;
                                    Message.display(context, "Some server error");
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.display(context, "Error in making volley request");
                        Log.e(LOG_TAG, error.toString() ) ;
                    }
                }) ;

                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext()) ;
                requestQueue.add(stringRequest2) ;
            }
        });
    }



    @Override
    public void onSelectingExchange(Object_Exchange exchangeObj) {
        dismiss();
        listener_ActivityTransaction.onSelectingExchange(exchangeObj);
    }
}
