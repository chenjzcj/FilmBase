package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 18:03
 * 获取评论列表事件
 */
public class FindCommentEvent {
    private Object object;

    public FindCommentEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
