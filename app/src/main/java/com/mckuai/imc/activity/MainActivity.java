package com.mckuai.imc.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mckuai.imc.BuildConfig;
import com.mckuai.imc.R;
import com.mckuai.imc.fragment.ForumFragment;
import com.mckuai.imc.fragment.WaFragment;
import com.mckuai.imc.utils.AutoUpgrade.AppUpdate;
import com.mckuai.imc.utils.AutoUpgrade.AppUpdateService;
import com.mckuai.imc.utils.AutoUpgrade.internal.SimpleJSONParser;
import com.mckuai.mcwa.activity.ContributionActivity;
import com.mckuai.mcwa.activity.RankingActivity;
import com.mckuai.mcwa.activity.UserCenterActivity;
import com.mckuai.mcwa.utils.CircleBitmap;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.lang.reflect.Method;
import java.net.URLEncoder;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private MCKuai4 mApplication;
    private FragmentManager mFM;
    private ForumFragment mCommunityFragment;
    private AppUpdate updateService;

    private FloatingActionButton fab;
    private Toolbar mToolbar;
    private Menu mMenu;
    private MenuItem mCreatePost ;
    private MenuItem mMcwaCenter;
    private MenuItem mMcwaRanking;
    private MenuItem mMcContribution;

    private boolean isShowMCWa = true;
    private boolean isCheckUpadte = false;

    private static final int REQUEST_MCWA_USERCENTER = 1;
    private static final int REQUEST_MCWA_CONTRIBUTION = 2;
    private static final int REQUEST_ANSWER = 4;
    private static final int REQUEST_MCKUAI_USERCENTER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        mFM = getFragmentManager();
        this.mApplication = MCKuai4.getInstance();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserInfo();
        if (!isCheckUpadte){
            checkUpgrade();
            isCheckUpadte = true;
        }
//        String token = UmengRegistrar.getRegistrationId(this);
//        Log.e("token=",token);
    }


    private void initView(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT > 20) {
            mToolbar.setElevation(0);
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setSubtitle("MC哇");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    public void setMenuItemVisible(Menu menu){
        if (null == mCreatePost){
            mCreatePost = menu.findItem(R.id.action_createpost);
            mMcwaCenter = menu.findItem(R.id.action_mcwa_usercenter);
            mMcwaRanking = menu.findItem(R.id.action_mcwa_ranking);
            mMcContribution = menu.findItem(R.id.action_mcwa_contribution);
        }
        mCreatePost.setVisible(!isShowMCWa);
        mMcwaCenter.setVisible(isShowMCWa);
        mMcwaRanking.setVisible(isShowMCWa);
        mMcContribution.setVisible(isShowMCWa);
    }

    private void showUserInfo(){
        if (mApplication.isLogin()){
            final String cover = mApplication.user.getHeadImg();
            if (null != cover && 10 < cover.length()){
                ImageLoader loader = ImageLoader.getInstance();
                loader.loadImage(cover, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (null != loadedImage){
                            mToolbar.setNavigationIcon(new BitmapDrawable(getResources(), CircleBitmap.getCircleBitmap(loadedImage, getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small))));
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }
        } else {
            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
            mToolbar.setNavigationIcon(new BitmapDrawable(getResources(), CircleBitmap.getCircleBitmap(drawable.getBitmap(), getResources().getDimensionPixelSize(R.dimen.usercover_diameter_small))));
        }
    }

    private void checkUpgrade() {
        updateService = AppUpdateService.getAppUpdate(this);
        String url = getString(R.string.interface_domain_update) + getString(R.string.interface_checkupgread);
        url = url + "&pushMan=" + URLEncoder.encode(getString(R.string.channel));
        updateService.checkLatestVersionQuiet(url, new SimpleJSONParser());
        isCheckUpadte = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent com.mckuai.mcwa.activity in AndroidManifest.xml.
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.action_mcwa_usercenter:
                if (mApplication.isLogin()) {
                    intent.setClass(this, UserCenterActivity.class);
                    startActivity(intent);
                } else {
                    callLogin(REQUEST_MCWA_USERCENTER);
                }
                break;
            case R.id.action_mcwa_contribution:
                if (mApplication.isLogin()) {
                    intent.setClass(this, ContributionActivity.class);
                    startActivity(intent);
                } else {
                    callLogin(REQUEST_MCWA_CONTRIBUTION);
                }
                break;
            case R.id.action_mcwa_ranking:
                intent.setClass(this, RankingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_createpost:
                ForumFragment fragment = (ForumFragment) mFM.findFragmentByTag("communityfragment");
                if (null != fragment.getmForums()){
                    intent.setClass(this, PublishPostActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("FORUM_LIST",fragment.getmForums());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if (null == mCommunityFragment){
                    mCommunityFragment = new ForumFragment();
                    isShowMCWa = false;
                    mFM.beginTransaction().replace(R.id.fragment, mCommunityFragment,"communityfragment").commit();
                    getSupportActionBar().setSubtitle("麦块社区");
                } else {
                    isShowMCWa = !isShowMCWa;
                    if (isShowMCWa){
                        mFM.beginTransaction().hide(mCommunityFragment).commit();
                        getSupportActionBar().setSubtitle("MC哇");
                    } else {
                        mFM.beginTransaction().show(mCommunityFragment).commit();
                        getSupportActionBar().setSubtitle("麦块社区");
                    }
                }
                setMenuItemVisible(mMenu);
                break;
            default:
                if (mApplication.isLogin()) {
                    Intent intent = new Intent(this, UserCenter.class);
                    startActivity(intent);
                } else {
                    callLogin(REQUEST_MCKUAI_USERCENTER);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode){
            Intent intent = new Intent();
            switch (requestCode){
                case REQUEST_MCKUAI_USERCENTER:
                    intent.setClass(this,UserCenter.class);
                    break;
                case REQUEST_MCWA_CONTRIBUTION:
                    intent.setClass(this,ContributionActivity.class);
                    break;
                case REQUEST_MCWA_USERCENTER:
                    intent.setClass(this,UserCenterActivity.class);
                    break;
                case REQUEST_ANSWER:
                    WaFragment fragment =  (WaFragment)mFM.findFragmentByTag("com.mckuai.imc.fragment.WaFragment");
                    fragment.onActivityResult(requestCode,resultCode,data);
                    return;
            }
            startActivity(intent);
        }
    }

    @Override
    protected boolean onBackKeyPressed() {
        showMessage(fab, R.string.exit, R.string.action_exit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.saveProfile();
//                mApplication.stopMusic();
//                System.exit(0);
                MainActivity.this.finish();
            }
        });
        return true;
    }
}
