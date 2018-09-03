package succ7.com.filmbase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HoriParentScrollView extends BaseParentScrollView {
    private float n;
    private float o;

    public HoriParentScrollView(Context context) {
        super(context);
    }

    public HoriParentScrollView(Context context, AttributeSet set) {
        super(context, set);
    }

    private void a(MotionEvent motionEvent) {
        this.n = motionEvent.getX();
    }

    private int b(int n) {
        return (int) (400.0f / this.getMeasuredWidth() * n);
    }

    private void b(MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final int n = (int) (this.n - x);
        this.n = x;
        this.d(n);
    }

    private int c(int n) {
        if (n > 0 && n + this.getScrollX() > this.getLeftScrollX()) {
            n = this.getLeftScrollX() - this.getScrollX();
        }
        return n;
    }

    private void c(MotionEvent motionEvent) {
        final int leftWidth = this.getLeftWidth();
        if (Math.abs(this.g) >= leftWidth / 3) {
            final int scrollX = this.getScrollX();
            int n;
            if (this.i()) {
                n = -leftWidth - scrollX;
                this.a(3);
            } else {
                n = -scrollX + this.getLeftScrollX();
                this.a(0);
            }
            this.a(n, true);
        } else {
            this.a(-this.g, true);
            this.a(this.a);
        }
        this.j();
    }

    private void d(int n) {
        final int n2 = n + this.getScrollX();
        if (n2 > 0) {
            n -= n2;
        }
        this.scrollBy(n, 0);
        this.g += n;
    }

    private int getColloseCloseDelta() {
        return -this.getScrollX() + this.getLeftScrollX();
    }

    private int getColloseOpenDelta() {
        return this.getScrollX() + this.getLeftWidth() + this.getRightScrollX();
    }

    private int getLeftScrollX() {
        return 0;
    }

    private int getRightScrollX() {
        return 0;
    }

    private void j() {
        this.e = 0;
        this.n = 0.0f;
        this.g = 0;
    }

    public void a(int n, boolean b) {
        final int c = this.c(n);
        if (c == 0) {
            if (this.IAnimation != null) {
                this.IAnimation.e(b);
            }
            return;
        }
        final int b2 = this.b(Math.abs(c));
        this.mHandler.obtainMessage(0, b2, 0, (Object) b).sendToTarget();
        if (this.a == 0) {
            this.mScroller.startScroll(this.getScrollX(), 0, -this.getScrollX(), 0, b2);
        } else {
            this.mScroller.startScroll(this.getScrollX(), 0, c, 0, b2);
        }
        this.invalidate();
    }

    @Override
    protected void a(Context context) {
        super.a(context);
        this.a = 0;
    }

    public boolean d() {
        return this.a == 3;
    }

    public boolean e() {
        return this.a == 4;
    }

    public void f() {
        if (this.e()) {
            return;
        }
        if (this.getScrollX() == this.getLeftScrollX()) {
            this.a(0);
        }
        int colloseCloseDelta;
        if (this.d()) {
            colloseCloseDelta = this.getColloseCloseDelta();
            this.a(0);
        } else {
            colloseCloseDelta = this.getLeftScrollX() - this.getColloseOpenDelta();
            this.a(3);
        }
        this.a(colloseCloseDelta, false);
    }

    protected void g() {
        if (this.k && this.a == 3) {
            this.scrollTo(-this.getMeasuredWidth(), 0);
            this.a = 3;
            this.b();
            this.k = false;
        }
    }

    public int getLeftWidth() {
        return this.getMeasuredWidth() - this.getRightScrollX();
    }

    public boolean h() {
        this.f();
        return true;
    }

    public boolean i() {
        return !this.d();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean onInterceptTouchEvent = true;
        if (!this.c()) {
            onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        } else {
            final int action = motionEvent.getAction();
            if (action != 2 || this.e == 0) {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                switch (action) {
                    case 2: {
                        if ((int) Math.abs(this.n - x) > this.f && Math.abs(this.o - y) / Math.abs(this.n - x) < 1.0f) {
                            this.e = (onInterceptTouchEvent ? 1 : 0);
                            break;
                        }
                        break;
                    }
                    case 0: {
                        this.n = x;
                        this.o = y;
                        this.e = ((!this.mScroller.isFinished() && onInterceptTouchEvent) ? 1 : 0);
                        break;
                    }
                    case 1:
                    case 3: {
                        this.e = 0;
                        break;
                    }
                }
                if (this.e == 0) {
                    return false;
                }
            }
        }
        return onInterceptTouchEvent;
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        this.g();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.l) {
            if (!this.c()) {
                return super.onTouchEvent(motionEvent);
            }
            switch (0xFF & motionEvent.getAction()) {
                default: {
                    return true;
                }
                case MotionEvent.ACTION_DOWN: {
                    this.a(motionEvent);
                    if (!this.i()) {
                        return super.onTouchEvent(motionEvent);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    this.b(motionEvent);
                    return true;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    this.c(motionEvent);
                    return true;
                }
            }
        }
        return true;
    }
}