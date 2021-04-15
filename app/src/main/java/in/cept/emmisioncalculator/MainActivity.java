package in.cept.emmisioncalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText edtstart, edtend;
    TextView txtkm, finalans, edtperprice;
    Button btncal,btnemi;
    Double ppp, distancefinal;

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);

    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);

    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;

    public String spnstrng;
    String stype;
    double lat1 = 0, long1 = 0, lat2 = 0, long2 = 0;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        edtstart = findViewById(R.id.edtstart);
        edtend = findViewById(R.id.edtstop);
        txtkm = findViewById(R.id.txtkm);
        edtperprice = findViewById(R.id.edtperprice);
        finalans = findViewById(R.id.finalans);
        btncal = findViewById(R.id.btncal);
        btnemi = findViewById(R.id.btnemi);
        btnemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateemission();
            }
        });
        btncal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calculate();
                mapsdet();
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyCEn7E1Sp9SLquexRacH0LakE4r1mD3-8U");

        edtstart.setFocusable(false);
        edtstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stype = "source";
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                ).build(MainActivity.this);

                startActivityForResult(intent, 100);

            }
        });

        edtend.setFocusable(false);
        edtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stype = "destination";

                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                ).build(MainActivity.this);

                startActivityForResult(intent, 100);

            }
        });

        txtkm.setText("Getting Distance...");

        String[] values =
                {"2-Wheeler", "4-Wheeler", "2-Wheeler Electric", "4-Wheeler Electric", "G-bike", "Public Transport (Bus)"};
        Spinner spinner = (Spinner) findViewById(R.id.simpleSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // On selecting a spinner item
                spnstrng = parent.getItemAtPosition(position).toString();


                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void calculateemission() {

        if (spnstrng.toString().equals("2-Wheeler")){
            ppp = 0.0417;
            double finalint = distancefinal;
            edtperprice.setText("Emission (Kg/km) - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Emission - "+mul);
            Toast.makeText(this, "Total Emission - "+mul, Toast.LENGTH_SHORT).show();
        }
        else if (spnstrng.toString().equals("4-Wheeler")){
            ppp = 0.142;
            double finalint = distancefinal;
            edtperprice.setText("Emission (Kh/km) - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Emission - "+mul);
            Toast.makeText(this, "Total Emission - "+mul, Toast.LENGTH_SHORT).show();

        }
        else if (spnstrng.toString().equals("2-Wheeler Electric")){
            ppp = 0.035;
            double finalint = distancefinal;
            edtperprice.setText("Emission (Kh/km) - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Emission - "+mul);
            Toast.makeText(this, "Total Emission - "+mul, Toast.LENGTH_SHORT).show();
        }
        else if (spnstrng.toString().equals("4-Wheeler Electric")){
            ppp = 0.075;
            double finalint = distancefinal;
            edtperprice.setText("Emission (Kh/km) - "+ppp);
            double mul = finalint*ppp;
            Toast.makeText(this, "Total Emission - "+mul, Toast.LENGTH_SHORT).show();
            finalans.setText("Total Emission - "+mul);
        }
        else if (spnstrng.toString().equals("G-bike")){
            ppp = 0.00;
            double finalint = distancefinal;
            edtperprice.setText("Emission (Kh/km) - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Emission - "+mul);
            Toast.makeText(this, "Total Emission - "+mul, Toast.LENGTH_SHORT).show();
        }
        else if (spnstrng.toString().contains("Bus")){
            ppp = 0.05;
            double finalint = distancefinal;
            edtperprice.setText("Emission (Kh/km) - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Emission - "+mul);
            Toast.makeText(this, "Total Emission - "+mul, Toast.LENGTH_SHORT).show();

        }


    }

    private void mapsdet() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.

        LatLng sydney = new LatLng(lat1, long1);
        LatLng TamWorth = new LatLng(lat2, long2);

        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);

    }

    private void Calculate() {

        if (spnstrng.toString().equals("2-Wheeler")){
            ppp = 3.37;
            double finalint = distancefinal;
            edtperprice.setText("Price Per Km.  - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Cost Of Travel - "+mul);
            Toast.makeText(this, "Total Cost - "+mul, Toast.LENGTH_SHORT).show();
        }
        else if (spnstrng.toString().equals("4-Wheeler")){
            ppp = 7.67;
            double finalint = distancefinal;
            edtperprice.setText("Price Per Km.  - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Cost Of Travel - "+mul);
            Toast.makeText(this, "Total Cost - "+mul, Toast.LENGTH_SHORT).show();


        }
        else if (spnstrng.toString().equals("2-Wheeler Electric")){
            ppp = 1.37;
            double finalint = distancefinal;
            edtperprice.setText("Price Per Km.  - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Cost Of Travel - "+mul);
            Toast.makeText(this, "Total Cost - "+mul, Toast.LENGTH_SHORT).show();

        }
        else if (spnstrng.toString().equals("4-Wheeler Electric")){
            ppp = 3.02;
            double finalint = distancefinal;
            edtperprice.setText("Price Per Km.  - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Cost Of Travel - "+mul);
            Toast.makeText(this, "Total Cost - "+mul, Toast.LENGTH_SHORT).show();

        }
        else if (spnstrng.toString().equals("G-bike")){
            ppp = 0.5;
            double finalint = distancefinal;
            edtperprice.setText("Price Per Km.  - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Cost Of Travel - "+mul);
            Toast.makeText(this, "Total Cost - "+mul, Toast.LENGTH_SHORT).show();

        }
        else if (spnstrng.toString().contains("Bus")){
            ppp = 1.0;
            double finalint = distancefinal;
            edtperprice.setText("Price Per Km.  - "+ppp);
            double mul = finalint*ppp;
            finalans.setText("Total Cost Of Travel - "+mul);
            Toast.makeText(this, "Total Cost - "+mul, Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK){

            Place place = Autocomplete.getPlaceFromIntent(data);

            if (stype.equals("source")){
                flag++;
                edtstart.setText(place.getAddress());
                String ssource = String.valueOf(place.getLatLng());
                ssource = ssource.replaceAll("lat/lng:","");
                ssource = ssource.replace("(","");
                ssource = ssource.replace(")","");
                String[] split = ssource.split(",");
                lat1 =Double.parseDouble(split[0]);
                long1 = Double.parseDouble(split[1]);
                Log.d("edtstart",""+lat1);
                Log.d("edtstart2",""+long1);

            }
            else {

                flag++;

                edtend.setText(place.getAddress());
                String sdes = String.valueOf(place.getLatLng());
                sdes = sdes.replaceAll("lat/lng:","");
                sdes = sdes.replace("(","");
                sdes = sdes.replace(")","");
                String[] split = sdes.split(",");
                lat2 =Double.parseDouble(split[0]);
                long2 = Double.parseDouble(split[1]);

            }

            if (flag >= 2){

                distance(lat1,long1,lat2,long2);
            }
        }else  if (requestCode == AutocompleteActivity.RESULT_ERROR){

            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void distance(double lat1, double long1, double lat2, double long2) {

        double londiff = long1 - long2;

        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(londiff));

        distance = Math.acos(distance);

        distance = red2deg(distance);

        distance = distance * 60 * 1.1515;

        distance = distance * 1.609344;

        distancefinal = distance;

        txtkm.setText(String.format(Locale.US,"%2f Kilometers", distance));


    }

    private double red2deg(double distance) {

        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double lat1) {
        return (lat1*Math.PI/180.0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        if (gmap == null) {
            gmap = googleMap;
            //setUpMap();
        }
        for (int i = 0; i < locationArrayList.size(); i++) {

            // below line is use to add marker to each location of our array list.
            gmap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

            // below lin is use to zoom our camera on map.
            gmap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            // below line is use to move our camera to the specific location.
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get(i), 12), 3000, null);
        }
    }
    }
