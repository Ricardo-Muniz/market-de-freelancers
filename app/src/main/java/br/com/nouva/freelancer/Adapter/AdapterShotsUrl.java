package br.com.nouva.freelancer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.com.nouva.freelancer.Activity.OverviewProject;
import br.com.nouva.freelancer.Model.ModelUrls;
import br.com.nouva.freelancer.R;

public class AdapterShotsUrl extends RecyclerView.Adapter<AdapterShotsUrl.ViewHolder> {

    protected Context context;
    protected List<ModelUrls> urls;
    private AdapterShotsUrl.ViewHolder selected = null;

    public AdapterShotsUrl(Context context, List<ModelUrls> urls) {
        this.context = context;
        this.urls = urls;
    }

    @NonNull
    @Override
    public AdapterShotsUrl.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_image_overview,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterShotsUrl.ViewHolder holder, int position) {
       final ModelUrls url = urls.get(position);
       int max = urls.size() - 1;

       if (url.equals(urls.get(0))) {
           holder.layout.setForeground(ContextCompat.getDrawable(context, R.drawable.shape_label_asset_wallpaper));
           ((OverviewProject)context).changeImage(url.getUrl());
           selected = holder;
       } else if (url.equals(urls.get(max))) {
           holder.layout.setVisibility(View.GONE);
       }

       Glide.with(context)
               .asBitmap()
               .load(url.getUrl())
               .apply(new RequestOptions()
               .format(DecodeFormat.PREFER_ARGB_8888)
               .override(500, 500))
               .into(holder.image);

       holder.card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               unselectAsset(selected, url.getUrl());
               holder.layout.setForeground(ContextCompat.getDrawable(context, R.drawable.shape_label_asset_wallpaper));
               selected = holder;
           }
       });

    }

    private void unselectAsset(ViewHolder viewHolder, String url) {
        viewHolder.layout.setForeground(null);
        ((OverviewProject)context).changeImage(url);

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected CardView card;
        protected CardView layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_image_shot_overview);
            card = itemView.findViewById(R.id.cv_image_overview_asset);
            layout = itemView.findViewById(R.id.layout_asset_overview);
        }
    }
}
