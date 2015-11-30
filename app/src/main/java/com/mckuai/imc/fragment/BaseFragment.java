package com.mckuai.imc.fragment;


import android.app.Fragment;
import android.graphics.Point;
import android.util.Log;
import android.view.View;


import com.loopj.android.http.RequestParams;
import com.mckuai.imc.R;
import com.mckuai.imc.activity.MCKuai4;
import com.mckuai.imc.utils.JsonCache;
import com.umeng.analytics.MobclickAgent;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    protected  boolean isLoading = false;    //正在加载数据
    protected  boolean isCacheEnabled = true;         //启用缓存
    protected boolean isSecondCacheEnable = true;//第二个项目的缓存
    protected boolean isShowCatche = true;//显示的是缓存
    protected  String mTitle;

    private Point mPoint;
    private JsonCache mCache = MCKuai4.getInstance().cache;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mTitle);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mTitle);
    }

    /**
     * 显示通知
     * @param level 通知级别， 2为错误，1为警告，其它为普通
     * @param msg 通知内容
     * @param rootId 用于显示通知的viewgroup
     */
    protected  void showNotification(int level,String msg,int rootId){
    }



    private String getColorString(int colorResId)
    {
        int color = getResources().getColor(colorResId);
        String c = "#" + Integer.toHexString(color);
        return c.toUpperCase();
    }

    protected  void showMessage(String title,String msg){

    }

    protected  void showAlert(final String title,final String msg, final View.OnClickListener onCancle, final View.OnClickListener onOk){
    }

    protected void popupLoadingToast(String msg){

    }

    protected  void cancleLodingToast(boolean isSuccess){

    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * 将数据缓存到缓存中
     *
     * @param url
     *            数据所归属的url
     * @param data
     *            要缓存的数据
     */
    public void cacheData(String url, String data)
    {
        mCache.put(url, data);
    }

    /**
     * 将数据缓存到缓存中
     *
     * @param url
     *            数据所归属的url
     * @param params
     *            对应的请求参数
     * @param data
     *            要缓存的数据
     */
    public void cacheData(String url, RequestParams params, String data)
    {
        if (null == params)
        {
            mCache.put(url, data);
        } else
        {
            mCache.put((url + "&" + params.toString()), data);
        }
    }

    /**
     * 从缓存中获取对应url缓存下来的数据
     *
     * @param url
     *            要获取的数据对应的url
     * @return 如果对应的url有缓存数据,则返回缓存数据,否则返回空
     */
    public String getData(String url)
    {
        return mCache.get(url);
    }

    /**
     * 从缓存中获取对应url缓存下来的数据
     *
     * @param url
     *            要获取的数据对应的url
     * @param params
     *            对应url的参数
     * @return 如果对应的url有缓存数据,则返回缓存数据,否则返回空
     */
    public String getData(String url, RequestParams params)
    {
        if (null == params)
        {
            return mCache.get(url);
        } else
        {
            return mCache.get((url + "&" + params.toString()));
        }
    }

    public boolean onBackKeyPressed(){
        return false;
    }

    public void onRightButtonClicked(String searchContent){
        Log.e("BF","标题栏右侧按钮");
    }

    public void onPageShow(){

    }

}
