package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.FilmAdapter;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.eventbus.FindFilmsByContributorEvent;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 我贡献的资源
 */
public class MyResActivity extends ScrollerBaseUIActivity {

    private List<Film> films;
    private FilmAdapter filmAdapter;
    private ListView lvMyres;
    private Button btnAddMyFilm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.my_res));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_my_res, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        lvMyres = (ListView) view.findViewById(R.id.lv_myres);
        btnAddMyFilm = (Button) view.findViewById(R.id.btn_add_my_film);
        BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_FIND_FILM_BY_CONTRIBUTOR,
                creatWaitDialog(getString(R.string.loading)));
        films = new ArrayList<>();
        filmAdapter = new FilmAdapter(mBaseActivity, films);
        lvMyres.setAdapter(filmAdapter);
        lvMyres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilmDetailActivity.enterFilmDetailActivity(mBaseActivity, films.get(position));
            }
        });

        btnAddMyFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFilmActivity.enterAddFilmActivity(mBaseActivity, "", null);
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof FindFilmsByContributorEvent) {
            hideWaitDialog();
            FindFilmsByContributorEvent findFilmsByContributorEvent = (FindFilmsByContributorEvent) obj;
            Object[] object = (Object[]) findFilmsByContributorEvent.getObject();
            BmobException e = (BmobException) object[1];
            if (e == null) {
                films = (List<Film>) object[0];
                if (films.size() == 0) {
                    ToastUtils.showLong("你还没有贡献任何资源哦");
                    btnAddMyFilm.setVisibility(View.VISIBLE);
                } else {
                    btnAddMyFilm.setVisibility(View.GONE);
                    filmAdapter.setFilms(films);
                }
            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        }
    }

}
