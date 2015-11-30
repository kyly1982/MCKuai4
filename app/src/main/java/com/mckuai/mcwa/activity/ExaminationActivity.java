package com.mckuai.mcwa.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.mckuai.imc.R;
import com.mckuai.imc.bean.Paper;
import com.mckuai.mcwa.fragment.AnswerFragment;
import com.mckuai.mcwa.fragment.ReadyFragment;
import com.mckuai.mcwa.fragment.ResulltFragment;

import java.util.ArrayList;

public class ExaminationActivity extends BaseActivity implements AnswerFragment.OnAnswerQuestionListener {
    private FragmentManager mFragmentManager;
    private int mPosition = 0;  //页面
    private ResulltFragment resulltFragment;

    public Paper paper;

    public static int score = 0;
    public static ArrayList<Integer> rightQuestions;
    public static ArrayList<Integer> wrongQuestions;

    private final String TAG="Examination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e(TAG,"onCreate");
        setContentView(R.layout.wa_activity_examination);
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume");
        if (null == paper) {
            paper = (Paper) getIntent().getSerializableExtra(getString(R.string.tag_paper));
            mPosition = 0;
            if (null != paper.getQuestion()){
                switchFragment(mPosition);
            }
        } else if (null == resulltFragment){
            this.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mApplication.stopMusic();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("Exam", "onActivityResult");
        if (null != resulltFragment){
            resulltFragment.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initToolBar() {
        super.initToolBar();
        mTitle.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showNextFragment() {
        mPosition++;
        switchFragment(mPosition);
    }

    private void switchFragment(int position) {
//        Log.e(TAG,"switchFragment,position="+position);
        if (null == mFragmentManager){
            mFragmentManager = getFragmentManager();
        }
        switch (position) {
            case 0:
                ReadyFragment paperFragment = new ReadyFragment();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.tag_name), getString(R.string.title_ready));
                bundle.putSerializable(getString(R.string.tag_users),paper.getUser());
                paperFragment.setArguments(bundle);
                mFragmentManager.beginTransaction().replace(R.id.context, paperFragment, getString(R.string.title_ready)).commit();
                break;
            case 1:
                mToolBar.setVisibility(View.GONE);
                AnswerFragment questionFragment = new AnswerFragment();
                questionFragment.setOnAnswerQuestionListener(this);
                Bundle bundle1 = new Bundle();
                bundle1.putString(getString(R.string.tag_name), getString(R.string.title_answer));
                bundle1.putSerializable(getString(R.string.tag_questions), paper.getQuestion());
                questionFragment.setArguments(bundle1);
                mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(getString(R.string.title_ready))).replace(R.id.context, questionFragment, getString(R.string.title_answer)).commit();
                mApplication.playMusic();
                break;
            case 2:
                mToolBar.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.title_achievement);
                resulltFragment = new ResulltFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(getString(R.string.tag_name), getString(R.string.title_achievement));
                bundle2.putInt(getString(R.string.tag_score), score);
                bundle2.putIntegerArrayList(getString(R.string.tag_wrongquestion), wrongQuestions);
                bundle2.putIntegerArrayList(getString(R.string.tag_rightquestion), rightQuestions);
                resulltFragment.setArguments(bundle2);
                mFragmentManager.beginTransaction().remove(mFragmentManager.findFragmentByTag(getString(R.string.title_answer))).replace(R.id.context, resulltFragment, getString(R.string.title_achievement)).commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_examination,menu);
        return true;
    }

    @Override
    public void onSelectedSuccess() {
        feedback(true,true);
    }

    @Override
    public void onSelectedWrong() {
        feedback(false,true);
    }

}
