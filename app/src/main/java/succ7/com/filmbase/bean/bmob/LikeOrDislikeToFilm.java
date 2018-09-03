package succ7.com.filmbase.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/24 18:40
 * 赞与踩与对应的电影,只要数据库中出现此条数据,说明该用户已经赞过或者踩过了此电影
 */
public class LikeOrDislikeToFilm extends BmobObject {
    private User user;//点赞或者踩的用户
    private Film film;//点赞或踩的电影
    private boolean like;//赞过

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
