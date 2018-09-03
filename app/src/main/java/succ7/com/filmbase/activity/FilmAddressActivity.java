package succ7.com.filmbase.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.vitamio.MainActivity;

public class FilmAddressActivity extends Activity {

    private static final String EXTRA_FILM = "film";

    /**
     * 进入电影详情页面
     *
     * @param activity Activity
     * @param film     Film
     */
    public static void enterFilmAddressActivity(Activity activity, Film film) {
        Intent intent = new Intent(activity, FilmAddressActivity.class);
        intent.putExtra(EXTRA_FILM, film);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_address);
        final Film film = (Film) getIntent().getSerializableExtra(EXTRA_FILM);
        StringBuilder sb = new StringBuilder();
        if (!MyTextUtils.isEmpty(film.getFilmAddress())) {
            sb.append(film.getFilmAddress());
        }
        if (!MyTextUtils.isEmpty(film.getOtherInfo())) {
            sb.append(getString(R.string.secret)).append(film.getOtherInfo());
        }
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        tvAddress.setText(getString(R.string.filmAddress, sb.toString()));
        tvAddress.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvAddress.getPaint().setAntiAlias(true);//抗锯齿
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //WebActivity.openWebNewsActivity(FilmAddressActivity.this, film);
                //测试地址 : http://baobab.wdjcdn.com/145076769089714.mp4
                MainActivity.playVideo(FilmAddressActivity.this,film.getFilmAddress(),film.getFilmName());

                /*XWebViewActivity.openXWebViewActivity(FilmAddressActivity.this, film);

                MyTextUtils.copy(film.getOtherInfo(), FilmAddressActivity.this,
                        getString(R.string.chat_had_copy), !MyTextUtils.isEmpty(film.getOtherInfo()));*/
                finish();
            }
        });
    }

}
