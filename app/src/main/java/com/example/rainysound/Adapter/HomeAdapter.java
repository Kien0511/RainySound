package com.example.rainysound.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rainysound.MyInterface.IHomeClickListener;
import com.example.rainysound.MyUtil;
import com.example.rainysound.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<String> list;
    private IHomeClickListener iHomeClickListener;

    private int[] listImvIcon = new int[]{R.drawable.circle_perfect_storm,R.drawable.circle_rain_on_window,R.drawable.circle_leaves
            ,R.drawable.circle_begging_of_the_rain,R.drawable.circle_lake,R.drawable.circle_rain_on_roof,R.drawable.circle_sidewalk
            ,R.drawable.circle_beach,R.drawable.circle_peaceful_water,R.drawable.circle_rain_on_tent,R.drawable.circle_ocean_storm
            ,R.drawable.circle_fireplace,R.drawable.circle_thunderstorm};

    public HomeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_list_home,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final int pos = holder.getAdapterPosition();
        holder.txtTitle.setText(list.get(position));
        MyUtil.loadImageFromUrl(context,holder.imvIcon,listImvIcon[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iHomeClickListener = (IHomeClickListener) context;
                iHomeClickListener.HomeItemOnClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle;
        private CircleImageView imvIcon;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imvIcon = itemView.findViewById(R.id.imvIcon);
        }
    }
}
