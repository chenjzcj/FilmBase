package succ7.com.filmbase.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import succ7.com.filmbase.R;


/**
 * Created by Administrator on 2015/6/10.
 */
public class EditDialog extends Dialog
{
    protected int mWith;
    protected Context mContext;
    protected View view;
    protected ImageView titleIcon;
    protected TextView titleText;
    protected TextView mTextView;
    protected Button rightButton;
    protected Button middleButton;
    protected Button leftButton;
    protected LinearLayout dialog_title;
    protected LinearLayout dialog_title_line;
    protected LinearLayout dialog_body;
    protected LinearLayout dialog_space_bar;
    protected LinearLayout dialog_botton;
    protected LinearLayout layoutContainer;
    protected ScrollView mScrollView;
    protected LinearLayout dialog_fix_body;
    private float mDensity;
    private int s;
    private View.OnClickListener mClickLisnter;
    private boolean cancelFlag;

    public EditDialog(Context context) {
        super(context, R.style.dialog);
        this.mDensity = 1.0f;
        this.s = 0;
        this.mClickLisnter = null;
        this.cancelFlag = true;
        this.mContext =context;
        this.initView();
    }

    private void setLayoutParamsButton( Button button) {
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.weight = 1.0f;
        button.setPadding(this.s, this.s, this.s, this.s);
        this.dialog_botton.addView((View)button, (ViewGroup.LayoutParams)params);
    }

