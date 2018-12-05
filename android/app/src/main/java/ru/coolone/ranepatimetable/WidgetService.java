package ru.coolone.ranepatimetable;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import lombok.extern.java.Log;

import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Date;

import lombok.var;

import static ru.coolone.ranepatimetable.WidgetProvider.globalwidth;

/**
 * This is the service that provides the factory to be bound to the collection service.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

/**
 * This is the factory that will provide data to the collection widget.
 */
@Log
class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Cursor cursor;

    public static final String INTENT_WIDTH = "intent_width";
    public static final String INTENT_HEIGHT = "intent_width";

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        log.severe("Widget columns count: " + cursor.getCount());
        return cursor.getCount();
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    private float dpToPixel(float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private static final int rectMargins = 8;
    private static final int iconSize = 29;
    private static final int circleRadius = 23;

    private Bitmap buildItemBitmap(Context context, float w, float h) {
        var bitmap = Bitmap.createBitmap((int) dpToPixel(w), (int) dpToPixel(h), Bitmap.Config.ARGB_8888);
        var canvas = new Canvas(bitmap);
        var paint = new Paint();
        paint.setAntiAlias(true);

        var circleX = dpToPixel(rectMargins * 2 + 70 + circleRadius);
        var circleY = dpToPixel((80 + rectMargins) / 2);

        paint.setStrokeWidth(dpToPixel(2));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        var first = cursor.getInt(cursor.getColumnIndex(Timeline.COLUMN_FIRST)) != 0;
        var last = cursor.getInt(cursor.getColumnIndex(Timeline.COLUMN_LAST)) != 0;
        if(!(first && last)) {
            if (first || !last) {
                canvas.drawLine(
                        circleX, circleY + dpToPixel(circleRadius / 2),
                        circleX, dpToPixel(h),
                        paint
                );
            }
            if (last || !first) {
                canvas.drawLine(
                        circleX, circleY - dpToPixel(circleRadius / 2),
                        circleX, 0,
                        paint
                );
            }
        }

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(
                circleX,
                circleY,
                dpToPixel(circleRadius),
                paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(
                circleX,
                circleY,
                dpToPixel(circleRadius),
                paint
        );

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(0);
        paint.setTextSize(dpToPixel(iconSize));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(
                Typeface.createFromAsset(
                        context.getAssets(),
                        "fonts/TimetableIcons.ttf"
                )
        );
        canvas.drawText(
                String.valueOf(
                        Character.toChars(
                                cursor.getInt(
                                        cursor.getColumnIndex(
                                                Timeline.PREFIX_LESSON
                                                        + Timeline.LessonModel.COLUMN_LESSON_ICON)
                                )
                        )
                ), circleX, circleY + dpToPixel(10), paint);


        return bitmap;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider
        if (cursor.moveToPosition(position)) {
            var date = new Date(cursor.getInt(cursor.getColumnIndex(Timeline.COLUMN_DATE)));
            var lesson = cursor.getString(cursor.getColumnIndex(Timeline.PREFIX_LESSON + Timeline.LessonModel.COLUMN_LESSON_TITLE));

            var rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            rv.setTextViewText(R.id.widget_item_text, lesson);
            rv.setImageViewBitmap(
                    R.id.widget_item_image,
                    buildItemBitmap(
                            context,
                            globalwidth,
                            80
                    )
            );

            // Set the click intent so that we can handle it and show a toast message
            var fillInIntent = new Intent();
            var extras = new Bundle();
            extras.putString(WidgetProvider.EXTRA_DAY_ID, date.toString());
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
            return rv;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        // We aren't going to return a default loading view in this sample
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // Technically, we have two types of views (the dark and light background views)
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (cursor != null) {
            cursor.close();
        }
        cursor = TimetableDatabase.getInstance(context).timetable().selectAll();
    }
}