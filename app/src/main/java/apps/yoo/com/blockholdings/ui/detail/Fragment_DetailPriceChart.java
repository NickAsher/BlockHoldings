package apps.yoo.com.blockholdings.ui.detail;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Message;
import apps.yoo.com.blockholdings.util.Utils;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;


public class Fragment_DetailPriceChart extends Fragment {
    Context context ;
    String LOG_TAG = "Fragment_DetailPriceChart --> " ;
    AppDatabase db;

    LineChart chart;
    RadioRealButtonGroup radioGroup_ChartInterval ;
    RecyclerView rv_ProjectLinks ;
    TextView tv_CoinDescription, tv_CoinRank, tv_CoinMarketCap, tv_CoinTotalVolume, tv_High24H, tv_Low424H,
            tv_totalSupply, tv_CurrentSupply, tv_ChangePercentage ;
    ExpandableTextView expandableTextView_CoinDescription ;

    String coinId ;
    Object_Coin currentCoin ;
    Object_Currency currentCurrency ;
    NestedScrollView nsv_Main ;
    ViewPager viewPagerContainer_from_ParentActivity;
    RVAdapter_ProjectLinks adapter_projectLinks ;
    String dataString ;
    List<Object_ProjectLink> listOfProjectLinks ;


    public Fragment_DetailPriceChart() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coinId = getArguments().getString("coinId") ;
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_price, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity() ;
        db = AppDatabase.getInstance(getActivity().getApplicationContext()) ;

        getReferences(view) ;

        currentCoin = db.coinDao().getCoinById(coinId) ;
        currentCurrency = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()) ;
        getCoinData() ;
