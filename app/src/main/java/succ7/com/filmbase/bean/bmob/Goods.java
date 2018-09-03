package succ7.com.filmbase.bean.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/16 0016 10:32
 * 兑换商品
 */
public class Goods extends BmobObject implements Serializable {
    private String goodsName;//商品名称
    private String pic;//商品图片
    private String price;//商品影币价格
    private String count;//商品数量
    private String actTime;//活动时间
    private String endTime;//兑换截止时间

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsName='" + goodsName + '\'' +
                ", pic='" + pic + '\'' +
                ", price='" + price + '\'' +
                ", count='" + count + '\'' +
                ", actTime='" + actTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
