package mehagarg.android.drawannotation;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Created by meha on 5/15/16.
 */
public class StickyNoteHolder {
    public interface NotePositionListener {
        void onNotePositionChanged(int x, int y);
    }

    private View stickyNote;
    private LayoutParams lp;
    private NotePositionListener listener;

    public StickyNoteHolder(View sn) {
        this.stickyNote = sn;
        lp = (LayoutParams) sn.getLayoutParams();
        this.stickyNote.setOnTouchListener(new View.OnTouchListener() {

            private int dx;
            private int dy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("", "onTouchAnnotation");
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = x - lp.leftMargin;
                        dy = y - lp.topMargin;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        lp.leftMargin = x - dx;
                        lp.topMargin = y - dy;
                        v.setLayoutParams(lp);
                        listener.onNotePositionChanged(lp.leftMargin + stickyNote.getWidth() / 2,
                                lp.topMargin + stickyNote.getHeight() / 2);
                        break;
                }
                StickyNoteHolder.this.stickyNote.invalidate();
                return true;
            }
        });
    }

    public void setOnNotePositionChangedListener(NotePositionListener listener) {
        this.listener = listener;
    }
}
