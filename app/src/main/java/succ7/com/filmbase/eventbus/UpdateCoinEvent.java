package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 更新影币事件
 */
public class UpdateCoinEvent {

    private Object object;

    public UpdateCoinEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
