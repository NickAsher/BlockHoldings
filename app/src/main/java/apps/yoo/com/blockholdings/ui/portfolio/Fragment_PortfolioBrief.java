package apps.yoo.com.blockholdings.ui.portfolio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.ui.detail.ChartMarkerView;
import apps.yoo.com.blockholdings.ui.home.Activity_Home;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;


public class Fragment_PortfolioBrief extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_PortfolioBrief --> " ;
    AppDatabase db;
    FragmentManager fragmentManager ;

    TextView textView_PortfolioName, textView_PortfolioValue, textView_PortfolioPriceChange ;
    LineChart chart_Portfolio ;
//    RadioGroup radioGroup_PortfolioDateValue ;
    RadioRealButtonGroup radioGroup_PortfolioDateValue ;

    ImageView imageView_BackBtn ;

    Object_Portfolio currentPortfolioObj ;
    Object_Currency currentCurrency ;
    List<Entry> listOfPortfolioUpdateLogValues ;


    public Fragment_PortfolioBrief() {
        // Required empty public constructor
    }

    public static Fragment_PortfolioBrief newInstance() {
        Fragment_PortfolioBrief fragment = new Fragment_PortfolioBrief();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio_brief, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext() ;
        db = AppDatabase.getInstance(context.getApplicationContext()) ;
        fragmentManager = getActivity().getSupportFragmentManager() ;

        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        currentPortfolioObj = db.portfolioDao().getPortfolioById(MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext())) ;


        getReferences(view);
        setupBasicUI();
        setPortfolioTextValues();
        setPortfolioUpdateLog(view) ;
        setupChartRadioGroup() ;
    }



    private void getReferences(View v){
        textView_PortfolioName = v.findViewById(R.id.activityPortfolio_TextView_PortfolioName) ;
        textView_PortfolioValue = v.findViewById(R.id.activityPortfolio_TextView_PortfolioBalance) ;
        textView_PortfolioPriceChange =v.findViewById(R.id.activityPortfolio_TextView_PortfolioPriceChange) ;
        chart_Portfolio = v.findViewById(R.id.activityPortfolio_LineChart_Main) ;
        radioGroup_PortfolioDateValue = v.findViewById(R.id.radioRealButtonGroup_1) ; //activityPortfolio_RadioGroup_ChartDateValues
        imageView_BackBtn = v.findViewById(R.id.fragmentPortfolioBrief_ImageView_BackButton) ;
    }

    private void setupBasicUI(){
        // The following is used to set a onClick listener to textViewPortfolioName,
        // which when clicked opens the main activity if we are in the Activity_Portfolio
        textView_PortfolioName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(LOG_TAG, getActivity().getClass().getName()) ;
                if(getActivity().getClass().getSimpleName().equalsIgnoreCase("Activity_Home")){
                    Intent intent =  new Intent(context, Activity_Portfolio.class) ;
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        // the following code makes the back button visible when the fragment is inside Activity_Portfolio
        if(getActivity().getClass().getSimpleName().equalsIgnoreCase("Activity_Portfolio")){
            imageView_BackBtn.setVisibility(View.VISIBLE);
            imageView_BackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(context, Activity_Home.class) ;
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }

    private void setPortfolioTextValues(){
        textView_PortfolioName.setText(currentPortfolioObj.getPortfolioName());
        textView_PortfolioValue.setText(currentCurrency.getCurrencySymbol() + currentPortfolioObj.getPortfolioValue());
    }

    private void setPortfolioPriceChange(String priceTimeAgo){
        /*
         * The price change of portfolio cannot be calculated from update log of portfolio
         * This is because the update log of portfolio contains data about deleted transaction also
         * Whereas the price change is needed only for the current coins
         * So we will calculate it from the transactions of the portfolio
         * This will be done using the same way we calculate the price change for each individual coin
         *
         */

        BigDecimal priceChange = new BigDecimal(currentPortfolioObj.getPortfolioValue()).subtract(new BigDecimal(priceTimeAgo)) ;
        BigDecimal percentageChange = priceChange.divide(new BigDecimal(currentPortfolioObj.getPortfolioValue()), BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) ;
        if(priceChange.compareTo(new BigDecimal(0)) >= 0){
            // the price change is positive
            String str_PriceChange = priceChange.toPlainString() ;
            String str_percentageChange = percentageChange.toPlainString() ;

            textView_PortfolioPriceChange.setText(currentCurrency.getCurrencySymbol() + str_PriceChange + " (" + str_percentageChange + "%)" );
            textView_PortfolioPriceChange.setTextColor(ContextCompat.getColor(context, R.color.green));

        } else {
            String str_PriceChange = priceChange.toPlainString() ;
            String str_percentageChange = percentageChange.toPlainString() ;

            textView_PortfolioPriceChange.setText(currentCurrency.getCurrencySymbol() + str_PriceChange + " (" + str_percentageChange + "%)" );
            textView_PortfolioPriceChange.setTextColor(ContextCompat.getColor(context, R.color.red));

        }
    }


    private void setupChartRadioGroup(){

        radioGroup_PortfolioDateValue.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                switch (button.getId()){
                    case R.id.activityPortfolio_RadioBtn_Chart1Day :
                        listOfPortfolioUpdateLogValues = Helper_Portfolio.getFormattedChartValues_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1DAY) ;

                        setPortfolioPriceChange(Helper_Portfolio.getPriceTimeAgo_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1DAY) );
                        break;
                    case R.id.activityPortfolio_RadioBtn_Chart1Week :
                        listOfPortfolioUpdateLogValues = Helper_Portfolio.getFormattedChartValues_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1WEEK) ;

                        setPortfolioPriceChange(Helper_Portfolio.getPriceTimeAgo_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1WEEK) );
                        break;
                    case R.id.activityPortfolio_RadioBtn_Chart1Month :
                        listOfPortfolioUpdateLogValues = Helper_Portfolio.getFormattedChartValues_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1MONTH) ;

                        setPortfolioPriceChange(Helper_Portfolio.getPriceTimeAgo_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1MONTH) );
                        break;
                    case R.id.activityPortfolio_RadioBtn_Chart1Year :
                        listOfPortfolioUpdateLogValues = Helper_Portfolio.getFormattedChartValues_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1YEAR) ;

                        setPortfolioPriceChange(Helper_Portfolio.getPriceTimeAgo_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_1YEAR) );
                        break;
                    case R.id.activityPortfolio_RadioBtn_ChartMax :
                        listOfPortfolioUpdateLogValues = Helper_Portfolio.getFormattedChartValues_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog()) ;

                        setPortfolioPriceChange(Helper_Portfolio.getPriceTimeAgo_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_MAX) );
                        break;
                    default:
                        listOfPortfolioUpdateLogValues = Helper_Portfolio.getFormattedChartValues_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog()) ;

                        setPortfolioPriceChange(Helper_Portfolio.getPriceTimeAgo_from_UpdateLogOfPortfolio(
                                currentPortfolioObj.getPortfolioUpdateLog(), Helper_Portfolio.TIMEFRAME_MAX) );
                }
                setupGraph();

            }
        });

