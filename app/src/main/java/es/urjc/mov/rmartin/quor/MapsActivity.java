package es.urjc.mov.rmartin.quor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    public double latitude;
    public double longitude;
    public String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Bundle configuration = getIntent().getExtras();
        if(configuration!=null){
            user= configuration.getString("user");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    LatLng position2 = new LatLng(location.getLatitude()+0.0001, location.getLongitude());
                    longitude=location.getLongitude();
                    latitude=location.getLatitude();
                    Toast.makeText(MapsActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
                    MarkerOptions marker = new MarkerOptions().position(position).title(user);
                    MarkerOptions marker2 = new MarkerOptions().position(position2);
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(marker);
                    mMap.addMarker(marker2);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.setMinZoomPreference(12f);
                    mMap.setMaxZoomPreference(16f);
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.strokeColor(Color.WHITE);
                    circleOptions.center(position);
                    circleOptions.radius(4000);
                    mMap.addCircle(circleOptions);
                }
            }
        });
    }
}
