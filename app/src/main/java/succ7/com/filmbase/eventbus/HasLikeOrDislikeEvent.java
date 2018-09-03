package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 判断是否已经赞过或者踩过某电影事件
 */
public class HasLikeOrDislikeEvent {

    private Object object;

    public HasLikeOrDislikeEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
