package apps.yoo.com.blockholdings.ui.detail;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_Transaction;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.ui.transaction.Activity_DetailTransactionEdit;
import apps.yoo.com.blockholdings.util.Utils;


public class RVAdapter_DetailTransactions extends RecyclerView.Adapter<RVAdapter_DetailTransactions.YoloCartViewHolder>{
    private List<Object_TransactionFullData> listOfitems ;
    private Context context ;
    String currencySymbol ;

    public RVAdapter_DetailTransactions(Context context, List<Object_TransactionFullData> listOfitems) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        this.currencySymbol = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencySymbol() ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_detail_transaction,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_TransactionFullData currentItem = listOfitems.get(position);
        final Object_Transaction currentItemTransaction = currentItem.getTransactionObject();


        if (currentItemTransaction.getType() == Object_Transaction.TYPE_BUY) {
            holder.textView_TxnType.setText("BUY") ;
            holder.textView_DescriptionAmount.setText("Amount Bought") ;
            holder.textView_DescriptionSingleCoinPrice.setText("Buy Price") ;
        } else if (currentItemTransaction.getType() == Object_Transaction.TYPE_SELL){
            holder.textView_TxnType.setText("SELL") ;
            holder.textView_DescriptionAmount.setText("Amount Sold") ;
            holder.textView_DescriptionSingleCoinPrice.setText("Sell Price") ;
        }




        holder.textView_MarketName.setText(currentItem.getExchangeObject().getName()) ;
        holder.textView_Date.setText(new SimpleDateFormat().format(currentItemTransaction.getTransactionDateTime()).toString()) ;
        holder.textView_Notes.setText(currentItemTransaction.getNote()) ;


        if(currentItemTransaction.getTradingPair().isEmpty()){
            holder.textView_TradingPair.setText("N/A") ;

        }else{
            holder.textView_TradingPair.setText(currentItem.getCoinObject().getSymbol() + "/" + currentItemTransaction.getTradingPair()) ;

        }
        holder.textView_Amount.setText(currentItemTransaction.getNoOfCoins()) ;
        holder.textView_SingleCoinPrice_BuyPrice.setText(currentItemTransaction.getSingleCoinPrice_CurrencyOriginal()) ;

        holder.textView_TotalCostOriginal.setText(currentItemTransaction.getTotalValue_Original()) ;
        holder.textView_TotalCostCurrent.setText(currentItemTransaction.getTotalValue_Current()) ;


        // calculating percentage change between two
        BigDecimal totalCostOriginal = new BigDecimal(currentItemTransaction.getSingleCoinPrice_CurrencyOriginal()) ;
        BigDecimal totalCostCurrent= new BigDecimal(currentItemTransaction.getSingleCoinPrice_CurrencyCurrent()) ;
        String percentageChange = Utils.showHumanDecimals(((totalCostCurrent.subtract(totalCostOriginal)).divide(totalCostOriginal, 5, RoundingMode.HALF_UP)).multiply(new BigDecimal(100)).toString()) ;

        if(percentageChange.charAt(0) == '-'){
            holder.textView_PercentageChange.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.textView_PercentageChange.setText(percentageChange.substring(1) + " %");
        } else {
            holder.textView_PercentageChange.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.textView_PercentageChange.setText(percentageChange + " %");
        }



        holder.linLt_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_DetailTransactionEdit.class) ;
                intent.putExtra("transactionId", currentItemTransaction.getTransactionNo()) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                context.startActivity(intent);
            }
        });








    }

    public void refreshData(List<Object_TransactionFullData> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linLt_Container ;
        TextView textView_TxnType, textView_MarketName, textView_Date, textView_TradingPair, textView_DescriptionAmount,
                textView_Amount, textView_PercentageChange, textView_DescriptionSingleCoinPrice,
                textView_SingleCoinPrice_BuyPrice, textView_TotalCostOriginal, textView_TotalCostCurrent, textView_Notes;


        public YoloCartViewHolder(View itemView) {
            super(itemView);
            linLt_Container = itemView.findViewById(R.id.singleRowDetailTransaction_LinLt_MainContainer) ;
            textView_TxnType = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueTxnType) ;
            textView_MarketName = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueMarketName) ;
            textView_Date = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueDate) ;
            textView_TradingPair = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueTradingPair) ;
            textView_DescriptionAmount = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_DescriptionAmount) ;
            textView_Amount = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueAmount) ;
            textView_PercentageChange = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValuePercentageChange) ;
            textView_DescriptionSingleCoinPrice = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_DescriptionSingleCoinPrice) ;
            textView_SingleCoinPrice_BuyPrice = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueSingleCoinPrice) ;
            textView_TotalCostOriginal = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueOriginalTotalCost) ;
            textView_TotalCostCurrent = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueCurrentTotalCost) ;
            textView_Notes = (TextView) itemView.findViewById(R.id.singleRowDetailTransaction_TextView_ValueNotes) ;



        }
    }
}








