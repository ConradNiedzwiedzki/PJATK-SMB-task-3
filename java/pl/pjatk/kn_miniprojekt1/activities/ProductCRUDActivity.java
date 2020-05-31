package pl.pjatk.kn_miniprojekt1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.pjatk.kn_miniprojekt1.models.Product;
import pl.pjatk.kn_miniprojekt1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductCRUDActivity extends AppCompatActivity
{
    private Product product = new Product();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent i = getIntent();
        String productId = i.getStringExtra("product_id");

        Button addProductButton = findViewById(R.id.add_product);
        Button editProductButton = findViewById(R.id.edit_product);
        Button deleteProductButton = findViewById(R.id.delete_button);

        if (productId == "0")
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("product");
            addProductButton.setVisibility(View.VISIBLE);
            editProductButton.setVisibility(View.GONE);
            deleteProductButton.setVisibility(View.GONE);
        }
        else
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("product").child(productId);
            product.setName(i.getStringExtra("product_name"));
            product.setPrice(i.getStringExtra("product_price"));
            product.setAmount(i.getStringExtra("product_amount"));
            product.isBoughtSet(i.getBooleanExtra("product_isBought", false));

            addProductButton.setVisibility(View.GONE);
            editProductButton.setVisibility(View.VISIBLE);
            deleteProductButton.setVisibility(View.VISIBLE);
        }

        EditText productName = findViewById(R.id.product_name);
        EditText productPrice = findViewById(R.id.price);
        EditText productAmount = findViewById(R.id.amount);

        productName.setText(product.getName());
        productPrice.setText(product.getPrice());
        productAmount.setText(product.getAmount());

        productName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                product.setName(charSequence.toString());
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

        productPrice.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                product.setPrice(charSequence.toString());
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

        productCount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                product.setCount(charSequence.toString());
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

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void insertData(View v) {
        if(firebaseAuth.getCurrentUser() != null)
        {

            String id = databaseReference.push().getKey();
            product.setId(id);
            databaseReference.child(id).setValue(product);

            Toast.makeText(this, "Dodano produkt", Toast.LENGTH_LONG).show();

            Intent i = new Intent();
            i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            i.setAction("pl.pjatk.kn_miniprojekt1");
            i.putExtra("product_id", product.getName());
            sendBroadcast(i, "pl.pjatk.mypermission");

            startActivity(new Intent(this, ProductListActivity.class));
        }
        else
        {
            Toast.makeText(this, "Brak uprawnień!", Toast.LENGTH_SHORT).show();
        }

    }

    public void editData(View v) {
        if(firebaseAuth.getCurrentUser() != null)
        {
            databaseReference.child("name").setValue(product.getName());
            databaseReference.child("amount").setValue(product.getAmount());
            databaseReference.child("price").setValue(product.getPrice());
            databaseReference.child("bought").setValue(product.isBought());

            Toast.makeText(ProductCRUDActivity.this, "Zedytowano produkt " + product.getName(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(ProductCRUDActivity.this, ProductListActivity.class));
        }
        else
        {
            Toast.makeText(ProductCRUDActivity.this, "Brak uprawnień!", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteData(View v)
    {
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(ProductCRUDActivity.this, "Usunięto produkt", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ProductCRUDActivity.this, "Błąd, nie usunięto", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(ProductCRUDActivity.this, ProductListActivity.class));
            }
        });
    }
}
