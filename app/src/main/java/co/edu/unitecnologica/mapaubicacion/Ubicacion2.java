package co.edu.unitecnologica.mapaubicacion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Tetrimino on 26/9/2016.
 */

public class Ubicacion2 extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double lat=0.0, lon=0.0;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Button next = (Button) findViewById(R.id.Cambialo);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Ubicacion1.class);
                startActivityForResult(myIntent, 0);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        GetRequest();
                    }
                });
            }
        }, 0, 5000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        texto=(TextView)findViewById(R.id.LatLong);

        // Add a marker in Sydney and move the camera
        texto.setText("\nLatitud: "+String.valueOf(lat)+"\nLongitud: "+String.valueOf(lon));
        LatLng ubic = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(ubic).title("Ubicación desde la API"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubic,15));
    }

    public void GetRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://labsoftware03.unitecnologica.edu.co/archivoNicolas";

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            lat=response.getDouble("latitud");
                            lon=response.getDouble("longitud");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(mMap!=null)
                            onMapReady(mMap);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Ubicacion2.this, "Error de conexion", LENGTH_SHORT).show();
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);

    }

}
