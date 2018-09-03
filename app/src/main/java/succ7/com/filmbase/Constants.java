package succ7.com.filmbase;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/4 0004 13:41
 * 应用常量
 */
public class Constants {
    //聚合数据接口及appkey

    //影视影讯检索
    public static final String MOVIE_APPKEY = "acb4a3f25145a9a4ba8c5c9ef565b020";
    //影视搜索接口地址
    public static final String INTERFACE_MOVIE_SEARCH = "http://op.juhe.cn/onebox/movie/video";
    //最近影讯接口地址
    public static final String INTERFACE_PMOVIE_SEARCH = "http://op.juhe.cn/onebox/movie/pmovie";

    //最新票房榜
    public static final String BOXOFFICE_APPKEY = "9a75aba0ef97bcb7b5007420dc49c54f";
    public static final String INTERFACE_BOXOFFICE = "http://v.juhe.cn/boxoffice/rank";

    //官网
    public static final String APP_WEB = "http://filmbase.bmob.cn";

    //电影类型共10个,可以动态从服务器获取
    public static String[] types = MyApp.getInstance().getResources().getStringArray(R.array.types);
    //电影地区共15个,可以动态从服务器获取
    public static String[] areas = MyApp.getInstance().getResources().getStringArray(R.array.areas);
    //电影年代共11个,可以动态从服务器获取
    public static String[] years = MyApp.getInstance().getResources().getStringArray(R.array.years);
    //电影演员共20个,可以动态从服务器获取
    public static String[] actors = MyApp.getInstance().getResources().getStringArray(R.array.actors);

    //服务器请求类型
    public static final int REQUEST_TYPE_ADD_FILM = 0;//添加电影
    public static final int REQUEST_TYPE_UPDATE_FILM = 1;//更新电影
    public static final int REQUEST_TYPE_FIND_ALL_FILM = 2;//查找所有电影(热门版块)
    public static final int REQUEST_TYPE_FIND_FILM_BY_CATEGORY = 3;//通过四个分类查找电影(分类版块)
    public static final int REQUEST_TYPE_FIND_FILM_BY_CONTRIBUTOR = 4;//通过贡献人查找电影(我贡献的资源版块)
    public static final int REQUEST_TYPE_FIND_FILM_BY_KEYWORD = 5;//通过关键字搜索
    public static final int REQUEST_TYPE_ADD_COMMENT = 6;//添加评论
    public static final int REQUEST_TYPE_FIND_COMMENT = 7;//查找评论
    public static final int REQUEST_TYPE_FIND_GOODS = 8;//查找所有兑换商品
    public static final int REQUEST_TYPE_FIND_VERSION = 9;//查找版本信息
    public static final int REQUEST_TYPE_UPLOAD_PIC = 10;//上传图片
    public static final int REQUEST_TYPE_ADDLIKEORDISLIKE = 11;//添加赞或者踩
    public static final int REQUEST_TYPE_ISLIKEORDISLIKE = 12;//是否已经赞过或者踩过
    public static final int REQUEST_TYPE_FEEDBACK = 13;//用户反馈
}
