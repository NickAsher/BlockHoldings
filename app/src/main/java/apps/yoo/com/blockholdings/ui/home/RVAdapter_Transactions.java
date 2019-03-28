package apps.yoo.com.blockholdings.ui.home;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.ui.detail.Activity_Detail;
import apps.yoo.com.blockholdings.util.Utils;


public class RVAdapter_Transactions extends RecyclerView.Adapter<RVAdapter_Transactions.YoloCartViewHolder>{
    private List<Object_TransactionFullData> listOfitems ;
    private Context context ;
    String currencySymbol ;
    Map<String, BigDecimal> listOf_PriceTimeAgo;

    public final static int PRICE_CHANGE_DURATION_1H = 1 ;
    public final static int PRICE_CHANGE_DURATION_24H = 2 ;
    public final static int PRICE_CHANGE_DURATION_7D = 3 ;
    public final static int PRICE_CHANGE_DURATION_ALL = 4 ;

    int currentPriceChangeDuation  ;


    public RVAdapter_Transactions(Context context, List<Object_TransactionFullData> listOfitems, Map<String, BigDecimal> holdingsChangeList) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        this.currencySymbol = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencySymbol() ;
        this.listOf_PriceTimeAgo = holdingsChangeList ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_transaction,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_TransactionFullData currentItem = listOfitems.get(position) ;


        Glide.with(context).load(currentItem.getCoinObject().getImageLogoLink()).into(holder.imageView_Link) ;

        holder.textView_Name.setText(currentItem.getCoinObject().getName());
        holder.textView_SingleCoinPrice.setText(this.currencySymbol + Utils.showHumanDecimals(currentItem.getTransactionObject().getSingleCoinPrice_CurrencyCurrent() ));
        holder.textView_TotalCost.setText(this.currencySymbol + Utils.showHumanDecimals(currentItem.getTransactionObject().getTotalValue_Current() )) ;
        holder.textView_noOfCoins.setText(Utils.showHumanDecimals(currentItem.getTransactionObject().getNoOfCoins()) + " " + currentItem.getCoinObject().getSymbol());


        if(listOf_PriceTimeAgo.size() > 0) {


            final BigDecimal holdingChange = listOf_PriceTimeAgo.get(currentItem.getCoinObject().getId()) ;

            if (holdingChange.toPlainString().isEmpty()) {
                holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                holder.textView_24hChange.setText("Empty");
            } else {
                if (holdingChange.signum() >= 0) {
                    holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.green));
                    holder.textView_24hChange.setText(holdingChange.toPlainString());
                } else {
                    holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.red));
                    holder.textView_24hChange.setText(holdingChange.toPlainString());
                }


//            if(percentageChange.charAt(0) == '-'){
//                holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.red));
//                holder.textView_24hChange.setText(percentageChange.substring(1) + " %");
//            } else {
//                holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.green));
//                holder.textView_24hChange.setText(percentageChange + " %");
//            }
            }





//            final String priceTimeAgo = listOf_PriceTimeAgo.get(position);
//            if (priceTimeAgo.isEmpty()) {
//                holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.yellow));
//                holder.textView_24hChange.setText("Empty");
//            } else {
//                BigDecimal originalPrice = new BigDecimal(priceTimeAgo);
//                BigDecimal currentPrice = new BigDecimal(currentItem.getTransactionObject().getSingleCoinPrice_CurrencyCurrent());
//                BigDecimal priceDifference_InCurrency = currentPrice.subtract(originalPrice);
//                if (priceDifference_InCurrency.signum() >= 0) {
//                    holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.green));
//                    holder.textView_24hChange.setText(priceDifference_InCurrency.toPlainString());
//                } else {
//                    holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.red));
//                    holder.textView_24hChange.setText(priceDifference_InCurrency.toPlainString());
//                }
//
//
////            if(percentageChange.charAt(0) == '-'){
////                holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.red));
////                holder.textView_24hChange.setText(percentageChange.substring(1) + " %");
////            } else {
////                holder.textView_24hChange.setTextColor(ContextCompat.getColor(context, R.color.green));
////                holder.textView_24hChange.setText(percentageChange + " %");
////            }
//            }
        }






        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Detail.class) ;
                intent.putExtra("coinId", currentItem.getCoinObject().getId()) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                context.startActivity(intent);



            }
        });


    }

    public void refreshData(List<Object_TransactionFullData> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    public void refreshHoldingsChange(List<Object_TransactionFullData> newListOfItems, Map<String, BigDecimal> listOf_PriceTimeAgo){
        this.listOfitems = newListOfItems ;
        this.listOf_PriceTimeAgo = listOf_PriceTimeAgo ;
        notifyDataSetChanged();
    }

    public void showChange(int changeDuration){

    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout_Container ;
        TextView textView_Name, textView_SingleCoinPrice, textView_24hChange, textView_TotalCost, textView_noOfCoins  ;
        ImageView imageView_Link ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowTransaction_RelativeLayout_Main) ;
            textView_Name = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_CoinName) ;
            textView_SingleCoinPrice = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_SingleCoinPrice) ;
            textView_24hChange = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_24hChange) ;
            textView_TotalCost = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_TotalCost) ;
            textView_noOfCoins = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_NoOfCoins) ;

            imageView_Link = (ImageView) itemView.findViewById(R.id.singleRowTransaction_ImageView_CoinImage) ;

        }
    }
}








