package co.edu.unitecnologica.mapaubicacion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class Ubicacion1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat=0.0, lon=0.0;
    private TextView texto;
    private Location Ubicacion;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
            if(mMap!=null){
                onMapReady(mMap);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Button next = (Button) findViewById(R.id.Cambialo);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Ubicacion2.class);
                startActivityForResult(myIntent, 0);
            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        Ubicacion=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        locationListener.onLocationChanged(Ubicacion);
                    }
                });
            }
        }, 0, 5000);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        texto=(TextView)findViewById(R.id.LatLong);

        // Add a marker in Sydney and move the camera
        texto.setText("\nLatitud: "+String.valueOf(lat)+"\nLongitud: "+String.valueOf(lon));
        LatLng ubic = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(ubic).title("Mi ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubic,15));
    }
}
