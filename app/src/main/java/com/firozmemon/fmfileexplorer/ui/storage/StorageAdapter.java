package com.firozmemon.fmfileexplorer.ui.storage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.models.FileModel;
import com.firozmemon.fmfileexplorer.ui.base.AdapterItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firoz on 11/6/17.
 */

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.MyViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<FileModel> fileModelList;
    AdapterItemClickListener itemClickListener;

    public StorageAdapter(Context context, List<FileModel> fileModelList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fileModelList = fileModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_base, parent, false);
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

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    contextMenu.setHeaderTitle("Select Action");
                    contextMenu.add("Delete")
                            .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    // If activity is subscribed to adapter Click,
                                    // notify activity about it
                                    if (itemClickListener != null) {
                                        itemClickListener.onAdapterItemDeleteClicked(view, getAdapterPosition());
                                    }
                                    return true;
                                }
                            });
                }
            });
        }
    }

    public void setItemClickListener(AdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
