package com.corp.t3ll0.hikerswatch;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Float altitude;
    Double latitude;
    Double longitude;
    Float speed;
    Float accurecy;
    String address;
    //------------------------
    TextView AltitudeView;
    TextView LatitudeView;
    TextView LongitudeView;
    TextView SpeedView;
    TextView AccuracyView;
    TextView AddressView;
    LocationManager locMan;
    String provider;
    Geocoder geoCoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locMan=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        AltitudeView=(TextView)findViewById(R.id.textViewAltitude);
        LatitudeView=(TextView)findViewById(R.id.textViewLatitude);
        LongitudeView=(TextView)findViewById(R.id.textViewLongitude);
        SpeedView=(TextView)findViewById(R.id.textViewSpeed);
        AccuracyView=(TextView)findViewById(R.id.textViewAccuracy);
        AddressView=(TextView)findViewById(R.id.textViewAddress);
        AddressView.setMovementMethod(new ScrollingMovementMethod());
        provider=locMan.getBestProvider(new Criteria(),false);
        geoCoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try{Location location= locMan.getLastKnownLocation(provider);

        if(location != null){
            altitude=(float)location.getAltitude();
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            speed=location.getSpeed();
            accurecy=location.getAccuracy();
            getAdress();
            seLabels();
            Toast.makeText(this,"no es null yei",Toast.LENGTH_SHORT);
        }}
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       try{ locMan.requestLocationUpdates(provider, 500,1,this);}
       catch(SecurityException e){
           e.printStackTrace();
       }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locMan.removeUpdates(this);
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onMapClick(View view){
        //startActivity(new Intent("android.intent.action.MAP"));
        if(latitude!=null && longitude!=null){
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("latitude",latitude);
            intent.putExtra("longitude",longitude);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Espera hasta qe se obtenga la informacion",Toast.LENGTH_SHORT);
        }

    }
public void seLabels(){
     AltitudeView.setText("Altitude:"+altitude);
     LatitudeView.setText("Latitude: "+latitude);
     LongitudeView.setText("Longitude: "+longitude);
     SpeedView.setText("Speed: "+speed);
     AccuracyView.setText("Accuracy: "+accurecy);
    AddressView.setText("Address: "+address);
}
    public void getAdress(){
        try{
            List<Address> list= geoCoder.getFromLocation(latitude,longitude,1);
            if(list!=null&&list.size()>0){
                String str=list.get(0).getAddressLine(0);
                String col=list.get(0).getAddressLine(1);
                String code=list.get(0).getPostalCode();
                String state=list.get(0).getAddressLine(2);
                String country=list.get(0).getAddressLine(3);
                address="\n    Street:"+str+"\n    Colony:"+col+"\n    PC & State:"+state+"\n    Country:"+country;
            }else{
                address="not Found";
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        altitude=(float)location.getAltitude();
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        speed=location.getSpeed();
        accurecy=location.getAccuracy();
        getAdress();
        seLabels();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
      try{  Location location= locMan.getLastKnownLocation(provider);
          altitude=(float)location.getAltitude();
          latitude=location.getLatitude();
          longitude=location.getLongitude();
          speed=location.getSpeed();
          accurecy=location.getAccuracy();
          getAdress();
          seLabels();
    }catch(SecurityException e){
          e.printStackTrace();
      }

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
