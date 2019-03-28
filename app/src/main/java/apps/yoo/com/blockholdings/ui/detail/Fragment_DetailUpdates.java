package apps.yoo.com.blockholdings.ui.detail;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Coin;


public class Fragment_DetailUpdates extends Fragment {
    Context context;
    String LOG_TAG = "Fragment_DetailUpdates --> ";
    AppDatabase db;

    RecyclerView rv;


    String coinId;
    Object_Coin currentCoin;
    RVAdapter_DetailUpdates adapter;



    public Fragment_DetailUpdates() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coinId = getArguments().getString("coinId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_updates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        db = AppDatabase.getInstance(getActivity().getApplicationContext());

        getReferences(view);
        setupRecyclerView();

        db.coinDao().getCoinById_LiveData(coinId).observe(this, new Observer<Object_Coin>() {
            @Override
            public void onChanged(@Nullable Object_Coin object_coin) {
                try {
                    JSONArray jsonArray = new JSONArray(object_coin.getPriceData());
                    adapter.refreshData(jsonArray);
                } catch (JSONException e){
                    Log.e(LOG_TAG, e.toString()) ;
                }
            }
        });





    }

    private void getReferences(View v) {
        rv = v.findViewById(R.id.frgDetailUpdates_RV_Main);

    }

    private void setupRecyclerView() {
        try{
        adapter = new RVAdapter_DetailUpdates(context,  new JSONArray("[]"));
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }
    }


}