package succ7.com.filmbase.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import org.xutils.common.util.DensityUtil;

import java.io.File;
import java.lang.ref.WeakReference;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.BaseApplication;

public class ImageWorker {
    private static final int FADE_IN_TIME = 200;
    private boolean mFadeInBitmap = false;
    private boolean mExitTasksEarly = false;
    protected boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();
    protected Resources mResources;
    private String mDiskCacheDir;
    private static LruCache<String, BitmapDrawable> mImageCache;
    private static ImageWorker mInstance = null;
    private ImageWorkerListener mImageWorkerListener;
    private boolean mIsCache;
    private int mMaxWidth;
    private int pix = 0;
    private boolean mLoadLocal;

    /**
     * 获取默认的ImageWorker对象
     *
     * @return ImageWorker
     */
    public static synchronized ImageWorker getDefaultWorker() {
        if (mInstance == null) {
            mInstance = new ImageWorker(BaseApplication.getContext(), null, true);
        }
        return mInstance;
    }

    /**
     * @param context      上下文
     * @param diskCacheDir 缓存目录
     * @param isCache      是否需要进行缓存
     */
    public ImageWorker(Context context, String diskCacheDir, boolean isCache) {
        mDiskCacheDir = diskCacheDir;
        mIsCache = isCache;
        if (TextUtils.isEmpty(mDiskCacheDir)) {
            mDiskCacheDir = PathUtils.generatePhotoCacheDir();
        }
        File cacheDir = new File(mDiskCacheDir);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        mResources = context.getResources();
        if (mImageCache == null && mIsCache) {
            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
            int cacheSize = 1024 * 1024 * memClass / 4;
            mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
                @Override
                protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    System.gc();
                }

                @Override
                protected int sizeOf(String key, BitmapDrawable drawable) {
                    if (drawable != null) {
                        Bitmap bitmap = drawable.getBitmap();
                        if (bitmap != null && !bitmap.isRecycled())
                            return bitmap.getRowBytes() * bitmap.getHeight();
                    }
                    return 0;
                }
            };
        }
    }

    public interface ImageWorkerListener {
        void onImageWorker(BitmapDrawable drawable, Object object);
    }

    public void setImageWorkerListener(ImageWorkerListener listener) {
        mImageWorkerListener = listener;
    }

    /**
     * 获取默认的LruCache对象
     *
     * @return LruCache < String,BitmapDrawable >
     */
    public LruCache<String, BitmapDrawable> getDefaultLruCache() {
        return ImageWorker.mImageCache;
    }


    /**
     * 加载图片
     *
     * @param url       图片url
     * @param resId     默认图片资源id
     * @param imageView ImageView控件
     * @param width     指定宽度
     * @param height    指定高度
     */
    public void loadImage(String url, int resId, final ImageView imageView, int width, int height) {
        loadImage(url, mResources.getDrawable(resId), imageView, width, height, 0);
    }

    /**
     * 加载图片
     *
     * @param url       图片url
     * @param resId     默认图片资源id
     * @param imageView ImageView控件
     */
    public void loadImage(String url, int resId, final ImageView imageView) {
        loadImage(url, mResources.getDrawable(resId), imageView);
    }

    /**
     * 加载图片
     *
     * @param url       图片url
     * @param drawable  默认图片
     * @param imageView ImageView控件
     */
    public void loadImage(final String url, final Drawable drawable, final ImageView imageView) {
        loadImage(url, drawable, imageView, 0);
    }

    /**
     * @param url       图片url
     * @param resId     默认图片资源id
     * @param imageView ImageView控件
     * @param px        图片倒角,为0时为不需要进行倒角
     */
    public void loadImage(final String url, int resId, final ImageView imageView, int px) {
        loadImage(url, mResources.getDrawable(resId), imageView, px);
    }

    /**
     * 加载图片
     *
     * @param url       图片url
     * @param drawable  默认图片
     * @param imageView ImageView控件
     * @param px        图片倒角,为0时为不需要进行倒角
     */
    public void loadImage(final String url, final Drawable drawable, final ImageView imageView, int px) {
        loadImage(url, drawable, imageView, imageView.getWidth(), imageView.getHeight(), px);
    }

    /**
     * @param url       图片url
     * @param drawable  默认图片
     * @param imageView ImageView控件
     * @param width     指定宽度
     * @param height    指定高度
     * @param px        图片倒角,为0时为不需要进行倒角
     */
    public void loadImage(final String url, final Drawable drawable, final ImageView imageView, int width, int height, int px) {
        if (null == imageView) {
            return;
        }
        //1.如果没有提供url,则设置默认图片显示
        if (TextUtils.isEmpty(url)) {
            if (drawable != null) {
                if (px > 0) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    imageView.setImageBitmap(BitmapUtils.toRoundCorner(bitmap, px));
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }
            return;
        }

        //2.判断是否imageView中包含中图片的url
        final String loadUrl = String.valueOf(imageView.getTag(R.string.app_name));
        if (!TextUtils.isEmpty(loadUrl) && loadUrl.equals(url)) {
            final BitmapWorkerTask bwt = getBitmapWorkerTask(imageView);
            if (null != bwt && !bwt.isCancelled()) {
                if (px > 0) {
                    Bitmap bs = ((BitmapDrawable) drawable).getBitmap();
                    imageView.setImageBitmap(BitmapUtils.toRoundCorner(bs, px));
                } else {
                    imageView.setImageDrawable(drawable);
                }
                return;
            }
        } else {
            cancelWork(imageView);
        }

        //3.从内存缓存中取,如果存在,则显示缓存中的(Bitmap found in memory cache)
        BitmapDrawable bitmapDrawable = null;
        if (mImageCache != null && mIsCache) {
            bitmapDrawable = mImageCache.get(getUrlHashCode(url));
        }
        if (bitmapDrawable != null) {
            if (px > 0) {
                Bitmap bitmap = bitmapDrawable.getBitmap();
                imageView.setImageBitmap(BitmapUtils.toRoundCorner(bitmap, px));
            } else {
                imageView.setImageDrawable(bitmapDrawable);
            }
            return;
        }
        //4.先显示默认图片,并将url设置成imageView的tag值
        if (px > 0) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            imageView.setImageBitmap(BitmapUtils.toRoundCorner(bitmap, px));
        } else {
            imageView.setImageDrawable(drawable);
        }
        imageView.setTag(R.string.app_name, url);
        //5.从网络加载
        if ((width <= 0 || height <= 0)) {
            ViewTreeObserver vto = imageView.getViewTreeObserver();
            ViewTreeObserver.OnPreDrawListener listener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    startAsyncWork(url, imageView, imageView.getWidth(), imageView.getHeight(), null);
                    return true;
                }
            };
            vto.addOnPreDrawListener(listener);
        } else {
            startAsyncWork(url, imageView, width, height, null);
        }

    }

    /**
     * 取消任务
     *
     * @param imageView ImageView
     */
    public static void cancelWork(ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        imageView.setTag(R.string.imageworker_tag, null);
        imageView.setTag(R.string.app_name, null);
        if (bitmapWorkerTask != null) {
            bitmapWorkerTask.cancel(true);
            bitmapWorkerTask.mImageViewReference.clear();
        }
    }

    /**
     * 加载图片
     *
     * @param url          图片url
     * @param attachObject 附属对象
     * @param width        指定宽度
     * @param height       指定高度
     */
    public void loadImage(String url, Object attachObject, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mImageCache != null && mIsCache) {
            BitmapDrawable drawable = mImageCache.get(getUrlHashCode(url));
            if (drawable != null && mImageWorkerListener != null) {
                mImageWorkerListener.onImageWorker(drawable, attachObject);
                return;
            }
        }
        startAsyncWork(url, null, width, height, attachObject);
    }

    /**
     * 开始异步任务
     *
     * @param url       图片url
     * @param imageView ImageView控件
     * @param width     指定宽度
     * @param height    指定高度
     * @param object    Object
     */
    private void startAsyncWork(final String url, final ImageView imageView, int width, int height, final Object object) {
        if (cancelPotentialWork(url, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, object, width, height);
            if (imageView != null)
                imageView.setTag(R.string.imageworker_tag, task);
            task.executeOnExecutor(AsyncTask.MULTI_THREAD_EXECUTOR, url);
        }
    }


    /**
     * 从本地获取图片
     *
     * @param url    图片路径
     * @param width  宽
     * @param height 高
     * @return BitmapDrawable
     */
    public BitmapDrawable loadImageFromLocal(String url, int width, int height) {
        BitmapDrawable drawable = null;
        String path = mDiskCacheDir + File.separator + getUrlHashCode(url);
        if (mImageCache != null && mIsCache) {
            drawable = mImageCache.get(getUrlHashCode(url));
        }
        Bitmap bitmap = null;
        if (drawable == null) {
            try {
                if (SDCardUtil.isSDCardExist()) {
                    if (width != 0 && height != 0) {
                        bitmap = BitmapUtils.loadBitmapFromFile(path, width, height);
                    } else {
                        bitmap = BitmapUtils.loadBitmapFromFile(path);
                    }
                }
                if (bitmap != null)
                    drawable = new BitmapDrawable(mResources, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return drawable;
    }

    /**
     * 获取图片url的hashCode
     *
     * @param url 图片url
     * @return 图片url的hashCode
     */
    private String getUrlHashCode(String url) {
        return "" + url.hashCode();
    }

    /**
     * 设置图片是否渐变显示
     *
     * @param fadeIn true即为渐变显示图片
     */
    public void setImageFadeIn(boolean fadeIn) {
        mFadeInBitmap = fadeIn;
    }

    /**
     * 设置是否需要提前退出任务
     *
     * @param exitTasksEarly true即提前结束任务
     */
    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
        setPauseWork(false);
    }

    /**
     * 设置暂停任务
     *
     * @param pauseWork 是否暂停任务
     */
    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    /**
     * 三级缓存
     *
     * @param url    图片url地址
     * @param width  图片宽
     * @param height 图片高
     * @return 从本地或者网络获取到的bitmap
     */
    protected Bitmap processBitmap(String url, int width, int height) {
        String path = url;
        //if(!mLoadLocal){
        boolean isExists = true;
        if (url.contains("http:")) {
            path = mDiskCacheDir + File.separator + getUrlHashCode(url);
            File file = new File(path);
            isExists = file.exists();
        }

        // 从sdcard加载
        Bitmap bitmap = null;
        if (isExists) {
            if (width != 0 && height != 0) {
                bitmap = BitmapUtils.loadBitmapFromFile(path, width, height);
            } else {
                bitmap = BitmapUtils.loadBitmapFromFile(path, mMaxWidth);
            }
        }
        // 从网络加载
        if (bitmap == null) {
            try {
                if (SDCardUtil.isSDCardExist()) {
                    boolean ret = FileUtils.downloadFile(url, path);
                    if (!ret) {
                        return null;
                    }
                    if (width != 0 && height != 0) {
                        bitmap = BitmapUtils.loadBitmapFromFile(path, width, height);
                    } else {
                        bitmap = BitmapUtils.loadBitmapFromFile(path, mMaxWidth);
                    }
                } else {
                    if (width != 0 && height != 0) {
                        bitmap = BitmapUtils.getBitmapFromUrl(url, width, height);
                    } else {
                        bitmap = BitmapUtils.getBitmapFromUrl(url);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bitmap;
    }


    /**
     * 取消潜在的任务
     *
     * @param data      Object
     * @param imageView ImageView
     * @return true 取消成功
     */
    public static boolean cancelPotentialWork(Object data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.mUrl;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
                imageView.setTag(R.string.imageworker_tag, null);
                imageView.setTag(R.string.app_name, null);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active work task (if any) associated with
     * this imageView. null if there is no such task.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Object object = imageView.getTag(R.string.imageworker_tag);
            if (object instanceof BitmapWorkerTask) {
                return (BitmapWorkerTask) object;
            }
        }
        return null;
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    private final class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {
        private String mUrl;
        private final WeakReference<ImageView> mImageViewReference;
        private final Object mObject;
        private int mWidth;
        private int mHeight;

        public BitmapWorkerTask(ImageView imageView, Object object, int widht, int height) {
            if (imageView != null) {
                mImageViewReference = new WeakReference<>(imageView);
            } else {
                mImageViewReference = null;
            }
            mObject = object;
            mWidth = widht;
            mHeight = height;
        }

        /**
         * Background processing.
         */
        @Override
        protected BitmapDrawable doInBackground(String... params) {

            synchronized (mPauseWorkLock) {
                while (mPauseWork && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            mUrl = params[0];
            Bitmap bitmap = null;
            BitmapDrawable drawable = null;

            if (!isCancelled() && !mExitTasksEarly) {
                bitmap = processBitmap(params[0], mWidth, mHeight);
            }
            if (!mIsCache && isCancelled()) {
                if (bitmap != null)
                    bitmap.recycle();
                return null;
            }

            if (bitmap != null) {
                drawable = new BitmapDrawable(mResources, bitmap);
                if (mIsCache && mImageCache != null)
                    mImageCache.put(getUrlHashCode(mUrl), drawable);
            }
            return drawable;
        }

        /**
         * Once the image is processed, associates it to the imageView
         */
        @Override
        protected void onPostExecute(BitmapDrawable value) {
            // if cancel was called on this task or the "exit early" flag is set
            // then we're done
            if (isCancelled() || mExitTasksEarly) {
                value = null;
            }

            ImageView imageView = getAttachedImageView();
            // if (value != null) {
            if (imageView != null) {
                String url = String.valueOf(imageView.getTag(R.string.app_name));
                if (!TextUtils.isEmpty(url) && url.equals(mUrl)) {
                    if (value != null)
                        setImageDrawable(imageView, value);
                }
                imageView.setTag(R.string.imageworker_tag, null);
            }

            if (mImageWorkerListener != null && value != null) {
                mImageWorkerListener.onImageWorker(value, getAttachObject());
            }
            // }
            if (null != mImageViewReference) {
                mImageViewReference.clear();
            }

        }

        protected void onCancelled(BitmapDrawable value) {
            super.onCancelled(value);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
            if (null != mImageViewReference) {
                mImageViewReference.clear();
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the
         * ImageView's task still points to this task as well. Returns null
         * otherwise.
         */
        private ImageView getAttachedImageView() {
            if (mImageViewReference != null) {
                final ImageView imageView = mImageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

                if (this == bitmapWorkerTask) {
                    return imageView;
                }
            }
            return null;
        }

        private Object getAttachObject() {
            return mObject;
        }
    }

    private void setImageDrawable(ImageView imageView, Drawable drawable) {
        if (mFadeInBitmap) {
            if (imageView.getDrawable() != null) {
                TransitionDrawable td = new TransitionDrawable(
                        new Drawable[]{imageView.getDrawable(), drawable});
                imageView.setImageDrawable(td);
                td.startTransition(FADE_IN_TIME);
            } else {
                if (pix > 0) {
                    Bitmap bs = ((BitmapDrawable) drawable).getBitmap();
                    imageView.setImageBitmap(BitmapUtils.toRoundCorner(bs, pix));
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }
        } else {
            if (pix > 0) {
                Bitmap bs = ((BitmapDrawable) drawable).getBitmap();
                imageView.setImageBitmap(BitmapUtils.toRoundCorner(bs, pix));
            } else {
                imageView.setImageDrawable(drawable);
            }
        }
    }

    /**
     * 清除缓存目录下的所有文件
     */
    public void clearDiskCache() {
        File file = new File(mDiskCacheDir);
        final File[] files = file.listFiles();
        for (File file1 : files) {
            file1.delete();
        }
    }

    public int getmMaxWidth() {
        return mMaxWidth;
    }

    public void setmMaxWidth(int mMaxWidth) {
        this.mMaxWidth = DensityUtil.dip2px(mMaxWidth);
    }

    public void setIsCache(boolean cache) {
        this.mIsCache = cache;
    }

    public void setLoadLocal(boolean localLocal) {
        this.mLoadLocal = localLocal;
    }

    /**
     * 资源回收
     */
    public static void destroy() {
        if (mInstance != null) {
            mInstance.clearCache();
            mInstance = null;
        }
        if (mImageCache != null) {
            mImageCache = null;
        }
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        if (null != mImageCache) {
            mImageCache.evictAll();
        }
        System.gc();
    }

}
