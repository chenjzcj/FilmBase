package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 18:03
 * 根据贡献者名称获取电影列表事件
 */
public class FindFilmsByContributorEvent {
    private Object object;

    public FindFilmsByContributorEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
