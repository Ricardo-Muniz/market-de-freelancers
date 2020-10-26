package br.com.nouva.freelancer.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.nouva.freelancer.Adapter.AdapterDialogAsset;
import br.com.nouva.freelancer.MainActivity;
import br.com.nouva.freelancer.Model.ModelAsset;
import br.com.nouva.freelancer.R;

public class DialogAssets extends AppCompatDialog {

    private Context context;
    private MainActivity main;
    private List<ModelAsset> list;
    public AdapterDialogAsset adapterDialogAsset;
    public RecyclerView recyclerView;

    public DialogAssets(MainActivity main, Context context, List<ModelAsset> list) {
        super(context);
        this.context = context;
        this.main = main;
        this.list = list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_asset);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        recyclerView = v.findViewById(R.id.recyclerview_asset);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 3);

        recyclerView.setLayoutManager(mLayoutManager);
        adapterDialogAsset = new AdapterDialogAsset(main, list);

        recyclerView.setAdapter(adapterDialogAsset);
        adapterDialogAsset.notifyDataSetChanged();

        builder.create();
    }
}
