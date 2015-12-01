package com.mckuai.imc.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;

import com.google.gson.Gson;
import com.mckuai.imc.R;
import com.mckuai.imc.bean.MCUser;
import com.mckuai.imc.bean.Token;
import com.mckuai.imc.utils.JsonCache;
import com.mckuai.imc.widget.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * Created by kyly on 2015/11/26.
 */
public class MCKuai4 extends Application {
    static MCKuai4 instance;
    public static MCUser user;
    public static Gson gson;
    public JsonCache cache;
    public boolean isFirstBoot = true;
    public ImageLoader loader;

    private final int IMAGE_POOL_SIZE = 3;// 线程池数量
    private final int CONNECT_TIME = 15 * 1000;// 连接时间
    private final int TIME_OUT = 30 * 1000;// 超时时间

    private String mCacheDir;
    private MediaPlayer mPlayer;
    private DisplayImageOptions circleOption;
    private DisplayImageOptions normalOption;

    public Token token;
    public Tencent tencent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    @Override
    public void onTerminate() {
        if (null != cache){
            cache.saveCacheFile();
        }
        super.onTerminate();
    }

    public static MCKuai4 getInstance() {
        return instance;
    }


    public void init() {
        gson = new Gson();
        user =readPreference();
        initImageLoader();
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        cache = new JsonCache(this);
    }



    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(750, 480)
                .threadPoolSize(IMAGE_POOL_SIZE)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                        // 对于同一url只缓存一个图
                        //.memoryCache(new UsingFreqLimitedMemoryCache(MEM_CACHE_SIZE)).memoryCacheSize(MEM_CACHE_SIZE)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO)
                .discCache(new UnlimitedDiskCache(new File(getImageCacheDir())))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), CONNECT_TIME, TIME_OUT))
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(configuration);
    }

    public MCUser readPreference() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_filename), 0);
        isFirstBoot = preferences.getBoolean(getString(R.string.preferences_isFirstBoot), true);
        if (!isFirstBoot) {
            token = new Token();
            token.setBirthday(preferences.getLong(getString(R.string.preferences_tokentime), 0));
            token.setExpires(preferences.getLong(getString(R.string.preferences_tokenexpires), 0));
            // 检查qq的token是否有效如果在有效期内则获取qqtoken
            if (token.isTokenSurvival()) {
                user = new MCUser();
                token.setToken(preferences.getString(getString(R.string.preferences_token), null));
                token.setType(preferences.getInt(getString(R.string.preferences_tokentype), 0));
                user.setId(preferences.getInt(getString(R.string.preferences_id), 0));                    //id
                user.setName(preferences.getString(getString(R.string.preferences_username), null));      //姓名,实为wx的access_token或者qq的openId
                user.setNike(preferences.getString(getString(R.string.preferences_nickname), null));      // 显示名
                user.setHeadImg(preferences.getString(getString(R.string.preferences_cover), null));      // 用户头像
                user.setGender(preferences.getString(getString(R.string.preferences_gender), null));         // 性别
                user.setProcess(preferences.getFloat(getString(R.string.preferences_process), 0));         //进度
                user.setScore(preferences.getInt(getString(R.string.preferences_score), 0));              //积分
                user.setLevel(preferences.getInt(getString(R.string.preferences_level), 0));              //level
                user.setAddr(preferences.getString(getString(R.string.preferences_addr), null));           //地址
                user.setAllScore(preferences.getLong(getString(R.string.preferences_wa_score), 0));       //mcwa积分
                user.setRanking(preferences.getLong(getString(R.string.preferences_wa_ranking), 0));      //mcwa积分排行
                user.setScoreRank(preferences.getInt(getString(R.string.preferences_wa_scorerank), 0));   //mcwa积分排行
                user.setAnswerNum(preferences.getInt(getString(R.string.preferences_wa_answercount), 0)); //mcwa回答数
                user.setUploadNum(preferences.getInt(getString(R.string.preferences_wa_uploadcount), 0)); //mcwa贡献数
            }
        }
        return user;
    }

    public void saveProfile(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.preferences_isFirstBoot),false);
        if (null != token){
            editor.putInt(getString(R.string.preferences_tokentype),token.getType());
            editor.putLong(getString(R.string.preferences_tokentime), token.getBirthday());
            editor.putLong(getString(R.string.preferences_tokenexpires),token.getExpires());
            editor.putString(getString(R.string.preferences_token), token.getToken());
        }
        if (null != user && user.isUserValid()){
            editor.putInt(getString(R.string.preferences_id), user.getId());          //id
            editor.putString(getString(R.string.preferences_username), user.getName() + "");//openid
            editor.putString(getString(R.string.preferences_nickname), user.getNike() + "");//显示名
            editor.putString(getString(R.string.preferences_cover), user.getHeadImg() + "");// 用户头像
            editor.putString(getString(R.string.preferences_gender), user.getGender() + "");// 性别
            editor.putInt(getString(R.string.preferences_score), user.getScore());         //积分
            editor.putFloat(getString(R.string.preferences_process), user.getProcess());    //进度
            editor.putInt(getString(R.string.preferences_level), user.getLevel());          //level
            editor.putString(getString(R.string.preferences_addr), user.getAddr());         //地址
            editor.putLong(getString(R.string.preferences_wa_score), user.getAllScore());   //mcwa积分
            editor.putLong(getString(R.string.preferences_wa_ranking), user.getRanking());   //mcwa积分排行
            editor.putInt(getString(R.string.preferences_wa_scorerank), user.getScoreRank());//mcwa积分排行
            editor.putInt(getString(R.string.preferences_wa_answercount), user.getAnswerNum());//mcwa回答数
            editor.putInt(getString(R.string.preferences_wa_uploadcount), user.getUploadNum());//mcwa贡献数
            editor.commit();
        }
    }

    public String getImageCacheDir() {
        return getCacheRoot() + File.separator + getString(R.string.imagecache_dir) + File.separator;
    }

    public String getJsonFile() {
        return getCacheRoot() + File.separator + getString(R.string.jsoncache_dir) + File.separator + getString(R.string.jsoncache_file);
    }

    private String getCacheRoot() {
        if (null == mCacheDir) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                if (null != getExternalCacheDir()) {
                    mCacheDir = getExternalCacheDir().getPath();
                } else {
                    mCacheDir = getCacheDir().getPath();
                }
            } else {
                mCacheDir = getCacheDir().getPath();
            }
        }
        return mCacheDir;
    }

    public boolean isLogin(){
        return  (null != user && user.isUserValid());
    }

    public boolean isTokenValid(){
        return (null != token && token.isTokenSurvival());
    }

    public DisplayImageOptions getCircleOption() {
        if (null == circleOption) {
            circleOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
        }
        return circleOption;
    }

    public DisplayImageOptions getNormalOption(){
        if (null == normalOption) {
            normalOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        }
        return normalOption;
    }


    public boolean isMusicPlaying(){
        return true;
    }

    public boolean switchPlayPauseMusic(){
        return true;
    }

    public void stopMusic(){

    }

    public void playMusic(){

    }

    public void logout(){
        token.setExpires(0);
        saveProfile();
        token = null;
        user = null;
    }
}
