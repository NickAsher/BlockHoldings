package apps.yoo.com.blockholdings.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.data.MySharedPreferences;
import apps.yoo.com.blockholdings.data.models.Object_TransactionFullData;
import apps.yoo.com.blockholdings.data.models.Object_TransactionGroup;
import apps.yoo.com.blockholdings.ui.detail.Activity_Detail;
import apps.yoo.com.blockholdings.util.Utils;

class RVAdapter_TransactionsExpandable
        extends AbstractExpandableItemAdapter<RVAdapter_TransactionsExpandable.MyGroupViewHolder, RVAdapter_TransactionsExpandable.MyChildViewHolder> {
    private static final String LOG_TAG = "RVAdapter_TransactionsExpandable --> ";

    String currencySymbol ;
    ArrayList<Object_TransactionGroup> listOfTransactionGroups ;
    Context context ;
    RecyclerViewExpandableItemManager RVItemManager ;


    public RVAdapter_TransactionsExpandable(Context context, ArrayList<Object_TransactionGroup> listOfTransactionGroups, RecyclerViewExpandableItemManager RVItemManager ){
        this.context = context ;
        this.listOfTransactionGroups = listOfTransactionGroups ;
        this.RVItemManager = RVItemManager ;
        this.currencySymbol = MySharedPreferences.getCurrencyObj_FromPreference(context.getApplicationContext()).getCurrencySymbol() ;
        setHasStableIds(true);
    }


    @Override
    @NonNull
    public MyGroupViewHolder onCreateGroupViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(context).inflate(R.layout.singlerow_transaction, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    @NonNull
    public MyChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(context).inflate(R.layout.singlerow_transaction_child, parent, false);
        return new MyChildViewHolder(v);
    }


    @Override
    public int getGroupCount() {
        Log.e(LOG_TAG, "Size is " + listOfTransactionGroups.size()) ;
        return listOfTransactionGroups.size() ;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return listOfTransactionGroups.get(groupPosition).getListOfChildTransactionsFD().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        //Here we need to return a Unique GroupId
        // So we return the transactionNo
        // The transaction whose TransactionNo is returned is first transaction from list in the group

        return listOfTransactionGroups.get(groupPosition).getListOfChildTransactionsFD().iterator().next()
                .getTransactionObject().getTransactionId() ;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // Here we will need a child Id
        // Since Each coin in a listOfChildTransactions will have the same coinName
        // and thus the same child Id,
        // We can use the transactionId here
        return listOfTransactionGroups.get(groupPosition).getListOfChildTransactionsFD().get(childPosition).getTransactionObject().getTransactionId() ;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }




    @Override
    public void onBindGroupViewHolder(@NonNull MyGroupViewHolder holder, final int groupPosition, int viewType) {

        final Object_TransactionGroup currentItem = listOfTransactionGroups.get(groupPosition) ;
        final Object_TransactionFullData anyChildTransaction = currentItem.getListOfChildTransactionsFD().iterator().next() ;


        Glide.with(context).load(anyChildTransaction.getCoinObject().getImageLogoLink()).into(holder.imageView_Link) ;

        holder.textView_Name.setText(anyChildTransaction.getCoinObject().getName());
        holder.textView_SingleCoinPrice.setText(this.currencySymbol + Utils.showHumanDecimals(currentItem.getSingleCoinPrice_CurrencyCurrent() ));
        holder.textView_noOfCoins.setText(Utils.showHumanDecimals(currentItem.getNoOfCoins()) + " " + anyChildTransaction.getCoinObject().getSymbol());

        holder.textView_TotalCost.setText(this.currencySymbol + Utils.showHumanDecimals(currentItem.getNoOfCoins().multiply(currentItem.getSingleCoinPrice_CurrencyCurrent()))) ;


        final BigDecimal holdingChange = listOfTransactionGroups.get(groupPosition).getSummedPriceChange() ;
                //listOf_PriceTimeAgo.get(currentItem.getCoinObject().getId());

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

        }



        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Detail.class) ;
                intent.putExtra("coinId", anyChildTransaction.getCoinObject().getId()) ;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                context.startActivity(intent);



            }
        });

        holder.imageView_Link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(!RVItemManager.isGroupExpanded(groupPosition)){
                    RVItemManager.expandGroup(groupPosition) ;
                } else {
                    RVItemManager.collapseGroup(groupPosition) ;

                }
                return true;
            }
        });





    }

    @Override
    public void onBindChildViewHolder(@NonNull MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // group item

        final Object_TransactionFullData currentItem = listOfTransactionGroups.get(groupPosition)
                .getListOfChildTransactionsFD().get(childPosition) ;


        Glide.with(context).load(currentItem.getCoinObject().getImageLogoLink()).into(holder.imageView_Link) ;

        holder.textView_Name.setText(currentItem.getCoinObject().getName());
        holder.textView_SingleCoinPrice.setText(this.currencySymbol + Utils.showHumanDecimals(currentItem.getTransactionObject().getSingleCoinPrice_CurrencyCurrent() ));
        holder.textView_TotalCost.setText(this.currencySymbol + Utils.showHumanDecimals(currentItem.getTransactionObject().getTotalValue_Current() )) ;
        holder.textView_noOfCoins.setText(Utils.showHumanDecimals(currentItem.getTransactionObject().getNoOfCoins()) + " " + currentItem.getCoinObject().getSymbol());








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

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(@NonNull MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // Dont expand anything, we will handle the expansion ourseleves
        return false;
    }





    public void refreshAdapter(ArrayList<Object_TransactionGroup> newList){
        Log.d(LOG_TAG, "Refresh adapter is called") ;
        this.listOfTransactionGroups = newList ;
        notifyDataSetChanged();
    }






    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {


        public MyBaseViewHolder(View v) {
            super(v);

        }
    }



    public static class MyGroupViewHolder extends MyBaseViewHolder {
        RelativeLayout relativeLayout_Container ;
        TextView textView_Name, textView_SingleCoinPrice, textView_24hChange, textView_TotalCost, textView_noOfCoins  ;
        ImageView imageView_Link ;


        public MyGroupViewHolder(View v) {
            super(v);

            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRowTransaction_RelativeLayout_Main) ;
            textView_Name = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_CoinName) ;
            textView_SingleCoinPrice = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_SingleCoinPrice) ;
            textView_24hChange = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_24hChange) ;
            textView_TotalCost = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_TotalCost) ;
            textView_noOfCoins = (TextView) itemView.findViewById(R.id.singleRowTransaction_TextView_NoOfCoins) ;

            imageView_Link = (ImageView) itemView.findViewById(R.id.singleRowTransaction_ImageView_CoinImage) ;

        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        RelativeLayout relativeLayout_Container ;
        TextView textView_Name, textView_SingleCoinPrice, textView_24hChange, textView_TotalCost, textView_noOfCoins  ;
        ImageView imageView_Link ;

        public MyChildViewHolder(View v) {
            super(v);
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
