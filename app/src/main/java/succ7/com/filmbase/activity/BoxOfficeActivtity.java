package succ7.com.filmbase.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.BoxOfficeAdapter;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.juhe.MovieBoxOffice;

/**
 * 电影票房
 */
public class BoxOfficeActivtity extends ScrollerBaseUIActivity implements View.OnClickListener {

    private ListView lvBoxoffice;
    private List<MovieBoxOffice> movieBoxOffices = new ArrayList<>();
    private BoxOfficeAdapter boxOfficeAdapter;
    private Button boxofficeChina, boxofficeUs, boxofficeHk;

    public static void enterBoxOfficeActivtity(Activity activity) {
        Intent intent = new Intent(activity, BoxOfficeActivtity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.lastest_box_office));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_box_office_activtity, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        lvBoxoffice = (ListView) view.findViewById(R.id.lv_boxoffice);
        lvBoxoffice = (ListView) view.findViewById(R.id.lv_boxoffice);
        (boxofficeChina = (Button) view.findViewById(R.id.boxoffice_china)).setOnClickListener(this);
        (boxofficeUs = (Button) view.findViewById(R.id.boxoffice_us)).setOnClickListener(this);
        (boxofficeHk = (Button) view.findViewById(R.id.boxoffice_hk)).setOnClickListener(this);
        boxOfficeAdapter = new BoxOfficeAdapter(mBaseActivity, movieBoxOffices);
        lvBoxoffice.setAdapter(boxOfficeAdapter);
        getBoxOfficeInfo("CN");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boxoffice_china:
                getBoxOfficeInfo("CN");
                break;
            case R.id.boxoffice_us:
                getBoxOfficeInfo("US");
                break;
            case R.id.boxoffice_hk:
                getBoxOfficeInfo("HK");
                break;
        }
    }

    /**
     * 设置选中状态
     *
     * @param area 地区
     */
    private void setSelected(String area) {
        boxofficeChina.setSelected("CN".equals(area));
        boxofficeUs.setSelected("US".equals(area));
        boxofficeHk.setSelected("HK".equals(area));
    }

    private void getBoxOfficeInfo(final String area) {
        setSelected(area);
        creatWaitDialog(getString(R.string.loading)).show();
        RequestParams params = new RequestParams(Constants.INTERFACE_BOXOFFICE);
        params.addParameter("key", Constants.BOXOFFICE_APPKEY);
        params.addParameter("area", (area != null) ? area : "CN");
        x.http().get(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    movieBoxOffices.clear();
                    String reason = result.getString("reason");
                    String resultcode = result.getString("resultcode");
                    int error_code = result.getInt("error_code");
                    if ("success".equals(reason)) {
                        JSONArray jsonArray = result.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            MovieBoxOffice movieBoxOffice = new MovieBoxOffice();
                            movieBoxOffice.setRid(jsonObject.getInt("rid"));
                            movieBoxOffice.setName(jsonObject.getString("name"));
                            movieBoxOffice.setWk(jsonObject.getString("wk"));
                            movieBoxOffice.setTboxoffice(jsonObject.getString("tboxoffice"));
                            movieBoxOffice.setWboxoffice(jsonObject.getString("wboxoffice"));
                            movieBoxOffices.add(movieBoxOffice);
                        }
                        boxOfficeAdapter.setMovieBoxOffices(movieBoxOffices);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
