package net.byloth.sky.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.byloth.sky.R;
import net.byloth.sky.updaters.LocationUpdater;

public class SummaryFragment extends Fragment
{
    protected TextView latitudeView;
    protected TextView longitudeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_summary_layout, container, false);

        latitudeView = (TextView) view.findViewById(R.id.lat_value);
        longitudeView = (TextView) view.findViewById(R.id.lng_value);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (LocationUpdater.hasLocation() == true)
        {
            latitudeView.setText(String.format("%1f°", LocationUpdater.getLatitude()));
            longitudeView.setText(String.format("%1f°", LocationUpdater.getLongitude()));
        }
        else
        {
            latitudeView.setText("N.A.");
            longitudeView.setText("N.A.");
        }
    }
}
