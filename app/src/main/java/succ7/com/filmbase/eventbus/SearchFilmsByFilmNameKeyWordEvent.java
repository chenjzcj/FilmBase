package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 18:03
 * 根据电影名关键字搜索电影事件
 */
public class SearchFilmsByFilmNameKeyWordEvent {
    private Object object;

    public SearchFilmsByFilmNameKeyWordEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
