package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/29 21:45
 * 添加电影事件
 */
public class AddFilmEvent {

    private Object object;

    public AddFilmEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
