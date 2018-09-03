package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 点赞事件
 */
public class AddLikeEvent {

    private Object object;

    public AddLikeEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
