package com.mckuai.mcwa.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.utils.NetInterface;
import com.mckuai.imc.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class UserCenterActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, NetInterface.OnGetUserInfoListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private AppCompatTextView name;
    private AppCompatTextView count;
    private AppCompatTextView score;
    private AppCompatTextView score_avg;
    private AppCompatTextView contribution;
    private AppCompatButton share;
    private ImageView cover;
    private SwipeRefreshLayout refreshLayout;

    private ImageLoader mLoader;
    private DisplayImageOptions options;

    private boolean isLoading = false;
    private com.umeng.socialize.controller.UMSocialService mShareService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wa_activity_usercenter);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
        mShareService = UMServiceFactory.getUMSocialService("com.umeng.share");
        configPlatforms();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        if (null == name) {
            initView();
            mLoader = ImageLoader.getInstance();
        }
        showData();
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMSsoHandler ssoHandler = mShareService.getConfig().getSsoHandler(requestCode);
        if (null != ssoHandler) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        name = (AppCompatTextView) findViewById(R.id.username);
        count = (AppCompatTextView) findViewById(R.id.papercount);
        contribution = (AppCompatTextView) findViewById(R.id.contributioncount);
        score = (AppCompatTextView) findViewById(R.id.totlescore);
        score_avg = (AppCompatTextView) findViewById(R.id.avgscore);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        cover = (ImageView) findViewById(R.id.usercover);
        refreshLayout.setOnRefreshListener(this);
        share = (AppCompatButton) findViewById(R.id.btn_share);
        share.setOnClickListener(this);
    }

    private void loadData() {
        NetInterface.getUserInfo(this, mApplication.user.getId(), this);
        refreshLayout.setRefreshing(true);
        isLoading = true;
    }

    private void share(Bitmap bitmap) {
        String content = getString(R.string.share_content_usercenter, mApplication.user.getAnswerNum(), mApplication.user.getAllScore());
        mShareService.setAppWebSite(getString(R.string.share_url_download));
        mShareService.setShareContent(content);
        if (null != bitmap) {
            UMImage image = new UMImage(this, bitmap);
            mShareService.setShareImage(image);
        }
        mShareService.openShare(this, false);
    }

    private void showData() {
        MCUser user = mApplication.user;
        if (null != user) {
            name.setText(user.getNike() + "");
            count.setText(user.getAnswerNum() + "");
            contribution.setText(user.getUploadNum() + "");
            score.setText(user.getAllScore() + "");
            if (0 < user.getAnswerNum()) {
                score_avg.setText((int) (user.getAllScore() / user.getAnswerNum()) + "");
            } else {
                score_avg.setText("0");
            }
            if (null != user.getHeadImg() && 10 < user.getHeadImg().length()) {
                mLoader.displayImage(user.getHeadImg(), cover, options);
            }
        } else {
            Snackbar.make(refreshLayout, "未登录！", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setOnMenuItemClickListener(this);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usercenter, menu);
        MenuItem item = menu.findItem(R.id.action_playmusic);
        item.setVisible(false);
        if (null != item) {
            if (mApplication.isMusicPlaying()) {
                item.setIcon(R.mipmap.ic_menu_music_enable);
                item.setTitle(R.string.music_disable);
            } else {
                item.setTitle(R.string.music_enable);
                item.setIcon(R.mipmap.ic_menu_music_disable);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_playmusic:
                if (mApplication.switchPlayPauseMusic()) {
                    MobclickAgent.onEvent(this, "enableMusic");
                    item.setIcon(R.mipmap.ic_menu_music_enable);
                    item.setTitle(R.string.music_disable);
                } else {
                    MobclickAgent.onEvent(this, "disableMusic");
                    item.setTitle(R.string.music_enable);
                    item.setIcon(R.mipmap.ic_menu_music_disable);
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                MobclickAgent.onEvent(this, "share_UC");
                View view = v.getRootView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                share(bitmap);
                break;
        }
    }

    @Override
    public void onSuccess(MCUser userInfo) {
        MobclickAgent.onEvent(this, "reqUC_S");
        refreshLayout.setRefreshing(false);
        isLoading = false;
        mApplication.user.setAllScore(userInfo.getAllScore());
        mApplication.user.setRanking(userInfo.getRanking());
        mApplication.user.setScoreRank(userInfo.getScoreRank());
        mApplication.user.setAnswerNum(userInfo.getAnswerNum());
        mApplication.user.setUploadNum(userInfo.getUploadNum());
        showData();
    }

    @Override
    public void onFalse(String msg) {
        MobclickAgent.onEvent(this, "reqUC_F");
        feedback(false, false);
        refreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            MobclickAgent.onEvent(this, "reqRefUC");
            loadData();
        }
    }

    private void configPlatforms() {

        String title = getString(R.string.share_title_rank);
        String url = getString(R.string.share_url_download);
        String appID_QQ = "1104907496";
        String appAppKey_QQ = "DbdC0Qvfkj4yOLsG";
        // 添加qq
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appID_QQ, appAppKey_QQ);
        qqSsoHandler.setTargetUrl(url);
        qqSsoHandler.setTitle(title);
        qqSsoHandler.addToSocialSDK();
        // 添加QQ空间参数
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appID_QQ, appAppKey_QQ);
        qZoneSsoHandler.setTargetUrl(url);
        qZoneSsoHandler.addToSocialSDK();

        String appIDWX = "wxc49b6a0e3c78364d";
        String appSecretWX = "d4624c36b6795d1d99dcf0547af5443d";
        // 添加微信
        UMWXHandler wxHandler = new UMWXHandler(this, appIDWX, appSecretWX);
        wxHandler.setTargetUrl(url);
        wxHandler.setTitle(title);
        wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appIDWX, appSecretWX);
        wxCircleHandler.setTitle(title);
        wxCircleHandler.setTargetUrl(url);
        wxCircleHandler.setToCircle(true);
        wxHandler.showCompressToast(false);
        wxCircleHandler.addToSocialSDK();
        // 移除多余平台
        mShareService.getConfig().removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);
    }
}
