package apps.yoo.com.blockholdings.ui.home.portfolio;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.ui.detail.RVAdapter_DetailUpdates;


public class DialogFragment_UpdateLog extends DialogFragment {
    Context context;
    String LOG_TAG = "Fragment_DetailUpdates --> ";
    AppDatabase db;

    RecyclerView rv;



    RVAdapter_DetailUpdates adapter;
    int portfolioId ;
    Object_Portfolio portfolioObj ;


    public DialogFragment_UpdateLog() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            coinId = getArguments().getString("coinId");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_updates, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
//        int height = metrics.heightPixels ;
        getDialog().getWindow().setLayout((9*width)/10, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().setTitle("Portfolio UpdateLog");
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        db = AppDatabase.getInstance(getActivity().getApplicationContext());




        portfolioId = getArguments().getInt("portfolioId");
        getReferences(view);
        setupRecyclerView();

        db.portfolioDao().getPortfolioById_LiveData(portfolioId).observe(this, new Observer<Object_Portfolio>() {
            @Override
            public void onChanged(@Nullable Object_Portfolio portfolioObj) {
                try {
                    JSONArray jsonArray = new JSONArray(portfolioObj.getPortfolioUpdateLog());
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