package com.mckuai.mcwa.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.mckuai.imc.R;
import com.mckuai.imc.activity.MCKuai4;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.HashMap;

/**
 * Created by kyly on 2015/9/29.
 */
public class BaseActivity extends AppCompatActivity {
    protected static MCKuai4 mApplication = MCKuai4.getInstance();
    protected static Toolbar mToolBar;
    protected static TextView mTitle;
    protected Vibrator vibrator;
    protected SoundPool soundPool;
    protected HashMap<Integer,Integer> sound;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        PushAgent.getInstance(this).onAppStart();
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
        stopSound();
    }

    public void initToolBar() {
        mTitle = (TextView) findViewById(R.id.title);
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
    }

    public void callLogin(int requestCode) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, requestCode);
    }

    public void showMessage(final int level, String msg) {
        Snackbar.make(null, msg, 0 == level ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(final int level, final String msg, final String actionName, final View.OnClickListener listener) {
        Snackbar.make(null, msg, Snackbar.LENGTH_LONG).setAction(actionName, listener).show();
    }

    public void showMessage(@NonNull View view,@StringRes int msg,@StringRes int actionName,@NonNull final View.OnClickListener listener){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction(actionName,listener).setActionTextColor(getResources().getColor(R.color.red)).show();
    }



    public static void setTitles(@StringRes final int titleId, @StringRes final int toolbarTitleId, @StringRes final int toolbarSubTitleId) {
        if (0 != titleId) {
            mTitle.setText(titleId);
        } else {
            mTitle.setText("");
        }

        if (0 != toolbarTitleId) {
            mToolBar.setTitle(toolbarTitleId);
        } else {
            mToolBar.setTitle("");
        }

        if (0 != toolbarSubTitleId) {
            mToolBar.setSubtitle(toolbarSubTitleId);
        } else {
            mToolBar.setSubtitle("");
        }
    }



    protected void feedback(final boolean isSuccess, final boolean needMusic) {
        if (null == vibrator) {
            vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator.hasVibrator() && !isSuccess) {
            long[] pattern1 = {100, 300, 100, 300};
            vibrator.vibrate(pattern1, -1);
        }
        if (needMusic) {
            playSound(true == isSuccess ? 1 : 0);
        }
    }

    protected void loadSound(){
        if (null == soundPool) {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
            sound = new HashMap<>(2);
            sound.put(0, soundPool.load(this, R.raw.error, 1));
            sound.put(1, soundPool.load(this, R.raw.right, 1));
        }
    }



    private void playSound(int type){
        soundPool.play(sound.get(type), 1, 1, 2, 0, 1);
    }

    private void stopSound(){
        if (null != soundPool){
            try {
                soundPool.stop(sound.get(0));
                soundPool.stop(sound.get(1));
            } catch (Exception e){
                e.printStackTrace();
            }
            sound.clear();
            sound = null;
            soundPool.release();
            soundPool = null;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (onMenuKeyPressed()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackKeyPressed()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onMenuKeyPressed() {
        return false;
    }

    public boolean onBackKeyPressed() {
        return false;
    }

}
