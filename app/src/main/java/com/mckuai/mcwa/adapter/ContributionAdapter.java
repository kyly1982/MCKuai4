package com.mckuai.mcwa.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mckuai.imc.R;
import com.mckuai.mcwa.bean.Question;

import java.util.ArrayList;


/**
 * Created by kyly on 2015/10/12.
 */
public class ContributionAdapter extends RecyclerView.Adapter<ContributionAdapter.ViewHolder>  {
    private ArrayList<Question> mQuestions;
    private Context mContext;

    public ContributionAdapter(@NonNull Context context){
        this.mContext = context;
    }

    public void setData(@NonNull ArrayList<Question> questions){
        this.mQuestions = questions;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.wa_item_contribution,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mQuestions && !mQuestions.isEmpty()){
            Question question = mQuestions.get(position);
            holder.topic.setText(question.getTitle()+"");
            holder.time.setText(question.getInsertTime()+"");

            Drawable drawable =null;

            switch (question.getStatus()){
                case "pass":
                    //审核通过
                    drawable = mContext.getResources().getDrawable(R.mipmap.ic_audit_passed);
                    assert drawable != null;
                    drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.userCount.setCompoundDrawables(drawable, null, null, null);
                    holder.userCount.setText(mContext.getString(R.string.answer_count, question.getAllCount()));
                    if (0 == question.getAllCount()){
                        holder.ratio.setText(mContext.getString(R.string.rate_notused));
                    } else {
                        holder.ratio.setText(mContext.getString(R.string.correct_rate,(int)(question.getRightCount() * 100 / question.getAllCount())));
                    }
                    break;
                case "nopass":
                    //审核未通过
                    holder.ratio.setText("");
                    drawable =  mContext.getResources().getDrawable(R.mipmap.ic_audit_faile);
                    holder.userCount.setText(mContext.getResources().getString(R.string.status_audit_false));
                    assert drawable != null;
                    drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.userCount.setCompoundDrawables(drawable, null, null, null);
                    break;
                default:
                    //审核中
                    holder.ratio.setText("");
                    drawable =  mContext.getResources().getDrawable(R.mipmap.ic_auditing);
                    holder.userCount.setText(mContext.getString(R.string.status_auditing));
                    assert drawable != null;
                    drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.userCount.setCompoundDrawables(drawable, null, null, null);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mQuestions ? 0: mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView topic;
        AppCompatTextView userCount;
        AppCompatTextView time;
        AppCompatTextView ratio;

        public ViewHolder(View itemView) {
            super(itemView);
            topic = (AppCompatTextView) itemView.findViewById(R.id.topic);
            userCount = (AppCompatTextView) itemView.findViewById(R.id.status);
            time = (AppCompatTextView) itemView.findViewById(R.id.posttime);
            ratio = (AppCompatTextView) itemView.findViewById(R.id.ratio);
        }
    }
}
