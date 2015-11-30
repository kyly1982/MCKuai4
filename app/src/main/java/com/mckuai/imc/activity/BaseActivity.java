package com.mckuai.imc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mckuai.imc.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


public class BaseActivity extends AppCompatActivity {

    protected boolean isLoading = false;    //正在加载数据
    protected boolean isCacheEnabled = true;         //启用缓存
    protected boolean isSecondCacheEnable = true;//第二个项目的缓存
    protected boolean isShowingMenu = false;
    protected String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        PushAgent.getInstance(this).onAppStart();
//        Debug.startMethodTracing("mckial");
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Debug.stopMethodTracing();
    }

    protected void setTitle(String title) {
        if (null != title) {
            mTitle = title;
        }
    }


    public String getmTitle() {
        return mTitle;
    }



    private String getColorString(int colorResId) {
        int color = getResources().getColor(colorResId);
        String c = "#" + Integer.toHexString(color);
        return c.toUpperCase();
    }


    /**
     * 重写此方法时请勿调用super方法
     *
     * @return
     */
    protected boolean onMenuKeyPressed() {
        return false;
    }

    /**
     * 重写此方法时请勿调用super方法
     *
     * @return
     */
    protected boolean onBackKeyPressed() {
        return false;
    }

    public void showMessage(@NonNull View view,@StringRes int msg,@StringRes int actionName,@NonNull final View.OnClickListener listener){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction(actionName,listener).setActionTextColor(getResources().getColor(R.color.red)).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (onMenuKeyPressed()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackKeyPressed()){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void callLogin(int requestCode){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent,requestCode);
    }
}
