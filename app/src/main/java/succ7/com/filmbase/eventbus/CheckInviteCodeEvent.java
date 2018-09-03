package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 检查邀请码合法性事件
 */
public class CheckInviteCodeEvent {

    private Object object;

    public CheckInviteCodeEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
