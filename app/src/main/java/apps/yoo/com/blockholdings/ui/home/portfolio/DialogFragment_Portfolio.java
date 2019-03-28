package apps.yoo.com.blockholdings.ui.home.portfolio;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.ui.home.Activity_Portfolio;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_Portfolio extends DialogFragment implements MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio,
        MyListener.dlgFrg_AddNewPortfolio_to_DlgFrg_Portfolio, MyListener.dlgFrg_EditPortfolioName_to_DlgFrg_Portfolio {
    Context context ;
    String LOG_TAG = "DialogFragment_Portfolio -->" ;
    AppDatabase db ;
    FragmentManager fragmentManager ;

    RecyclerView rv ;
    RVAdapter_PortfolioNames adapter ;
    RVAdapter_PortfolioNamesEdit editAdapter ;
    SearchView searchView_Main ;
    Button btn_AddNewPortfolio, btn_InnerSavePortfolio ;
    RelativeLayout relLt_InnerAddPortfolioContainer ;
    EditText editText_NewPortfolioName ;
    Button btn_EditPortfolios, btn_SaveEditing ;

    List<Object_Portfolio> listOfPortfolios ;
    MyListener.DialogFragmentPortfolios_to_ActivityPortfolio listener_ActivityPortfolio ;
    Object_Currency currencyObj ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_portfolionames, null) ;
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
        listener_ActivityPortfolio = (Activity_Portfolio)getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;
        currencyObj = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        listOfPortfolios = new ArrayList<>() ;
        db.portfolioDao().getListOfAllPortfolios_LiveData().observe(this, new Observer<List<Object_Portfolio>>() {
            @Override
            public void onChanged(List<Object_Portfolio> list) {
                listOfPortfolios = list ;
                if(btn_AddNewPortfolio.getVisibility() == View.VISIBLE){
                    adapter.refreshData(listOfPortfolios);
                }else {
                    editAdapter.refreshData(listOfPortfolios);
                }

            }
        });


        getReferences(view);
        setupRecyclerView();
        setupAddNewPortfolio() ;

        setupEditPortfolioButton();
        setupSaveEditingPortfolioBtn();




    }


    private void getReferences(View view){
        rv = (RecyclerView) view.findViewById(R.id.dialogFragmentExchanges_RecyclerView_CoinList) ;

        btn_AddNewPortfolio  =  view.findViewById(R.id.dialogFragmentPortfolioNames_Btn_AddNewPortfolio) ;
        btn_InnerSavePortfolio =  view.findViewById(R.id.dialogPortfolioNames_Btn_InnerSavePortfolio) ;
        btn_EditPortfolios = view.findViewById(R.id.dialogFragmentPortfolioNames_Btn_EditPortfolio) ;
        btn_SaveEditing = view.findViewById(R.id.dialogFragmentPortfolioNames_TextView_EditSavePortfolioBtn) ;


        relLt_InnerAddPortfolioContainer =  view.findViewById(R.id.dialogFragmentPortfolioNames_RelLt_AddingNewPortfolioContent) ;
        editText_NewPortfolioName = view.findViewById(R.id.dialogPortfolioNames_EditText_NewPortfolioName) ;



    }




    private void setupRecyclerView(){
        adapter = new RVAdapter_PortfolioNames(context, listOfPortfolios, this, currencyObj) ;
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
    }


    @Override
    public void onSelectingPortfolio(int newPortfolioId) {
        Object_Portfolio newPortfolioObj = db.portfolioDao().getPortfolioById(newPortfolioId) ;
        MyGlobals.setupCurrentPortfolioObj(newPortfolioObj);
        MySharedPreferences.setPortfolioId_InPreferences(context.getApplicationContext(), newPortfolioId);

        dismiss();
        listener_ActivityPortfolio.onChangingPortfolio();
    }


    private void setupAddNewPortfolio(){
        btn_AddNewPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_AddNewPortfolio dfAddNewPortfolio = new DialogFragment_AddNewPortfolio() ;
                dfAddNewPortfolio.setTargetFragment(DialogFragment_Portfolio.this, 0);
                dfAddNewPortfolio.show(fragmentManager, "dfAddNewPortfolio");
//                relLt_InnerAddPortfolioContainer.setVisibility(View.VISIBLE);
//                btn_AddNewPortfolio.setVisibility(View.GONE);
            }
        });

        btn_InnerSavePortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPortfolioName = editText_NewPortfolioName.getText().toString() ;
                db.portfolioDao().insertPortfolio(new Object_Portfolio(newPortfolioName, "0"));
                adapter.refreshData(db.portfolioDao().getListOfAllPortfolios());
                relLt_InnerAddPortfolioContainer.setVisibility(View.GONE);
                btn_AddNewPortfolio.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupEditPortfolioButton(){
        btn_EditPortfolios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.display(context, "Edit is being clicked");
                btn_EditPortfolios.setVisibility(View.GONE);
                btn_SaveEditing.setVisibility(View.VISIBLE);
                editAdapter = new RVAdapter_PortfolioNamesEdit(context, listOfPortfolios, DialogFragment_Portfolio.this) ;
                rv.setAdapter(editAdapter);

                btn_AddNewPortfolio.setVisibility(View.GONE);
            }
        });
    }

    private void setupSaveEditingPortfolioBtn(){
        btn_SaveEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_SaveEditing.setVisibility(View.GONE);
                btn_EditPortfolios.setVisibility(View.VISIBLE);
                rv.setAdapter(adapter);
                adapter.refreshData(db.portfolioDao().getListOfAllPortfolios());
                btn_AddNewPortfolio.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onAddingNewPortfolio(){
        adapter.refreshData(db.portfolioDao().getListOfAllPortfolios());
    }

    @Override
    public void onEditingPortfolioName() {
        editAdapter.refreshData(db.portfolioDao().getListOfAllPortfolios());
    }

    @Override
    public void onDeletingPortfolio(int portfolioId) {
        db.transactionDao().deleteTransactions_OfPortfolio(portfolioId);
        db.portfolioDao().deletePortfolioById(portfolioId);

    }
}
