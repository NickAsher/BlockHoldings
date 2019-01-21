package apps.yoo.com.blockholdings.ui.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.util.MyListener;


public class RVAdapter_TradingPair extends RecyclerView.Adapter<RVAdapter_TradingPair.YoloCartViewHolder>{
    private List<String> listOfitems ;
    private Context context ;
    MyListener.RVAdapterTradingPair_to_DialogFragmentTradingPair listener ;


    public RVAdapter_TradingPair(Context context, List<String> listOfitems, MyListener.RVAdapterTradingPair_to_DialogFragmentTradingPair listener) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        this.listener = listener ;

    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_tradingpair,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {

        holder.textView_PairName.setText(Helper_Transaction.getTransactionObject().getCoinSymbol() + "/" + listOfitems.get(position));
        holder.textView_PairName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper_Transaction.getTransactionObject().setTradingPair(listOfitems.get(position));
                listener.closeDialog();
            }
        });
    }

    public void refreshData(List<String> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        TextView textView_PairName ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            textView_PairName = (TextView) itemView.findViewById(R.id.singleRowTradingPair_TextView_Pairame) ;

        }
    }
}

