package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 验证邮箱事件
 */
public class VerifyEmailEvent {

    private Object object;

    public VerifyEmailEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
