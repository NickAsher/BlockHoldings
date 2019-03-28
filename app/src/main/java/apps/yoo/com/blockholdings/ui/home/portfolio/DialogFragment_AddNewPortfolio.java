package apps.yoo.com.blockholdings.ui.home.portfolio;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_AddNewPortfolio extends DialogFragment  {
    Context context ;
    String LOG_TAG = "DialogFragment_Portfolio -->" ;
    AppDatabase db ;
    FragmentManager fragmentManager ;

    EditText editText_NewPortfolioName ;
    Button btn_Save, btn_Cancel ;

    List<Object_Portfolio> listOfPortfolios ;
    MyListener.dlgFrg_AddNewPortfolio_to_DlgFrg_Portfolio listener ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dlgfrg_addnewportfolio, null) ;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels ;
        getDialog().getWindow().setLayout((9*width)/10, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().setTitle("Make New Portfolio");
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setCancelable(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setCancelable(true);

        context = getActivity() ;
        fragmentManager = getActivity().getSupportFragmentManager() ;
        listener = (DialogFragment_Portfolio)getTargetFragment() ; // this method gets the calling parent fragment
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;


        getReferences(view);
        setupBasicUI() ;





    }


    private void getReferences(View view){
        editText_NewPortfolioName = view.findViewById(R.id.dlgfrgAddNewPortfolio_EditText_NewName) ;
        btn_Cancel  =  view.findViewById(R.id.dlgfrgAddNewPortfolio_Btn_Cancel) ;
        btn_Save =  view.findViewById(R.id.dlgfrgAddNewPortfolio_Btn_Save) ;
    }




    private void setupBasicUI(){

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editText_NewPortfolioName.getText().toString() ;
                db.portfolioDao().insertPortfolio(new Object_Portfolio(newName, "0"));
                dismiss();
                listener.onAddingNewPortfolio();
            }
        });

    }













}
