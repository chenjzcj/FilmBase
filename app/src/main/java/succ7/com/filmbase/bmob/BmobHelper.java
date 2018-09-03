package succ7.com.filmbase.bmob;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Comment;
import succ7.com.filmbase.bean.bmob.Feedback;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bean.bmob.Goods;
import succ7.com.filmbase.bean.bmob.LikeOrDislikeToFilm;
import succ7.com.filmbase.bean.bmob.UpdateInfo;
import succ7.com.filmbase.eventbus.AddCommentEvent;
import succ7.com.filmbase.eventbus.AddDislikeEvent;
import succ7.com.filmbase.eventbus.AddFeedbackEvent;
import succ7.com.filmbase.eventbus.AddFilmEvent;
import succ7.com.filmbase.eventbus.AddLikeEvent;
import succ7.com.filmbase.eventbus.FindCommentEvent;
import succ7.com.filmbase.eventbus.FindFilmEvent;
import succ7.com.filmbase.eventbus.FindFilmsByCategoryEvent;
import succ7.com.filmbase.eventbus.FindFilmsByContributorEvent;
import succ7.com.filmbase.eventbus.FindGoodsEvent;
import succ7.com.filmbase.eventbus.FindUpdateInfoEvent;
import succ7.com.filmbase.eventbus.HasLikeOrDislikeEvent;
import succ7.com.filmbase.eventbus.SearchFilmsByFilmNameKeyWordEvent;
import succ7.com.filmbase.eventbus.UpdateFilmEvent;
import succ7.com.filmbase.eventbus.UploadPicEvent;
import succ7.com.filmbase.utils.AppUtils;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.NetUtils;
import succ7.com.filmbase.utils.ToastUtils;
import succ7.com.filmbase.view.dialog.WaitDialog;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/24 22:58
 * 数据操作工具类
 */
public class BmobHelper {
    /**
     * 应用appId
     */
    private static final String appId = "b98522cb5cecb387293166ffd7876fa2";

    /**
     * bmob初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Bmob.initialize(context, appId);
    }

    /**
     * 请求服务器获取数据,更新数据,删除数据的操作
     *
     * @param context     上下文
     * @param requestType 请求类型
     * @param waitDialog  提示对话框
     * @param data        需要提交的数据
     */
    public static void requestServer(Context context, int requestType, WaitDialog waitDialog, Object... data) {
        if (!NetUtils.isNetConnected(context)) {
            ToastUtils.showShort(context.getString(R.string.error_code_10_));
            return;
        }
        if (waitDialog != null) {
            waitDialog.show();
        }
        switch (requestType) {
            case Constants.REQUEST_TYPE_ADD_FILM:
                addFilm((String) data[0], (String) data[1], (String) data[2], (double) data[3],
                        (String) data[4], (String) data[5], (String) data[6], (String) data[7],
                        (String) data[8], (String) data[9], (String) data[10]);
                break;
            case Constants.REQUEST_TYPE_UPDATE_FILM:
                updateFilm((Film) data[0], (String) data[1], (String) data[2], (String) data[3],
                        (double) data[4], (String) data[5], (String) data[6], (String) data[7],
                        (String) data[8], (String) data[9], (String) data[10], (String) data[11]);
                break;
            case Constants.REQUEST_TYPE_FIND_ALL_FILM:
                findAllFilms((int) data[0], (int) data[1]);
                break;
            case Constants.REQUEST_TYPE_FIND_FILM_BY_CATEGORY:
                findFilmsByCategory((int) data[0], (int) data[1], (String) data[2],
                        (String) data[3], (String) data[4], (String) data[5]);
                break;
            case Constants.REQUEST_TYPE_FIND_FILM_BY_CONTRIBUTOR:
                findFilmsByContributor();
                break;
            case Constants.REQUEST_TYPE_FIND_FILM_BY_KEYWORD:
                searchFilmsByKeyWord((String) data[0]);
                break;
            case Constants.REQUEST_TYPE_ADD_COMMENT:
                addComment((String) data[0], (Film) data[1]);
                break;
            case Constants.REQUEST_TYPE_FIND_COMMENT:
                findCommentsByFilm((Film) data[0]);
                break;
            case Constants.REQUEST_TYPE_FIND_GOODS:
                findAllGoods();
                break;
            case Constants.REQUEST_TYPE_FIND_VERSION:
                findUpdateInfo();
                break;
            case Constants.REQUEST_TYPE_UPLOAD_PIC:
                uploadPic((String) data[0]);
                break;
            case Constants.REQUEST_TYPE_ADDLIKEORDISLIKE:
                addLikeOrDislike((Film) data[0], (boolean) data[1]);
                break;
            case Constants.REQUEST_TYPE_ISLIKEORDISLIKE:
                isLikeOrDislike((Film) data[0]);
                break;
            case Constants.REQUEST_TYPE_FEEDBACK:
                addFeedback(context, (String) data[0], (String) data[1]);
                break;
        }
    }

