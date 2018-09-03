package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 添加反馈事件
 */
public class AddFeedbackEvent {

    private Object object;

    public AddFeedbackEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
