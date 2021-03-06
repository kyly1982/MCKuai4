package com.mckuai.imc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mckuai.imc.R;
import com.mckuai.imc.activity.MCKuai4;
import com.mckuai.imc.activity.PostActivity;
import com.mckuai.imc.activity.UserCenter;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.bean.Post;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
	private ArrayList<Post> mPostList = new ArrayList<Post>(10);
	private static Context mContext;
	private LayoutInflater mInflater;
	private ImageLoader mLoader;
	private DisplayImageOptions options;

	private static final String TAG = "PostAdapter";

	public PostAdapter(Context context)
	{
		init(context);
	}

	protected void init(Context context)
	{
		this.mContext = context;
		this.mLoader = ImageLoader.getInstance();
		this.mInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.options = MCKuai4.getInstance().getCircleOption();
	}

	public void setData(ArrayList<Post> data)
	{
		if (null != data)
		{
			this.mPostList = data;
			notifyDataSetChanged();
		}
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.mckuai_item_post_normal,parent,false);
		final ViewHolder holder = new ViewHolder(view);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Post post = (Post) v.getTag();
				if (null != post){
					Intent intent = new Intent(mContext, PostActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(mContext.getString(R.string.tag_post), post);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			}
		});
        holder.rl_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Post post = mPostList.get(position);
                if (null != post){
                    MCUser user = new MCUser();
                    user.setId(post.getUserId());
                    user.setName(post.getUserName());
                    Intent intent = new Intent(mContext, UserCenter.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(mContext.getString(R.string.user),user);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Post post =    mPostList.get(position);
		 if (null != post){
			 if (null != post.getIcon() && 10 < post.getIcon().length()) {
				 mLoader.displayImage(post.getIcon(),holder.iv_cover,options);
			 }

			 if (post.isEssence()){
			 	holder.iv_essence.setVisibility(View.VISIBLE);
			 }
			 else {
				 holder.iv_essence.setVisibility(View.GONE);
			 }

			 if (post.isTop()){
				 holder.iv_top.setVisibility(View.VISIBLE);
			 }
			 else {
				 holder.iv_top.setVisibility(View.GONE);
			 }

			 holder.tv_title.setText(post.getTalkTitle()+"");
			 holder.tv_owner.setText(post.getUserName()+"");
			 holder.tv_reply.setText(post.getReplyNum() + "");
			 holder.tv_time.setText(post.getLastReplyTime() + "");
			 holder.itemView.setTag(post);
			 if (null != post.getHeadImg() && 10 < post.getHeadImg().length()){
				 mLoader.displayImage(post.getHeadImg(),holder.iv_cover,options);
			 }
             holder.rl_owner.setTag(position);
		 }
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getItemCount() {
		return null == mPostList ? 0:mPostList.size();
	}




	/**
	 * @author kyly
	 *
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView tv_title ;
		private TextView tv_owner;
		private TextView tv_reply;
		private TextView tv_time;
		private ImageView iv_cover;
		private ImageView iv_top;
		private ImageView iv_essence;
        private RelativeLayout rl_owner;

		/**
		 * @param itemView
		 */
		public ViewHolder(View itemView)
		{
			super(itemView);
			// TODO Auto-generated constructor stub
			iv_cover = (ImageView) itemView.findViewById(R.id.civ_postOwner_bottom);
			iv_top = (ImageView) itemView.findViewById(R.id.iv_typeTop);
			iv_essence = (ImageView) itemView.findViewById(R.id.iv_typeEssence);
			tv_title = (TextView) itemView.findViewById(R.id.tv_postTitle);
			tv_owner = (TextView) itemView.findViewById(R.id.tv_postOwner_bottom);
			tv_reply = (TextView) itemView.findViewById(R.id.tv_postReply);
			tv_time = (TextView) itemView.findViewById(R.id.tv_postrepayTime);
            rl_owner=(RelativeLayout)itemView.findViewById(R.id.rl_bottom);
		}


	}




}
