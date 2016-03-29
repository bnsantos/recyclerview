package com.bnsantos.recyclerview;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by bruno on 29/03/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
  private List<String> imageList;
  private final int size;
  private final HolderListener viewHolderListener;
  private SparseBooleanArray selections = new SparseBooleanArray();
  private ActionMode.Callback supportActionMode;
  private boolean selectable;

  public ImageAdapter(List<String> urls, int size, @NonNull final ClickListener listener){
    imageList = urls;
    this.size = size;

    this.viewHolderListener = new HolderListener() {
      @Override
      public void click(ImageHolder holder, String url) {
        if(isSelectionMode()){
          toggleSelection(holder);
        }else{
          listener.click(url);
        }
      }

      @Override
      public void longClick(ImageHolder holder, String url) {
        if(isSelectionMode()){
          toggleSelection(holder);
        }else{
          if(selectable){
            toggleSelection(holder);
          }else{
            listener.click(url);
          }
        }
      }
    };
  }

  @Override
  public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
    view.setLayoutParams(new GridView.LayoutParams(size, size));
    return new ImageHolder(view, viewHolderListener);
  }

  @Override
  public void onBindViewHolder(ImageHolder holder, int position) {
    holder.bind(selections.get(position), imageList.get(position));
  }

  @Override
  public int getItemCount() {
    return imageList.size();
  }

  private void toggleSelection(ImageHolder holder){
    int position = holder.getAdapterPosition();
    if(selections.get(position)){
      selections.delete(position);
    }else{
      selections.put(position, true);
    }
    holder.contentLayout.setChecked(selections.get(position));

    notifyItemChanged(position);
  }

  public void clearSelection(){
    selections.clear();
    notifyDataSetChanged();
  }

  public boolean isSelectionMode(){
    return selections.size()>0;
  }

  public boolean isSelectable() {
    return selectable;
  }

  public void setSelectable(boolean selectable) {
    this.selectable = selectable;
  }

  class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private final SimpleDraweeView view;
    private final CheckableFrameLayout contentLayout;
    private final HolderListener listener;
    private String url;

    public ImageHolder(View itemView, HolderListener listener) {
      super(itemView);
      view = (SimpleDraweeView) itemView.findViewById(R.id.view);
      contentLayout = (CheckableFrameLayout) itemView.findViewById(R.id.contentLayout);
      view.setOnClickListener(this);
      view.setOnLongClickListener(this);
      this.listener = listener;
    }

    public void bind(final boolean selected, final String url){
      this.url = url;
      view.setImageURI(Uri.parse(url));
      contentLayout.setChecked(selected);
    }

    @Override
    public boolean onLongClick(View v) {
      if(listener!=null){
        listener.longClick(this, url);
        return true;
      }else {
        return false;
      }
    }

    @Override
    public void onClick(View v) {
      if (listener != null) {
        listener.click(this, url);
      }
    }
  }

  private interface HolderListener {
    void click(ImageHolder holder, String url);
    void longClick(ImageHolder holder, String url);
  }

  public interface ClickListener {
    void click(String url);
  }
}
