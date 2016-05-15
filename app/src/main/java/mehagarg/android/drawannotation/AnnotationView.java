package mehagarg.android.drawannotation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by meha on 5/15/16.
 */
public class AnnotationView extends RelativeLayout implements StickyNoteHolder.NotePositionListener {
    private View stickyNote;
    private DrawView drawView;
    private StickyNoteHolder nh;

    public AnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnnotationView(Context context) {
        super(context);
        init();
    }

    private void init() {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawView = (DrawView) findViewById(R.id.draw_view);
        stickyNote = findViewById(R.id.sticky_note);
        nh = new StickyNoteHolder(stickyNote);
        nh.setOnNotePositionChangedListener(this);

    }

    @Override
    public void onNotePositionChanged(int x, int y) {
        drawView.onNotePositionChanged(x, y);
    }
}
