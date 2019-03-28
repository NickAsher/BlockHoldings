package apps.yoo.com.blockholdings.ui.detail;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;


public class RVAdapter_DetailUpdates extends RecyclerView.Adapter<RVAdapter_DetailUpdates.YoloCartViewHolder>{
    private JSONArray listOfitems ;
    private Context context ;
    String currencySymbol ;
    private static final String LOG_TAG = "RVAdapter_DetailUpdates --> " ;

    public RVAdapter_DetailUpdates(Context context, JSONArray listOfitems) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        this.currencySymbol = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencySymbol() ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_detail_updates,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        try {
            JSONArray data = listOfitems.getJSONArray(position) ;
            String price = data.getString(0) ;
            long timeInLong = data.getLong(1) ;
            String updater = data.getString(2) ;

            holder.textView_CoinPrice.setText(currencySymbol + price) ;
            holder.textView_updateTime.setText(new SimpleDateFormat("MMM-dd-yyyy, hh:mm:ss a").format(new Date(timeInLong))) ;
            holder.textView_Updater.setText(updater) ;


        }catch (JSONException e){
            Log.e(LOG_TAG, e.toString()) ;
        }


    }

    public void refreshData(JSONArray newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.length();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout lt_Container ;
        TextView textView_CoinPrice, textView_updateTime, textView_Updater ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            lt_Container = itemView.findViewById(R.id.frgDetailUpdates_Lt_Parent) ;
            textView_CoinPrice = (TextView) itemView.findViewById(R.id.frgDetailUpdates_TV_ValueCoinPrice) ;
            textView_updateTime = (TextView) itemView.findViewById(R.id.frgDetailUpdates_TV_ValueUpdateTime) ;
            textView_Updater = (TextView) itemView.findViewById(R.id.frgDetailUpdates_TV_ValueUpdater) ;



        }
    }
}








