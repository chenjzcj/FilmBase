package succ7.com.filmbase.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

import succ7.com.filmbase.R;

/**
 * Created by MZIA(527633405@qq.com) on 2016/1/15 0015 11:12
 * Bitmap操作工具类
 */
public class BitmapUtils {

    /**
     * 将bitmap转换成字节数组形式
     *
     * @param bitmap 需要转换的bitmap对象
     * @return bitmap的字节数组
     */
    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param bytes      需要保存的字节数组
     * @param outputFile 保存的文件路径
     * @return 保存后的文件
     */
    public static File getFileFromBytes(byte[] bytes, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fos = new FileOutputStream(file);
            stream = new BufferedOutputStream(fos);
            stream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 通过图片资源名称获取图片资源id(扩展 : 比如想根据资源名称获取layout下的文件id,可以将drawable替换成layout)
     *
     * @param activity Activity
     * @param iconName icon名称,不包含后缀
     * @return 图片资源id
     */
    public static int getResIDByResName(Activity activity, String iconName) {
        Resources resources = activity.getResources();
        return resources.getIdentifier(activity.getPackageName()
                + ":drawable/" + iconName, null, null);
    }

    /**
     * 从字节数组中解析出bitmap
     *
     * @param data 图片的字节数组
     * @return 从字节数组中解析出的bitmap
     */
    public static Bitmap decodeBitmapFromByte(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 图片缩放
     *
     * @param filePath 图片路径
     * @param w        指定的宽
     * @param h        指定的高
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap(String filePath, int w, int h) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        int width = (int) Math.ceil(options.outWidth / (float) w);
        int height = (int) Math.ceil(options.outHeight / (float) h);
        if (width > 1 && height > 1) {
            if (height > width) {
                options.inSampleSize = height;
            } else {
                options.inSampleSize = width;
            }
        }
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 保存图片到文件
     *
     * @param bitmap 需要保存的图片
     * @param file   图片保存的文件
     * @return 是否保存成功
     */
    public static boolean saveBitmpa2File(Bitmap bitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos)) {
                fos.flush();
                fos.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从文件中获取图片
     *
     * @param file 目标文件
     * @return 获取到的图片
     * @throws FileNotFoundException 没有找到文件异常
     */
    public static Bitmap getBitmapFromFile(File file) throws FileNotFoundException {
        if (file == null)
            return null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inJustDecodeBounds = false;

        int rate = (int) (options.outHeight / (float) options.outWidth);
        if (rate <= 0) {
            rate = 1;
        }
        options.inSampleSize = rate;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * @param view View
     * @return Bitmap
     */
    public static Bitmap getBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 从网络url中获得bitmap
     *
     * @param url 图片网络地址
     * @return bitmap
     */
    public static Bitmap getBitmapFromUrl(String url) {
        InputStream is = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                conn.disconnect();
                return bitmap;
            } else {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从网络url中获得bitmap
     *
     * @param url    图片网络地址
     * @param width  指定的宽
     * @param height 指定的高
     * @return 获取到的bitmap
     */
    public static Bitmap getBitmapFromUrl(String url, int width, int height) {
        Bitmap bitmap = getBitmapFromUrl(url);
        return scaledBitmap(bitmap, width, height);
    }

    /**
     * 获取缩放的位置
     *
     * @param bitmap    需要被缩放的位置
     * @param dstWidth  缩放的预期宽度
     * @param dstHeight 缩放的预期高度
     * @return 缩放后的位置
     */
    public static Bitmap scaledBitmap(Bitmap bitmap, int dstWidth, int dstHeight) {
        if (bitmap == null)
            return null;
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();

        if (w > h) {
            if (w > dstWidth || h > dstHeight) {
                float scaleWidth = ((float) dstWidth) / w;
                float scaleHeight = ((float) dstHeight) / h;
                float scale = Math.min(scaleWidth, scaleHeight);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h,
                        matrix, true);
                if (bitmap != newBitmap) {
                    bitmap.recycle();
                }
                return newBitmap;
            } else {
                return bitmap;
            }
        } else {
            if (w > dstWidth || h > dstHeight) {
                float scaleWidth = ((float) dstHeight) / w;
                float scaleHeight = ((float) dstWidth) / h;
                float scale = Math.min(scaleWidth, scaleHeight);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h,
                        matrix, true);
                if (bitmap != newBitmap) {
                    bitmap.recycle();
                    bitmap = null;
                }
                return newBitmap;
            } else {
                return bitmap;
            }
        }

    }

    /**
     * 返回圆角图片
     *
     * @param bitmap 需要转换的位图
     * @param pixels 圆角半径
     * @return 转换后的bitmap
     **/
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 第一种旋转图片的方法
     *
     * @param bitmap 需要被旋转的图片
     * @param degree 旋转的角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap1(Bitmap bitmap, int degree) {
        if (!(degree != 0 && bitmap != null)) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 第二种旋转图片的方法
     *
     * @param bitmap 需要被旋转的图片
     * @param degree 旋转的角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap2(Bitmap bitmap, int degree) {
        if (degree != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                if (bitmap != b) {
                    bitmap.recycle();
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap createReflectedBitmap(Bitmap srcBitmap) {
        if (null == srcBitmap) {
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 4;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int reflectionWidth = srcBitmap.getWidth();
        int reflectionHeight = srcBitmap.getHeight() / 2;

        if (0 == srcWidth || srcHeight == 0) {
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {
            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                    srcHeight / 2, srcWidth, srcHeight / 2, matrix, false);

            if (null == reflectionBitmap) {
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(reflectionWidth,
                    srcHeight + reflectionHeight + REFLECTION_GAP,
                    Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP,
                    null);

            // srcBitmap.recycle();
            reflectionBitmap.recycle();

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    Mode.DST_IN));

            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 计算样本比率
     *
     * @param options   Options
     * @param dstWidth  目标宽度
     * @param dstHeight 目标高度
     * @return 根据实际宽高与目标宽高计算出的样本比率
     */
    public static int calcSampleSize(Options options, int dstWidth, int dstHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > dstHeight || width > dstWidth) {
            int samplesize = Math.round((float) height / (float) dstHeight);
            inSampleSize = Math.round((float) width / (float) dstWidth);
            inSampleSize = Math.max(samplesize, inSampleSize);
        }
        return inSampleSize;
    }

    public static Bitmap loadBitmapFromFile(String path, int dstWidth, int dstHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calcSampleSize(options, dstWidth, dstHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap loadBitmapFromFile(String path, int dstWidth, int dstHeight, int minSize) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth <= minSize && options.outHeight <= minSize) { // 做下特殊处理，小于多少的图片，就整张加载，避免失真
            options.inSampleSize = 1;
        } else {
            options.inSampleSize = calcSampleSize(options, dstWidth, dstHeight);
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * @param path
     * @param dstWidth
     */
    public static int getScaleHeight(String path, int dstWidth) {
        int dstHeight = 0;
        try {
            if (!TextUtils.isEmpty(path) && dstWidth > 0) {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);

                dstHeight = options.outHeight * dstWidth / options.outWidth;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return dstHeight;
        }
    }

    public static Bitmap loadBitmapFromFile(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap loadBitmapFromFile(String path, int maxWidth) {
        Bitmap bm;
        if (maxWidth > 0) {
            int dstHeight = BitmapUtils.getScaleHeight(path, maxWidth);
            bm = loadBitmapFromFile(path, maxWidth, dstHeight);
        } else {
            bm = loadBitmapFromFile(path);
        }

        return bm;
    }

    public static int calculateInSampleSize(Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static String saveBitmap(Bitmap bitmap, String path,
                                    Bitmap.CompressFormat format) {
        if (path == null || bitmap == null)
            return null;

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(path));
            bitmap.compress(format, 100, bos);
            bos.flush();
            bos.close();
            //bitmap.recycle();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float x) {

        int w = bitmap.getWidth();
        float sx = x / w;

        Matrix matrix = new Matrix();
        matrix.postScale(sx, sx); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public static InputStream getRequest(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        if (conn.getResponseCode() == 200) {
            return conn.getInputStream();
        }
        return null;
    }

    public static Drawable getDrawableFromUrl(String url) throws Exception {
        return Drawable.createFromStream(getRequest(url), null);
    }

    public static int[] getBitmapSize(Context context, int resourceID) {
        int[] ret = new int[2];
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        opts.inDensity = Bitmap.DENSITY_NONE;
        BitmapFactory.decodeResource(context.getResources(), resourceID, opts);
        ret[0] = opts.outWidth;
        ret[1] = opts.outHeight;
        return ret;
    }

    /**
     * 根据图片文件路径获取图片的大小
     *
     * @param path 图片文件路径
     * @return 图片宽高度
     */
    public static int[] getBitmapSize(String path) {
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            return new int[]{options.outWidth, options.outHeight};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
        return Drawable.createFromStream(ins, null);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 将指定图片转化成带有指定倒角半径的目标图片
     *
     * @param bitmap 需要倒角的位图
     * @param pixels 倒角半径
     * @return 倒角后的图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final int color = 0xff424242;
        paint.setAntiAlias(true);
        paint.setColor(color);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 保存图片到相册中去
     *
     * @param context 上下文
     * @param bitmap  需要被保存到相册的图片
     * @param path    图片路径
     */
    public static void saveImageToGallery(Context context, Bitmap bitmap, String path) {
        // 首先保存图片
        File appDir = new File(path, "photo");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            ToastUtils.showShort(context.getString(R.string.image_save) + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            ToastUtils.showShort(context.getString(R.string.image_save_fail_file_not_found));
        } catch (IOException e) {
            ToastUtils.showShort(context.getString(R.string.image_save_fail));
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + Uri.fromFile(new File(path + "/photo/" + fileName)))));
    }

    /******************************************************************/

    /**
     * 获取缩略图，并保存缩略图到指定目录
     *
     * @param srcPath 原图地址
     * @param desPath 缩略图保存地址
     * @param width   缩略图宽度
     */
    public static Bitmap getImageThumbnailAndSave(String srcPath, String desPath, int width) {
        try {
            Bitmap bitmap = getImageThumbnail(srcPath, width);
            int angle = getExifOrientation(srcPath);
            if (angle > 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(angle);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            FileOutputStream fos = new FileOutputStream(desPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取缩略图
     *
     * @param srcPath 原图片地址
     * @param width   最大图片宽度
     */
    public static Bitmap getImageThumbnail(String srcPath, int width) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        int h = options.outHeight;
        int w = options.outWidth;
        int height = h * width / w;
        return getImageThumbnail(srcPath, width, height);
    }

    /**
     * 获取缩略图
     *
     * @param imagePath 图片地址
     * @param width     最大图片宽度
     * @param height    最大图片高度
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        //这个值是压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰
        options.inSampleSize = 2;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        if (be == 1) {
            return bitmap;
        }
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 得到 图像信息的旋转 的角度
     * Exif : 图像信息
     *
     * @param srcPath 图片地址
     * @return 图片旋转 的角度
     */
    private static int getExifOrientation(String srcPath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(srcPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return 转换成圆形后的图片
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * 雾化效果
     *
     * @param sentBitmap       Bitmap
     * @param canReuseInBitmap boolean
     * @return Bitmap
     */
    public static Bitmap doBlur(Bitmap sentBitmap, boolean canReuseInBitmap) {
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        int radius = 5;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * 通过图片文件路径验证图片大小
     *
     * @param path 需要验证的图片的文件路径
     * @return true为尺寸小于1280*720
     */
    public static boolean verifyPictureSize(String path) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        return opts.outHeight <= 1280 && opts.outWidth <= 720;
    }

    /**
     * 通过图片文件路径列表获取图片总大小
     *
     * @param pathList 图片文件列表
     * @return 大小
     */
    public static String getPictureSize(List<String> pathList) {
        long totalSize = 0;
        for (String path : pathList) {
            File file = new File(path);
            if (file.exists() && file.isFile())
                totalSize += file.length();
        }
        NumberFormat format = NumberFormat.getNumberInstance();
        //保留小数点后两位
        format.setMaximumFractionDigits(2);
        double size = totalSize / 1048576.0;
        return format.format(size) + "M";
    }


    /**
     * 从文件中获取图片,指定宽高
     *
     * @param path   图片文件路径
     * @param width  指定宽度
     * @param height 指定高度
     * @return 目标图片
     */
    public static Bitmap getBitmapFromFile(String path, int width, int height) {
        Options opts = null;
        if (path != null) {
            if (width > 0 && height > 0) {
                opts = new Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts);
                int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
            }
            return BitmapFactory.decodeFile(path, opts);
        } else {
            return null;
        }
    }

    /**
     * 计算样本比率大小
     *
     * @param options        Options
     * @param minSideLength  最小边长度
     * @param maxNumOfPixels 总像素
     * @return 样本比率
     */
    public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 计算初始样本比率
     *
     * @param options        Options
     * @param minSideLength  最小边长度
     * @param maxNumOfPixels 总像素
     * @return 初始样本比率
     */
    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 根据bitmap保存图片到本地
     *
     * @param bitmap   Bitmap
     * @param userName 发送给的用户名
     * @return 图片路径
     */
    public static String saveBitmapToLocal(Bitmap bitmap, String userName) {
        if (null == bitmap) {
            return null;
        }
        String filePath;
        FileOutputStream fileOutput = null;
        File imgFile;
        try {
            imgFile = new File(PathUtils.getInstance().getImagePath(), userName + ".png");
            fileOutput = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
            fileOutput.flush();
            filePath = imgFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            filePath = null;
        } finally {
            if (null != fileOutput) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    /**
     * 图片bitmap与uri互相转换
     *
     * @param context Context
     * @param bitmap  Bitmap
     * @return Uri
     */
    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
    }

    /**
     * 图片bitmap与uri互相转换
     *
     * @param context Context
     * @param uri     Uri
     * @return Bitmap
     */
    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
