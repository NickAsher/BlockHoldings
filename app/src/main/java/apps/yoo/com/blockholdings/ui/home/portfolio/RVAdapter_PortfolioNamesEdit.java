package apps.yoo.com.blockholdings.ui.home.portfolio;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.models.Object_Portfolio;
import apps.yoo.com.blockholdings.util.MyGlobals;
import apps.yoo.com.blockholdings.util.MyListener;
import apps.yoo.com.blockholdings.util.Utils;


public class RVAdapter_PortfolioNamesEdit extends RecyclerView.Adapter<RVAdapter_PortfolioNamesEdit.YoloCartViewHolder>{
    private List<Object_Portfolio> listOfitems ;
    private Context context ;
    private static final String LOG_TAG = "RVAdapter_PortfolioNamesEdit --> " ;
    MyListener.RVAdapter_PortfolioNames_to_DialogFragmentPortfolio listener ;
    int currentPortfolioId ;
    FragmentManager fragmentManager ;
    DialogFragment_Portfolio parentFragmentInstance ;



    public RVAdapter_PortfolioNamesEdit(Context context, List<Object_Portfolio> listOfitems,DialogFragment_Portfolio parentFragmentInstance) {
        this.context = context ;
        this.listOfitems = listOfitems ;
        currentPortfolioId = MyGlobals.getCurrentPortfolioObj().getPortfolioId() ;
        fragmentManager = ((FragmentActivity)context).getSupportFragmentManager() ;
        this.parentFragmentInstance = parentFragmentInstance ;
        listener = parentFragmentInstance ;


    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_portfolioedit,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_Portfolio currentItem = listOfitems.get(position) ;
        holder.textView_PortfolioBalance.setText(Utils.showHumanDecimals(currentItem.getPortfolioValue()));
        holder.textView_PortfolioName.setText(currentItem.getPortfolioName());

        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_EditPortfolioName df = new DialogFragment_EditPortfolioName() ;
                Bundle bundle = new Bundle() ;
                bundle.putInt("portfolioId", currentItem.getPortfolioId()) ;
                df.setArguments(bundle);
                df.setTargetFragment(parentFragmentInstance, 0);
                df.show(fragmentManager, "dfEditPortfolio");
            }
        });

        if(currentItem.getPortfolioId() == MyGlobals.getCurrentPortfolioObj().getPortfolioId()){
            holder.btn_Delete.setVisibility(View.GONE);
        }
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                listOfitems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listOfitems.size());
                listener.onDeletingPortfolio(currentItem.getPortfolioId()) ;
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
        ImageView btn_Edit, btn_Delete ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowPortfolioNames_RelLt_Container) ;
            textView_PortfolioName = (TextView) itemView.findViewById(R.id.singleRowPortfolioEdit_TextView_PortfolioName) ;
            textView_PortfolioBalance  = (TextView) itemView.findViewById(R.id.singleRowPortfolioEdit_TextView_PortfolioBalance) ;
            editText_PortfolioEdit = itemView.findViewById(R.id.singleRowPortfolioEdit_EditText_PortfolioName);
            btn_Edit = itemView.findViewById(R.id.singleRowPortfolioEdit_ImgBtn_EditPortfolio);
            btn_Delete = itemView.findViewById(R.id.singleRowPortfolioEdit_ImgBtn_DeletePortfolio);


        }
    }
}

