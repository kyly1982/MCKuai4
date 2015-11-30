package com.mckuai.mcwa.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.mcwa.activity.ExaminationActivity;
import com.mckuai.mcwa.adapter.UserListAdapter;

import java.util.ArrayList;

public class ReadyFragment extends BaseFragment {
    private TextView mTimeCount;
    private UltimateRecyclerView mList;
    private RelativeLayout mHint;
    private UserListAdapter mAdapter;
    private ArrayList<MCUser> mUsers;
    private int time = 4;
    private View view;

    private CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wa_fragment_ready, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUsers = (ArrayList<MCUser>) getArguments().getSerializable(getString(R.string.tag_users));
        if (null != view) {
            if (null == mList) {
                initView();
            }
        }
        showData();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }


    private void initView() {
        mTimeCount = (TextView) view.findViewById(R.id.time);
        mList = (UltimateRecyclerView) view.findViewById(R.id.userlist);
        mHint = (RelativeLayout) view.findViewById(R.id.layout_hint);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        mList.setLayoutManager(manager);
    }

    private void showData() {
        if (null != mUsers && !mUsers.isEmpty()) {
            if (null == mAdapter) {
                mAdapter = new UserListAdapter(getActivity());
                mList.setAdapter(mAdapter);
            }
            mAdapter.setData(mUsers);
        } else {
            mHint.setVisibility(View.GONE);
            mList.setVisibility(View.GONE);
        }
        mTimeCount.setText(getActivity().getString(R.string.time_countdown, time));
        timer = new CountDownTimer(time * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time--;
                mTimeCount.setText(getActivity().getString(R.string.time_countdown, time));
            }

            @Override
            public void onFinish() {
                mTimeCount.setText("0");
                ((ExaminationActivity) getActivity()).showNextFragment();
            }
        };
        timer.start();
    }


}
