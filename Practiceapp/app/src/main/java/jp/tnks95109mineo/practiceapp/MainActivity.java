package jp.tnks95109mineo.practiceapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.TextView;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LocationListener{

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},1000);
            return;
        }else{
            locationStart();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,50,this);
        }
    }

    private void locationStart() {
        Log.d("debug", "locationStart()");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled");
        } else {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable,startActivity");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,50,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == 1000){
            //使用が許可された

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //位置測定を始めるコードへ飛ぶ
                locationStart();
            }else{
                Toast toast = Toast.makeText(this,"これ以上何もできません",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    @Override
    public void onStatusChanged(String provider,int status, Bundle extras){
        switch (status){
            case LocationProvider.AVAILABLE:
                Log.d("debug","LocationProvider,AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug","LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug","LocationProvider>TEMPORARILY_UNAVAILABLE");
                break;
        }
    }




    @Override
    public  void onLocationChanged(Location location){
        TextView textView1 = (TextView)findViewById(R.id.text_view1);
        String str1,str2,str3;
        if (location.getLatitude() < 0) {
            str1 = "緯度: 南緯" + location.getLatitude()*-1;
        }else if (location.getLatitude() == 0){
            str1 = "緯度:" + location.getLatitude();
        }else {
            str1 = "緯度: 北緯" + location.getLatitude();
        }
            textView1.setText(str1 + "°");

        TextView textView2 = (TextView)findViewById(R.id.text_view2);
        if (location.getLongitude() < 0){
            str2 = "経度: 西経" + location.getLongitude()*-1;
        }else if (location.getLongitude() == 0){
            str2 = "経度: " + location.getLongitude();
        }else {
            str2 = "経度: 東経" + location.getLongitude();
        }
        textView2.setText(str2 + "°");

        TextView textView3 = (TextView)findViewById(R.id.text_view3);
        str3 = "高度: " + location.getAltitude();
        textView3.setText(str3 + "m");
    }

    @Override
    public void onProviderEnabled(String provider){

    }

    @Override
    public void onProviderDisabled(String provider){

    }
}
