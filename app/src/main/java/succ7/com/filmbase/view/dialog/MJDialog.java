package succ7.com.filmbase.view.dialog;

/**
 * Created by Administrator on 2015/5/22.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import succ7.com.filmbase.R;


public class MJDialog extends Dialog
{
    private Context mContext;

    public MJDialog(Context context){
        super(context, R.style.loading_dialog);
        this.mContext = context;
        this.setContentView(R.layout.layout_common_progress);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }
    public void setTipTxt( String text)
     {
         TextView textView = (TextView)this.findViewById(R.id.tip_text_view);
        if (textView != null)
        {
            textView.setText((CharSequence)text);
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ((ImageView)this.findViewById(R.id.loading_image_view)).setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim));
    }
}