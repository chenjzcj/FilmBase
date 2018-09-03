package succ7.com.filmbase.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.base.TabBaseFragment;
import succ7.com.filmbase.bmob.BmobHelper;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/5 0005 10:35
 * 从相册中选择,拍一张等功能
 */
public class PhotoUtils {
    /* 用来标识请求照相功能 */
    public static final int CAMERA_WITH_DATA = 50;
    /* 用来标识请求gallery */
    public static final int PHOTO_PICKED_WITH_DATA = 60;
    /* 用来标识裁剪的返回 */
    public static final int CUT_PHOTO = 70;

    public static Uri tempuri;

    /**
     * 拍一张照片
     * 需要权限:一定要记得在清单文件声明以下两个权限
     * <uses-permission android:name="android.permission.CAMERA" />
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * @param obj obj只能为Activity或者Fragment
     */
    public static void takePicture(Object obj) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempuri = Uri.fromFile(PathUtils.generateAvatarFilePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
        startActivityFromActivityOrFragment(obj, intent, CAMERA_WITH_DATA);
    }

    /**
     * 从相册中选择
     */
    public static void selectFromAlbum(Object obj) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型 如果朋友们要限制上传到服务器的图片类型时可以直接写如
         * ："image/jpeg 、 image/png等的类型"
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityFromActivityOrFragment(obj, intent, PHOTO_PICKED_WITH_DATA);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri Uri
     */
    public static void startPhotoZoom(Object obj, Uri uri) {
        /**
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityFromActivityOrFragment(obj, intent, CUT_PHOTO);
    }

    /**
     * 从Activity或者Fragment开启Activity
     *
     * @param obj         只能为Activity或者Fragment
     * @param intent      Intent
     * @param requestCode 请求码
     */
    private static void startActivityFromActivityOrFragment(Object obj, Intent intent, int requestCode) {
        Fragment fragment = null;
        Activity activity = null;
        if (obj instanceof Fragment) {
            fragment = (Fragment) obj;
        } else if (obj instanceof Activity) {
            activity = (Activity) obj;
        } else {
            throw new IllegalArgumentException("obj can only be Activity or Fragment,but get" + obj);
        }
        if (fragment != null)
            fragment.startActivityForResult(intent, requestCode);
        if (activity != null)
            activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 保存裁剪之后的图片数据并上传到服务器
     *
     * @param picdata Intent
     */
    public static void saveAndUpload(Object obj, Intent picdata) {
        if (picdata != null) {
            Bundle extras = picdata.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if (photo == null) {
                    if (obj instanceof Fragment) {
                        ((TabBaseFragment) obj).hideWaitDialog();
                        ToastUtils.showShortToast(((TabBaseFragment) obj).getActivity(), R.string.text_failed);
                    } else if (obj instanceof Activity) {
                        ((ScrollerBaseUIActivity) obj).hideWaitDialog();
                        ToastUtils.showShortToast(((ScrollerBaseUIActivity) obj).getApplication(), R.string.text_failed);
                    }
                    return;
                }
                //将图片压缩
                File finalfile = PathUtils.generateAvatarFilePath();
                BitmapUtils.saveBitmpa2File(photo, finalfile);
                if (obj instanceof Fragment) {
                    upAvatar((Fragment) obj, finalfile);
                } else if (obj instanceof Activity) {
                    upAvatar((Activity) obj, finalfile);
                }
            }
        }
    }

    private static void upAvatar(Fragment obj, File finalfile) {
        BmobHelper.requestServer(obj.getActivity(), Constants.REQUEST_TYPE_UPLOAD_PIC, null, finalfile.getAbsolutePath());
    }

    private static void upAvatar(Activity obj, File finalfile) {
        BmobHelper.requestServer(obj, Constants.REQUEST_TYPE_UPLOAD_PIC, null, finalfile.getAbsolutePath());
    }

}
