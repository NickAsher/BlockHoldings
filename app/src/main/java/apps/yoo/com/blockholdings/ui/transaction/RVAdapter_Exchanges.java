package apps.yoo.com.blockholdings.ui.transaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.Objects.Object_Coin;
import apps.yoo.com.blockholdings.data.Objects.Object_Exchange;
import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.MyListener;


public class RVAdapter_Exchanges extends RecyclerView.Adapter<RVAdapter_Exchanges.YoloCartViewHolder>{
    private List<Object_Exchange> listOfitems ;
    private Context context ;
    MyListener.RVAdapterExchange_to_DialogFragmentExchange listener ;


    public RVAdapter_Exchanges(Context context, List<Object_Exchange> listOfitems, MyListener.RVAdapterExchange_to_DialogFragmentExchange listener) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        this.listener = listener ;

    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_exchanges,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_Exchange currentItem = listOfitems.get(position) ;

        Glide.with(context).load( currentItem.getImageLink()).into(holder.imageView_Link) ;
        holder.textView_Name.setText(currentItem.getName());

        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper_Transaction.getTransactionObject().setExchangeId(currentItem.getId());
                Helper_Transaction.getTransactionObject().setExchangeName(currentItem.getName());
                listener.closeDialog();
            }
        });
    }

    public void refreshData(List<Object_Exchange> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout_Container ;
        TextView textView_Name ;
        ImageView imageView_Link ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowExchanges_RelativeLayout_Main) ;
            textView_Name = (TextView) itemView.findViewById(R.id.singleRowExchanges_TextView_CoinName) ;
            imageView_Link = (ImageView) itemView.findViewById(R.id.singleRowExchanges_ImageView_CoinImage) ;

        }
    }
}

