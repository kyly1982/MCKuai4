package com.mckuai.imc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.bean.Token;
import com.mckuai.imc.utils.QQLoginListener;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, QQLoginListener.OnQQResponseListener {


    private final String TAG = "LoginActivity";

    private TextView tv_title;

    private static Tencent mTencent;
    private static MCKuai4 mApplication;
    private AsyncHttpClient mClint;
    private QQLoginListener mQQListener;

//    UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");
        setContentView(R.layout.mckuai_activity_login);
        mApplication = MCKuai4.getInstance();
        mTencent = Tencent.createInstance("101155101", getApplicationContext());
        mClint = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getmTitle());
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getmTitle());
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_anonymous).setOnClickListener(this);
        findViewById(R.id.btn_left).setOnClickListener(this);
    }

    private void logoutQQ() {
        if (null != mTencent) {
            mTencent.logout(LoginActivity.this);
        }
    }


    private void loginToQQ() {
        MobclickAgent.onEvent(this, "qqLogin");
        if (null == mTencent) {
            mTencent = Tencent.createInstance("101155101", getApplicationContext());
        }
        if (!mTencent.isSessionValid()) {
            mQQListener = new QQLoginListener(this, mTencent, this);
            mTencent.login(this, "all", mQQListener);
        }
    }

    private void loginToMC(Token token, MCUser user) {
        final String url = getString(R.string.interface_domainName) + getString(R.string.interface_qqlogin);
        final RequestParams params = new RequestParams();
        // 添加参数
        params.put("accessToken", token.getToken());
        params.put("openId", user.getName());
        params.put("nickName", user.getNike());
        params.put("gender", user.getGender());
        params.put("headImg", user.getHeadImg());
//        String requestUrl = url + "&" + params.toString();
        mClint.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                MCUser tempUser = null;
                if (response.has("state")) {
                    try {
                        if (response.getString("state").equalsIgnoreCase("ok")) {
                            // 开始解析
                            tempUser = gson.fromJson(response.getString("dataObject"), MCUser.class);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
//                        showNotification(3, "登录到麦块时失败！" + e.getLocalizedMessage(), R.id.rl_login_root);
                        return;
                    }
                    if (null != tempUser) {
                        tempUser.setGender(mApplication.user.getGender());
                        mApplication.user.clone(tempUser);
                    }
                    mApplication.saveProfile();
                    handleResult(true);
                    return;
                }
//                showNotification(1, "登录到麦块时失败！", R.id.rl_login_root);
                logoutQQ();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
//                showNotification(1, "登录失败，原因：" + throwable.getLocalizedMessage(), R.id.rl_login_root);
                logoutQQ();
            }
        });
    }

    private void handleResult(Boolean result) {
        setResult(true == result ? RESULT_OK : RESULT_CANCELED);
        this.finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Token token = mApplication.token;
                if (null != token && token.isTokenSurvival()) {
                    loginToMC(token, mApplication.user);
                } else {
                    loginToQQ();
                }
                break;
            default:
                handleResult(false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQListener);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(Token token, MCUser user) {
        if (null == mApplication.token){
            mApplication.token = token;
        } else {
            mApplication.token.clone(token);
        }
        if (null == mApplication.user){
            mApplication.user = user;
        } else {
            mApplication.user.clone(user);
        }
        loginToMC(token, user);
    }

    @Override
    public void onError(String msg) {
        if (null != msg) {
            Snackbar.make(tv_title, msg, Snackbar.LENGTH_LONG).show();
        }
    }
}

