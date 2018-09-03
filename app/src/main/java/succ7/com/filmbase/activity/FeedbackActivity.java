package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.eventbus.AddFeedbackEvent;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 意见反馈
 */
public class FeedbackActivity extends ScrollerBaseUIActivity {

    private EditText etContact;
    private EditText etContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.feedback));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_feedback, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        etContact = (EditText) view.findViewById(R.id.et_contact);
        etContent = (EditText) view.findViewById(R.id.et_content);
    }

    public void feedback(View view) {
        String contact = etContact.getText().toString();
        String content = etContent.getText().toString();
        if (MyTextUtils.isEmpty(contact) || MyTextUtils.isEmpty(content)) {
            ToastUtils.showShort(getString(R.string.enjoy_remind));
        } else {
            BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_FEEDBACK,
                    creatWaitDialog(getString(R.string.submitting)), contact, content);
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof AddFeedbackEvent) {
            hideWaitDialog();
            AddFeedbackEvent addFeedbackEvent = (AddFeedbackEvent) obj;
            BmobException e = (BmobException) addFeedbackEvent.getObject();
            if (e == null) {
                ToastUtils.showShort(getString(R.string.thanks_your_feedback));
                onBackPressed();
            } else {
                ToastUtils.showShort("操作失败" + e.getMessage());
            }
        }
    }
}
