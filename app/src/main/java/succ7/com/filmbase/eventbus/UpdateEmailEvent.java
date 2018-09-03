package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 修改邮箱事件
 */
public class UpdateEmailEvent {

    private Object object;

    public UpdateEmailEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
