package br.com.nouva.freelancer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import br.com.nouva.freelancer.Activity.OverviewProject;
import br.com.nouva.freelancer.Model.ModelMoreProjects;
import br.com.nouva.freelancer.R;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class AdapterNewUsers extends RecyclerView.Adapter<AdapterNewUsers.ViewHolder> {

    private List<ModelMoreProjects> users;
    private Context context;
    private DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


    public AdapterNewUsers(Context context, List<ModelMoreProjects> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterNewUsers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_card_home, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterNewUsers.ViewHolder holder, int position) {
        final ModelMoreProjects user = users.get(position);

        holder.name.setText(user.getName());
        holder.category.setText(user.getType());
        Glide.with(context)
                .asBitmap()
                .load(user.getProfile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888))
                .into(new CustomTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.profile.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        Glide.with(context)
                .asBitmap()
                .load(user.getWallpaper())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade(factory))
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(512, 512))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        holder.master.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent)));
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.scrProject);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               generateIntent(user.getKey());
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
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView category;
        private ImageView profile;
        private ImageView scrProject;
        private CardView container;
        private CardView master;
        private ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name_item_home);
            container = itemView.findViewById(R.id.cv_project_home);
            profile = itemView.findViewById(R.id.iv_profile_image_home);
            scrProject = itemView.findViewById(R.id.iv_item_home);
            master = itemView.findViewById(R.id.cv_wallpaper_home);
            progressBar = itemView.findViewById(R.id.progress_image_home);
            category = itemView.findViewById(R.id.tv_category);
        }
    }

}
