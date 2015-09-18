package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {


//    private static final String[] FORECAST_COLUMNS = {
//            // In this case the id needs to be fully qualified with a table name, since
//            // the content provider joins the location & weather tables in the background
//            // (both have an _id column)
//            // On the one hand, that's annoying.  On the other, you can search the weather table
//            // using the location set by the user, which is only in the Location table.
//            // So the convenience is worth it.
//            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
//            WeatherContract.WeatherEntry.COLUMN_DATE,
//            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
//            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
//            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
//            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
//            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
//            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
//            WeatherContract.LocationEntry.COLUMN_COORD_LONG
//    };
//
//    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
//    // must change.
//    static final int COL_WEATHER_ID = 0;
//    static final int COL_WEATHER_DATE = 1;
//    static final int COL_WEATHER_DESC = 2;
//    static final int COL_WEATHER_MAX_TEMP = 3;
//    static final int COL_WEATHER_MIN_TEMP = 4;
//    static final int COL_LOCATION_SETTING = 5;
//    static final int COL_WEATHER_CONDITION_ID = 6;
//    static final int COL_COORD_LAT = 7;
//    static final int COL_COORD_LONG = 8;


    public final int VIEW_TYPE_TODAY = 0;
    public final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        Log.i("Fragment","CONSTRuctor CALLED");
    }



    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
//    String[] convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
//        // return strings to keep UI functional for now
//        String[] resultStrs = new String[cvv.size()];
//        for ( int i = 0; i < cvv.size(); i++ ) {
//            ContentValues weatherValues = cvv.elementAt(i);
//            String highAndLow = formatHighLows(
//                    weatherValues.getAsDouble(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP),
//                    weatherValues.getAsDouble(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
//            resultStrs[i] = getReadableDateString(
//                    weatherValues.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE)) +
//                    " - " + weatherValues.getAsString(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC) +
//                    " - " + highAndLow;
//        }
//        return resultStrs;
//    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */


    /*
        Remember that these views are reused as needed.
     */


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if (viewType == VIEW_TYPE_TODAY)
        {
            layoutId = R.layout.list_item_forecast_today;
        }
        else if (viewType == VIEW_TYPE_FUTURE_DAY)
        {
            layoutId = R.layout.activity_list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

//        TextView tv = (TextView)view;
//        tv.setText(convertCursorRowToUXFormat(cursor));

        ViewHolder viewHolder = (ViewHolder) view.getTag();


        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
            // Get weather icon
            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
            cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
            break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
             // Get weather icon
             viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
             cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
             break;
             }
            }


        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
               // Find TextView and set formatted date on it

        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        String weather = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        // TODO Read weather forecast from cursor

        viewHolder.descView.setText(weather);



        boolean isMetric = Utility.isMetric(context);

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);

        viewHolder.highView.setText(Utility.formatTemperature(context,high, isMetric));


        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

        viewHolder.lowView.setText(Utility.formatTemperature(context,low, isMetric));
        // TODO Read low temperature from cursor
    }


    public static class ViewHolder
    {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descView;
        public final TextView highView;
        public final TextView lowView;

        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descView = (TextView) view.findViewById(R.id.list_item_forecast_desc);
            highView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);

        }
    }
}
