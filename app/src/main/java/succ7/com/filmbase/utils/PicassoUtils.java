package succ7.com.filmbase.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import succ7.com.filmbase.R;


/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/6/15 0015
 */
public class PicassoUtils {

    /**
     * 根据图片url将图片显示在ImageView控件上
     *
     * @param context   Context
     * @param url       String
     * @param imageView ImageView
     */
    public static void showImage(Context context, String url, ImageView imageView) {
        showImage(context, url, R.mipmap.defalut_avatar, imageView);
    }

    /**
     * 根据图片url将图片显示在ImageView控件上
     *
     * @param context         Context
     * @param url             String
     * @param defalutAvatarId 默认图片资源id
     * @param imageView       ImageView
     */
    public static void showImage(Context context, String url, int defalutAvatarId, ImageView imageView) {
        showImage(context, url, context.getResources().getDrawable(defalutAvatarId), imageView);
    }

    /**
     * 根据图片url将图片显示在ImageView控件上
     *
     * @param context         Context
     * @param url             String
     * @param defalutDrawable 默认图片
     * @param imageView       ImageView
     */
    public static void showImage(Context context, String url, Drawable defalutDrawable, ImageView imageView) {
        /*Picasso.with(context)
                .load(url)
                .error(defalutDrawable)
                .placeholder(defalutDrawable)
                .into(imageView);*/
        //可以缓存到SD止进行持久化
        ImageWorker.getDefaultWorker().loadImage(url, defalutDrawable, imageView);
    }

    /**
     * 取消请求
     *
     * @param context   Context
     * @param imageView ImageView
     */
    public static void cancelRequest(Context context, ImageView imageView) {
        Picasso.get().cancelRequest(imageView);
    }
}
