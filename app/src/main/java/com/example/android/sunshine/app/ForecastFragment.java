package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.sunshine.app.data.WeatherContract;

import java.util.ArrayList;

/**
 * Created by galaxy on 09/09/15.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public ForecastFragment() {
    }

    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;


    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    private ForecastAdapter adapter;
    private ListView listView;
    private String dayForeCast;
    private static final int FORECAST_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<String> list = new ArrayList<String>();
/*        list.add("Today-Sunny-88/63");
        list.add("Today-Sunny-88/63");
        list.add("Today-Sunny-88/63");
        list.add("Today-Sunny-88/63");
        list.add("Today-Sunny-88/63");
        list.add("Today-Sunny-88/63");

        */

//        adapter = new ForecastAdapter();
//
//        listView = (ListView)  rootView.findViewById(R.id.listview_forecast);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String forecast = adapter.getItem(position);
//                Intent intent = new Intent(getActivity(), DetailsActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
//
//                //Toast toast = Toast.makeText(getActivity(), adapter.getItem(position), Toast.LENGTH_SHORT);
//
//               //toast.show();
//            }
//        });




        Log.i("Fragment","SETTING_ADAPTER");
        adapter = new ForecastAdapter(getActivity(), null, 0);
        Log.i("Fragment","Adapter Created");
        listView = (ListView)  rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
        Log.i("Fragment", "Adapter_APPLIED TO List");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), DetailsActivity.class)
                            .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    void onLocationChanged()
    {
        updateWeather();
        getLoaderManager().restartLoader(FORECAST_LOADER,null,this);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.action_refresh:
            {
                updateWeather();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);


    }


    public void updateWeather()
    {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }



//    @Override
//    public void onStart() {
//        super.onStart();
//        updateWeather();
//    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String locationSetting = Utility.getPreferredLocation(getActivity());
        Log.i("Fragment","GETTING_LOCATION"+locationSetting);
        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Log.i("Fragment","LOADER_SUCCESS");
        return new CursorLoader(getActivity(),weatherForLocationUri,
                FORECAST_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}