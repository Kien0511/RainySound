package com.example.rainysound.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rainysound.MyInterface.IMenuTopClickListener;
import com.example.rainysound.MyUtil;
import com.example.rainysound.R;

import java.util.List;

public class MenuTopAdapter extends RecyclerView.Adapter<MenuTopAdapter.ViewHolder> {
    private Context context;
    private List<Integer> listItemTopMenu;
    private IMenuTopClickListener iMenuTopClickListener;

    public MenuTopAdapter(Context context, List<Integer> listItemTopMenu) {
        this.context = context;
        this.listItemTopMenu = listItemTopMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_top_menu,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        MyUtil.loadImageFromUrl(context,holder.imvItem,listItemTopMenu.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMenuTopClickListener = (IMenuTopClickListener) context;
                iMenuTopClickListener.menuTopOnClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemTopMenu.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imvItem;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvItem = itemView.findViewById(R.id.imvItem);
        }
    }
}
