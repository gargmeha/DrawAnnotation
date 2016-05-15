package mehagarg.android.drawannotation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by meha on 5/15/16.
 */
public class PenHolder {
    private static final float TOUCH_TOLERANCE = 4;

    private Paint paint;
    private Paint bitmapPaint;
    private Path path;
    private float oldX, oldY;
    private Bitmap bitmap;
    private Canvas canvas;

    public PenHolder() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[] { 15, 15 }, 0));
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void init(int w, int h) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public void touchDown(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        oldX = x;
        oldY = y;
    }

    public void touchMove(float x, float y) {
        float dx = Math.abs(x - oldX);
        float dy = Math.abs(y - oldY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(oldX, oldY, (x + oldX) / 2, (y + oldY) / 2);
            oldX = x;
            oldY = y;
        }
    }

    public RectF touchUp() {
        path.lineTo(oldX, oldY);
        canvas.drawPath(path, paint);
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        path.reset();
        return bounds;
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
