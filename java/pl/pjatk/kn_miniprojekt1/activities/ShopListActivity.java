package pl.pjatk.kn_miniprojekt1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import pl.pjatk.kn_miniprojekt1.data.ShopListAdapter;
import pl.pjatk.kn_miniprojekt1.models.Shop;
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

public class ShopListActivity extends AppCompatActivity
{

    private List<Shop> shopList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference permissionReference;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("shop");
        permissionReference = FirebaseDatabase.getInstance().getReference("permission");
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
                        Toast.makeText(ShopListActivity.this, "Autoryzacja " + user.getUid(), Toast.LENGTH_SHORT).show();
                        getShopList();
                    }
                    else
                    {
                        Toast.makeText(ShopListActivity.this, "Blad autoryzacji", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(ShopListActivity.this, "Sukces " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            getShopList();
        }
    }
    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.shop_list_view);
        RecyclerView.Adapter recyclerViewAdapter = new ShopListAdapter(shopList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getShopList()
    {
        databaseReference.addValueEventListener(valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                shopList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Shop shop = ds.getValue(Shop.class);
                    shopList.add(shop);
                }
                Toast.makeText(ShopListActivity.this, "Uprawnienia ", Toast.LENGTH_SHORT).show();
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    public void backToMain(View view)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void goToMapActivity(View view)
    {
        Intent i = new Intent(this,MapActivity.class);
        startActivity(i);
    }
}
