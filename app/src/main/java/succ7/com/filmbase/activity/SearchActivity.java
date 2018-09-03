package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.SearchFilmAdapter;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.eventbus.SearchFilmsByFilmNameKeyWordEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;
import succ7.com.filmbase.view.dialog.WaitDialog;

public class SearchActivity extends ScrollerBaseUIActivity {

    private String[] hotFilms = new String[]{"绝地逃亡", "封神传奇", "伦敦陷落", "疯狂动物城", "我的特工爷爷",
            "寻龙诀", "九层妖塔", "寒战2", "大鱼海棠", "末日崩塌"};
    private EditText etSearch;
    private ListView lvSearchFilms;
    private Random mRandom;
    private List<Film> films;
    private boolean clickSearch;
    private SearchFilmAdapter filmAdapter;
    private Handler mChangeKeyWordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mChangeKeyWordHandler.sendEmptyMessageDelayed(0, 3500);
            etSearch.setHint(hotFilms[mRandom.nextInt(hotFilms.length)]);
        }
    };
    private LinearLayout llNothingSearch;
    private Button btnAddMyFilm;
    private String mKeyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.film_search));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_search, null);
        this.addMainView(view);
        mRandom = new Random();
        initView(view);
        mChangeKeyWordHandler.sendEmptyMessage(0);
    }

    private void initView(View view) {
        etSearch = (EditText) view.findViewById(R.id.et_search);
        llNothingSearch = (LinearLayout) view.findViewById(R.id.ll_nothing_search);
        btnAddMyFilm = (Button) view.findViewById(R.id.btn_add_my_film);
        btnAddMyFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = etSearch.getText().toString();
                if (!MyTextUtils.isEmpty(keyWord)) {
                    AddFilmActivity.enterAddFilmActivity(mBaseActivity, keyWord, null);
                }
            }
        });
        lvSearchFilms = (ListView) view.findViewById(R.id.lv_search_films);
        films = new ArrayList<>();
        filmAdapter = new SearchFilmAdapter(mBaseActivity, films);
        lvSearchFilms.setAdapter(filmAdapter);
        lvSearchFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilmDetailActivity.enterFilmDetailActivity(mBaseActivity, films.get(position));
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWord = s.toString();
                LogUtils.i("keyWord = " + keyWord);
                if (!MyTextUtils.isEmpty(keyWord)) {
                    search(keyWord, false);
                } else {
                    llNothingSearch.setVisibility(View.GONE);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LogUtils.i("actionId = " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWord = v.getText().toString();
                    String defalutKeyWord = v.getHint().toString();
                    if (MyTextUtils.isEmpty(keyWord)) {
                        if (MyTextUtils.isEmpty(defalutKeyWord)) {
                            ToastUtils.showShortToast(mBaseActivity, getString(R.string.keyword_is_null));
                        } else {
                            search(defalutKeyWord, true);
                        }
                    } else {
                        clickSearch = true;
                        search(keyWord, true);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 搜索
     *
     * @param keyWord        关键字
     * @param showWaitDialog 是否显示等待对话框,true为显示
     */
    private void search(String keyWord, boolean showWaitDialog) {
        mKeyword = keyWord;
        WaitDialog waitDialog = null;
        if (showWaitDialog)
            waitDialog = creatWaitDialog(getString(R.string.searching));
        BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_FIND_FILM_BY_KEYWORD, waitDialog, keyWord);
    }

    private void searchFromThirdParty() {
        RequestParams params = new RequestParams(Constants.INTERFACE_MOVIE_SEARCH);
        params.addParameter("key", Constants.MOVIE_APPKEY);
        params.addParameter("q", mKeyword);
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                LogUtils.i("result = " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideWaitDialog();
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof SearchFilmsByFilmNameKeyWordEvent) {
            hideWaitDialog();
            SearchFilmsByFilmNameKeyWordEvent searchFilmsByFilmNameKeyWordEvent = (SearchFilmsByFilmNameKeyWordEvent) obj;
            Object[] object = (Object[]) searchFilmsByFilmNameKeyWordEvent.getObject();
            BmobException e = (BmobException) object[1];
            if (e == null) {
                films = (List<Film>) object[0];
                filmAdapter.setFilms(films);
                if (films.size() == 0 && clickSearch) {
                    clickSearch = false;
                    //从第三方服务器请求
                    searchFromThirdParty();
                }
                llNothingSearch.setVisibility(films.size() == 0 ? View.VISIBLE : View.GONE);
            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        }
    }
}
