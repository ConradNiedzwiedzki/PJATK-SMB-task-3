package pl.pjatk.kn_miniprojekt1.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import pl.pjatk.kn_miniprojekt1.models.Shop;
import pl.pjatk.kn_miniprojekt1.data.GeofenceReceiver;
import pl.pjatk.kn_miniprojekt1.data.GeofenceTransitionsIntentService;
import pl.pjatk.kn_miniprojekt1.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback
{
    private GoogleMap googleMap;
    private List<Shop> shopArrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth;
    List<Geofence> geofenceArrayList = new ArrayList<>();
    private GeofencingClient geofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (ActivityCompat.checkSelfPermission(
                MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isProviderEnabled)
        {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("shop");
        firebaseAuth = FirebaseAuth.getInstance();
        geofencingClient = LocationServices.getGeofencingClient(this);

        Intent i = new Intent(this, GeofenceTransitionsIntentService.class);
        startService(i);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null)
        {
            firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                }
            });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setOnMyLocationButtonClickListener(this);
            this.googleMap.setOnMyLocationClickListener(this);
        }
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                Intent intent = new Intent(MapActivity.this, ShopCRUDActivity.class);
                intent.putExtra("latlng", latLng);
                intent.putExtra("shop_id", "0");
                startActivity(intent);
            }
        });

        databaseReference.addListenerForSingleValueEvent(valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                shopArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Shop shop = ds.getValue(Shop.class);
                    shopArrayList.add(shop);
                }
                databaseReference.removeEventListener(valueEventListener);
                prepareMarkers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void prepareMarkers()
    {
        if (shopArrayList.size() > 0)
        {
            for (int i = 0; i < shopArrayList.size(); i++)
            {
                Shop shop = shopArrayList.get(i);
                LatLng marker = new LatLng(Double.parseDouble(shop.getLatitude()), Double.parseDouble(shop.getLongitude()));
                googleMap.addMarker(new MarkerOptions().position(marker).title(shop.getName()));
                buildGeofence(shop);
            }
            geofencingClient.addGeofences(buildGeofencingRequest(), geofencePendingIntent()).addOnSuccessListener(this, new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    Toast.makeText(MapActivity.this, "Dodałem geofence ", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(this, new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private GeofencingRequest buildGeofencingRequest()
    {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(this.geofenceArrayList)
                .build();
    }

    private void buildGeofence(Shop shop)
    {
        this.geofenceArrayList.add(new Geofence.Builder()
                .setRequestId(shop.getId())
                .setCircularRegion(Double.valueOf(shop.getLatitude()), Double.valueOf(shop.getLongitude()), Float.valueOf(shop.getRadius()))
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        googleMap.addCircle(new CircleOptions().center(new LatLng(Double.valueOf(shop.getLatitude()),Double.valueOf(shop.getLongitude())))
        .strokeColor(Color.argb(50,70,70,70))
        .fillColor(Color.argb(100,150,150,150))
        .radius(Double.valueOf(shop.getRadius())));
    }

    private PendingIntent geofencePendingIntent()
    {
        Intent intent = new Intent(this, GeofenceReceiver.class);
        return PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location)
    {
        Toast.makeText(this, "Obecna lokalizacja: " + location, Toast.LENGTH_LONG).show();
    }

}
