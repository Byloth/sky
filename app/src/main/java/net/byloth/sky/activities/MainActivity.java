package net.byloth.sky.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.byloth.sky.LiveWallpaper;
import net.byloth.sky.R;
import net.byloth.sky.fragments.SettingsFragment;
import net.byloth.sky.fragments.SummaryFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    static final private int PERMISSION_REQUEST_LOCATION = 1;
    static final private String TAG = "MainActivity";

    private Fragment currentFragment;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private Animation clockwiseRotation;

    private FloatingActionButton updateFab;
    private FloatingActionButton localizationFab;

    private LiveWallpaper getLiveWallpaper()
    {
        return (LiveWallpaper) getApplication();
    }

    private void replaceFragment(Fragment fragment)
    {
        currentFragment = fragment;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForPermissions()
    {
        Context context = getApplicationContext();

        int accessFineLocationPermission = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessFineLocationPermission == PackageManager.PERMISSION_DENIED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) == true)
            {
                localizationFab.show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, MainActivity.PERMISSION_REQUEST_LOCATION);
            }
        }
        else
        {
            updateFab.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        clockwiseRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);

        updateFab = (FloatingActionButton) findViewById(R.id.update_fab);
        updateFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.startAnimation(clockwiseRotation);

                sendBroadcast(new Intent("net.byloth.sky.activities.FORCE_UPDATE"));
            }
        });

        localizationFab = (FloatingActionButton) findViewById(R.id.localization_fab);
        localizationFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.PERMISSION_REQUEST_LOCATION);
            }
        });

        if (savedInstanceState != null)
        {
            Fragment restoredFragment = getFragmentManager().getFragment(savedInstanceState, "current_fragment");

            replaceFragment(restoredFragment);
        }
        else
        {
            navigationView.setCheckedItem(R.id.nav_settings);

            replaceFragment(new SettingsFragment());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            askForPermissions();
        }
        else
        {
            updateFab.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START) == true)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    /* TODO: Verificare se è veramente necessario implementare il seguente controllo. */
    /*
        else if ((currentFragment instanceof SettingsFragment) == false)
        {
            replaceFragment(new SettingsFragment());
        }
    */
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_summary:
                replaceFragment(new SummaryFragment());
                break;

            case R.id.nav_gallery:
                break;

            case R.id.nav_slideshow:
                break;

            case R.id.nav_settings:
                replaceFragment(new SettingsFragment());
                break;

            case R.id.nav_share:
                break;

            case R.id.nav_send:
                break;

            default:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MainActivity.PERMISSION_REQUEST_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    getLiveWallpaper().getLocationUpdater().initializeLocationListening(this);  // TODO: Controllare questo!

                    updateFab.show();
                    localizationFab.hide();
                }
                else
                {
                    updateFab.hide();
                    localizationFab.show();
                }

                break;

            default:
                Log.w(TAG, "Oh, oh! Something went fu***n' wrong!");

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        try
        {
            getFragmentManager().putFragment(outState, "current_fragment", currentFragment);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
