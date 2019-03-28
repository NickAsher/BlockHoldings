package apps.yoo.com.blockholdings.ui.news;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import apps.yoo.com.blockholdings.R;



public class RVAdapter_NewsArticle extends RecyclerView.Adapter<RVAdapter_NewsArticle.YoloCartViewHolder>{
    private ArrayList<Object_NewsArticle> listOfitems ;
    private Context context ;

    public RVAdapter_NewsArticle(Context context, ArrayList<Object_NewsArticle> listOfitems) {
        this.context = context ;
        this.listOfitems = listOfitems ;
    }

    @Override
    public YoloCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_newsarticle,parent,false) ;
        return new YoloCartViewHolder(convertView) ;
    }


    @Override
    public void onBindViewHolder(final YoloCartViewHolder holder, final int position) {
        final Object_NewsArticle currentItem = listOfitems.get(position) ;

        Glide.with(context).load(currentItem.getImageLink()).into(holder.imageView_Link) ;
        holder.textView_title.setText(currentItem.getTitle());
        holder.textView_Description.setText(currentItem.getDescription());
        holder.textView_DateTime.setText(currentItem.getPubDate());

        holder.relativeLayout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


















    }

    public void refreshData(ArrayList<Object_NewsArticle> newListOfItems){
        this.listOfitems = newListOfItems ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfitems.size();
    }

    public class YoloCartViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout_Container ;
        TextView textView_title, textView_Description, textView_Origin, textView_DateTime ;
        ImageView imageView_Link ;

        public YoloCartViewHolder(View itemView) {
            super(itemView);
            relativeLayout_Container= (RelativeLayout) itemView.findViewById(R.id.singleRow_RelativeLayout_Main) ;
            textView_title = (TextView) itemView.findViewById(R.id.singleRow_TextView_ArticleTitle) ;
            textView_Description = (TextView) itemView.findViewById(R.id.singleRow_TextView_ArticleDescription) ;
            textView_Origin = (TextView) itemView.findViewById(R.id.singleRow_TextView_ArticleOrigin) ;
            textView_DateTime = (TextView) itemView.findViewById(R.id.singleRow_TextView_ArticleDate) ;
            imageView_Link = (ImageView) itemView.findViewById(R.id.singleRow_ImageView_ArticleImage) ;

        }
    }
}

