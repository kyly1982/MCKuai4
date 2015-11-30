package com.mckuai.mcwa.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mckuai.imc.R;
import com.mckuai.imc.activity.MCKuai4;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.bean.Token;
import com.mckuai.imc.utils.NetInterface;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, NetInterface.OnLoginServerListener {

    private static MCUser user;
    private static Button login;
    private static AppCompatTextView hint;
    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        login = (Button) findViewById(R.id.login);
        hint = (AppCompatTextView) findViewById(R.id.hint_login);
        login.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText(R.string.title_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                MobclickAgent.onEvent(this,"wxLogin");
                login.setEnabled(false);
                if (null == mApplication.user || null == mApplication.user.getName()) {
                    loginWX();
                } else {
                    loginServer();
                }
                break;
            default:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }

    private void loginWX() {
        String appIDWX = "wxc49b6a0e3c78364d";
        String appSecretWX = "d4624c36b6795d1d99dcf0547af5443d";
        UMWXHandler wxHandler = new UMWXHandler(this, appIDWX, appSecretWX);
        wxHandler.addToSocialSDK();
        if (wxHandler.isClientInstalled()) {
            mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    hint.setText(getString(R.string.hint_loginwx_ready));
                    hint.setVisibility(View.VISIBLE);
                }

                @Override
                public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                    MobclickAgent.onEvent(LoginActivity.this, "wxGetToken_S");
                    initOpenIdAndToken(bundle);
                    mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete(int status, Map<String, Object> userinfo) {
                            if (200 == status && null != userinfo) {
                                MobclickAgent.onEvent(LoginActivity.this, "wxVali_S");
                                updateUserInfo_WX(userinfo);
                                loginServer();
                            } else {
                                MobclickAgent.onEvent(LoginActivity.this, "wxVali_F");
                                hint.setText("微信验证失败，原因：" + status);
                                login.setEnabled(true);
                            }
                        }

                    });
                }

                @Override
                public void onError(SocializeException e, SHARE_MEDIA share_media) {
                    MobclickAgent.onEvent(LoginActivity.this, "wxGetToken_F");
                    feedback(false,true);
                    hint.setText(e.getMessage() + getString(R.string.error_code, e.getErrorCode()));
                    login.setEnabled(true);
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    MobclickAgent.onEvent(LoginActivity.this, "wxGetToken_F");
                    feedback(false,true);
                    login.setEnabled(true);
                    hint.setText(getString(R.string.hint_canclelogin));
                }
            });
        } else {
            Toast.makeText(this,getString(R.string.error_wxisuninstall),Toast.LENGTH_LONG).show();
            login.setEnabled(true);
            installWX();
        }
    }

    private void initOpenIdAndToken(@NonNull Bundle bundle) {
        user = new MCUser();
        mApplication.token = new Token(1);
        mApplication.token.setBirthday(System.currentTimeMillis() - 600000);
        String expires = bundle.getString("expires_in");
        if (null != expires){
            mApplication.token.setExpires(Integer.valueOf(expires));
        }
        mApplication.token.setToken(bundle.getString("access_token"));
        user.setName(bundle.getString("openid"));
    }

    private void updateUserInfo_WX(Map<String, Object> userinfo) {
        user.setNike((String) userinfo.get("nickname"));
        user.setHeadImg((String) userinfo.get("headimgurl"));
        int sex = (Integer) userinfo.get("sex");
        switch (sex){
            case 1:
                user.setGender("男");
                break;
            default:
                user.setGender("女");
                break;
        }
        if (null == mApplication.user){
            mApplication.user = user;
        } else {
            mApplication.user.clone(user);
        }
    }


    private void loginServer() {
        MobclickAgent.onEvent(this, "login");
        NetInterface.loginServer(this, mApplication.user, mApplication.token.getToken(), this);
        hint.setText(getString(R.string.hint_loginserver));
    }

    private void installWX(){
        StringBuilder localStringBuilder = new StringBuilder().append("market://details?id=com.tencent.mm");
        Uri localUri = Uri.parse(localStringBuilder.toString());
        Intent intent = new Intent("android.intent.action.VIEW", localUri);
        List<ResolveInfo> localList = this.getPackageManager().queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        if (null != localList && 0 < localList.size()){
            startActivity(intent);
        }
    }



    @Override
    public void onSuccess(MCUser user) {
        MobclickAgent.onEvent(this, "login_S");
        MCKuai4.user.clone(user);
        mApplication.saveProfile();
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onFalse(String msg) {
        feedback(false,false);
        login.setEnabled(true);
        hint.setText(msg);
        hint.setVisibility(View.VISIBLE);
        MobclickAgent.onEvent(this, "login_F");
        setResult(RESULT_CANCELED);
    }
}

