package succ7.com.filmbase.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/8 0008
 * 获取手机各种信息工具类,记得添加权限 : READ_PHONE_STATE
 */
public class PhoneUtils {
    private TelephonyManager mTelephonyMgr;

    public PhoneUtils(Context context) {
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取手机IMEI(手机串码)
     *
     * @return 手机串码
     */
    public long getIMEI() {
        long imei;
        if (NumUtil.isNumber(mTelephonyMgr.getDeviceId())) {
            imei = Long.parseLong(mTelephonyMgr.getDeviceId());
        } else {
            //电信手机,获取的是MEID号码,如: A0000049CEA0E4的16进制字符串,将其转换成10进制的数
            imei = Long.parseLong(mTelephonyMgr.getDeviceId(),16);
        }
        return imei;
    }

    /**
     * 获取sim卡的IMSI(sim卡号 ) IMSI 国际移动用户识别码（IMSI：International Mobile Subscriber
     * Identification Number）是区别移动用户的标志， 储存在SIM卡中，可用于区别移动用户的有效信息。
     * IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成 // 唯一地识别移动客户所属的国家，我国为460；
     * MNC为网络id，由2位数字组成// 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；
     * MSIN为移动客户识别码，采用等长11位数字构成 //唯一地识别国内GSM移动通信网中移动客户。
     *
     * @return sim卡号
     */
    public long getIMSI() {
        if(TextUtils.isEmpty(mTelephonyMgr.getSubscriberId())||mTelephonyMgr.getSubscriberId().equals("null")){
            return 0;
        }else {
            return Long.parseLong(mTelephonyMgr.getSubscriberId());
        }
    }

    /**
     * 获取安卓系统版本
     *
     * @return 安卓系统版本
     */
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 如果存在的话,则返回true
     * mTelephonyMgr.getSubscriberId()返回值有可能不是null,但却是"",妹的这是一个坑
     *
     * @return 判断手机中是否存在sim卡
     */
    public boolean checkHasSim() {
        boolean result = false;
        if (mTelephonyMgr.getSubscriberId() != null) {
            if (!mTelephonyMgr.getSubscriberId().equals("")) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取sim卡的状态
     */
    public int getSimState() {
        return mTelephonyMgr.getSimState();
    }
}
