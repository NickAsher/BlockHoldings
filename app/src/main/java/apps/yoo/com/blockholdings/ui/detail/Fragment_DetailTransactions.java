package apps.yoo.com.blockholdings.ui.detail;

import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.ui.transaction.Activity_Transaction;


public class Fragment_DetailTransactions extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_DetailPriceChart --> " ;
    AppDatabase db;

    RecyclerView rv ;
    TextView textView_AddTransactionsText ;
    ImageView imageView_AddTransactionBtn ;

    String coinId;
    int portfolioId ;
    List<Object_TransactionFullData> listOfTransactionFullData ;
    RVAdapter_DetailTransactions adapter ;


    public Fragment_DetailTransactions() {
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
        return inflater.inflate(R.layout.fragment_detail_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;

        portfolioId = MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext());
        listOfTransactionFullData = new ArrayList<>() ;
        db.transactionDao().getListOfAllTransaction_FullData_OfCoin(portfolioId, coinId).observe(this, new Observer<List<Object_TransactionFullData>>() {
            @Override
            public void onChanged(@Nullable List<Object_TransactionFullData> list) {
                listOfTransactionFullData = list ;
                adapter.refreshData(list);
                setupAddTransactionButton();
            }
        });

        getReferences(view) ;
        setupAddTransactionButton() ;
        setupRecyclerView() ;
    }

    private void getReferences(View v){
        rv = v.findViewById(R.id.fragmentDetailTransactions_RV_Main) ;
        textView_AddTransactionsText = v.findViewById(R.id.frgDetailTransactions_TV_AddTransactionText);
        imageView_AddTransactionBtn = v.findViewById(R.id.frgDetailTransactions_ImageView_AddTransactionBtn);

    }

    private void setupRecyclerView(){
        adapter = new RVAdapter_DetailTransactions(context, listOfTransactionFullData) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        DividerItemDecoration plainHorizontalLines = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL) ;
        rv.addItemDecoration(plainHorizontalLines);
    }

    private void setupAddTransactionButton(){
        if(listOfTransactionFullData.size() == 0){
            textView_AddTransactionsText.setVisibility(View.VISIBLE);
            imageView_AddTransactionBtn.setVisibility(View.VISIBLE);

        } else if(listOfTransactionFullData.size() > 0){
            textView_AddTransactionsText.setVisibility(View.GONE);
            imageView_AddTransactionBtn.setVisibility(View.GONE);
        }

        imageView_AddTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Transaction.class) ;
                intent.putExtra("coin", db.coinDao().getCoinById(coinId).toJson().toString()) ;
                context.startActivity(intent);
            }
        });
    }
}
