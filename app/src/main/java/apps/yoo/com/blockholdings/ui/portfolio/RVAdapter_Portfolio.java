package apps.yoo.com.blockholdings.ui.portfolio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;
import apps.yoo.com.blockholdings.util.Utils;


public class RVAdapter_Portfolio extends RecyclerView.Adapter<RVAdapter_Portfolio.YoloCartViewHolder>{
    private List<Object_Portfolio> listOfitems ;
    private Context context ;
    private static final String LOG_TAG = "RVAdapter_PortfolioNamesEdit --> " ;
    MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio listener ;
    int currentPortfolioId ;
    FragmentManager fragmentManager ;





    public RVAdapter_Portfolio(Context context, List<Object_Portfolio> listOfitems, MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio listener) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        currentPortfolioId = MyGlobals.getCurrentPortfolioObj().getPortfolioId() ;
        fragmentManager = ((FragmentActivity)context).getSupportFragmentManager() ;
        this.listener = listener ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_portfolio,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_Portfolio currentItem = listOfitems.get(position) ;
        holder.textView_PortfolioBalance.setText(Utils.showHumanDecimals(currentItem.getPortfolioValue()));
        holder.textView_PortfolioName.setText(currentItem.getPortfolioName());



        if(currentItem.getPortfolioId() == MyGlobals.getCurrentPortfolioObj().getPortfolioId()){
            holder.relativeLayout_Container.setBackgroundColor(Color.parseColor("#effaef"));
            holder.btn_Delete.setVisibility(View.INVISIBLE);
            holder.btn_Select.setColorFilter(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.relativeLayout_Container.setBackgroundColor(Color.parseColor("#00000000"));
            holder.btn_Delete.setVisibility(View.VISIBLE);
            holder.btn_Select.setColorFilter(null);
        }

        holder.btn_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectingPortfolio(currentItem.getPortfolioId());
            }
        });

        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_EditPortfolioName df = new DialogFragment_EditPortfolioName() ;
                Bundle bundle = new Bundle() ;
                bundle.putInt("portfolioId", currentItem.getPortfolioId()) ;
                df.setArguments(bundle);
                df.show(fragmentManager, "dfEditPortfolio");
            }
        });

        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                AlertDialog.Builder builder_DeleteButtonConfiramtion = new AlertDialog.Builder(context);
                builder_DeleteButtonConfiramtion.setMessage("Are you sure you want to delete this portfolio \n This action cannot be undone?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listOfitems.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, listOfitems.size());
                                listener.onDeletingPortfolio(currentItem.getPortfolioId()) ;
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog_DeleteButtonConfiramtion = builder_DeleteButtonConfiramtion.create();
                dialog_DeleteButtonConfiramtion.show();

            }
        });




//        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onSelectingPortfolio(currentItem.getPortfolioId());
//            }
//        });
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
        EditText editText_PortfolioEdit ;
        ImageView btn_Select, btn_Edit, btn_Delete ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowPortfolioNames_RelLt_Container) ;
            textView_PortfolioName = (TextView) itemView.findViewById(R.id.singleRowPortfolioEdit_TextView_PortfolioName) ;
            textView_PortfolioBalance  = (TextView) itemView.findViewById(R.id.singleRowPortfolioEdit_TextView_PortfolioBalance) ;
            editText_PortfolioEdit = itemView.findViewById(R.id.singleRowPortfolioEdit_EditText_PortfolioName);
            btn_Select = itemView.findViewById(R.id.singleRowPortfolioEdit_ImgBtn_SelectPortfolio);
            btn_Edit = itemView.findViewById(R.id.singleRowPortfolioEdit_ImgBtn_EditPortfolio);
            btn_Delete = itemView.findViewById(R.id.singleRowPortfolioEdit_ImgBtn_DeletePortfolio);



        }
    }
}

