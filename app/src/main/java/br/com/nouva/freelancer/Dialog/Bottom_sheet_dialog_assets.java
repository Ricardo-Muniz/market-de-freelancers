package br.com.nouva.freelancer.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import br.com.nouva.freelancer.Adapter.AdapterDialogAsset;
import br.com.nouva.freelancer.MainActivity;
import br.com.nouva.freelancer.Model.ModelAsset;
import br.com.nouva.freelancer.R;

public class Bottom_sheet_dialog_assets extends BottomSheetDialogFragment {
    private View bottomSheet;
    private int bottomSheetPeekHeight;
    private Context context;

    private MainActivity main;
    private List<ModelAsset> list;
    public AdapterDialogAsset adapterDialogAsset;
    public RecyclerView recyclerView;

    static Bottom_sheet_dialog_assets newInstance(List<ModelAsset> list, MainActivity main) {
        return new Bottom_sheet_dialog_assets(list, main);
    }

    public Bottom_sheet_dialog_assets(List<ModelAsset> list, MainActivity main) {
        this.list = list;
        this.main = main;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_MaterialComponents_Light_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        final View view = inflater.inflate(R.layout.dialog_layout_asset, container, false);

        bottomSheet = view.findViewById(R.id.modal_assets);

        recyclerView = view.findViewById(R.id.recyclerview_asset);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 3);

        recyclerView.setLayoutManager(mLayoutManager);
        adapterDialogAsset = new AdapterDialogAsset(main, list);

        recyclerView.setAdapter(adapterDialogAsset);
        adapterDialogAsset.notifyDataSetChanged();

        bottomSheetPeekHeight = getResources()
                .getDimensionPixelSize(R.dimen.bottom_sheet_default_peek_height_asset);

        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorDrk));


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpBottomSheet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void setUpBottomSheet() {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior
                .from((View) getView().getParent());

        bottomSheetBehavior.setPeekHeight(bottomSheetPeekHeight);

        final ViewGroup.LayoutParams childLayoutParams = bottomSheet.getLayoutParams();

        bottomSheet.setLayoutParams(childLayoutParams);
    }

}
