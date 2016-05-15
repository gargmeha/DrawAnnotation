package mehagarg.android.drawannotation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by meha on 5/15/16.
 */
public class DrawView extends View {
    enum Shape {
        RECTANGLE, OVAL
    }

    enum Mode {
        NONE, SELECT, MOVE, RESIZE
    }

    private ShapeHolder sh;
    private Mode mode = Mode.SELECT;
    private Shape shape = Shape.OVAL;
    private PenHolder ph;
    private int noteX;
    private int noteY;
    private Paint pathPaint, clipPaint;
    private Path path;
    private PorterDuffXfermode xfermode;
    private RectF clipRect;

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Drawable drawable = null;
        switch (shape) {
            case RECTANGLE:
                drawable = getResources().getDrawable(R.drawable.rectangle);
                break;
            case OVAL:
                drawable = getResources().getDrawable(R.drawable.oval);
        }
        sh = new ShapeHolder(drawable);
        sh.setBounds(400, 200, 700, 500);
        ph = new PenHolder();

        path = new Path();
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStrokeWidth(3);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setPathEffect(new DashPathEffect(new float[] { 10, 10 }, 0));
        clipPaint = new Paint();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        clipRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mode == Mode.SELECT) {
            ph.draw(canvas);
        }
        else {
            int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, 0);
            path.moveTo(sh.centerX(), sh.centerY());
            path.lineTo(noteX, noteY);
            canvas.drawPath(path, pathPaint);

            // clip shape inner
            clipPaint.setXfermode(xfermode);
            clipRect.set(sh.getBounds());
            clipRect.inset(10, 10);
            switch (shape) {
                case RECTANGLE:
                    canvas.drawRect(clipRect, clipPaint);
                    break;
                case OVAL:
                    canvas.drawArc(clipRect, 0, 360, true, clipPaint);
                    break;
            }

            sh.draw(canvas);
            path.reset();
            canvas.restoreToCount(sc);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("", "onTouchEvent");
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.d("", "onTouchEvent: x:" + x + " y:" + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("", "Action: DOWN");
                handleDown(x, y);
                // invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("", "Action: MOVE");
                handleMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("", "Action: UP");
                handleUp(x, y);
                invalidate();
                break;
        }
        return true;
    }

    private void handleDown(int x, int y) {
        if (mode == Mode.SELECT) {
            ph.touchDown(x, y);
            return;
        }
        if (sh.isHitActivePoint(x, y)) {
            mode = Mode.RESIZE;
            return;
        }
        if (sh.contains(x, y)) {
            mode = Mode.MOVE;
        }
    }

    private void handleMove(int x, int y) {
        switch (mode) {
            case MOVE:
                sh.move(x, y);
                break;
            case RESIZE:
                sh.resize(x, y);
                break;
            case SELECT:
                ph.touchMove(x, y);
            default:
                break;
        }
    }

    private void handleUp(int x, int y) {
        if (mode == Mode.SELECT) {
            RectF bounds = ph.touchUp();
            sh.setBounds(bounds);
        }
        mode = Mode.NONE;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ph.init(w, h);
        sh.setCanvasSize(getWidth(), getHeight());
    }

    public void clear() {
        mode = Mode.SELECT;
        invalidate();
    }

    public void switchShape() {
        Drawable drawable = null;
        if (shape == Shape.RECTANGLE) {
            shape = Shape.OVAL;
            drawable = getResources().getDrawable(R.drawable.oval);
        }
        else {
            shape = Shape.RECTANGLE;
            drawable = getResources().getDrawable(R.drawable.rectangle);
        }
        sh.setShape(drawable);
        invalidate();
    }

    public void onNotePositionChanged(int x, int y) {
        noteX = x;
        noteY = y;
        invalidate();
    }
}
