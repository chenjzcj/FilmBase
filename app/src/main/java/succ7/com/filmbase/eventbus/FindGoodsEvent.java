package succ7.com.filmbase.eventbus;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 18:03
 * 获取所有兑换商品列表事件
 */
public class FindGoodsEvent {
    private Object object;

    public FindGoodsEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
