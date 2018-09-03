package succ7.com.filmbase.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.CommentAdapter;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Comment;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bean.bmob.LikeOrDislikeToFilm;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.AddCommentEvent;
import succ7.com.filmbase.eventbus.AddDislikeEvent;
import succ7.com.filmbase.eventbus.AddLikeEvent;
import succ7.com.filmbase.eventbus.FindCommentEvent;
import succ7.com.filmbase.eventbus.HasLikeOrDislikeEvent;
import succ7.com.filmbase.eventbus.LoginRequestEvent;
import succ7.com.filmbase.eventbus.RefreshFilmEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.NumUtil;
import succ7.com.filmbase.utils.PicassoUtils;
import succ7.com.filmbase.utils.ShareUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 电影详情页面
 */
public class FilmDetailActivity extends ScrollerBaseUIActivity implements View.OnClickListener {


    private static final String EXTRA_FILM = "film";
    private ImageView ivFilmPic;
    private TextView tvFilmcontributor;
    private TextView tvFilmSource;
    private TextView tvFilmTags;
    private TextView tvFilmLength;
    private TextView tvFilmAddress;
    private Button btnLike;
    private Button btnDislike;
    private Button btnComment;
    private LinearLayout llHascomment;
    private TextView tvIneedcomment;
    private ListView lvComments;
    private LinearLayout llNocomment;
    private Button btnClickAddcomment;
    private LinearLayout llAddComment;
    private EditText etCommentContent;
    private Button btnRelease;
    private Film film;
    private List<Comment> comments;
    private CommentAdapter commentAdapter;
    private boolean isLike;//是赞还是踩
    private ImageView ivShare;
    private String content;
    private ProgressBar pb;

