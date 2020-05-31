package pl.pjatk.kn_miniprojekt1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import pl.pjatk.kn_miniprojekt1.data.ProductListAdapter;
import pl.pjatk.kn_miniprojekt1.models.Product;
import pl.pjatk.kn_miniprojekt1.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class ProductListActivity extends AppCompatActivity
{
    private List<Product> productList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null)
        {
            firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(ProductListActivity.this, "Autoryzacja " + user.getUid(), Toast.LENGTH_SHORT).show();
                        getProductList();
                    }
                    else
                    {
                        Toast.makeText(ProductListActivity.this, "Blad autoryzacji", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(ProductListActivity.this, "Sukces " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            getProductList();
        }
    }

    public void getProductList()
    {
        databaseReference.addValueEventListener(valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                productList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Product product = ds.getValue(Product.class);
                    productList.add(product);
                }
                Toast.makeText(ProductListActivity.this, "Uprawnienia ", Toast.LENGTH_SHORT).show();
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }
}

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.product_list_view);
        RecyclerView.Adapter recyclerViewAdapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void backToMain(View v)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
