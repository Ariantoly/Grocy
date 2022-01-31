package com.hms.grocy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.location.Location;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hms.grocy.fragment.CartFragment;
import com.hms.grocy.fragment.HomeFragment;
import com.hms.grocy.fragment.ProfileFragment;
import com.hms.grocy.fragment.StoreFragment;
import com.hms.grocy.model.Consumer;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.GeocoderService;
import com.huawei.hms.location.GetFromLocationRequest;
import com.huawei.hms.location.HWLocation;
import com.huawei.hms.location.LocationAvailability;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStates;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.api.entity.location.geocoder.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    final Fragment homeFragment = new HomeFragment();
    final Fragment storeFragment = new StoreFragment();
    final Fragment cartFragment = new CartFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = homeFragment;

    private AuthAccount authAccount;
    private static Consumer consumer;

    private String currentLocation = "";

    private SettingsClient settingsClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authAccount = getIntent().getParcelableExtra("authAccount");
        consumer = (Consumer) getIntent().getSerializableExtra("account");

        getToken();

        // Dynamically apply for required permissions if the API level is 28 or lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            // Dynamically apply for the android.permission.ACCESS_BACKGROUND_LOCATION permission in addition to the preceding permissions if the API level is higher than 28.
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "4")
                .hide(profileFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, cartFragment, "3")
                .hide(cartFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, storeFragment, "2")
                .hide(storeFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, homeFragment, "1")
                .commit();

        settingsClient = LocationServices.getSettingsClient(this);
        checkLocationSettings();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        // Instantiate the fusedLocationProviderClient object.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(mLocationCallback == null) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null) {
                        // TODO: Process the location callback result.
                        List<Location> locations = locationResult.getLocations();
                        if (!locations.isEmpty()) {
                            for (Location location : locations) {
                                Log.v("Grocy",
                                        "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.getLongitude()
                                                + "," + location.getLatitude() + "," + location.getAccuracy());
                                getLastLocation();
                            }
                        }
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    if(locationAvailability != null) {
                        Log.v("Grocy", "isLocationAvailable: " + locationAvailability.isLocationAvailable());
                    }
                }
            };
        }

        requestLocationUpdates();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_navigation_home:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(homeFragment)
                        .commit();
                activeFragment = homeFragment;
                return true;
            case R.id.menu_navigation_store:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(storeFragment)
                        .commit();
                activeFragment = storeFragment;
                return true;
            case R.id.menu_navigation_cart:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(cartFragment)
                        .commit();
                activeFragment = cartFragment;
                return true;
            case R.id.menu_navigation_profile:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(profileFragment)
                        .commit();
                activeFragment = profileFragment;
                return true;
        }

        return false;

    }

    public AuthAccount getAuthAccount() {
        return authAccount;
    }

    public static Consumer getConsumer() {
        return consumer;
    }

    public void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
                // Define the listener for success in calling the API for checking device location settings.
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        LocationSettingsStates locationSettingsStates =
                                locationSettingsResponse.getLocationSettingsStates();
                        StringBuilder stringBuilder = new StringBuilder();
                        // Checks whether the location function is enabled.
                        stringBuilder.append(",\nisLocationUsable=")
                                .append(locationSettingsStates.isLocationUsable());
                        // Checks whether HMS Core (APK) is available.
                        stringBuilder.append(",\nisHMSLocationUsable=")
                                .append(locationSettingsStates.isHMSLocationUsable());
                    }
                })
                // Define callback for failure in checking the device location settings.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Processing when the device is a Huawei device and has HMS Core (APK) installed, but its settings do not meet the location requirements.
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
//                                     Call startResolutionForResult to display a popup asking the user to enable relevant permissions.
                                    rae.startResolutionForResult(MainActivity.this, 0);
                                } catch (IntentSender.SendIntentException sie) {
                                    // TODO
                                }
                                break;
                        }
                    }
                });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void requestLocationUpdates() {
        fusedLocationProviderClient
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: Processing when the API call is successful.
                        Log.v("Grocy", "requestLocationUpdates Success: ");
                        getLastLocation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO: Processing when the API call fails.
                        Log.v("Grocy", "requestLocationUpdates Failed: " + e.getMessage());
                    }
                });
    }

    public void getLastLocation() {
        // Obtain the last known location.
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location == null) {
                    Log.v("Grocy", "getLastLocation Success, Location is NULL");
                    return;
                }

                setCurrentLocation(location);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.v("Grocy", "getLastLocation Failed");
            }
        });

    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        GetFromLocationRequest getFromLocationRequest = new GetFromLocationRequest(location.getLatitude(), location.getLongitude(), 1);
        Locale locale = new Locale("en", "ID");
        GeocoderService geocoderService = LocationServices.getGeocoderService(MainActivity.this, locale);
        geocoderService.getFromLocation(getFromLocationRequest)
                .addOnSuccessListener(new OnSuccessListener<List<HWLocation>>() {
                    @Override
                    public void onSuccess(List<HWLocation> hwLocation) {
                        for(HWLocation l : hwLocation) {
                            currentLocation = l.getCity() + ", " + l.getCountryName();
                            Log.v("Grocy", currentLocation);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.v("Grocy", "setCurrentLocation Failed");
                    }
                });

    }

    private void getToken() {
        // Create a thread.
        new Thread() {
            @Override
            public void run() {
                try {
                    // Obtain the app ID from the agconnect-services.json file.
                    String appId = "104981177";

                    // Set tokenScope to HCM.
                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, tokenScope);
                    Log.i("Grocy", "get token: " + token);

                    // Check whether the token is null.
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e("Grocy", "get token failed, " + e);
                }
            }
        }.start();
    }

    private void sendRegTokenToServer(String token) {
        Log.i("Grocy", "sending token to server. token:" + token);
    }
}