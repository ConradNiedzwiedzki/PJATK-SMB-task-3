package pl.pjatk.kn_miniprojekt1.activities; 

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.pjatk.kn_miniprojekt1..models.Shop;
import pl.pjatk.kn_miniprojekt1.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopCRUDActivity extends AppCompatActivity
{
    private Shop shop = new Shop();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        Intent i = getIntent();
        String shopId = i.getStringExtra("shop_id");
        LatLng latLng = i.getParcelableExtra("latlng");

        Button addShopButton = findViewById(R.id.add_shop);
        Button editShopButton = findViewById(R.id.edit_shop);
        Button deleteShopButton = findViewById(R.id.delete_button);

        if (shopId == "0")
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("shop");
            shop.setLatitude(String.valueOf(latLng.latitude));
            shop.setLongitude(String.valueOf(latLng.longitude));

            addShopButton.setVisibility(View.VISIBLE);
            editShopButton.setVisibility(View.GONE);
            deleteShopButton.setVisibility(View.GONE);
        }
        else
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("shop").child(shopId);
            shop.setName(i.getStringExtra("shop_name"));
            shop.setDescription(i.getStringExtra("shop_desc"));
            shop.setRadius(i.getStringExtra("shop_radius"));
            shop.setLatitude(i.getStringExtra("shop_latitude"));
            shop.setLongitude(i.getStringExtra("shop_longitude"));

            addShopButton.setVisibility(View.GONE);
            editShopButton.setVisibility(View.VISIBLE);
            deleteShopButton.setVisibility(View.VISIBLE);
        }

        EditText shopName = findViewById(R.id.shop_name);
        EditText shopDescription = findViewById(R.id.desc);
        EditText shopLatitude = findViewById(R.id.latitude);
        EditText shopLongtitude = findViewById(R.id.longitude);
        EditText shopRadius = findViewById(R.id.radius);

        shopName.setText(shop.getName());
        shopDescription.setText(shop.getDescription());
        shopLatitude.setText(shop.getLatitude());
        shopLongtitude.setText(shop.getLongitude());
        shopRadius.setText(shop.getRadius());


        shopName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                shop.setName(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });
        shopDescription.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                shop.setDescription(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });


        shopLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                shop.setLatitude(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });


        shopLongtitude.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                shop.setLongitude(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        shopRadius.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                shop.setRadius(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        FirebaseDatabase.getInstance().getReference("permission");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void addData(View v)
    {

        if(firebaseAuth.getCurrentUser() != null)
        {
            String key = databaseReference.push().getKey();
            shop.setId(key);
            databaseReference.child(key).setValue(shop);

            Toast.makeText(this, "Dodano pomyślnie", Toast.LENGTH_LONG).show();

            Intent i = new Intent();
            i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            i.setAction("pl.pjatk.kn_miniprojekt1.myapplication");
            i.putExtra("product_id", shop.getName());
            sendBroadcast(i, "pl.pjatk.mypermission");

            startActivity(new Intent(this, ShopListActivity.class));
        }
        else
        {
            Toast.makeText(this, "Brak uprawnień", Toast.LENGTH_SHORT).show();
        }
    }

    public void editData(View v)
    {
        if(firebaseAuth.getCurrentUser() != null)
        {
            databaseReference.child("name").setValue(shop.getName());
            databaseReference.child("desc").setValue(shop.getDescription());
            databaseReference.child("radius").setValue(shop.getRadius());
            databaseReference.child("longitude").setValue(shop.getLongitude());
            databaseReference.child("latitude").setValue(shop.getLatitude());

            Toast.makeText(ShopCRUDActivity.this, "Edytowano pomyślnie: " + shop.getName(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(ShopCRUDActivity.this, ShopListActivity.class));
        }
        else
        {
            Toast.makeText(ShopCRUDActivity.this, "Nie masz uprawnień", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteData(View v)
    {
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(ShopCRUDActivity.this, "Usunięto pomyślnie", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ShopCRUDActivity.this, "Usuwanie nie powiodło się", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(ShopCRUDActivity.this, ShopListActivity.class));
            }
        });
    }
}
