package br.com.nouva.freelancer.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.nouva.freelancer.MainActivity;
import br.com.nouva.freelancer.Model.ModelAsset;
import br.com.nouva.freelancer.R;


public class AdapterDialogAsset extends RecyclerView.Adapter<AdapterDialogAsset.ViewHolder> {

    private List<ModelAsset> assets;
    private static final String TAG = "DialogAsset";
    private WeakReference<MainActivity> context;


    public AdapterDialogAsset(MainActivity main, List<ModelAsset> assets) {
        this.context = new WeakReference<>(main);
        this.assets = assets;
    }

    @NonNull
    @Override
    public AdapterDialogAsset.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.get()).inflate(R.layout.dialog_form_image,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterDialogAsset.ViewHolder holder, final int position) {
       final ModelAsset asset = assets.get(position);
       holder.iv_asseted_form.setImageBitmap(asset.getBitmap());

       holder.remove_uri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int pos = holder.getAdapterPosition();
               Log.i(TAG, String.valueOf(pos));

               String after = holder.iv_asseted_form.getDrawable().toString();
               Log.i(TAG, after);

               assets.remove(pos);
               notifyItemRemoved(pos);
               notifyItemRangeChanged(pos, assets.size());

               context.get().updateOBJ(assets.size());
               if (assets.size() == 0) context.get().removeLabelAsset();

               Log.d(TAG, "Assets size: " + assets.size());

               String before = holder.iv_asseted_form.getDrawable().toString();
               Log.i(TAG, before);

           }
       });
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_asseted_form;
        private CardView remove_uri;
        private CardView card;

       private ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_asseted_form = itemView.findViewById(R.id.iv_asseted_form);
            remove_uri = itemView.findViewById(R.id.cv_remove_image_asset);
            card = itemView.findViewById(R.id.card_image_asset);
        }
    }
}
