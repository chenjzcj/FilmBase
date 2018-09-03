package succ7.com.filmbase.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.AddFilmEvent;
import succ7.com.filmbase.eventbus.RefreshFilmEvent;
import succ7.com.filmbase.eventbus.UpdateFilmEvent;
import succ7.com.filmbase.eventbus.UploadPicEvent;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.PhotoUtils;
import succ7.com.filmbase.utils.PicassoUtils;
import succ7.com.filmbase.utils.ToastUtils;

public class AddFilmActivity extends ScrollerBaseUIActivity implements View.OnClickListener {

    private EditText etFilmname, etFilmSource, etFilmLength, etFilmTypes, etFilmArea, etFilmYear, etFilmActors, etFilmAddress, etOther;
    public static final String EXTRA_FILMNAME = "filmname";
    public static final String EXTRA_FILM = "film";
    private String filmName;
    private Film film;
    private ImageView etFilmpic;
    private Dialog dialog;
    private String filmPicUrl;
    private String unit;
    private LinearLayout llSource, llUnit, llType1, llType2, llArea1, llArea2, llYear1, llYear2;
    private TextView tvBaidu, tv360, tvOther, tvUnit, tvKb, tvMb, tvGb;
    private int[] typeIds = new int[]{R.id.tv_type0, R.id.tv_type1, R.id.tv_type2, R.id.tv_type3, R.id.tv_type4,
            R.id.tv_type5, R.id.tv_type6, R.id.tv_type7, R.id.tv_type8, R.id.tv_type9};
    private int[] areaIds = new int[]{R.id.tv_area0, R.id.tv_area1, R.id.tv_area2, R.id.tv_area3, R.id.tv_area4,
            R.id.tv_area5, R.id.tv_area6, R.id.tv_area7, R.id.tv_area8, R.id.tv_area9, R.id.tv_area10,
            R.id.tv_area11, R.id.tv_area12, R.id.tv_area13};
    private int[] yearIds = new int[]{R.id.tv_year0, R.id.tv_year1, R.id.tv_year2, R.id.tv_year3, R.id.tv_year4,
            R.id.tv_year5, R.id.tv_year6, R.id.tv_year7, R.id.tv_year8, R.id.tv_year9};

