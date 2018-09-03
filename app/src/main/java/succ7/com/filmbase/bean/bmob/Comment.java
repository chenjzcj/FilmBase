package succ7.com.filmbase.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/24 18:40
 * 评论
 */
public class Comment extends BmobObject {
    private User critic;//评论的人
    private String content;//评论内容
    private Film film;//评论的电影

    public User getCritic() {
        return critic;
    }

    public void setCritic(User critic) {
        this.critic = critic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}