    private void setButtonTxtSize(Button button, int n)
    {

        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dialog_button_text_size));
        button.setTextColor(this.mContext.getResources().getColorStateList(R.color.selector_dialog_button_text_default));
    }
    private void setLayoutParamsButton(){
         LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.mContext.getResources().getDimensionPixelSize(R.dimen. dialog_button_line_width), LinearLayout.LayoutParams.MATCH_PARENT);
         layoutParams.gravity = Gravity.CENTER_VERTICAL;
         LinearLayout linearLayout = new LinearLayout(this.mContext);
         linearLayout.setBackgroundResource(R.color.base_dialog_button_line);
        this.dialog_botton.addView((View)linearLayout, (ViewGroup.LayoutParams)layoutParams);
    }

    private void setDialogbutton(){
        this.dialog_botton.removeAllViewsInLayout();
        if (this.middleButton != null) {
            this.setLayoutParamsButton(this.middleButton);
            if (this.leftButton != null || this.rightButton!= null) {
                this.middleButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default_left);
            }
            else {
                this.middleButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default_mid);
            }
        }
        if (this.leftButton!= null) {
            if (this.middleButton != null) {
                this.setLayoutParamsButton();
            }
            if (this.middleButton != null || this.rightButton != null) {
                if (this.middleButton == null) {
                    this.leftButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default_left);
                }
                else if (this.rightButton == null) {
                    this.leftButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default_right);
                }
                else {
                    this.leftButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default);
                }
            }
            else {
                this.leftButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default);
            }
            this.setLayoutParamsButton(this.leftButton);
        }
        if (this.rightButton != null) {
            if (this.middleButton!= null || this.leftButton != null) {
                this.setLayoutParamsButton();
                this.rightButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default_right);
            }
            else {
                this.rightButton.setBackgroundResource(R.drawable.selector_button_bg_dialog_default);
            }
            this.setLayoutParamsButton(this.rightButton);
        }
    }

    protected int getLayoutId() {
        return R.layout.layout_dialog;
    }

    public void setRightButtonTxt( int n) {
        this.rightButton.setText((CharSequence) this.mContext.getString(n));
    }

    public void setRightButton(int n, View.OnClickListener listener, int n2) {
        this.setRightButton(n, listener, n2, -1);
    }
    public void setRightButton(int n, View.OnClickListener onClickListener, int n2, int n3) {
        if (this.rightButton != null) {
            this.dialog_botton.removeView((View)this.rightButton);
        }
        this.setButtonTxtSize(this.rightButton = new Button(this.mContext), n2);
        this.dialog_space_bar.setVisibility(View.VISIBLE);
        this.dialog_botton.setVisibility(View.VISIBLE);
        this.rightButton.setText((CharSequence) this.mContext.getString(n));
        this.rightButton.setOnClickListener(onClickListener);
        this.rightButton.setGravity(Gravity.CENTER);
    }

    public void setBitDrawableByTitle( BitmapDrawable imageDrawable) {
        this.titleIcon.setVisibility(View.VISIBLE);
        this.titleIcon.setImageDrawable((Drawable)imageDrawable);
    }

    public void setViewlisnter( View.OnClickListener lisntener) {
        this.mClickLisnter = lisntener;
    }

    public void setDialogBody( View view) {
        this.dialog_body.removeAllViews();
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).gravity = Gravity.CENTER;
        this.dialog_body.setPadding(0, 0, 0, 0);
        this.dialog_body.addView(view);
    }

    public void setFixBody(View view, ViewGroup.LayoutParams params) {
        this.mScrollView.setVisibility(View.GONE);
        this.dialog_fix_body.setVisibility(View.VISIBLE);
        this.dialog_fix_body.removeAllViews();
        this.dialog_fix_body.addView(view, params);
    }

    public void setBodyText( String text){
        this.dialog_body.removeAllViews();
        (this.mTextView = new TextView(this.mContext)).setTextColor(this.mContext.getResources().getColor(R.color.base_dialog_content_text));
        this.mTextView.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dialog_content_text_size));
        this.mTextView.setLineSpacing(0.0f, 1.0f);
        this.mTextView.setGravity(Gravity.CENTER_VERTICAL);
        final LinearLayout.LayoutParams  layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1.0f;
        this.dialog_body.addView((View)this.mTextView, (ViewGroup.LayoutParams)layoutParams);
        this.mTextView.setText((CharSequence)text);
    }

    public void setRightButton(String s, View.OnClickListener onClickListener, int n) {
        this.setRightButton(s, onClickListener, n, -1);
    }

    public void setRightButton(String text, View.OnClickListener onClickListener, int n, int n2) {
        if (this.rightButton != null) {
            this.dialog_botton.removeView((View)this.rightButton);
        }
        this.setButtonTxtSize(this.rightButton = new Button(this.mContext), n);
        this.dialog_space_bar.setVisibility(View.VISIBLE);
        this.dialog_botton.setVisibility(View.VISIBLE);
        this.rightButton.setText((CharSequence)text);
        this.rightButton.setOnClickListener(onClickListener);
        this.rightButton.setGravity(Gravity.CENTER);
    }

    protected void initView() {
        this.mWith = this.mContext.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        this.mDensity = this.mContext.getResources().getDisplayMetrics().density;
        this.s = (int)(6.0f*this.mDensity);
        this.view = LayoutInflater.from(this.mContext).inflate(this.getLayoutId(), (ViewGroup)null);
        this.layoutContainer = (LinearLayout)this.view.findViewById(R.id.layout_container);
        this.titleIcon=(ImageView)this.view.findViewById(R.id.title_icon);
        this.titleText = (TextView)this.view.findViewById(R.id.title_text);
        this.dialog_title = (LinearLayout)this.view.findViewById(R.id.dialog_title);
        this.dialog_title_line = (LinearLayout)this.view.findViewById(R.id.dialog_title_line);
        this.dialog_body = (LinearLayout)this.view.findViewById(R.id.dialog_body);
        this.dialog_botton = (LinearLayout)this.view.findViewById(R.id.dialog_botton);
        this.dialog_space_bar = (LinearLayout)this.view.findViewById(R.id.dialog_space_bar);
        this.mScrollView = (ScrollView)this.view.findViewById(R.id.dialog_scroll_body);
        this.dialog_fix_body = (LinearLayout)this.view.findViewById(R.id.dialog_fix_body);
         int n= 5 *this.mWith/ 100;
        this.view.setPadding(n, n, n, n);
    }

    public void setdialgBodyBG(int backgroundResource) {
        this.dialog_body.setBackgroundResource(backgroundResource);
    }

    public void setMiddleButton(int n, View.OnClickListener onClickListener, int n2) {
        this.setMiddleButton(n, onClickListener, n2, -1);
    }

    public void setMiddleButton(int n, View.OnClickListener onClickListener, int n2, int n3) {
        if (this.middleButton != null) {
            this.dialog_botton.removeView((View)this.middleButton);
        }
        this.setButtonTxtSize(this.middleButton = new Button(this.mContext), n2);
        this.dialog_space_bar.setVisibility(View.VISIBLE);
        this.dialog_botton.setVisibility(View.VISIBLE);
        this.middleButton.setText((CharSequence) this.mContext.getString(n));
        this.middleButton.setOnClickListener(onClickListener);
        this.middleButton.setGravity(Gravity.CENTER);
    }

    public void setMiddleButton(String s, View.OnClickListener listener, int n){
        this.setMiddleButton(s, listener, n, -1);
    }

    public void setMiddleButton(String text, View.OnClickListener onClickListener, int n, int n2) {
        if(this.middleButton != null)
        {
            this.dialog_botton.removeView((View)this.middleButton);
        }
        this.setButtonTxtSize(this.middleButton = new Button(this.mContext), n);
        this.dialog_space_bar.setVisibility(View.VISIBLE);
        this.dialog_botton.setVisibility(View.VISIBLE);
        this.middleButton.setText((CharSequence) text);
        this.middleButton.setOnClickListener(onClickListener);
        this.middleButton.setGravity(Gravity.CENTER);
    }

    public ImageView getTitleIcon() {
        return this.titleIcon;
    }

    public void setBodyText( int n) {
        this.setBodyText(this.mContext.getString(n));
    }

    public void setLeftButton(int n, View.OnClickListener onClickListener, int n2) {
        this.setLeftButton(n, onClickListener, n2, -1);
    }

    public void setLeftButton(int n, View.OnClickListener onClickListener, int n2, final int n3) {
        if (this.leftButton!= null) {
            this.dialog_botton.removeView((View)this.leftButton);
        }
        this.setButtonTxtSize(this.leftButton = new Button(this.mContext), n2);
        this.dialog_space_bar.setVisibility(View.VISIBLE);
        this.dialog_botton.setVisibility(View.VISIBLE);
        this.leftButton.setText((CharSequence) this.mContext.getString(n));
        this.leftButton.setOnClickListener(onClickListener);
        this.leftButton.setGravity(Gravity.CENTER);
    }
    public void dstoryMiddButotn()
        {
            if (this.middleButton != null)
            {
                this.dialog_botton.removeView((View)this.middleButton);
            }
        }
    @Override
    protected void onCreate( Bundle bundle) {
        super.onCreate(bundle);
        super.setContentView(this.view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    @Override
    public boolean onKeyDown( int keyCode,  KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (this.mClickLisnter != null){
                this.mClickLisnter.onClick((View)null);
            }
            else if (this.cancelFlag) {
                this.dismiss();
                return true;
            }
        }
        else if (keyCode != KeyEvent.KEYCODE_SEARCH) {
            return super.onKeyDown(keyCode, keyEvent);
        }
        return true;
    }

    public void setCancelable( boolean flag){
        super.setCancelable(flag);
        this.cancelFlag =flag;
    }

    public void setContentView( View view) {
        this.dialog_body.removeAllViews();
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        this.dialog_body.addView(view,(ViewGroup.LayoutParams)layoutParams);
    }

    public void setTitle( int n) {
        this.setTitle(this.mContext.getResources().getString(n));
    }

    public void setTitle( CharSequence text) {
        this.dialog_title.setVisibility(View.VISIBLE);
        this.dialog_title_line.setVisibility(View.VISIBLE);
        this.titleText.setText(text);
    }
    public void show()
    {
        this.setDialogbutton();
        super.show();
    }
}