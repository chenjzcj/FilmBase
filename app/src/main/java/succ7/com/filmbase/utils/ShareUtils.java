package succ7.com.filmbase.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bmob.UserHelper;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/22 0022 21:30
 */
public class ShareUtils {

    /**
     * 配置初始化
     */
    public static void init() {
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxbe87617e2858ea1d", "264c5971f8788cf32903455b5c6770c1");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105583248", "ZNjTlaLEddmsLK1Z");
    }

    /**
     * 分享电影
     *
     * @param activity Activity
     * @param film     Film
     */
    public static void shareFilm(Activity activity, Film film) {
        String content = "电影基地用户 " + film.getUser().getUsername() + " 来自" + film.getFilmSource() + "的分享~~\n点击立即播放";
        shareToFriend(activity, film.getFilmName(), content, Constants.APP_WEB, film.getFilmPicUrl());
    }

    /**
     * 分享给好友邀请好友
     *
     * @param activity Activity
     */
    public static void inviteFriend(Activity activity) {
        String content = "你的电影基地好友" + UserHelper.getCurrentUserName() + "邀请您一起在线" +
                "看电影哟~~ \n记得注册邀请码是:" + UserHelper.getCurrentUser().getInviteCode();
        String title = "电影基地--亿万影迷的最爱";
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.share_to_wx);
        shareToFriend(activity, title, content, Constants.APP_WEB, bitmap);
    }

    /**
     * 分享到微信
     */
    private static void shareToFriend(final Activity activity, String title, String content, String targetUrl, Object obj) {
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
        UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                //Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                //Toast.makeText(activity, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                //Toast.makeText(activity, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
            }
        };
        UMImage image = null;
        if (obj instanceof Bitmap) {
            image = new UMImage(activity, (Bitmap) obj);
        } else if (obj instanceof String) {
            image = new UMImage(activity, (String) obj);
        } else if (obj instanceof Integer) {
            image = new UMImage(activity, (Integer) obj);
        } else if (obj instanceof byte[]) {
            image = new UMImage(activity, (byte[]) obj);
        } else if (obj instanceof File) {
            image = new UMImage(activity, (File) obj);
        }
        new ShareAction(activity).setDisplayList(displaylist)
                .withText(content)
                .withTitle(title)
                .withTargetUrl(targetUrl)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
    }
}
