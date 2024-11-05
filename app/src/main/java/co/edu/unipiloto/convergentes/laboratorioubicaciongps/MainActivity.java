package co.edu.unipiloto.convergentes.laboratorioubicaciongps;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static androidx.constraintlayout.motion.widget.Debug.getLocation2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button bt_location, bt_route;
    TextView text_view1, text_view2, text_view3, text_view4, text_view5, text_view6, location_view;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bt_location = findViewById(R.id.bt_location);
        bt_route = findViewById(R.id.bt_route);

        text_view1 = findViewById(R.id.text_view1);
        text_view2 = findViewById(R.id.text_view2);
        text_view3 = findViewById(R.id.text_view3);
        text_view4 = findViewById(R.id.text_view4);
        text_view5 = findViewById(R.id.text_view5);
        text_view6 = findViewById(R.id.text_view6);
        location_view = findViewById(R.id.location_view);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_view.setText("Check permission");

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location_view.setText(":)");
                    getLocation();

                } else {
                    location_view.setText("when permission denied");
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        bt_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_view.setText("Check permission");

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Verruta();

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1
                                    );

                                    text_view1.setText("Latitud:  " + (double) addresses.get(0).getLatitude());
                                    text_view2.setText("Longitud:  " + (double) addresses.get(0).getLongitude());
                                    text_view3.setText(addresses.get(0).getCountryName());
                                    text_view4.setText(addresses.get(0).getLocality());
                                    text_view5.setText(addresses.get(0).getAddressLine(0));
                                    text_view6.setText("Localidad: " + addresses.get(0).getSubLocality());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    getLocation2();
                                }
                            }
                        }
                    }
            );
        }
    }

    public void Verruta() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location currentLocation = task.getResult();
                if (currentLocation != null) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + 4.6399488 + "," + -74.088448 + "&mode=d");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    location_view.setText("Ubicación actual no disponible.");
                }
            }
        });
    }
}