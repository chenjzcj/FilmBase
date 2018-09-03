package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 18:03
 * 获取最新电影列表事件(分类里面)
 */
public class FindFilmsByCategoryEvent {
    private Object object;

    public FindFilmsByCategoryEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
