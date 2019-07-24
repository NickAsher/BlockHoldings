package apps.yoo.com.blockholdings.ui.transaction;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Exchange;
import apps.yoo.com.blockholdings.util.MyListener;


public class DialogFragment_DateTimePicker extends DialogFragment {
    Context context ;
    String LOG_TAG = "DialogFragment_DateTimePicker -->" ;
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
        return inflater.inflate(R.layout.dlgfrg_datetime, null) ;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
//        getDialog().getWindow().setLayout((9*width)/10, WindowManager.LayoutParams.WRAP_CONTENT);
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        context = getActivity() ;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setCancelable(true);
        listener_ActivityTransaction = (MyListener.DialogFragments_to_ActivityTransaction)getActivity() ;




                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, final int year,
                                                  final int monthOfYear, final int dayOfMonth) {

//


                                final Calendar c = Calendar.getInstance();
                                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                                        int mMinute = c.get(Calendar.MINUTE);

                                        // Launch Time Picker Dialog
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                                new TimePickerDialog.OnTimeSetListener() {

                                                    @Override
                                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                                          int minute) {

                                                        Calendar newCalendar = Calendar.getInstance() ;
                                                        newCalendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute ) ;
                                                        long timeinLong = newCalendar.getTimeInMillis()  ;
                                                        listener_ActivityTransaction.onSelectingDateTime(timeinLong);
                                                        dismiss();

                                                        

                                                    }
                                                }, mHour, mMinute, false);
                                        timePickerDialog.show();



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
        dismiss();



//        getReferences(view);
//        getListOfExchanges_From_ExhangeIds() ;
//        setupSearchView();
//        setupRecyclerView();




    }







}
