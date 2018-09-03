package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 注册事件
 */
public class RegisterEvent {

    private Object object;

    public RegisterEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
