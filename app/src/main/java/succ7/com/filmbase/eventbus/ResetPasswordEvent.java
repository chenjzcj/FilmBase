package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 密码重置事件
 */
public class ResetPasswordEvent {

    private Object object;

    public ResetPasswordEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
