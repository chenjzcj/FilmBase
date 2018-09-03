package succ7.com.filmbase.utils;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by MZIA(527633405@qq.com) on 2015/1/20 0020 11:12
 * SD工具类
 */
public class SDCardUtil {
    /**
     * 判断SDCard是否存在 ; Environment.MEDIA_MOUNTED ：已经挂载并且拥有可读可写权限
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SDCard的路径；如：/mnt/sdcard
     */
    public static String getSDCardPath() {
        if (isSDCardExist()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
            /** + File.separator; */
        }
        return "";
    }

    /**
     * 获取SDCard可以使用的容量 单位MB; SDCard不存在，返回0;
     */
    public static int getAvailableCapacity() {
        String path = getSDCardPath();
        if (!"".equals(path)) {
            StatFs sf = new StatFs(path);
            long blockSize;
            long availCount;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = sf.getBlockSizeLong();
                availCount = sf.getAvailableBlocksLong();
            }else {
                blockSize = sf.getBlockSize();
                availCount = sf.getAvailableBlocks();
            }
            //http://jingyan.baidu.com/article/19020a0a00e065529c284242.html 友好显示比如 2.3GB,43MB等
            //String s = Formatter.formatFileSize(MyApp.context(), availCount * blockSize);
            return (int) (availCount * blockSize / (1024 * 1024));
        }
        return -1;
    }

    /**
     * 获取SDCard的总容量, 单位MB; SDCard不存在，返回0;
     */
    public static int getTotalCapacity() {
        String path = getSDCardPath();
        if (!"".equals(path)) {
            StatFs sf = new StatFs(path);
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            return (int) ((blockCount * blockSize) / (1024 * 1024));
        }
        return -1;
    }
}
