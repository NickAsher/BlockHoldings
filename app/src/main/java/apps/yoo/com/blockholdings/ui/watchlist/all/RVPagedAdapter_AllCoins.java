package apps.yoo.com.blockholdings.ui.watchlist.all;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Coin;
import apps.yoo.com.blockholdings.ui.detail.Activity_Detail;
import apps.yoo.com.blockholdings.util.Utils;


public class RVPagedAdapter_AllCoins extends PagedListAdapter<Object_Coin, RVPagedAdapter_AllCoins.YoloCartViewHolder> {
    private Context context ;
    String currencySymbol ;
    String LOG_TAG = "RVAdapter_WatchlistAllCoins --> " ;

    private static DiffUtil.ItemCallback<Object_Coin> DIFF_COIN_CALLBACK = new DiffUtil.ItemCallback<Object_Coin>() {
        @Override
        public boolean areItemsTheSame(@NonNull Object_Coin oldItem, @NonNull Object_Coin newItem) {
            return oldItem.getId().equalsIgnoreCase(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Object_Coin oldItem, @NonNull Object_Coin newItem) {
            return oldItem.equals(newItem);
        }
    } ;

    public RVPagedAdapter_AllCoins(Context context) {
        super(DIFF_COIN_CALLBACK);
        this.context = context ;
        this.currencySymbol = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencySymbol() ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_watchlist_allcoins,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        try{


             final Object_Coin currentItem = getItem(position) ;
            JSONObject marketCachedData = new JSONObject(currentItem.getCachedData()) ;



            Glide.with(context).load(currentItem.getImageLogoLink()).into(holder.imageView_Link) ;

            holder.textView_Name.setText(currentItem.getName());
            holder.textView_SingleCoinPrice.setText(this.currencySymbol + Utils.formatNumber_ie_SingleCoinPriceCurrency(marketCachedData.getString("current_price")) );
            holder.textView_MarketCap.setText(this.currencySymbol + Utils.formatNumber_ie_MarketCap(marketCachedData.getString("market_cap")) ) ;
            holder.textView_CoinRank.setText("" + marketCachedData.getInt("market_cap_rank"));
            String percentageChange = marketCachedData.getString("price_change_percentage_7d_in_currency") ;

            boolean isChartGreen ;
            if(percentageChange.charAt(0) == '-'){
                isChartGreen = false ;
                holder.textView_percentageChange.setText(Html.fromHtml("<font color='#FFFFFF'> - " + new BigDecimal(percentageChange).setScale(2, RoundingMode.HALF_UP).toString().substring(1) + "%</font> <font color='#ee0979'><b>\u2193</b></font>"));
    //            holder.textView_percentageChange.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                isChartGreen = true ;
                holder.textView_percentageChange.setText(Html.fromHtml("<font color='#FFFFFF'> + " +new BigDecimal(percentageChange).setScale(2, RoundingMode.HALF_UP).toString() + "% </font>  <font color='#92FE9D'><b>\u2191</b></font>"));
    //            holder.textView_percentageChange.setTextColor(ContextCompat.getColor(context, R.color.green));

            }


            holder.lt_Container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Activity_Detail.class) ;
                    intent.putExtra("coinId", currentItem.getId()) ;
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                    context.startActivity(intent);

                }
            });

            List<Entry> listOfEntries = new ArrayList<>() ;

            JSONArray sparklineArray = marketCachedData.getJSONArray("sparkline_in_7d") ;

            for(int i = 0; i<sparklineArray.length(); i++){
                listOfEntries.add(new Entry(i, (float) sparklineArray.getDouble(i))) ;
            }




            LineDataSet dataSet = new LineDataSet(listOfEntries, ""); // add entries to dataset
            dataSet.setDrawHorizontalHighlightIndicator(false); // remove the horizontal line from indicator
            dataSet.setDrawCircles(false); // remove the circle of each point on chart
            dataSet.setLineWidth(1.0f); /// change the thickness of line
            dataSet.setDrawFilled(true);

            if(isChartGreen){

                Paint paint = holder.chart.getRenderer().getPaintRender();
                int width = context.getResources().getDisplayMetrics().widthPixels/3 ;
    //
                LinearGradient linGrad = new LinearGradient(0, 0, width, 0,
                        ContextCompat.getColor(context, R.color.positive1),
                        ContextCompat.getColor(context, R.color.positive2),
                        Shader.TileMode.REPEAT);
                paint.setShader(linGrad);


                dataSet.setFillDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_fade_green));

                } else {
                Paint paint = holder.chart.getRenderer().getPaintRender();
                int width = context.getResources().getDisplayMetrics().widthPixels/3 ;
    //
                LinearGradient linGrad = new LinearGradient(0, 0, width, 0,
                        ContextCompat.getColor(context, R.color.negative1),
                        ContextCompat.getColor(context, R.color.negative2),
                        Shader.TileMode.REPEAT);
                paint.setShader(linGrad);

                dataSet.setFillDrawable(ContextCompat.getDrawable(context, R.drawable.gradient_fade_red));
            }


            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);   // remove the text value of each point on chart


            holder.chart.setData(lineData);
            holder.chart.setScaleEnabled(false);
            holder.chart.invalidate();
        }catch (Exception e){
            Log.e(LOG_TAG, e.toString()) ;
        }

    }

    private void bindChart(final YoloCartViewHolder holder, final Object_Coin currentItem){


    }



    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        CardView lt_Container ;
        LineChart chart ;
        TextView textView_Name, textView_SingleCoinPrice, textView_MarketCap, textView_percentageChange, textView_CoinRank  ;
        ImageView imageView_Link ;
        FrameLayout lt_ChartContainer ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            lt_Container= itemView.findViewById(R.id.singleRowWatchlistAllCoin_Lt_Parent) ;

            textView_Name = (TextView) itemView.findViewById(R.id.singleRowWatchlistAllCoin_TV_CoinName) ;
            textView_SingleCoinPrice = (TextView) itemView.findViewById(R.id.singleRowWatchlistAllCoin_TV_SingleCoinPrice) ;
            textView_MarketCap = (TextView) itemView.findViewById(R.id.singleRowWatchlistAllCoin_TV_MarketCap) ;
            textView_percentageChange = (TextView) itemView.findViewById(R.id.singleRowWatchlistAllCoin_TV_PriceChangePercentage) ;
            textView_CoinRank =   itemView.findViewById(R.id.singleRowWatchlistAllCoin_TV_CoinRank) ;
            imageView_Link = (ImageView) itemView.findViewById(R.id.singleRowWatchlistAllCoin_ImageV_CoinImage) ;
            chart = itemView.findViewById(R.id.singleRowWatchlistAllCoin_LineChart_Main) ;
            lt_ChartContainer = itemView.findViewById(R.id.singleRowWatchlistAllCoin_FrameLt_ChartContainer) ;

            setupChart() ;

        }

        private void setupChart(){
            chart.setTouchEnabled(false);
//            chart.setExtraOffsets(0, 0, 0, 0);
            chart.setViewPortOffsets(0,0,0,0);
            chart.fitScreen();
            chart.setScaleEnabled(false);
            chart.getXAxis().setEnabled(false);
            chart.getAxisLeft().setEnabled(false);
            chart.getAxisRight().setEnabled(false);
            chart.getDescription().setEnabled(false);



//            XAxis xAxis = chart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTextSize(3f);
//            xAxis.setTextColor(ContextCompat.getColor(context, R.color.teal));
//            xAxis.setDrawAxisLine(false);
//            xAxis.setDrawGridLines(false);
//
//            xAxis.setDrawLabels(false);

//            YAxis yAxisLeft = chart.getAxisLeft();
//            yAxisLeft.setTextSize(10f);
//            yAxisLeft.setTextColor(ContextCompat.getColor(context, R.color.teal));
//            yAxisLeft.setDrawGridLines(false); // no grid lines
//            yAxisLeft.setDrawZeroLine(false); // draw a zero line
//            yAxisLeft.setDrawAxisLine(false);
//            yAxisLeft.setDrawLabels(false);
//
//            YAxis yAxisRight = chart.getAxisRight();
//            yAxisRight.setDrawAxisLine(false);
//            yAxisRight.setDrawLabels(false);
//            yAxisRight.setDrawGridLines(false);




//         // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);
//
//        // draw legend entries as lines
//        l.setForm(Legend.LegendForm.LINE);





        }
    }
}








