package com.nhodev.service_power.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhodev.service_power.R;
import com.nhodev.service_power.listener.OnRecyclerViewItemClickListener;
import com.nhodev.service_power.models.MusicModel;

import java.util.ArrayList;

/**
 * Được tạo bởi Phạm Nhớ ngày 01/11/2021.
 **/
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MusicModel> listMusic;
    private OnRecyclerViewItemClickListener listener;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public MusicAdapter(Context mContext, ArrayList<MusicModel> listMusic) {
        this.mContext = mContext;
        this.listMusic = listMusic;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.music_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MusicModel hero = listMusic.get(position);
        holder.mTextName.setText(hero.getName());
        holder.viewParentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecyclerViewItemClicked(listMusic.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMusic.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private ConstraintLayout viewParentHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.tvName);
            viewParentHolder = itemView.findViewById(R.id.viewParentHolder);
        }
    }
}
