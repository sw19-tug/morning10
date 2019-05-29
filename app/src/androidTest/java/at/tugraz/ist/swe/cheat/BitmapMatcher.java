package at.tugraz.ist.swe.cheat;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class BitmapMatcher extends TypeSafeMatcher<View> {
    public static Matcher<View> withBitmap(final Bitmap bm) {
        return new BitmapMatcher(bm);
    }

    private final Bitmap bm;

    public BitmapMatcher(Bitmap bm) {
        this.bm = bm;
    }

    @Override
    public void describeTo(org.hamcrest.Description description) {
    }

    @Override
    protected boolean matchesSafely(View item) {
        final ImageView imageView = (ImageView) item;
        final BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        final Bitmap bm2 = drawable.getBitmap().copy(bm.getConfig(), bm.isMutable());
        if (bm2.getWidth() != bm.getWidth()) return false;
        if (bm2.getHeight() != bm.getHeight()) return false;
        for (int x = 0; x< bm.getWidth(); x++ ){
           for (int y = 0; y< bm.getHeight(); y++ ){
               int p1 = bm.getPixel(x, y);
               int p2 = bm2.getPixel(x, y);
               if (Math.abs(p1 - p2) > 2) {  // to make up for small conversion errors
                   return false;
               }
           }
        }
        return true;
    }
}
