package apps.yoo.com.blockholdings.ui.settings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.util.MyListener;


public class RVAdapter_Currency extends RecyclerView.Adapter<RVAdapter_Currency.YoloCartViewHolder>{
    private List<Object_Currency> listOfitems ;
    private Context context ;
    MyListener.RVAdapterCurrency_to_DialogFragmentCurrency listener ;


    public RVAdapter_Currency(Context context, List<Object_Currency> listOfitems, MyListener.RVAdapterCurrency_to_DialogFragmentCurrency listener) {
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
        final Object_Currency currentItem = listOfitems.get(position) ;

        holder.textView.setText(currentItem.getCurrencyName() + " (" +currentItem.getCurrencySymbol() + ") ");
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySharedPreferences.setCurrency_InPreferences((context.getApplicationContext()), currentItem);
                listener.onCurrencySelected(currentItem);
            }
        });
    }

    public void refreshData(List<Object_Currency> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        TextView textView ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.singleRowTradingPair_TextView_Pairame) ;

        }
    }
}

