package net.byloth.sky.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.byloth.engine.DayTime;
import net.byloth.sky.R;
import net.byloth.sky.updaters.LocationUpdater;
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

        if (LocationUpdater.hasLocation() == true)
        {
            latitudeView.setText(String.format("%f°", LocationUpdater.getLatitude()));
            longitudeView.setText(String.format("%f°", LocationUpdater.getLongitude()));
        }
        else
        {
            latitudeView.setText("N.A.");
            longitudeView.setText("N.A.");
        }

        sunriseOfficialView.setText(DayTime.toString(SunTimesUpdater.getOfficialSunriseTime()));
        sunriseCivilView.setText(DayTime.toString(SunTimesUpdater.getCivilSunriseTime()));
        sunriseNauticalView.setText(DayTime.toString(SunTimesUpdater.getNauticalSunriseTime()));
        sunriseAstronomicalView.setText(DayTime.toString(SunTimesUpdater.getAstronomicalSunriseTime()));

        sunsetOfficialView.setText(DayTime.toString(SunTimesUpdater.getOfficialSunsetTime()));
        sunsetCivilView.setText(DayTime.toString(SunTimesUpdater.getCivilSunsetTime()));
        sunsetNauticalView.setText(DayTime.toString(SunTimesUpdater.getNauticalSunsetTime()));
        sunsetAstronomicalView.setText(DayTime.toString(SunTimesUpdater.getAstronomicalSunsetTime()));
    }
}
