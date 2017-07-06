package com.firozmemon.fmfileexplorer.ui.apps;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.models.AppModel;
import com.firozmemon.fmfileexplorer.ui.base.AdapterItemClickListener;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firoz on 11/6/17.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<AppModel> appModelList;
    AppAdapterItemClickListener itemClickListener;

    public AppAdapter(Context context, List<AppModel> appModelList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.appModelList = appModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_base, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AppModel appModel = appModelList.get(position);
        if (appModel.getAppIcon() != null) {
            holder.iconImageView.setImageDrawable(appModel.getAppIcon());
        } else {
            holder.iconImageView.setImageResource(R.mipmap.ic_launcher);
        }
        String appText = appModel.getAppName();
        if (appText == null) {
            appText = appModel.getPackageName();
        }
        holder.folderNameTextView.setText(appText);

        holder.fileSizeTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return appModelList.size();
    }

    public AppModel getItem(int position) {
        return appModelList.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iconImageView)
        ImageView iconImageView;
        @BindView(R.id.folderNameTextView)
        TextView folderNameTextView;
        @BindView(R.id.fileSizeTextView)
        TextView fileSizeTextView;

        public MyViewHolder(final View itemView) {
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

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (itemClickListener != null)
//                        itemClickListener.onAdapterItemLongClick(view, getAdapterPosition());
//                    return true;
//                }
//            });

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    contextMenu.setHeaderTitle("Select Action");
                    contextMenu.add("Backup")
                            .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    // If activity is subscribed to adapter Click,
                                    // notify activity about it
                                    if (itemClickListener != null) {
                                        itemClickListener.onAppBackupClicked(view, getAdapterPosition());
                                    }
                                    return true;
                                }
                            });
                    contextMenu.add("Uninstall")
                            .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    // If activity is subscribed to adapter Click,
                                    // notify activity about it
                                    if (itemClickListener != null) {
                                        itemClickListener.onAppUnInstallClicked(itemView, getAdapterPosition());
                                    }
                                    return true;
                                }
                            });
                }
            });

        }
    }

    public void setItemClickListener(AppAdapterItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
