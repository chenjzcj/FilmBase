package succ7.com.filmbase.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.view.dialog.CommonDialog;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/3/21 0021
 * 应用更新工具类
 */
public class UpdateUtils {

    /**
     * 检查更新
     *
     * @param context 上下文
     */
    public static void checkUpdate(final Context context) {
        BmobHelper.requestServer(context, Constants.REQUEST_TYPE_FIND_VERSION, null);
    }

    /**
     * 发现新版本,提示应用更新对话框
     *
     * @param apkUrl String
     */
    public static void showUpdateDialog(final String apkUrl, final Context context,
                                        String content, final boolean isForce) {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setTitle(context.getString(R.string.version_update_hint,
                context.getString(R.string.app_name)));
        if (content == null) {
            commonDialog.setMessage(context.getString(R.string.verson_find_new_version));
        } else {
            commonDialog.setMessage(content);
        }
        commonDialog.setPositiveButton(context.getString(R.string.update_immediately), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(apkUrl, context, isForce);
                dialog.dismiss();
            }
        });
        commonDialog.setNegativeButton(context.getString(R.string.version_ignore), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isForce)
                    dialog.dismiss();
            }
        });
        commonDialog.setCancelable(!isForce);
        commonDialog.show();
    }

    /**
     * 下载apk
     *
     * @param apkUrl apk下载地址
     */
    public static void downloadApk(final String apkUrl, final Context context, final boolean isForce) {
        RequestParams params = new RequestParams(apkUrl);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            private ProgressBar pb;
            private TextView tv;
            private CommonDialog commonDialog1;

            @Override
            public void onWaiting() {
                LogUtils.i("onWaiting");
                commonDialog1 = new CommonDialog(context);
                View view = LayoutInflater.from(context).inflate(R.layout.layout_download_apk, null);
                pb = (ProgressBar) view.findViewById(R.id.pb_downapk);
                tv = (TextView) view.findViewById(R.id.tv);
                commonDialog1.setCancelable(false);
                commonDialog1.setContent(view);
                pb.setMax(100);
            }

            @Override
            public void onStarted() {
                if (isForce) {
                    commonDialog1.show();//如果需要强制更新,则直接在页面上,否则只在通知栏显示
                }
                LogUtils.i("onStarted:" + apkUrl);
                NotificationUtils.createDownloadNotification(context, context.getString(R.string.start_download));
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtils.i(current + "/" + total);
                String progressInfo = context.getString(R.string.app_name) +
                        context.getString(R.string.downloading_progress, (int) (current / (total / 100)));
                tv.setText(progressInfo);
                pb.setProgress((int) (current / (total / 100)));
                NotificationUtils.showDownloadNotification(current, total, progressInfo);
            }

            @Override
            public void onSuccess(File result) {
                LogUtils.i("onSuccess:" + result.getAbsolutePath());
                ApkUtils.installApk(context, result);
                NotificationUtils.dismissDownloadNotification();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("onError:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                if (commonDialog1 != null) commonDialog1.dismiss();
                LogUtils.i("onFinished");
                ToastUtils.showShortToast(context, context.getString(R.string.download_finish));
            }
        });
    }
}
