package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 必须要登录的操作
 */
public class LoginRequestEvent {

    private Object object;

    public LoginRequestEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