//
        radioGroup_PortfolioDateValue.setPosition(4);

    }

    public void setupGraph(){

        LineDataSet dataSet = new LineDataSet(listOfPortfolioUpdateLogValues, "Label"); // add entries to dataset
        dataSet.setHighLightColor(ContextCompat.getColor(context, R.color.black_Priamry)); // set color of indicator
        dataSet.setDrawHorizontalHighlightIndicator(false); // remove the horizontal line from indicator
        dataSet.setColor(ContextCompat.getColor(context, R.color.green)); // change the color of line
        dataSet.setDrawCircles(false); // remove the circle of each point on chart
        dataSet.setLineWidth(1.8f); /// change the thickness of line

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);   // remove the text value of each point on chart

        chart_Portfolio.setData(lineData);

        chart_Portfolio.setScaleEnabled(false);
        chart_Portfolio.getXAxis().setEnabled(false);  // disable the x axis
        chart_Portfolio.getAxisLeft().setEnabled(false);  // disable the Left Y axis
        chart_Portfolio.getAxisRight().setEnabled(false); // disable the Right Y axis
        chart_Portfolio.getDescription().setEnabled(false);
        chart_Portfolio.getLegend().setEnabled(false);


        ChartMarkerView mv = new ChartMarkerView(context, R.layout.marker_chartview);
        // Set the marker to the chart
        mv.setChartView(chart_Portfolio);
        chart_Portfolio.setMarker(mv);

        chart_Portfolio.highlightValue(null); // THis is done to make sure that once chart refreshes, no marker is shown
        chart_Portfolio.invalidate();



        chart_Portfolio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        chart_Portfolio.highlightValue(null);
                        break;
                    }


                }
                return false;
            }
        });
    }


    private void setPortfolioUpdateLog(View v){
        Button btn = v.findViewById(R.id.activityPortfolio_Btn_PortfolioUpdates) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_UpdateLog dfPortfolioUpdate = new DialogFragment_UpdateLog() ;
                Bundle bundle = new Bundle() ;
                bundle.putInt("portfolioId", MySharedPreferences.getPortfolioId_FromPreference(context.getApplicationContext()));
                dfPortfolioUpdate.setArguments(bundle);
                dfPortfolioUpdate.show(fragmentManager, "dfPortfolioUpdate ");
            }
        });
    }


    public void onSelectingNewPortfolio(int newPortfolioId){
        currentPortfolioObj = db.portfolioDao().getPortfolioById(newPortfolioId) ;
        setPortfolioTextValues();
        radioGroup_PortfolioDateValue.deselect(); //TODO clearCheck();
        setupChartRadioGroup();
    }

    public void refreshPortfolio(){
        // since portfolio has been refreshed, we need to get the new values from the database and then refresh it
        // the method onSelectingNewPortfolio does exactly this. So we call it
        // For portfolioId in the method, we call it with current Portfolio's Id
        onSelectingNewPortfolio(currentPortfolioObj.getPortfolioId());
    }









}
