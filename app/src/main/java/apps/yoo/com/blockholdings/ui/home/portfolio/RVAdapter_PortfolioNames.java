package apps.yoo.com.blockholdings.ui.home.portfolio;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.AppDatabase;
import apps.yoo.com.blockholdings.data.models.Object_Currency;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;
import apps.yoo.com.blockholdings.util.Utils;


public class RVAdapter_PortfolioNames extends RecyclerView.Adapter<RVAdapter_PortfolioNames.YoloCartViewHolder>{
    private List<Object_Portfolio> listOfitems ;
    private Context context ;
    MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio listener ;
    int currentPortfolioId ;
    AppDatabase db ;
    Object_Currency currencyObj;


    public RVAdapter_PortfolioNames(Context context, List<Object_Portfolio> listOfitems, MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio listener, Object_Currency currency) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        this.listener = listener ;
        currentPortfolioId = MyGlobals.getCurrentPortfolioObj().getPortfolioId() ;
        db = AppDatabase.getInstance(context.getApplicationContext()) ;
        this.currencyObj = currency ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_portfolionames,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_Portfolio currentItem = listOfitems.get(position) ;

        holder.textView_PortfolioBalance.setText(currencyObj.getCurrencySymbol() + Utils.showHumanDecimals(currentItem.getPortfolioValue()));
        holder.textView_PortfolioName.setText(currentItem.getPortfolioName());
        if(currentItem.getPortfolioId() == currentPortfolioId){
            holder.radioButton_IsSelected.setChecked(true);
        }


        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectingPortfolio(currentItem.getPortfolioId());
            }
        });
    }

    public void refreshData(List<Object_Portfolio> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout_Container ;
        TextView textView_PortfolioName, textView_PortfolioBalance ;
        RadioButton radioButton_IsSelected ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowPortfolioNames_RelLt_Container) ;
            textView_PortfolioName = (TextView) itemView.findViewById(R.id.singleRowPortfolioNames_TextView_PortfolioName) ;
            textView_PortfolioBalance  = (TextView) itemView.findViewById(R.id.singleRowPortfolioNames_TextView_PortfolioBalance) ;
            radioButton_IsSelected  = itemView.findViewById(R.id.singleRowPortfolioNames_RadioButton_IsCurrentPortfolio) ;

        }
    }
}

