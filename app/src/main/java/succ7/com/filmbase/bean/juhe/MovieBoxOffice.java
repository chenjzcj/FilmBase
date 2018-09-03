package succ7.com.filmbase.bean.juhe;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/4 0004 14:19
 * 电影票房排名
 */
public class MovieBoxOffice {
    private int rid;// 排名。
    private String name;//影片名称。
    private String wk;//榜单周数。
    private String wboxoffice;// 周末票房
    private String tboxoffice;// 累计票房

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWk() {
        return wk;
    }

    public void setWk(String wk) {
        this.wk = wk;
    }

    public String getWboxoffice() {
        return wboxoffice;
    }

    public void setWboxoffice(String wboxoffice) {
        this.wboxoffice = wboxoffice;
    }

    public String getTboxoffice() {
        return tboxoffice;
    }

    public void setTboxoffice(String tboxoffice) {
        this.tboxoffice = tboxoffice;
    }
}
