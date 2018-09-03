package succ7.com.filmbase.litepal;

import org.litepal.tablemanager.Connector;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/9 0009
 * LitePal数据库框架操作帮助类
 */
public class LitePalHelper {
    /**
     * 创建数据库与数据表
     */
    public static void createDB() {
        Connector.getDatabase();
    }
}

