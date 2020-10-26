package br.com.nouva.freelancer.Dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import br.com.nouva.freelancer.MainActivity;
import br.com.nouva.freelancer.R;

public class Bottom_sheet_dialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static int SELECTED_CATEGORY;
    private View bottomSheet;
    private int bottomSheetPeekHeight;
    private Context context;

    private RelativeLayout radio_select_design, radio_select_dev, radio_select_art, radio_select_product;
    private RelativeLayout button_confirm_category;
    private TextView tv_select_design, tv_select_dev, tv_select_art, tv_select_product;
    private CardView cv_select_design, cv_select_dev, cv_select_art, cv_select_product;
    private ImageView iv_select_design, iv_select_dev, iv_select_art, iv_select_product;
    private String category = "Design";

    static Bottom_sheet_dialog newInstance() {
        return new Bottom_sheet_dialog();
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

        final View view = inflater.inflate(R.layout.layout_modal_category_login, container, false);

        SELECTED_CATEGORY = 0;

        bottomSheet = view.findViewById(R.id.modal_category_login);

        radio_select_design = view.findViewById(R.id.radio_select_design);
        radio_select_dev = view.findViewById(R.id.radio_select_dev);
        radio_select_art = view.findViewById(R.id.radio_select_art);
        radio_select_product = view.findViewById(R.id.radio_select_product);

        tv_select_design = view.findViewById(R.id.tv_select_design);
        tv_select_dev = view.findViewById(R.id.tv_select_dev);
        tv_select_art = view.findViewById(R.id.tv_select_art);
        tv_select_product = view.findViewById(R.id.tv_select_product);

        cv_select_design = view.findViewById(R.id.cv_select_design);
        cv_select_dev = view.findViewById(R.id.cv_select_dev);
        cv_select_art = view.findViewById(R.id.cv_select_art);
        cv_select_product = view.findViewById(R.id.cv_select_product);

        iv_select_design = view.findViewById(R.id.iv_select_design);
        iv_select_dev = view.findViewById(R.id.iv_select_dev);
        iv_select_art = view.findViewById(R.id.iv_select_art);
        iv_select_product = view.findViewById(R.id.iv_select_product);

        button_confirm_category = view.findViewById(R.id.button_confirm_category);

        bottomSheetPeekHeight = getResources()
                .getDimensionPixelSize(R.dimen.bottom_sheet_default_peek_height);

        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorDrk));

        radio_select_design.setOnClickListener(this);
        radio_select_dev.setOnClickListener(this);
        radio_select_art.setOnClickListener(this);
        radio_select_product.setOnClickListener(this);
        button_confirm_category.setOnClickListener(this);

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
        //final DisplayMetrics displayMetrics = new DisplayMetrics();

        /*requireActivity()
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);*/

        //childLayoutParams.height = displayMetrics.heightPixels;

        bottomSheet.setLayoutParams(childLayoutParams);
    }

    private void selectCategoryMark(int id) {
        switch (id) {
            case 1:
                if (SELECTED_CATEGORY != 0) {
                    unselectItemCategory();
                    tv_select_design.setTextColor(ContextCompat.getColor(context, R.color.colorLight05));
                    cv_select_design.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSecondary)));
                    iv_select_design.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_white));
                    SELECTED_CATEGORY = id;
                    category = "Design";
                }
                break;
            case 2:
                unselectItemCategory();
                tv_select_dev.setTextColor(ContextCompat.getColor(context, R.color.colorLight05));
                cv_select_dev.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSecondary)));
                iv_select_dev.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_white));
                SELECTED_CATEGORY = id;
                category = "Development";
                break;
            case 3:
                unselectItemCategory();
                tv_select_art.setTextColor(ContextCompat.getColor(context, R.color.colorLight05));
                cv_select_art.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSecondary)));
                iv_select_art.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_white));
                SELECTED_CATEGORY = id;
                category = "Art";
                break;
            case 4:
                unselectItemCategory();
                tv_select_product.setTextColor(ContextCompat.getColor(context, R.color.colorLight05));
                cv_select_product.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSecondary)));
                iv_select_product.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_check_white));
                SELECTED_CATEGORY = id;
                category = "Product";
                break;
        }
    }

    private void unselectItemCategory() {
        switch (SELECTED_CATEGORY) {
            case 0:
                tv_select_design.setTextColor(ContextCompat.getColor(context, R.color.colorsemiDark));
                cv_select_design.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorDark080)));
                iv_select_design.setImageDrawable(null);
                break;
            case 1:
                tv_select_design.setTextColor(ContextCompat.getColor(context, R.color.colorsemiDark));
                cv_select_design.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorDark080)));
                iv_select_design.setImageDrawable(null);
                break;
            case 2:
                tv_select_dev.setTextColor(ContextCompat.getColor(context, R.color.colorsemiDark));
                cv_select_dev.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorDark080)));
                iv_select_dev.setImageDrawable(null);
                break;
            case 3:
                tv_select_art.setTextColor(ContextCompat.getColor(context, R.color.colorsemiDark));
                cv_select_art.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorDark080)));
                iv_select_art.setImageDrawable(null);
                break;
            case 4:
                tv_select_product.setTextColor(ContextCompat.getColor(context, R.color.colorsemiDark));
                cv_select_product.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorDark080)));
                iv_select_product.setImageDrawable(null);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_select_design:
                selectCategoryMark(1);
                break;
            case R.id.radio_select_dev:
                selectCategoryMark(2);
                break;
            case R.id.radio_select_art:
                selectCategoryMark(3);
                break;
            case R.id.radio_select_product:
                selectCategoryMark(4);
                break;
            case R.id.button_confirm_category:
                confirmDataCategory();
                break;
        }
    }

    private void confirmDataCategory() {
        ((MainActivity) Objects.requireNonNull(getActivity())).setCategory(category);
        dismiss();
    }
}
