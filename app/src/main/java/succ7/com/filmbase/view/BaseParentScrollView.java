package succ7.com.filmbase.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import succ7.com.filmbase.base.listener.IAnimationListener;


public abstract class BaseParentScrollView extends FrameLayout implements
        Handler.Callback, IParentScrollView {
    protected int a;
    protected int b;
    protected Context mContext;
    protected Scroller mScroller;
    protected int e;
    protected int f;
    protected int g;
    protected IAnimationListener IAnimation;
    protected Handler mHandler;
    protected boolean k;
    protected boolean l;
    protected boolean drag;

    public BaseParentScrollView(final Context context) {
        super(context);
        init(context);
    }

    public BaseParentScrollView(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    public BaseParentScrollView(Context context, AttributeSet set,
                                int defStyleAttr) {
        super(context, set, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.a = 0;
        this.b = this.a;
        this.e = 0;
        this.g = 0;
        this.k = true;
        this.l = false;
        this.drag = true;
        this.a(context);
    }

    private void a(int n, boolean b) {
        if (this.l) {
            return;
        }
        if (this.IAnimation != null) {
            this.IAnimation.d(b);
        }
        this.l = true;
        Message message = new Message();
        message.what = 1;
        message.obj = b;
        this.mHandler.sendMessageDelayed(message, (long) n);
    }

    private void a(final boolean b) {
        this.l = false;
        if (this.IAnimation != null) {
            this.IAnimation.e(b);
        }
    }

    protected void a(int a) {
        this.b = this.a;
        if (this.a == a) {
            return;
        }
        this.a = a;
    }

    protected void a(Context context) {
        this.mContext = context;
        this.mScroller = new Scroller(this.mContext,
                (Interpolator) new AccelerateDecelerateInterpolator());
        this.f = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
        this.mHandler = new Handler(this);
    }

    public boolean a() {
        return this.b != this.a;
    }

    protected void b() {
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessageDelayed(2, 50L);
    }

    public boolean c() {
        return this.drag;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            this.postInvalidate();
        }
    }

    public boolean handleMessage(final Message message) {
        switch (message.what) {
            case 0: {
                this.a(message.arg1, (boolean) message.obj);
                break;
            }
            case 1: {
                this.a((boolean) message.obj);
                break;
            }
            case 2: {
                this.h();
                break;
            }
        }
        return false;
    }

    public void setDefaultOpenStatus(int a) {
        this.a = a;
    }

    /**
     * 设置屏幕是否可拖拽
     *
     * @param drag
     */
    public void setDrag(boolean drag) {
        this.drag = drag;
    }

    public void setOnAnimationListener(IAnimationListener h) {
        this.IAnimation = h;
    }
}