    /**
     * 添加电影
     *
     * @param filmPicUrl  电影图片文件地址
     * @param filmName    电影名称
     * @param filmSource  电影来源,如百度网盘等
     * @param filmLength  电影长度
     * @param unit        电影长度单位
     * @param filmTypes   电影类型,如动作,搞笑,3D等
     * @param filmArea    电影地区
     * @param filmYear    电影年代
     * @param filmActors  电影主演
     * @param filmAddress 电影网络地址,如网络
     * @param secret      电影网络地址的提取码,或者说密钥,可以为null
     */
    private static void addFilm(String filmPicUrl, String filmName, String filmSource, double filmLength,
                                String unit, String filmTypes, String filmArea, String filmYear,
                                String filmActors, String filmAddress, String secret) {
        Film film = new Film();
        film.setFilmPicUrl(filmPicUrl);
        film.setFilmName(filmName);
        film.setUser(UserHelper.getCurrentUser());
        film.setFilmSource(filmSource);
        film.setFilmLength(filmLength);
        film.setUnit(unit);
        film.setFilmTypes(filmTypes);
        film.setFilmArea(filmArea);
        film.setFilmYear(filmYear);
        film.setFilmActors(filmActors);
        film.setFilmAddress(filmAddress);
        film.setOtherInfo(secret != null ? secret : "");
        film.setLikes(0);
        film.setDisLikes(0);
        film.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtils.i("addFilm s = " + s + ",e = " + e);
                postEvent(new AddFilmEvent(e));
            }
        });
    }

    /**
     * 更新电影
     *
     * @param odlfilm     需要修改的电影
     * @param filmPicUrl  电影图片文件地址
     * @param filmName    电影名称
     * @param filmSource  电影来源,如百度网盘等
     * @param filmLength  电影长度
     * @param unit        电影长度单位
     * @param filmTypes   电影类型,如动作,搞笑,3D等
     * @param filmArea    电影地区
     * @param filmYear    电影年代
     * @param filmActors  电影主演
     * @param filmAddress 电影网络地址,如网络
     * @param secret      电影网络地址的提取码,或者说密钥,可以为null
     */
    private static void updateFilm(Film odlfilm, String filmPicUrl, String filmName, String filmSource, double filmLength,
                                   String unit, String filmTypes, String filmArea, String filmYear, String filmActors,
                                   String filmAddress, String secret) {
        Film film = new Film();
        film.setObjectId(odlfilm.getObjectId());
        film.setFilmPicUrl(filmPicUrl);
        film.setFilmName(filmName);
        film.setUser(UserHelper.getCurrentUser());
        film.setFilmSource(filmSource);
        film.setFilmLength(filmLength);
        film.setUnit(unit);
        film.setFilmTypes(filmTypes);
        film.setFilmArea(filmArea);
        film.setFilmYear(filmYear);
        film.setFilmActors(filmActors);
        film.setFilmAddress(filmAddress);
        film.setOtherInfo(secret != null ? secret : "");
        film.setLikes(odlfilm.getLikes());
        film.setDisLikes(odlfilm.getDisLikes());
        film.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("updateFilm e = " + e);
                postEvent(new UpdateFilmEvent(e));
            }
        });
    }

    /**
     * 查询所有电影,按电影的点赞数量来排序
     *
     * @param page      第几页
     * @param pageCount 每一页的数量,默认为10
     */
    private static void findAllFilms(int page, int pageCount) {
        //默认为每页加载10条数据
        pageCount = pageCount < 10 ? 10 : pageCount;
        LogUtils.i("page = " + page + ",pageCount = " + pageCount);
        BmobQuery<Film> query = new BmobQuery<>();
        query.order("-likes");
        query.setLimit(pageCount);
        query.setSkip(page * pageCount);
        query.include("user");//将user数据一并查询出来
        setCachePolicy(query, Film.class);
        query.findObjects(new FindListener<Film>() {
            @Override
            public void done(List<Film> list, BmobException e) {
                LogUtils.i("findAllFilms list = " + list + ",e = " + e);
                postEvent(new FindFilmEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 搜索电影(按照时间顺序)
     *
     * @param page      页
     * @param pageCount 每页的数量
     * @param type      电影类型
     * @param area      电影地区
     * @param year      电影年代
     * @param actor     电影主演
     */
    private static void findFilmsByCategory(int page, int pageCount, String type, String area, String year, String actor) {
        LogUtils.i("findFilmsByCategory type = " + type + ",area = " + area + ",year = " + year + ",actor = " + actor);
        BmobQuery<Film> query = new BmobQuery<>();
        addCondition(query, "filmTypes", type);
        addCondition(query, "filmArea", area);
        addCondition(query, "filmYear", year);
        addCondition(query, "filmActors", actor);
        //默认为每页加载10条数据
        pageCount = pageCount < 10 ? 10 : pageCount;
        LogUtils.i("page = " + page + ",pageCount = " + pageCount);
        query.setLimit(pageCount);
        query.setSkip(page * pageCount);
        query.include("user");
        query.findObjects(new FindListener<Film>() {
            @Override
            public void done(List<Film> list, BmobException e) {
                LogUtils.i("findFilmsByCategory list = " + list.size() + ",e = " + e);
                postEvent(new FindFilmsByCategoryEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 增加查询条件
     *
     * @param key   限制字段key
     * @param value 对应的值
     */
    private static void addCondition(BmobQuery query, String key, String value) {
        if (!value.equals("全部")) {
            query.addWhereContains(key, value);
        }
    }

    /**
     * 查询某个贡献都贡献的所有电影
     */
    private static void findFilmsByContributor() {
        BmobQuery<Film> query = new BmobQuery<>();
        query.addWhereEqualTo("user", UserHelper.getCurrentUser());
        query.include("user");//将user数据一并查询出来
        query.findObjects(new FindListener<Film>() {
            @Override
            public void done(List<Film> list, BmobException e) {
                LogUtils.i("findFilmsByContributor list = " + list + ",e = " + e);
                postEvent(new FindFilmsByContributorEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 根据电影名关键字搜索电影
     */
    private static void searchFilmsByKeyWord(String keyWord) {
        BmobQuery<Film> query = new BmobQuery<>();
        query.addWhereContains("filmName", keyWord);
        query.include("user");//将user数据一并查询出来
        query.findObjects(new FindListener<Film>() {
            @Override
            public void done(List<Film> list, BmobException e) {
                LogUtils.i("searchFilmsByKeyWord list = " + list + ",e = " + e);
                postEvent(new SearchFilmsByFilmNameKeyWordEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 添加评论
     *
     * @param content 评论内容
     * @param film    评论的电影
     */
    private static void addComment(String content, Film film) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCritic(UserHelper.getCurrentUser());
        comment.setFilm(film);
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtils.i("addComment s = " + s + ",e = " + e);
                postEvent(new AddCommentEvent(new Object[]{s, e}));
            }
        });
    }

    /**
     * 根据电影查询评论,一次显示20条
     *
     * @param film 所需要查询的电影
     */
    private static void findCommentsByFilm(Film film) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("film", film);
        query.include("critic");
        query.order("-createdAt");
        query.setLimit(20);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                LogUtils.i("findCommentsByFilm list = " + list + ",e = " + e);
                postEvent(new FindCommentEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 查询所有兑换的商品
     */
    private static void findAllGoods() {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.setLimit(20);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                LogUtils.i("findAllGoods list = " + list + ",e = " + e);
                postEvent(new FindGoodsEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 从服务器查找版本信息
     */
    private static void findUpdateInfo() {
        BmobQuery<UpdateInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UpdateInfo>() {
            @Override
            public void done(List<UpdateInfo> list, BmobException e) {
                LogUtils.i("findUpdateInfo,list = " + list + ",e =" + e);
                postEvent(new FindUpdateInfoEvent(new Object[]{list, e}));
            }
        });

    }

    /**
     * 上传文件
     *
     * @param picPath 需要上传的图片路径
     */
    private static void uploadPic(String picPath) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("uploadPic,e = " + e + ",bmobFile =" + bmobFile);
                postEvent(new UploadPicEvent(new Object[]{bmobFile, e}));
            }
        });
    }

    /**
     * 添加赞或者踩
     *
     * @param film   指定的电影
     * @param isLike 是否是赞
     */
    private static void addLikeOrDislike(Film film, boolean isLike) {
        saveLikeOrDislike(film, isLike);
        if (isLike) {
            addLike(film);
        } else {
            addDislike(film);
        }
    }


    /**
     * 将当前登录用户已经赞过的痕迹保存到数据库
     *
     * @param film 指定的电影
     */
    private static void saveLikeOrDislike(Film film, boolean isLike) {
        LikeOrDislikeToFilm likeOrDislikeToFilm = new LikeOrDislikeToFilm();
        likeOrDislikeToFilm.setFilm(film);
        likeOrDislikeToFilm.setUser(UserHelper.getCurrentUser());
        likeOrDislikeToFilm.setLike(isLike);
        likeOrDislikeToFilm.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtils.i("saveLikeOrDislike s = " + s + ",e = " + e);
            }
        });
    }

    /**
     * 赞了指定的电影
     *
     * @param film 被赞的电影
     */
    private static void addLike(Film film) {
        LogUtils.i("赞了电影 " + film.getFilmName() + ",objectId = " + film.getObjectId());
        film.increment("likes");
        film.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("addLike e = " + e);
                postEvent(new AddLikeEvent(e));
            }
        });
    }

    /**
     * 踩了指定的电影
     *
     * @param film 被踩的电影
     */
    private static void addDislike(Film film) {
        LogUtils.i("踩了电影 " + film.getFilmName() + ",objectId = " + film.getObjectId());
        film.increment("disLikes");
        film.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("addDislike e = " + e);
                postEvent(new AddDislikeEvent(e));
            }
        });
    }

    /**
     * 根据电影id与当前登录用户判断是否已经赞过该电影或者踩过该电影
     * 只要赞过一次或者踩过一次,都不能再次点赞或者踩
     * 此方法调用在赞或者踩之前
     *
     * @param film 指定的电影
     */
    private static void isLikeOrDislike(Film film) {
        BmobQuery<LikeOrDislikeToFilm> query = new BmobQuery<>();
        query.addWhereEqualTo("user", UserHelper.getCurrentUser());
        query.addWhereEqualTo("film", film);
        query.findObjects(new FindListener<LikeOrDislikeToFilm>() {
            @Override
            public void done(List<LikeOrDislikeToFilm> list, BmobException e) {
                LogUtils.i("isLikeOrDislike list = " + list + ",e = " + e);
                postEvent(new HasLikeOrDislikeEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 设置缓存策略
     *
     * @param query BmobQuery
     * @param clazz Class
     */
    private static void setCachePolicy(BmobQuery query, Class clazz) {
        boolean isCache = query.hasCachedResult(clazz);
        //query.setCachePolicy(isCache ? BmobQuery.CachePolicy.CACHE_ELSE_NETWORK : BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
    }

    /**
     * 添加反馈
     *
     * @param contacts 联系方式
     * @param content  反馈内容
     */
    private static void addFeedback(Context context, String contacts, String content) {
        Feedback feedback = new Feedback();
        feedback.setUser(UserHelper.getCurrentUser());
        feedback.setContact(contacts);
        feedback.setContent(content);

        PackageInfo pinfo = AppUtils.getPackageInfo(context);
        String deviceInfo = "Version: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n" +
                "Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n";
        feedback.setDeviceInfo(deviceInfo);

        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                LogUtils.i("addFeedback s = " + s + ",e = " + e);
                postEvent(new AddFeedbackEvent(e));
            }
        });
    }

    /**
     * 发送异步事件
     *
     * @param obj 事件
     */
    private static void postEvent(Object obj) {
        EventBus.getDefault().post(obj);
    }
}
