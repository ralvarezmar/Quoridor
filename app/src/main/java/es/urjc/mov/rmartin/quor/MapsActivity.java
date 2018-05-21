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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import es.urjc.mov.rmartin.quor.Game.Message;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final String IP="192.168.1.4";
    //private final String IP="10.0.2.2";
    private final int PORT=2020;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    public double latitude;
    public double longitude;
    public String user;
    public ArrayList<LatLng> positions = new ArrayList<>();
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

    private void sendToServer(final double longitude, final double latitude){
        Thread c = new Thread() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(IP, PORT);
                    DataInputStream in = new DataInputStream(s.getInputStream());
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    Message position = new Message.PositionMessage(user,longitude,latitude);
                    position.writeTo(out);
                    Message answer = Message.ReadFrom(in);
                    Message.PositionMessage otherClient = (Message.PositionMessage) answer;
                    LatLng client=new LatLng(otherClient.getLatitude()+0.00001, otherClient.getLongitude()+0.01);
                    positions.add(client);
                    s.close();
                    in.close();
                    out.close();
                } catch (ConnectException e) {
                    System.out.println("connection refused" + e);
                } catch (UnknownHostException e) {
                    System.out.println("cannot connect to host " + e);
                } catch (IOException e) {
                    System.out.println("IO exception" + e);
                }
            }
        };
        c.start();
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
                    //LatLng position2 = new LatLng(location.getLatitude()+0.0001, location.getLongitude());
                    longitude=location.getLongitude();
                    latitude=location.getLatitude();
                    /*sendToServer(longitude,latitude);
                    if(positions.size()>0){
                        LatLng pos = positions.get(0);
                        System.out.print("POSICION: " + pos);
                        MarkerOptions mark = new MarkerOptions().position(pos);
                        mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        mMap.addMarker(mark);
                    }*/
                    Toast.makeText(MapsActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
                    MarkerOptions marker = new MarkerOptions().position(position).title(user);
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                    mMap.setMinZoomPreference(12f);
                    //mMap.setMaxZoomPreference(16f);
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.strokeColor(Color.BLACK);
                    circleOptions.center(position);
                    circleOptions.radius(4000);
                    mMap.addCircle(circleOptions);
                }
            }
        });
    }
}
