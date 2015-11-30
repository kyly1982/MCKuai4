package com.mckuai.imc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mckuai.imc.R;
import com.mckuai.imc.bean.Paper;
import com.mckuai.imc.utils.NetInterface;
import com.mckuai.mcwa.activity.ExaminationActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * A placeholder fragment containing a simple view.
 */
public class WaFragment extends Fragment implements View.OnClickListener,NetInterface.OnGetQuestionListener{
    private View mView;
    private ImageButton mWa;
    private AppCompatTextView hint;

    private boolean isLoading = false;

    private static final int REQUEST_ANSWER = 4;


    public WaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mView){
            mView =inflater.inflate(R.layout.fragment_wa, container, false);
        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        YoYo.with(Techniques.Swing).playOn(hint);
    }

    private void initView(){
        mWa = (ImageButton) mView.findViewById(R.id.wa);
        hint = (AppCompatTextView) mView.findViewById(R.id.hint_getpaper);
        final RelativeLayout shandow = (RelativeLayout) mView.findViewById(R.id.wa_layout);

        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        float scal = screenWidth / 720f;
        float left = 122f * scal;
        float top = 159f * scal;
        float right = 124f * scal;
        float bottom = 87f * scal;
        float width = 474f * scal;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) width, (int) width);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth,screenWidth);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        shandow.setLayoutParams(layoutParams);
        params.setMargins((int) left, (int) top, (int) right, (int) bottom);
        mWa.setLayoutParams(params);

        mWa.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (!isLoading) {
            loadData();
        }
        YoYo.with(Techniques.Flash).playOn(mWa);
    }

    private void loadData(){
        MobclickAgent.onEvent(getActivity(), "reqPaper");
        NetInterface.getQuestions(getActivity(), this);
        isLoading = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && REQUEST_ANSWER == requestCode){
            loadData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(Paper paper) {
        MobclickAgent.onEvent(getActivity(), "reqPaper_S");
        isLoading = false;
        Intent intent = new Intent(getActivity(), ExaminationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.tag_paper), paper);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_ANSWER);
    }

    @Override
    public void onFalse(String msg) {
        //feedback(false, false);
        MobclickAgent.onEvent(getActivity(), "reqPaper_F");
        isLoading = false;
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
