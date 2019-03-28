package apps.yoo.com.blockholdings.ui.detail;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.yoo.com.blockholdings.R;
import apps.yoo.com.blockholdings.util.Message;


public class RVAdapter_ProjectLinks extends RecyclerView.Adapter<RVAdapter_ProjectLinks.YoloCartViewHolder>{
    private List<Object_ProjectLink> listOfitems ;
    private Context context ;

    public RVAdapter_ProjectLinks(Context context, List<Object_ProjectLink> listOfitems) {
        this.context = context ;
        this.listOfitems = listOfitems ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_projectlink,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_ProjectLink currentItem = listOfitems.get(position);

        switch (currentItem.getType()){
            case Object_ProjectLink.TYPE_WEBSITE :
                Glide.with(context).load(R.drawable.ic_www).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_BLOG :
                Glide.with(context).load(R.drawable.ic_blog).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_REDDIT :
                Glide.with(context).load(R.drawable.ic_reddit).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_TELEGRAM :
                Glide.with(context).load(R.drawable.ic_telegram).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_TWITTER :
                Glide.with(context).load(R.drawable.ic_twitter).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_FACEBOOK :
                Glide.with(context).load(R.drawable.ic_facebook).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_GITHUB :
                Glide.with(context).load(R.drawable.ic_github).into(holder.imageView_LinkImaae) ;
                break ;
            case Object_ProjectLink.TYPE_BLOCK_EXPLORER :
                Glide.with(context).load(R.drawable.ic_block_explorer).into(holder.imageView_LinkImaae) ;
                break ;

            case Object_ProjectLink.TYPE_SLACK :
                Glide.with(context).load(R.drawable.ic_slack).into(holder.imageView_LinkImaae) ;
                break ;

            case Object_ProjectLink.TYPE_WHITEPAPER :
                Glide.with(context).load(R.drawable.ic_whitepaper).into(holder.imageView_LinkImaae) ;
                break ;

            case Object_ProjectLink.TYPE_LINKEDIN :
                Glide.with(context).load(R.drawable.ic_linkedin).into(holder.imageView_LinkImaae) ;
                break ;
        }


        holder.textView_LinkName.setText(currentItem.getName()) ;


        holder.linLt_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.display(context, "Link is " + currentItem.getLink());
//                Intent intent = new Intent(context, Activity_DetailTransactionEdit.class) ;
//                intent.putExtra("transactionId", currentItemTransaction.getTransactionNo()) ;
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
//                context.startActivity(intent);
            }
        });








    }

    public void refreshData(List<Object_ProjectLink> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linLt_Container ;
        TextView textView_LinkName ;
        ImageView imageView_LinkImaae ;


        public YoloCartViewHolder(View itemView) {
            super(itemView);
            linLt_Container = itemView.findViewById(R.id.singleRowProjectLink_Lt_Parent) ;
            textView_LinkName = (TextView) itemView.findViewById(R.id.singleRowProjectLink_TextView_Link) ;
            imageView_LinkImaae = (ImageView) itemView.findViewById(R.id.singleRowProjectLink_ImageView_Link) ;

        }
    }
}








