package com.mckuai.mcwa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by kyly on 2015/10/12.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>  {
    private ArrayList<MCUser> mUser;
    private Context mContext;
    private ImageLoader mLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public UserListAdapter(@NonNull Context context){
        this.mContext = context;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
    }

    public void setData(@NonNull ArrayList<MCUser> mUser){
        this.mUser = mUser;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.mc_item_user,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mUser && !mUser.isEmpty()){
            MCUser user = mUser.get(position);
            if (null != user.getHeadImg() && 10 < user.getHeadImg().length()){
                mLoader.displayImage(user.getHeadImg(),holder.cover,options);
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mUser ? 0:mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.usercover);
        }
    }
}
