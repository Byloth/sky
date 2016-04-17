package net.byloth.sky.fragments;

import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.byloth.engine.utils.DayTime;
import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.R;
import net.byloth.sky.updaters.SunTimesUpdater;

public class SummaryFragment extends Fragment
{
    private TextView latitudeView;
    private TextView longitudeView;

    private TextView sunriseOfficialView;
    private TextView sunriseCivilView;
    private TextView sunriseNauticalView;
    private TextView sunriseAstronomicalView;

    private TextView sunsetOfficialView;
    private TextView sunsetCivilView;
    private TextView sunsetNauticalView;
    private TextView sunsetAstronomicalView;

    private LiveWallpaper getLiveWallaper()
    {
        return (LiveWallpaper) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_summary_layout, container, false);

        latitudeView = (TextView) view.findViewById(R.id.lat_value);
        longitudeView = (TextView) view.findViewById(R.id.lng_value);

        sunriseOfficialView = (TextView) view.findViewById(R.id.official_rising_time_value);
        sunriseCivilView = (TextView) view.findViewById(R.id.civil_rising_time_value);
        sunriseNauticalView = (TextView) view.findViewById(R.id.nautical_rising_time_value);
        sunriseAstronomicalView = (TextView) view.findViewById(R.id.astronomical_rising_time_value);

        sunsetOfficialView = (TextView) view.findViewById(R.id.official_setting_time_value);
        sunsetCivilView = (TextView) view.findViewById(R.id.civil_setting_time_value);
        sunsetNauticalView = (TextView) view.findViewById(R.id.nautical_setting_time_value);
        sunsetAstronomicalView = (TextView) view.findViewById(R.id.astronomical_setting_time_value);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Location currentLocation = getLiveWallaper().getLocationUpdater().getCurrentLocation();

        if (currentLocation != null)
        {
            latitudeView.setText(String.format("%f°", currentLocation.getLatitude()));
            longitudeView.setText(String.format("%f°", currentLocation.getLongitude()));
        }
        else
        {
            latitudeView.setText("N.A.");
            longitudeView.setText("N.A.");
        }

        SunTimesUpdater sunTimesUpdater = getLiveWallaper().getSunTimesUpdater();

        sunriseOfficialView.setText(DayTime.toString(sunTimesUpdater.getOfficialDawnTime()));
        sunriseCivilView.setText(DayTime.toString(sunTimesUpdater.getCivilDawnTime()));
        sunriseNauticalView.setText(DayTime.toString(sunTimesUpdater.getNauticalDawnTime()));
        sunriseAstronomicalView.setText(DayTime.toString(sunTimesUpdater.getAstronomicalDawnTime()));

        sunsetOfficialView.setText(DayTime.toString(sunTimesUpdater.getOfficialSunsetTime()));
        sunsetCivilView.setText(DayTime.toString(sunTimesUpdater.getCivilSunsetTime()));
        sunsetNauticalView.setText(DayTime.toString(sunTimesUpdater.getNauticalSunsetTime()));
        sunsetAstronomicalView.setText(DayTime.toString(sunTimesUpdater.getAstronomicalSunsetTime()));
    }
}
