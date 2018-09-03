package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 18:03
 * 获取最新版本信息事件
 */
public class FindUpdateInfoEvent {
    private Object object;

    public FindUpdateInfoEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