    /**
     * 进入电影详情页面
     *
     * @param activity Activity
     * @param film     Film
     */
    public static void enterFilmDetailActivity(Activity activity, Film film) {
        Intent intent = new Intent(activity, FilmDetailActivity.class);
        intent.putExtra(EXTRA_FILM, film);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        film = (Film) getIntent().getSerializableExtra(EXTRA_FILM);
        if (film == null) {
            onBackPressed();
            return;
        }
        this.mTitleBar.setTitle(film.getFilmName());
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_film_detail, null);
        this.addMainView(view);
        initView(view);
        if (film.getUser().getUsername().equals(UserHelper.getCurrentUserName())) {
            addEditListener();
        }
    }

    private void initView(View view) {
        ivFilmPic = (ImageView) view.findViewById(R.id.iv_filmPic);
        tvFilmcontributor = (TextView) view.findViewById(R.id.tv_filmcontributor);
        tvFilmSource = (TextView) view.findViewById(R.id.tv_filmSource);
        tvFilmTags = (TextView) view.findViewById(R.id.tv_filmTags);
        tvFilmLength = (TextView) view.findViewById(R.id.tv_filmLength);
        tvFilmAddress = (TextView) view.findViewById(R.id.tv_filmAddress);

        btnLike = (Button) view.findViewById(R.id.btn_like);
        btnDislike = (Button) view.findViewById(R.id.btn_dislike);
        btnComment = (Button) view.findViewById(R.id.btn_comment);

        llHascomment = (LinearLayout) view.findViewById(R.id.ll_hascomment);
        tvIneedcomment = (TextView) view.findViewById(R.id.tv_ineedcomment);

        ivShare = (ImageView) view.findViewById(R.id.iv_share);

        lvComments = (ListView) view.findViewById(R.id.lv_comments);

        llNocomment = (LinearLayout) view.findViewById(R.id.ll_nocomment);
        btnClickAddcomment = (Button) view.findViewById(R.id.btn_click_addcomment);

        llAddComment = (LinearLayout) view.findViewById(R.id.ll_add_comment);
        etCommentContent = (EditText) view.findViewById(R.id.et_comment_content);
        btnRelease = (Button) view.findViewById(R.id.btn_release);

        pb = (ProgressBar) view.findViewById(R.id.pb);

        tvFilmAddress.setOnClickListener(this);

        btnLike.setOnClickListener(this);
        btnDislike.setOnClickListener(this);
        btnComment.setOnClickListener(this);

        tvIneedcomment.setOnClickListener(this);
        ivShare.setOnClickListener(this);

        btnClickAddcomment.setOnClickListener(this);
        btnRelease.setOnClickListener(this);

        setData();

    }

    private void setData() {
        User user = film.getUser();
        String filmSource = film.getFilmSource();
        double filmLength = film.getFilmLength();
        String filmTypes = film.getFilmTypes();
        String filmAddress = film.getFilmAddress();

        PicassoUtils.showImage(this, film.getFilmPicUrl(), R.mipmap.defalut_film, ivFilmPic);

        tvFilmcontributor.setText(getString(R.string.contributor1, user.getUsername(), user.getFilmCoinCount()));
        tvFilmSource.setText(getString(R.string.filmSource, MyTextUtils.isEmpty(filmSource) ? "" : filmSource));
        tvFilmLength.setText(getString(R.string.filmLength, filmLength, film.getUnit()));
        StringBuilder filmtype = new StringBuilder();
        if (!MyTextUtils.isEmpty(filmTypes)) {
            String[] strings = filmTypes.split(";");
            for (String s : strings)
                filmtype.append(s).append(";");
        } else {
            filmtype.append("");
        }

        tvFilmTags.setText(getString(R.string.filmTags, filmtype.toString()));
        tvFilmAddress.setText(getString(R.string.filmAddress, MyTextUtils.isEmpty(filmAddress) ? "" : filmAddress));

        btnLike.setText(getString(R.string.like, film.getLikes()));
        btnDislike.setText(getString(R.string.dislike, film.getDisLikes()));
        btnComment.setText(getString(R.string.comment, 0));
        pb.setVisibility(View.VISIBLE);

        BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_FIND_COMMENT, null, film);
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, comments);
        lvComments.setAdapter(commentAdapter);
        lvComments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                llAddComment.setVisibility(View.GONE);
                return false;
            }
        });
    }

    /**
     * 添加编辑监听
     */
    private void addEditListener() {
        TextView textView = new TextView(mBaseActivity);
        textView.setText(R.string.edit);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        mTitleBar.setRightLayoutListener(textView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFilmActivity.enterAddFilmActivity(mBaseActivity, film.getFilmName(), film);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filmAddress://点击电影地址,可以弹出更多详情
                FilmAddressActivity.enterFilmAddressActivity(this, film);
                break;
            case R.id.btn_like://点赞
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                likeOrDislike(true);
                break;
            case R.id.btn_dislike://踩
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                likeOrDislike(false);
                break;
            case R.id.btn_comment://评论
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                llAddComment.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_ineedcomment://我要评论
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                llAddComment.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_share://分享
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                ShareUtils.shareFilm(this, film);
                break;
            case R.id.btn_click_addcomment://点击添加评论
                llAddComment.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_release://发布评论
                sendComment();
                break;
        }
    }

    /**
     * 赞或者踩
     *
     * @param isLike 是否是赞(否则是踩咯)
     */
    public void likeOrDislike(boolean isLike) {
        /*if (film.getUser().getUsername().equals(UserHelper.getCurrentUserName())) {
            ToastUtils.showLong("亲,自己的就不要" + (isLike ? "赞" : "踩") + "了吧...呵呵");
            return;
        }*/
        this.isLike = isLike;
        BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_ISLIKEORDISLIKE, null, film);
    }

    /**
     * 发布评论
     */
    private void sendComment() {
        String comment = etCommentContent.getText().toString().trim();
        if (MyTextUtils.isEmpty(comment)) {
            ToastUtils.showShort(getString(R.string.no_your_comment));
        } else {
            BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_ADD_COMMENT,
                    creatWaitDialog(getString(R.string.submitting)), comment, film);
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof AddCommentEvent) {
            hideWaitDialog();
            AddCommentEvent addCommentEvent = (AddCommentEvent) obj;
            Object[] object = (Object[]) addCommentEvent.getObject();
            Object o = object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                ToastUtils.showLong("评论增加成功");
                LogUtils.i("返回objectId为 :" + o.toString());
                llAddComment.setVisibility(View.GONE);
                hideSoftInputMethod();
                BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_FIND_COMMENT, null, film);
                etCommentContent.setText("");
            } else {
                ToastUtils.showLong("评论增加失败: " + e.getMessage());
            }
        } else if (obj instanceof FindCommentEvent) {
            pb.setVisibility(View.GONE);
            FindCommentEvent findCommentEvent = (FindCommentEvent) obj;
            Object[] object = (Object[]) findCommentEvent.getObject();
            List<Comment> comments1 = (List<Comment>) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                comments = comments1;
                if (comments.size() > 0) {
                    llHascomment.setVisibility(View.VISIBLE);
                    llNocomment.setVisibility(View.GONE);
                    btnComment.setText(getString(R.string.comment, comments.size()));
                } else {
                    llNocomment.setVisibility(View.VISIBLE);
                }
                commentAdapter.setComments(comments);
            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        } else if (obj instanceof AddLikeEvent) {
            AddLikeEvent addLikeEvent = (AddLikeEvent) obj;
            BmobException e = (BmobException) addLikeEvent.getObject();
            if (e == null) {
                EventBus.getDefault().post(new RefreshFilmEvent());
                ToastUtils.showShort("赞+1");
                String like = btnLike.getText().toString();
                btnLike.setText(getString(R.string.like, (NumUtil.getNumFromStr(like)) + 1));
            } else {
                ToastUtils.showShort("点赞失败" + e.getMessage());
            }
        } else if (obj instanceof AddDislikeEvent) {
            AddDislikeEvent addDislikeEvent = (AddDislikeEvent) obj;
            BmobException e = (BmobException) addDislikeEvent.getObject();
            if (e == null) {
                EventBus.getDefault().post(new RefreshFilmEvent());
                ToastUtils.showShort("踩+1");
                String dislike = btnDislike.getText().toString();
                btnDislike.setText(getString(R.string.dislike, (NumUtil.getNumFromStr(dislike)) + 1));
            } else {
                ToastUtils.showShort("操作失败" + e.getMessage());
            }
        } else if (obj instanceof HasLikeOrDislikeEvent) {
            HasLikeOrDislikeEvent hasLikeOrDislikeEvent = (HasLikeOrDislikeEvent) obj;
            Object[] object = (Object[]) hasLikeOrDislikeEvent.getObject();
            List<LikeOrDislikeToFilm> o = (List<LikeOrDislikeToFilm>) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                if (o.size() == 0) {
                    //还没有赞或者踩过
                    BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_ADDLIKEORDISLIKE, null, film, isLike);
                } else {
                    if (isLike) {
                        if (o.get(0).isLike()) {
                            ToastUtils.showShort("你已经赞过了哦");
                        } else {
                            ToastUtils.showShort("你已经踩过了,不能再赞了哦");
                        }
                    } else {
                        if (o.get(0).isLike()) {
                            ToastUtils.showShort("你已经赞过了,不能再踩了哦");
                        } else {
                            ToastUtils.showShort("你已经踩过了哦");
                        }

                    }
                }

            } else {
                ToastUtils.showShort("操作失败" + e.getMessage());
            }
        }
    }

}
