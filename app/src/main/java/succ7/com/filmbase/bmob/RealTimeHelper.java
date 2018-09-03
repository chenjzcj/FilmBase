package succ7.com.filmbase.bmob;

import com.google.gson.Gson;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.utils.LogUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/30 0030 12:04
 * 数据实时同步监听
 */
public class RealTimeHelper {

    private static BmobRealTimeData bmobRealTimeData;

    /**
     * 开始连接服务器(长连接)
     */
    public static void start() {
        if (bmobRealTimeData == null) {
            bmobRealTimeData = new BmobRealTimeData();
        }
        bmobRealTimeData.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                LogUtils.i("RealTimeHelper onConnectCompleted 连接成功 :" + bmobRealTimeData.isConnected());
                if (bmobRealTimeData.isConnected()) {
                    bmobRealTimeData.subTableUpdate("_User");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                LogUtils.i("RealTimeHelper onDataChange 数据 :" + jsonObject);
                Gson gson = new Gson();
                try {
                    User user = gson.fromJson(jsonObject.get("data").toString(), User.class);
                    UserHelper.getCurrentUser().setShareCount(user.getShareCount());
                    UserHelper.getCurrentUser().setFilmCoinCount(user.getFilmCoinCount());
                    UserHelper.getCurrentUser().setEmailVerified(user.getEmailVerified());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消某个表的监听
     *
     * @param tableName 表名
     */
    public static void cancel(String tableName) {
        if (bmobRealTimeData != null) {
            bmobRealTimeData.unsubTableUpdate(tableName);
        }
    }
}
