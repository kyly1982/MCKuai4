package com.mckuai.imc.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

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
    public com.umeng.socialize.controller.UMSocialService shareService;


    private final int IMAGE_POOL_SIZE = 3;// 线程池数量
    private final int CONNECT_TIME = 15 * 1000;// 连接时间
    private final int TIME_OUT = 30 * 1000;// 超时时间

    private String mCacheDir;
    private DisplayImageOptions circleOption;
    private DisplayImageOptions normalOption;

    private boolean isDetected = false;
    private boolean isQQInstalled = false;
    private boolean isWXInstalled = false;


    public Token token;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    @Override
    public void onTerminate() {
        if (null != cache) {
            cache.saveCacheFile();
        }
        super.onTerminate();
    }

    public static MCKuai4 getInstance() {
        return instance;
    }


    public void init() {
        gson = new Gson();
        user = readPreference();
        initImageLoader();
        shareService = UMServiceFactory.getUMSocialService("com.mckuai.imc");
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

    public void saveProfile() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_file), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.preferences_isFirstBoot), false);
        if (null != token) {
            editor.putInt(getString(R.string.preferences_tokentype), token.getType());
            editor.putLong(getString(R.string.preferences_tokentime), token.getBirthday());
            editor.putLong(getString(R.string.preferences_tokenexpires), token.getExpires());
            editor.putString(getString(R.string.preferences_token), token.getToken());
        }
        if (null != user && user.isUserValid()) {
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

    public boolean isLogin() {
        return (null != user && user.isUserValid());
    }

/*    public boolean isTokenValid() {
        return (null != token && token.isTokenSurvival());
    }*/

    public DisplayImageOptions getCircleOption() {
        if (null == circleOption) {
            circleOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();
        }
        return circleOption;
    }

    public DisplayImageOptions getNormalOption() {
        if (null == normalOption) {
            normalOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        }
        return normalOption;
    }


    public boolean isMusicPlaying() {
        return true;
    }

    public boolean switchPlayPauseMusic() {
        return true;
    }

    public void stopMusic() {

    }

    public void playMusic() {

    }

    public void logout() {
        token.setExpires(0);
        saveProfile();
        token = null;
        user = null;
    }



    public void share(@NonNull final Context context, String title, String content, String url, UMImage image) {
        switch (detectedSocialAPP(context)){
            case 1:
                shareToQQZone(context,title,content,url,image);
                break;
            case 2:
                shareToWXCircle(context,title,content,url,image);
                break;
            default:
                break;
        }
    }


    private void shareToQQZone(@NonNull final Context context, String title, String content, String url, UMImage image) {
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setTitle(title);
        qzone.setShareContent(content);
        qzone.setTargetUrl(url);
        qzone.setAppWebSite("http://www.mckuai.com/");
        if (null != image) {
            qzone.setShareImage(image);
        }
        shareService.setShareMedia(qzone);
        shareService.postShare(context, SHARE_MEDIA.QZONE, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
                Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                String msg;
                switch (i) {
                    case 200:
                        msg = "分享成功！";
                        break;
                    case -101:
                        msg = "分享失败，没有授权！";
                        break;
                    default:
                        msg = "分享失败[code=" + i + "]!";
                        break;
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void shareToWXCircle(@NonNull final Context context,String title, String content, String url, UMImage image) {
        WeiXinShareContent wx = new WeiXinShareContent();
        wx.setTitle(title);
        wx.setShareContent(content);
        wx.setTargetUrl(url);
        wx.setAppWebSite("http://www.mckuai.com/");
        if (null != image) {
            wx.setShareImage(image);
        }
        shareService.setShareMedia(wx);
        shareService.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
                Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                String msg;
                switch (i) {
                    case 200:
                        msg = "分享成功！";
                        break;
                    case -101:
                        msg = "分享失败，没有授权！";
                        break;
                    default:
                        msg = "分享失败[code=" + i + "]!";
                        break;
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int detectedSocialAPP(Context context) {
        if (!isDetected) {
            String appID_WX = "wx49ba2c7147d2368d";
            String appSecret_WX = "85aa75ddb9b37d47698f24417a373134";
            String appId_QQ = "101155101";
            String appKey_QQ = "78b7e42e255512d6492dfd135037c91c";

            QZoneSsoHandler qq = new QZoneSsoHandler((Activity) context, appId_QQ, appKey_QQ);
            if (qq.isClientInstalled()) {
                isDetected = true;
                isQQInstalled = true;
                qq.addToSocialSDK();
            } else {
                UMWXHandler wx = new UMWXHandler((Activity) context, appID_WX, appSecret_WX);
                if (wx.isClientInstalled()) {
                    wx.setToCircle(true);
                    isDetected = true;
                    isWXInstalled = true;
                    wx.addToSocialSDK();
                }
            }
        }

        if (isDetected) {
            if (isQQInstalled) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 0;
        }
    }
}
