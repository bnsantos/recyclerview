package com.bnsantos.recyclerview;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 29/03/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
  private List<String> imageList;
  private final int size;
  private final HolderListener viewHolderListener;
  private SparseBooleanArray selections = new SparseBooleanArray();
  private final ActionMode.Callback supportActionMode;
  private boolean selectable;
  private final WeakReference<ClickListener> listener;

  public ImageAdapter(List<String> urls, int size, @NonNull final ClickListener activity){
    imageList = urls;
    this.size = size;
    this.listener = new WeakReference<>(activity);

    supportActionMode = new ActionMode.Callback() {
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        listener.get().getMenuInflater().inflate(R.menu.selection_menu, menu);
        return true;
      }

      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
      }

      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int itemId = item.getItemId();
        if(itemId ==R.id.selecctAll){
          selectAll();
        }else if(itemId ==R.id.delete){
          removeSelected();
        }
        return true;
      }

      @Override
      public void onDestroyActionMode(ActionMode mode) {
        clearSelection();
      }
    };

    this.viewHolderListener = new HolderListener() {
      @Override
      public void click(ImageHolder holder, String url) {
        if(isSelectionMode()){
          toggleSelection(holder);
        }else{
          if(listener.get()!=null) {
            listener.get().click(url);
          }
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
            if(listener.get()!=null) {
              listener.get().click(url);
            }
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
      if(!isSelectionMode()){
        if(listener.get()!=null) {
          listener.get().setActionMode(supportActionMode);
        }
      }
      selections.put(position, true);
    }
    holder.view.setChecked(selections.get(position));
    updateActionMode();
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

  private void selectAll(){
    selections.clear();
    for(int i=0;i<imageList.size();i++){
      selections.put(i, true);
    }
    notifyDataSetChanged();
  }

  private void removeSelected(){
    int size = selections.size();
    List<String> toRemove = new ArrayList<>(size);
    for(int i = 0 ; i < size; i++ ){
      int pos = selections.keyAt(i);
      toRemove.add(imageList.get(pos));
      notifyItemRemoved(pos);
    }
    imageList.removeAll(toRemove);
    notifyItemRangeChanged(0, imageList.size());
    clearSelection();
    updateActionMode();
  }

  private void updateActionMode(){
    if(listener.get()!=null) {
      int size = selections.size();
      if(size==0){
        listener.get().finishActionMode();
      }else{
        listener.get().updateSelectionTitle(size);
      }
    }
  }

  class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private final CheckableDraweeView view;
    private final HolderListener listener;
    private String url;

    public ImageHolder(View itemView, HolderListener listener) {
      super(itemView);
      view = (CheckableDraweeView) itemView.findViewById(R.id.view);
      view.setOnClickListener(this);
      view.setOnLongClickListener(this);
      this.listener = listener;
    }

    public void bind(final boolean checked, final String url){
      this.url = url;

      view.setImageURI(Uri.parse(url));
      view.setChecked(checked);
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
    void setActionMode(ActionMode.Callback callback);
    MenuInflater getMenuInflater();
    void updateSelectionTitle(int count);
    void finishActionMode();
  }
}
