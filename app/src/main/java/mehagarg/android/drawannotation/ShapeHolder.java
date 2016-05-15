package mehagarg.android.drawannotation;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by meha on 5/15/16.
 */
public class ShapeHolder {
    private static final int TOUCHABLE_AREA = 400;

    enum ActiveCorner {
        LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM
    }

    private Drawable drawable;
    private int width, height;
    private int left, top, right, bottom;
    private ActiveCorner activeCorner;
    private Paint paint;
    private int dx, dy;
    private int canvasWidth;
    private int canvasHeight;

    public ShapeHolder(Drawable drawable) {
        this.drawable = drawable;
        setBounds(drawable.getBounds().left, drawable.getBounds().top, drawable.getBounds().right,
                drawable.getBounds().bottom);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[] { 10, 10 }, 0));
    }

    public void setShape(Drawable drawable) {
        this.drawable = drawable;
        drawable.setBounds(left, top, right, bottom);
    }

    public void setCanvasSize(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public boolean contains(int x, int y) {
        boolean contains = drawable.getBounds().contains(x, y);
        if (contains) {
            dx = x - left;
            dy = y - top;
            return true;
        }
        return false;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        drawable.setBounds(left, top, right, bottom);
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.width = drawable.getBounds().width();
        this.height = drawable.getBounds().height();
    }

    public void setBounds(RectF rect) {
        setBounds((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);
    }

    @Override
    public String toString() {
        return "l:" + left + " t:" + top + " r:" + right + " b:" + bottom + " w:" + width + " h:" + height;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(left + 7, top + 7, right - 7, bottom - 7, paint);
        drawable.draw(canvas);
    }

    public void move(int x, int y) {
        int l = x - dx;
        int t = y - dy;
        if (l < 0) {
            l = 0;
            dx = x;
        }
        if (l > canvasWidth - width) {
            l = canvasWidth - width;
            dx = x - l;
        }
        if (t < 0) {
            t = 0;
            dy = y;
        }
        if (t > canvasHeight - height) {
            t = canvasHeight - height;
            dy = y - t;
        }
        setBounds(l, t, l + width, t + height);
    }

    public boolean isHitActivePoint(int x, int y) {
        double d = Math.pow(x - left - 7, 2) + Math.pow(y - top - 7, 2);
        if (d <= TOUCHABLE_AREA) {
            Log.d("", "LEFT TOP CORNER");
            activeCorner = ActiveCorner.LEFT_TOP;
            return true;
        }

        d = Math.pow(x - right + 7, 2) + Math.pow(y - top - 7, 2);
        if (d <= TOUCHABLE_AREA) {
            Log.d("", "RIGHT TOP CORNER");
            activeCorner = ActiveCorner.RIGHT_TOP;
            return true;
        }

        d = Math.pow(x - right + 7, 2) + Math.pow(y - bottom + 7, 2);
        if (d <= TOUCHABLE_AREA) {
            Log.d("", "RIGHT BOTTOM CORNER");
            activeCorner = ActiveCorner.RIGHT_BOTTOM;
            return true;
        }

        d = Math.pow(x - left - 7, 2) + Math.pow(y - bottom + 7, 2);
        if (d <= TOUCHABLE_AREA) {
            Log.d("", "LEFT BOTTOM CORNER");
            activeCorner = ActiveCorner.LEFT_BOTTOM;
            return true;
        }
        activeCorner = null;
        return false;
    }

    public void resize(int x, int y) {
        switch (activeCorner) {
            case LEFT_TOP:
                if (x >= right) {
                    activeCorner = ActiveCorner.RIGHT_TOP;
                    setBounds(left, y, x, bottom);
                    break;
                }
                if (y >= bottom) {
                    activeCorner = ActiveCorner.LEFT_BOTTOM;
                    setBounds(x, top, right, y);
                    break;
                }
                setBounds(x, y, right, bottom);
                break;
            case RIGHT_TOP:
                if (x <= left) {
                    activeCorner = ActiveCorner.LEFT_TOP;
                    setBounds(x, y, right, bottom);
                    break;
                }
                if (y >= bottom) {
                    activeCorner = ActiveCorner.RIGHT_BOTTOM;
                    setBounds(left, top, x, y);
                    break;
                }
                setBounds(left, y, x, bottom);
                break;
            case RIGHT_BOTTOM:
                if (x <= left) {
                    activeCorner = ActiveCorner.LEFT_BOTTOM;
                    setBounds(x, top, right, y);
                    break;
                }
                if (y <= top) {
                    activeCorner = ActiveCorner.RIGHT_TOP;
                    setBounds(left, y, x, bottom);
                    break;
                }
                setBounds(left, top, x, y);
                break;
            case LEFT_BOTTOM:
                if (x >= right) {
                    activeCorner = ActiveCorner.RIGHT_BOTTOM;
                    setBounds(left, top, x, y);
                    break;
                }
                if (y <= top) {
                    activeCorner = ActiveCorner.LEFT_TOP;
                    setBounds(x, y, right, bottom);
                    break;
                }
                setBounds(x, top, right, y);
                break;
        }
    }

    public int centerX() {
        return drawable.getBounds().centerX();
    }

    public int centerY() {
        return drawable.getBounds().centerY();
    }

    public Rect getBounds() {
        return drawable.getBounds();
    }

}
