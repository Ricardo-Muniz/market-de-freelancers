package br.com.nouva.freelancer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import br.com.nouva.freelancer.Activity.OverviewProject;
import br.com.nouva.freelancer.Model.ModelMoreProjects;
import br.com.nouva.freelancer.R;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class AdapterMore extends RecyclerView.Adapter<AdapterMore.ViewHolder> {

    private Context context;
    private List<ModelMoreProjects> moreProjects;

    public AdapterMore(Context context, List<ModelMoreProjects> moreProjects) {
        this.context = context;
        this.moreProjects = moreProjects;
    }

    @NonNull
    @Override
    public AdapterMore.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_more,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMore.ViewHolder holder, int position) {
        final ModelMoreProjects more = moreProjects.get(position);
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        holder.tv_name_user.setText(more.getName());
        Glide.with(context)
                .asBitmap()
                .load(more.getProfile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade(factory))
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888))
                .into(new CustomTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.iv_image_profile_user.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        Glide.with(context)
                .asBitmap()
                .load(more.getWallpaper())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade(factory))
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(512, 512))
                .into(holder.iv_wallpaper_card);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateIntent(more.getKey());
            }
        });

    }

    private void generateIntent(String key) {
        Intent intent = new Intent(context, OverviewProject.class);
        intent.putExtra("from", key);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return moreProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name_user;
        private ImageView iv_wallpaper_card;
        private ImageView iv_image_profile_user;
        private CardView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_user = itemView.findViewById(R.id.tv_name_item_more);
            iv_wallpaper_card = itemView.findViewById(R.id.iv_item_more_wallpaper);
            iv_image_profile_user = itemView.findViewById(R.id.iv_profile_image_more);
            container = itemView.findViewById(R.id.cv_project_more);

        }
    }
}
