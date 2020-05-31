package pl.pjatk.kn_miniprojekt1.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.pjatk.kn_miniprojekt1.activities.ProductCRUDActivity;
import pl.pjatk.kn_miniprojekt1.models.Product;
import pl.pjatk.kn_miniprojekt1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>
{
    private List<Product> productList;
    private Context context;
    private DatabaseReference databaseReference;

    public ProductListAdapter(List<Product> productList, Context context)
    {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i)
    {
        viewHolder.productName.setText(productList.get(i).getName());
        viewHolder.productPrice.setText(String.valueOf(productList.get(i).getPrice()));
        viewHolder.productAmount.setText(String.valueOf(productList.get(i).getAmount()));
        viewHolder.productIsBought.setChecked(productList.get(i).isBought());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(v.getContext(), ProductCRUDActivity.class);
                i.putExtra("product_id", productList.get(viewHolder.getAdapterPosition()).getId());
                i.putExtra("product_name", productList.get(viewHolder.getAdapterPosition()).getName());
                i.putExtra("product_Amount", productList.get(viewHolder.getAdapterPosition()).getAmount());
                i.putExtra("product_price", productList.get(viewHolder.getAdapterPosition()).getPrice());
                i.putExtra("product_bought", productList.get(viewHolder.getAdapterPosition()).isBought());
                v.getContext().startActivity(i);
            }
        });

        viewHolder.productIsBought.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Product toUpdate = productList.get(viewHolder.getAdapterPosition());
                databaseReference = FirebaseDatabase.getInstance().getReference("product").child(toUpdate.getId()).child("bought");

                if(viewHolder.productIsBought.isChecked())
                {
                    databaseReference.setValue(true);
                }
                else
                {
                    databaseReference.setValue(false);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView productName;
        TextView productAmount;
        TextView productPrice;
        CheckBox productIsBought;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View v)
        {
            super(v);
            productName = v.findViewById(R.id.product_name);
            productAmount = v.findViewById(R.id.amount);
            productPrice = v.findViewById(R.id.price);
            productIsBought = v.findViewById(R.id.hasBeenBought);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }

}
