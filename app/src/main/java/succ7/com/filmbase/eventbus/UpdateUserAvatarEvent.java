package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 更新用户头像事件
 */
public class UpdateUserAvatarEvent {

    private Object object;

    public UpdateUserAvatarEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
