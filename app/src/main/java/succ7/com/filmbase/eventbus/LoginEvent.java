package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 登录事件
 */
public class LoginEvent {

    private Object object;

    public LoginEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
