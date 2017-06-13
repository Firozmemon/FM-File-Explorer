package com.firozmemon.fmfileexplorer.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.models.FileModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firoz on 11/6/17.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<FileModel> fileModelList;
    AdapterItemClickListener itemClickListener;

    public MainActivityAdapter(Context context, List<FileModel> fileModelList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fileModelList = fileModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_main, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FileModel fileModel = fileModelList.get(position);
        if (fileModel.getName() != null) {
            if (fileModel.isFolder()) {
                holder.iconImageView.setImageResource(R.drawable.folder);
            } else {
                holder.iconImageView.setImageResource(R.drawable.file);
            }
            holder.fileSizeTextView.setText("(" + fileModel.getFileSize() + ")");

            holder.folderNameTextView.setText(fileModel.getName());
        }
    }

    @Override
    public int getItemCount() {
        return fileModelList.size();
    }

    public FileModel getItem(int position) {
        return fileModelList.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iconImageView)
        ImageView iconImageView;
        @BindView(R.id.folderNameTextView)
        TextView folderNameTextView;
        @BindView(R.id.fileSizeTextView)
        TextView fileSizeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onAdapterItemClick(view, getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (itemClickListener != null)
                        itemClickListener.onAdapterItemLongClick(view, getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public void setItemClickListener(AdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface AdapterItemClickListener {
        void onAdapterItemClick(View view, int position);

        void onAdapterItemLongClick(View view, int position);
    }
}
