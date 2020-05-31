package pl.pjatk.kn_miniprojekt1.data;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.pjatk.kn_miniprojekt1.activities.ShopCRUDActivity;
import pl.pjatk.kn_miniprojekt1.R;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder>
{
    private List<Shop> shopList;

    public ShopListAdapter(List<Shop> shopList)
    {
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list_item, viewGroup, false);
        return new ShopListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.shopName.setText(shopList.get(i).getName());
        viewHolder.description.setText(shopList.get(i).getDescription());
        viewHolder.latitude.setText(shopList.get(i).getLatitude());
        viewHolder.longitude.setText(shopList.get(i).getLongitude());
        viewHolder.radius.setText(shopList.get(i).getRadius());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(v.getContext(), ShopCRUDActivity.class);
                i.putExtra("shop_id", shopList.get(viewHolder.getAdapterPosition()).getId());
                i.putExtra("shop_name", shopList.get(viewHolder.getAdapterPosition()).getName());
                i.putExtra("shop_desc", shopList.get(viewHolder.getAdapterPosition()).getDescription());
                i.putExtra("shop_latitude", shopList.get(viewHolder.getAdapterPosition()).getLatitude());
                i.putExtra("shop_longitude", shopList.get(viewHolder.getAdapterPosition()).getLongitude());
                i.putExtra("shop_radius", shopList.get(viewHolder.getAdapterPosition()).getRadius());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return shopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView shopName;
        TextView description;
        TextView radius;
        TextView latitude;
        TextView longitude;

        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View v)
        {
            super(v);
            shopName = v.findViewById(R.id.shop_name);
            description = v.findViewById(R.id.desc);
            radius = v.findViewById(R.id.radius);
            latitude = v.findViewById(R.id.latitude);
            longitude = v.findViewById(R.id.longitude);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }
}
