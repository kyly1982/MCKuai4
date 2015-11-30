package com.mckuai.mcwa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mckuai.imc.widget.CircleBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/12.
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder>  {
    private ArrayList<MCUser> mUser;
    private Context mContext;
    private ImageLoader mLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public RankingAdapter(@NonNull Context context){
        this.mContext = context;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
    }

    public void setData(@NonNull ArrayList<MCUser> mUser){
        this.mUser = mUser;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.wa_item_ranking,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mUser && !mUser.isEmpty()){
            MCUser user = mUser.get(position);
            holder.ranking.setText(user.getScoreRank()+"");
            holder.name.setText(null != user.getNike()? user.getNike():user.getNickName());
            holder.score.setText(mContext.getString(R.string.scores,user.getAllScore()));
            if (null != user.getHeadImg() && 10 < user.getHeadImg().length()){
                mLoader.displayImage(user.getHeadImg(),holder.cover,options);
            }
            if (1 == user.getScoreRank()){
                holder.lab.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            } else {
                holder.lab.setBackgroundColor(mContext.getResources().getColor(R.color.lab_rank));
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mUser ? 0:mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView ranking;
        AppCompatTextView name;
        AppCompatTextView score;
        ImageView cover;
        View lab;

        public ViewHolder(View itemView) {
            super(itemView);
            ranking = (AppCompatTextView) itemView.findViewById(R.id.item_userranking);
            name = (AppCompatTextView) itemView.findViewById(R.id.item_username);
            score = (AppCompatTextView) itemView.findViewById(R.id.item_userscore);
            cover = (ImageView) itemView.findViewById(R.id.item_usercover);
            lab = itemView.findViewById(R.id.item_ranklab);
        }
    }
}
