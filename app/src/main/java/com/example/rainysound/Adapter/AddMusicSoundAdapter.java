package com.example.rainysound.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rainysound.MyInterface.IAddMusicSoundClickListener;
import com.example.rainysound.MyUtil;
import com.example.rainysound.R;

import java.util.List;

public class AddMusicSoundAdapter extends RecyclerView.Adapter<AddMusicSoundAdapter.ViewHolder> {
    private Context context;
    private List<Integer> listImage;
    private IAddMusicSoundClickListener iAddMusicSoundClickListener;
    private List<Integer> listItemTopMenu;

    public AddMusicSoundAdapter(Context context, List<Integer> listImage, List<Integer> listItemTopMenu) {
        this.context = context;
        this.listImage = listImage;
        this.listItemTopMenu = listItemTopMenu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_more_sound,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        int check = 0;
        MyUtil.loadImageFromUrl(context,holder.imvItem,listImage.get(position));
        for (int i = 0; i < listItemTopMenu.size(); i++)
        {
            if(listItemTopMenu.get(i).equals(listImage.get(position)))
            {
                check = 1;
                break;
            }
        }

        if(check == 1)
        {
            holder.imvItem.setVisibility(View.GONE);
        }
        else
        {
            holder.imvItem.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAddMusicSoundClickListener = (IAddMusicSoundClickListener) context;
                iAddMusicSoundClickListener.addMusicSoundOnClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imvItem;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            imvItem = itemView.findViewById(R.id.imvItem);
        }
    }
}