    public static void enterAddFilmActivity(Activity activity, String filmName, Film film) {
        Intent intent = new Intent(activity, AddFilmActivity.class);
        intent.putExtra(EXTRA_FILMNAME, filmName);
        intent.putExtra(EXTRA_FILM, film);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mTitleBar.setTitle(getString(R.string.add_film));
        backListener();
        filmName = getIntent().getStringExtra(EXTRA_FILMNAME);
        film = (Film) getIntent().getSerializableExtra(EXTRA_FILM);
        View view = this.mInflater.inflate(R.layout.activity_add_film, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        etFilmpic = (ImageView) view.findViewById(R.id.iv_filmpic);
        etFilmname = (EditText) view.findViewById(R.id.et_filmname);
        etFilmSource = (EditText) view.findViewById(R.id.et_filmSource);
        llSource = (LinearLayout) view.findViewById(R.id.ll_source);
        etFilmLength = (EditText) view.findViewById(R.id.et_filmLength);
        llUnit = (LinearLayout) view.findViewById(R.id.ll_unit);
        etFilmTypes = (EditText) view.findViewById(R.id.et_filmTypes);
        etFilmArea = (EditText) view.findViewById(R.id.et_filmArea);
        etFilmYear = (EditText) view.findViewById(R.id.et_filmYear);
        etFilmActors = (EditText) view.findViewById(R.id.et_filmActors);
        llType1 = (LinearLayout) view.findViewById(R.id.ll_type1);
        llType2 = (LinearLayout) view.findViewById(R.id.ll_type2);
        llArea1 = (LinearLayout) view.findViewById(R.id.ll_area1);
        llArea2 = (LinearLayout) view.findViewById(R.id.ll_area2);
        llYear1 = (LinearLayout) view.findViewById(R.id.ll_year1);
        llYear2 = (LinearLayout) view.findViewById(R.id.ll_year2);
        etFilmAddress = (EditText) view.findViewById(R.id.et_filmAddress);
        etOther = (EditText) view.findViewById(R.id.et_other);
        etFilmname.setText(filmName);
        if (film != null) {
            filmPicUrl = film.getFilmPicUrl();
            PicassoUtils.showImage(mBaseActivity, film.getFilmPicUrl(), R.mipmap.defalut_film, etFilmpic);
            etFilmname.setText(film.getFilmName());
            etFilmSource.setText(film.getFilmSource());
            etFilmLength.setText(String.valueOf(film.getFilmLength()));
            String filmTypes = film.getFilmTypes();
            if (filmTypes != null) {
                String[] strings = filmTypes.split(";");
                for (String type : strings) {
                    setText(type, true, etFilmTypes);
                }
            }
            etFilmAddress.setText(film.getFilmAddress());
            etFilmArea.setText(film.getFilmArea());
            etFilmYear.setText(film.getFilmYear());
            etFilmActors.setText(film.getFilmActors());
            etOther.setText(film.getOtherInfo());
        }
        (tvBaidu = (TextView) view.findViewById(R.id.tv_baidu)).setOnClickListener(this);
        (tv360 = (TextView) view.findViewById(R.id.tv_360)).setOnClickListener(this);
        (tvOther = (TextView) view.findViewById(R.id.tv_other)).setOnClickListener(this);

        (tvUnit = (TextView) view.findViewById(R.id.tv_unit)).setOnClickListener(this);
        (tvKb = (TextView) view.findViewById(R.id.tv_kb)).setOnClickListener(this);
        (tvMb = (TextView) view.findViewById(R.id.tv_mb)).setOnClickListener(this);
        (tvGb = (TextView) view.findViewById(R.id.tv_gb)).setOnClickListener(this);

        initSelectItems(view, typeIds, Constants.types, etFilmTypes);
        initSelectItems(view, areaIds, Constants.areas, etFilmArea);
        initSelectItems(view, yearIds, Constants.years, etFilmYear);

        etFilmpic.setOnClickListener(this);
        view.findViewById(R.id.btn_addfilm).setOnClickListener(this);
        setFouchChageListener(etFilmSource, llSource);
        setFouchChageListener(etFilmTypes, llType1, llType2);
        setFouchChageListener(etFilmArea, llArea1, llArea2);
        setFouchChageListener(etFilmYear, llYear1, llYear2);
        setFouchChageListener(etFilmLength, llUnit);
    }

    /**
     * 设置编辑栏焦点变化监听
     *
     * @param et 需要监听其焦点变化的编辑框
     * @param ll 需要隐藏显示的view
     */
    private void setFouchChageListener(EditText et, final LinearLayout... ll) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                for (LinearLayout l : ll) {
                    l.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
                    l.startAnimation(AnimationUtils.loadAnimation(mBaseActivity, hasFocus ? R.anim.popup_enter : R.anim.popup_exit));
                }
            }
        });
    }

    private void initSelectItems(View view, int[] resIds, String[] itemStr, EditText tag) {
        for (int i = 0; i < resIds.length; i++) {
            TextView textView = (TextView) view.findViewById(resIds[i]);
            textView.setText(itemStr[i]);
            textView.setOnClickListener(new MyOnClickListener(resIds, itemStr, tag));
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        private int[] resIds;
        private String[] itemStr;
        private EditText et;

        public MyOnClickListener(int[] resIds, String[] itemStr, EditText et) {
            this.resIds = resIds;
            this.itemStr = itemStr;
            this.et = et;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < resIds.length; i++) {
                if (v.getId() == resIds[i]) {
                    v.setSelected(!v.isSelected());
                    setText(itemStr[i], v.isSelected(), et);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_filmpic:
                showSelectedPhotoDialog();
                break;
            case R.id.btn_addfilm:
                addFilm();
                break;
            case R.id.tv_baidu:
                tvBaidu.setSelected(true);
                tv360.setSelected(false);
                tvOther.setSelected(false);
                etFilmSource.setText("百度网盘");
                break;
            case R.id.tv_360:
                tvBaidu.setSelected(false);
                tv360.setSelected(true);
                tvOther.setSelected(false);
                etFilmSource.setText("360云盘");
                break;
            case R.id.tv_other:
                tvBaidu.setSelected(false);
                tv360.setSelected(false);
                tvOther.setSelected(true);
                etFilmSource.setText("其他");
                break;
            case R.id.tv_unit:
                boolean b = llUnit.getVisibility() == View.GONE;
                llUnit.setVisibility(b ? View.VISIBLE : View.GONE);
                llUnit.startAnimation(AnimationUtils.loadAnimation(mBaseActivity, b ? R.anim.popup_enter : R.anim.popup_exit));
                break;
            case R.id.tv_kb:
                tvUnit.setText("KB");
                unit = "KB";
                llUnit.setVisibility(View.GONE);
                tvKb.setSelected(true);
                tvMb.setSelected(false);
                tvGb.setSelected(false);
                break;
            case R.id.tv_mb:
                tvUnit.setText("MB");
                unit = "MB";
                llUnit.setVisibility(View.GONE);
                tvKb.setSelected(false);
                tvMb.setSelected(true);
                tvGb.setSelected(false);
                break;
            case R.id.tv_gb:
                tvUnit.setText("GB");
                unit = "GB";
                llUnit.setVisibility(View.GONE);
                tvKb.setSelected(false);
                tvMb.setSelected(false);
                tvGb.setSelected(true);
                break;
        }
    }

    /**
     * 设置电影标签
     *
     * @param type  标签
     * @param isAdd 是否是增加true为新增
     */
    /**
     * 设置电影类型,地区,年代的文本内容
     *
     * @param text  需要设置的文本内容
     * @param isAdd 是否是添加 true为添加
     * @param et    类型,分为三种: type,area,year
     */
    private void setText(String text, boolean isAdd, EditText et) {
        String tags = et.getText().toString();
        if (isAdd) {
            tags = tags + text + ";";
        } else {
            tags = tags.replace(text + ";", "");
        }
        et.setText(tags);
    }

    /**
     * 显示选择照片来源对话框
     */
    private void showSelectedPhotoDialog() {
        View view = mInflater.inflate(R.layout.layout_dialog_select_photo, null);
        view.findViewById(R.id.tv_takephoto).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.tv_selectfromgallery).setOnClickListener(itemsOnClick);

        dialog = new Dialog(mBaseActivity);
        dialog.setContentView(view);
        //去掉黑色背景
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (dialog != null) {
                dialog.dismiss();
            }
            switch (v.getId()) {
                case R.id.tv_takephoto:
                    PhotoUtils.takePicture(AddFilmActivity.this);
                    break;
                case R.id.tv_selectfromgallery:
                    PhotoUtils.selectFromAlbum(AddFilmActivity.this);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.PHOTO_PICKED_WITH_DATA:
                if (data != null && data.getData() != null)
                    PhotoUtils.startPhotoZoom(this, data.getData());
                break;
            case PhotoUtils.CAMERA_WITH_DATA:
                if (resultCode != 0)
                    PhotoUtils.startPhotoZoom(this, PhotoUtils.tempuri);
                break;
            case PhotoUtils.CUT_PHOTO:
                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃当前功能时，
                 * 会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况 &nbsp;
                 */
                if (data != null)
                    creatWaitDialog(getString(R.string.submit_film_pic)).show();
                PhotoUtils.saveAndUpload(this, data);
                break;
            default:
                break;
        }
    }

    private void addFilm() {
        String filmName = etFilmname.getText().toString();
        String filmSource = etFilmSource.getText().toString();
        String filmLength = etFilmLength.getText().toString();
        String filmTypes = etFilmTypes.getText().toString();
        String filmArea = etFilmArea.getText().toString();
        String filmYear = etFilmYear.getText().toString();
        String filmActors = etFilmActors.getText().toString();
        String filmAddress = etFilmAddress.getText().toString();
        String filmOther = etOther.getText().toString();
        if (MyTextUtils.isEmpty(filmName) || MyTextUtils.isEmpty(filmSource) || MyTextUtils.isEmpty(filmLength)
                || MyTextUtils.isEmpty(filmTypes) || MyTextUtils.isEmpty(filmArea) || MyTextUtils.isEmpty(filmYear)
                || MyTextUtils.isEmpty(filmActors) || MyTextUtils.isEmpty(filmAddress)) {
            ToastUtils.showLongToast(mBaseActivity, getString(R.string.enjoy_remind));
            return;
        }
        if (filmPicUrl == null) {
            ToastUtils.showLongToast(mBaseActivity, getString(R.string.give_film_a_pic));
            return;
        }
        unit = (unit == null) ? "GB" : unit;
        if (film != null) {
            BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_UPDATE_FILM,
                    creatWaitDialog(getString(R.string.submitting)), film, filmPicUrl,
                    filmName, filmSource, Double.parseDouble(filmLength), unit, filmTypes,
                    filmArea, filmYear, filmActors, filmAddress, filmOther);
        } else {
            BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_ADD_FILM,
                    creatWaitDialog(getString(R.string.submitting)), filmPicUrl, filmName,
                    filmSource, Double.parseDouble(filmLength), unit, filmTypes, filmArea,
                    filmYear, filmActors, filmAddress, filmOther);
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof AddFilmEvent) {
            hideWaitDialog();
            AddFilmEvent addFilmEvent = (AddFilmEvent) obj;
            BmobException e = (BmobException) addFilmEvent.getObject();
            if (e == null) {
                ToastUtils.showLong("电影增加成功,影币增加5个!");
                EventBus.getDefault().post(new RefreshFilmEvent());
                UserHelper.updateCoin(UserHelper.COIN_GET_TYPE_ADD_FILM);
                onBackPressed();
            } else {
                ToastUtils.showLong("电影增加失败: " + e.getMessage());
            }
        } else if (obj instanceof UpdateFilmEvent) {
            hideWaitDialog();
            UpdateFilmEvent updateFilmEvent = (UpdateFilmEvent) obj;
            BmobException e = (BmobException) updateFilmEvent.getObject();
            if (e == null) {
                ToastUtils.showLong("电影更新成功!");
                EventBus.getDefault().post(new RefreshFilmEvent());
                onBackPressed();
            } else {
                ToastUtils.showLong("电影更新失败: " + e.getMessage());
            }

        } else if (obj instanceof UploadPicEvent) {
            hideWaitDialog();
            UploadPicEvent uploadPicEvent = (UploadPicEvent) obj;
            Object[] object = (Object[]) uploadPicEvent.getObject();
            BmobFile bmobFile = (BmobFile) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                filmPicUrl = bmobFile.getUrl();
                ToastUtils.showLong("图片上传成功");
                PicassoUtils.showImage(mBaseActivity, filmPicUrl, R.mipmap.defalut_film, etFilmpic);
            } else {
                ToastUtils.showLong("图片上传失败: " + e.getMessage());
            }
        }
    }
}
