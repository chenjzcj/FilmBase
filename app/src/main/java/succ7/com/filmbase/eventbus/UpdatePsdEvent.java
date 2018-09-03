package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 修改密码事件
 */
public class UpdatePsdEvent {

    private Object object;

    public UpdatePsdEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