//        setupBasicUI() ;
        setRadioGroup_ChartInterval_ChangedListener();
        setupRecyclerView_ProjectLinks();


    }

    private void getReferences(View itemView){
        nsv_Main = itemView.findViewById(R.id.frgDetailChart_NSV_Main) ;
        chart = (LineChart)itemView.findViewById(R.id.frgDetailPrice_LineChart_Main);
        radioGroup_ChartInterval = itemView.findViewById(R.id.frgDetailChart_RadioGroup_ChartInterval) ;
        viewPagerContainer_from_ParentActivity = getActivity().findViewById(R.id.activityDetail_ViewPager_Main) ;

        expandableTextView_CoinDescription = itemView.findViewById(R.id.frgDetailChart_ExpandableTextView_CoinDescription) ;
//        tv_CoinDescription = itemView.findViewById(R.id.frgDetailChart_TextView_CoinDescription);
        tv_CoinRank = itemView.findViewById(R.id.frgDetailChart_TXTV_ValRank);
        tv_CoinMarketCap = itemView.findViewById(R.id.frgDetailChart_TXTV_ValMarketCap);
        tv_CoinTotalVolume = itemView.findViewById(R.id.frgDetailChart_TXTV_ValVolume24H);
        tv_High24H = itemView.findViewById(R.id.frgDetailChart_TXTV_ValHigh24H);
        tv_Low424H = itemView.findViewById(R.id.frgDetailChart_TXTV_ValLow24H);
        tv_totalSupply = itemView.findViewById(R.id.frgDetailChart_TXTV_ValTotalSupply);
        tv_CurrentSupply = itemView.findViewById(R.id.frgDetailChart_TXTV_ValCirculatingSupply);
        tv_ChangePercentage = itemView.findViewById(R.id.frgDetailChart_TXTV_ValPercentageChange);



        rv_ProjectLinks = itemView.findViewById(R.id.frgDetailChart_RV_ProjectLinks);


    }

    private void getCoinData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.getURL_APICALL_COIN_DESCRIPTIONDATA(coinId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(LOG_TAG, "Response from server is " + response) ;
                        Object_Coin.insertIntoDB_FullCoinData(response, context, db, currentCurrency.getCurrencyId(), currentCoin) ;
                        setupBasicUI();
                        listOfProjectLinks = currentCoin.getlistOfLinks();
                        adapter_projectLinks.refreshData(listOfProjectLinks);
// processData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in making volley request");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        }) ;

        RequestQueue requestQueue = Volley.newRequestQueue(context) ;
        requestQueue.add(stringRequest) ;


    }

    private void setupBasicUI(){

        expandableTextView_CoinDescription.setText(Html.fromHtml(currentCoin.getCoinDescription().replaceAll("\\r\\n", "<br>")));

        Animation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.1f);
        mAnimation.setDuration(350); //350 millisecond to complete one round of animation i.e. from up-to-down or down-to-up
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new AccelerateInterpolator());
        expandableTextView_CoinDescription.getChildAt(1).setAnimation(mAnimation);

        tv_CoinRank.setText("" + currentCoin.getRank());
        tv_CoinMarketCap.setText(currentCurrency.getCurrencySymbol() + Utils.formatNumber_ie_MarketCap(currentCoin.getMarketCap()));
        tv_CoinTotalVolume.setText(currentCurrency.getCurrencySymbol() + Utils.formatNumber_ie_MarketCap(currentCoin.getTotalVolume()));
        tv_High24H.setText(currentCurrency.getCurrencySymbol() + Utils.formatNumber_ie_SingleCoinPriceCurrency(currentCoin.getHigh24H()));
        tv_Low424H.setText(currentCurrency.getCurrencySymbol() + Utils.formatNumber_ie_SingleCoinPriceCurrency(currentCoin.getLow24H()));
        tv_totalSupply.setText(Utils.formatNumber_ie_Commas(currentCoin.getTotalSupply()));
        tv_CurrentSupply.setText(Utils.formatNumber_ie_Commas(currentCoin.getCirculatingSupply()));


        String percentageChange24H = currentCoin.getPercentageChange_24H() ;
        String percentageChange1W = currentCoin.getPercentageChange_1W() ;

        StringBuffer buffer = new StringBuffer("24H ") ;
        if(percentageChange24H.charAt(0) == '-'){
            buffer.append("<font color = '#f44336'>" + Utils.formatNumber_ie_PercentageChange(percentageChange24H.substring(1)) + " % </font>") ;
        } else {
            buffer.append("<font color = '#4caf50'>" + Utils.formatNumber_ie_PercentageChange(percentageChange24H) + " % </font>") ;

        }
        buffer.append(" &nbsp; &nbsp; &nbsp; 7D ") ;
        if(percentageChange1W.charAt(0) == '-'){
            buffer.append("<font color = '#f44336'>" + Utils.formatNumber_ie_PercentageChange(percentageChange1W.substring(1)) + " % </font>") ;
        } else {
            buffer.append("<font color = '#4caf50'>" + Utils.formatNumber_ie_PercentageChange(percentageChange1W) + " % </font>") ;
        }

        tv_ChangePercentage.setText(Html.fromHtml(buffer.toString())) ;




    }

    private void setRadioGroup_ChartInterval_ChangedListener(){

        radioGroup_ChartInterval.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                switch (button.getId()){
                    case R.id.frgDetailChart_RadioBtn_Interval1Day :
                        getDataFromServer_ForChart("1", ChartValueFormatter_XAxis.TYPE_1DAY);
                        break;
                    case R.id.frgDetailChart_RadioBtn_Interval3Day :
                        getDataFromServer_ForChart("3", ChartValueFormatter_XAxis.TYPE_3DAY);
                        break;
                    case R.id.frgDetailChart_RadioBtn_Interval1Week :
                        getDataFromServer_ForChart("7", ChartValueFormatter_XAxis.TYPE_WEEK);
                        break;
                    case R.id.frgDetailChart_RadioBtn_Interval1Month :
                        getDataFromServer_ForChart("31", ChartValueFormatter_XAxis.TYPE_MONTH);
                        break;
                    case R.id.frgDetailChart_RadioBtn_Interval6Month :
                        getDataFromServer_ForChart("183", ChartValueFormatter_XAxis.TYPE_6MONTH);
                        break;
                    case R.id.frgDetailChart_RadioBtn_Interval1Year :
                        getDataFromServer_ForChart("365", ChartValueFormatter_XAxis.TYPE_YEAR);
                        break;
                    case R.id.frgDetailChart_RadioBtn_IntervalMax :
                        getDataFromServer_ForChart("max", ChartValueFormatter_XAxis.TYPE_MAX);
                        break;
                }
            }
        });

        radioGroup_ChartInterval.setPosition(6);

        getDataFromServer_ForChart("max", ChartValueFormatter_XAxis.TYPE_MAX);

    }

    private void getDataFromServer_ForChart(String noOfDays, final int xAxis_ValueFormatterType){
        String url = Constants.getURL_APICALL_HISTORICAL_DATACHART(coinId, noOfDays, MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencyId()) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            setupChart(Helper_Chart.getFormattedList_from_ResponseData(response), xAxis_ValueFormatterType);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message.display(context, "Error in making volley request");
                Log.e(LOG_TAG, error.toString() ) ;
            }
        }) ;
        RequestQueue requestQueue = Volley.newRequestQueue(context) ;
        requestQueue.add(stringRequest) ;
    }

    private void setupChart(List<Entry> entries, int xAxis_ValueFormatterType){
        Log.e(LOG_TAG, entries.toString()) ;


        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setHighLightColor(ContextCompat.getColor(context, R.color.black_Priamry)); // set color of indicator
        dataSet.setDrawHorizontalHighlightIndicator(false); // remove the horizontal line from indicator
        dataSet.setColor(ContextCompat.getColor(context, R.color.green)); // change the color of line
        dataSet.setDrawCircles(false); // remove the circle of each point on chart
        dataSet.setLineWidth(1.8f); /// change the thickness of line

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);   // remove the text value of each point on chart


        chart.setData(lineData);
        chart.setScaleEnabled(false);



        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(3f);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.teal));
        xAxis.setDrawAxisLine(false  );
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ChartValueFormatter_XAxis(xAxis_ValueFormatterType));

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextSize(10f);
        yAxisLeft.setTextColor(ContextCompat.getColor(context, R.color.teal));
        yAxisLeft.setDrawGridLines(true); // no grid lines
        yAxisLeft.setDrawZeroLine(true); // draw a zero line

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);




        chart.getLegend().setEnabled(false);




        ChartMarkerView mv = new ChartMarkerView(context, R.layout.marker_chartview);

        // Set the marker to the chart
        mv.setChartView(chart);
        chart.setMarker(mv);

        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        nsv_Main.requestDisallowInterceptTouchEvent(true);

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        nsv_Main.requestDisallowInterceptTouchEvent(false);

                        chart.highlightValue(null);

                        break;
                    }


                }

                return false;
            }
        });

        chart.highlightValue(null);
        chart.invalidate();
    }


    private void setupRecyclerView_ProjectLinks(){
        listOfProjectLinks = currentCoin.getlistOfLinks();
        adapter_projectLinks = new RVAdapter_ProjectLinks(context, listOfProjectLinks) ;
        GridLayoutManager gd = new GridLayoutManager(context, 2) ;

        gd.setAutoMeasureEnabled(true);
        rv_ProjectLinks.setLayoutManager(gd);
        rv_ProjectLinks.setNestedScrollingEnabled(false);
        rv_ProjectLinks.setAdapter(adapter_projectLinks);
    }
}
