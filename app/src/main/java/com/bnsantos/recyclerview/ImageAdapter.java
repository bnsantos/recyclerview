package com.bnsantos.recyclerview;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
  private final ImageListener listener;

  public ImageAdapter(List<String> urls, int size, ImageListener listener){
    imageList = urls;
    this.size = size;
    this.listener = listener;
  }

  @Override
  public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
    view.setLayoutParams(new GridView.LayoutParams(size, size));
    return new ImageHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(ImageHolder holder, int position) {
    holder.bind(imageList.get(position));
  }

  @Override
  public int getItemCount() {
    return imageList.size();
  }

  class ImageHolder extends RecyclerView.ViewHolder{
    private final SimpleDraweeView view;
    private final ImageListener listener;

    public ImageHolder(View itemView, ImageListener listener) {
      super(itemView);
      view = (SimpleDraweeView) itemView.findViewById(R.id.view);
      this.listener = listener;
    }

    public void bind(final String url){
      view.setImageURI(Uri.parse(url));
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener!=null) {
            listener.click(url);
          }
        }
      });
    }
  }

  interface ImageListener{
    void click(String url);
  }
}
