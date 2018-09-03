package succ7.com.filmbase.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import succ7.com.filmbase.bean.bmob.SmsInfo;


/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/6/21 0021
 */
public class SMSUtils {

    public static final Uri URI_SMS_INBOX = Uri.parse("content://sms/inbox");
    public static final Uri URL_SMS = Uri.parse("content://sms/");

    /**
     * _id：短信序号，如100 thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
     * address：发件人地址，即手机号，如+8613811810000 person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
     * date：日期，long型，如1256539465022，可以对日期显示格式进行设置
     * protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信 read：是否阅读0未读，1已读
     * status：短信状态-1接收，0complete,64pending,128failed type：短信类型1是接收到的，2是已发出
     * body：短信具体内容 service_center：短信服务中心号码编号，如+8613800755500
     * 获取最新的短信
     */
    public static SmsInfo getNewestSms(Context context) {
        SmsInfo smsInfo = null;
        try {
            String[] projection = new String[]{"_id", "address", "body", "date"};
            Cursor cusor = context.getContentResolver().query(URI_SMS_INBOX, projection, null, null, "date desc");
            if (cusor != null) {
                int idColumn = cusor.getColumnIndex("_id");
                int phoneNumberColumn = cusor.getColumnIndex("address");
                int smsbodyColumn = cusor.getColumnIndex("body");
                int dateColumn = cusor.getColumnIndex("date");
                while (cusor.moveToNext()) {
                    String body = cusor.getString(smsbodyColumn);
                    if (body != null) {
                        SmsInfo smsinfo = new SmsInfo();
                        smsinfo.setId(cusor.getInt(idColumn));
                        smsinfo.setDate(cusor.getLong(dateColumn));
                        smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn).replace("+86", ""));
                        smsinfo.setSmsbody(body);
                        smsInfo = smsinfo;
                    }
                    cusor.close();
                    return smsInfo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smsInfo;
    }
}
