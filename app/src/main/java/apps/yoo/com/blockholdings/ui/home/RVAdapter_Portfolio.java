package apps.yoo.com.blockholdings.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.Objects.Object_Transaction;
import apps.yoo.com.blockholdings.util.Constants;
import apps.yoo.com.blockholdings.util.Utils;


public class RVAdapter_Portfolio extends RecyclerView.Adapter<RVAdapter_Portfolio.YoloCartViewHolder>{
    private List<Object_Transaction> listOfitems ;
    private Context context ;

    public RVAdapter_Portfolio(Context context, List<Object_Transaction> listOfitems) {
        this.context = context ;
        this.listOfitems = listOfitems ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_portfolio,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_Transaction currentItem = listOfitems.get(position) ;

        Glide.with(context).load(Constants.URL_PREFIX_IMAGELOGO + currentItem.getCoinSymbol().toUpperCase() + ".png").into(holder.imageView_Link) ;
        holder.textView_Name.setText(currentItem.getCoinName());
        holder.textView_SingleCoinPrice.setText(Utils.showHumanDecimals(currentItem.getSingleCoinPrice_TradingPair() ));
        holder.textView_24hChange.setText(Utils.showHumanDecimals(currentItem.getSingleCoinPrice_TradingPair() ));
        holder.textView_TotalCost.setText(Utils.showHumanDecimals(currentItem.getTotalValue() )) ;
        holder.textView_noOfCoins.setText(Utils.showHumanDecimals(currentItem.getNoOfCoins()) + " " + currentItem.getCoinSymbol());


        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void refreshData(List<Object_Transaction> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
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
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowPortfolio_RelativeLayout_Main) ;
            textView_Name = (TextView) itemView.findViewById(R.id.singleRowPortfolio_TextView_CoinName) ;
            textView_SingleCoinPrice = (TextView) itemView.findViewById(R.id.singleRowPortfolio_TextView_SingleCoinPrice) ;
            textView_24hChange = (TextView) itemView.findViewById(R.id.singleRowPortfolio_TextView_24hChange) ;
            textView_TotalCost = (TextView) itemView.findViewById(R.id.singleRowPortfolio_TextView_TotalCost) ;
            textView_noOfCoins = (TextView) itemView.findViewById(R.id.singleRowPortfolio_TextView_NoOfCoins) ;

            imageView_Link = (ImageView) itemView.findViewById(R.id.singleRowPortfolio_ImageView_CoinImage) ;

        }
    }
}

