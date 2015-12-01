package com.mckuai.mcwa.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.bean.Page;
import com.mckuai.imc.utils.NetInterface;
import com.mckuai.imc.widget.CircleBitmapDisplayer;
import com.mckuai.mcwa.adapter.RankingAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RankingActivity extends BaseActivity implements UltimateRecyclerView.OnLoadMoreListener,SwipeRefreshLayout.OnRefreshListener,NetInterface.OnGetRankingListener {

    private AppCompatTextView mRank;
    private AppCompatTextView mNo;
    private AppCompatTextView mScore;
    private ImageView mCover;
    private UltimateRecyclerView mList;
    private RelativeLayout mLayout;

    private RankingAdapter mAdapter;
    private Page mPage = new Page();
    private ArrayList<MCUser> mUsers = new ArrayList<>(20);

    private ImageLoader mLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wa_activity_ranking);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        if (null == mList){
            initView();
            loadData();
        }
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(R.string.title_rankinglist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBar.setBackgroundResource(R.color.primary);
    }

    private void initView() {
        mRank = (AppCompatTextView) findViewById(R.id.myranking);
        mNo = (AppCompatTextView) findViewById(R.id.myrank_layout);
        mScore = (AppCompatTextView) findViewById(R.id.myscore);
        mCover = (ImageView) findViewById(R.id.mycover);
        mList = (UltimateRecyclerView) findViewById(R.id.rankinglist);
        mLayout = (RelativeLayout) findViewById(R.id.myinfo);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(manager);
//        mList.setHasFixedSize(true);
        mList.enableLoadmore();
        mList.enableDefaultSwipeRefresh(true);
        mList.setOnLoadMoreListener(this);
        mList.setDefaultOnRefreshListener(this);
    }

    private void loadData() {
        if (mApplication.isLogin()){
            NetInterface.getRankingList(this,mApplication.user.getId(),mPage.getNextPage(),this);
        } else {
            MobclickAgent.onEvent(this,"reqRL_S");
            NetInterface.getRankingList(this,0,mPage.getNextPage(),this);
        }
    }

    private void showData() {
        if (mApplication.isLogin() && 0 != mApplication.user.getRanking()) {
            MCUser user = mApplication.user;
            if (null != user && 0 != user.getId() ) {
                mRank.setText((user.getRanking() +1) + "");
                mScore.setText(getString(R.string.scores,user.getAllScore()));
                mNo.setVisibility(View.VISIBLE);
            }
            if (null != user.getHeadImg() && 10 < user.getHeadImg().length()) {
                mLoader.displayImage(user.getHeadImg(),mCover,options);
            }
            mLayout.setVisibility(View.VISIBLE);
        }
        if (null == mAdapter){
            mAdapter = new RankingAdapter(this);
            mList.setAdapter(mAdapter);
        }
        mAdapter.setData(mUsers);
    }

    @Override
    public void loadMore(int itemsCount, int maxLastVisiblePosition) {
        if (!mPage.EOF()){
            MobclickAgent.onEvent(this,"reqRL_More");
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        MobclickAgent.onEvent(this,"rerRefRL");
        mPage.setPage(0);
        mUsers.clear();
        loadData();
    }

    @Override
    public void onSuccess(Page page, MCUser myself,ArrayList<MCUser> users) {
        MobclickAgent.onEvent(this,"reqRL_S");
        mPage.clone(page);
        if (null !=myself) {
            mApplication.user.setAllScore(myself.getAllScore());
            mApplication.user.setRanking(myself.getRanking());
            mApplication.user.setScoreRank(myself.getScoreRank());
            mApplication.user.setAnswerNum(myself.getAnswerNum());
            mApplication.user.setUploadNum(myself.getUploadNum());
        }
        mUsers.addAll(users);
        showData();
    }

    @Override
    public void onFalse(String msg) {
        MobclickAgent.onEvent(this,"reqRL_F");
        feedback(false,false);
        mList.setRefreshing(false);
    }
}
