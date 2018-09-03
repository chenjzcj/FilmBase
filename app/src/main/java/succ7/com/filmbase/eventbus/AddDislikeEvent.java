package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 倒彩事件
 */
public class AddDislikeEvent {

    private Object object;

    public AddDislikeEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
