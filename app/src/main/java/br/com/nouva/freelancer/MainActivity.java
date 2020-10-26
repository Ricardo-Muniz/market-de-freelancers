package br.com.nouva.freelancer;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.nouva.freelancer.Activity.HomeScreen;
import br.com.nouva.freelancer.Async.ProcessImages;
import br.com.nouva.freelancer.Async.UploadImages;
import br.com.nouva.freelancer.Dialog.Bottom_sheet_dialog;
import br.com.nouva.freelancer.Dialog.Bottom_sheet_dialog_assets;
import br.com.nouva.freelancer.Dialog.DialogAssets;
import br.com.nouva.freelancer.Model.ModelAsset;
import br.com.nouva.freelancer.Model.ModelReadFile;
import br.com.nouva.freelancer.Model.Project;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LOGIN_MAIN";
    private static boolean PROFILE_ASSETED = false;
    private static int SELECTED_RESULT_IMG = 0;

    private CardView button_save_project;
    private CardView cv_add_new_image, cv_image_one;
    private CardView card_content_image_profile_form;
    private CardView cv_card_count_assets;
    private TextView tv_title_image_asseted_one;
    private ImageView iv_icon_button_asseted_one;
    private ImageView iv_profile_user_form, iv_icon_user_plus_form;
    private EditText edit_text_name_add;
    private EditText edit_text_tile_add;
    private EditText edit_text_body_content_add;
    private TextInputLayout text_input_layout_name_add;
    private RelativeLayout layout_button_submit, layout_list_category;
    private ProgressBar progress_submit_form;
    private TextView tv_title_button_submit;
    private TextView tv_label_count_asset;
    private TextView tv_category_button;

    private View view_include_category, view_include_assets;

    private LinearLayout button_cancel_login, button_view_login;

    public static Uri path_asset_profile_pic;
    public static int CONT_ASSET = 0;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    public Context context = this;

    List<ModelAsset> imageListOBJ;
    String CATEGORY_SELECTED = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openIds();
        askPermissions();

        button_save_project.setOnClickListener(this);
        cv_add_new_image.setOnClickListener(this);
        iv_icon_button_asseted_one.setOnClickListener(this);
        card_content_image_profile_form.setOnClickListener(this);
        cv_image_one.setOnClickListener(this);
        layout_list_category.setOnClickListener(this);
        button_cancel_login.setOnClickListener(this);
        button_view_login.setOnClickListener(this);

    }


    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void openIds() {
        imageListOBJ = new ArrayList<>();

        button_save_project = findViewById(R.id.button_save_project);
        edit_text_name_add = findViewById(R.id.edit_text_name_add);

        edit_text_tile_add = findViewById(R.id.edit_text_tile_add);
        edit_text_body_content_add = findViewById(R.id.edit_text_body_content_add);

        text_input_layout_name_add = findViewById(R.id.text_input_layout_name_add);

        //cardview image assets
        cv_add_new_image = findViewById(R.id.cv_add_new_image);
        cv_image_one = findViewById(R.id.cv_image_one);

        //textview image label uri
        tv_title_image_asseted_one = findViewById(R.id.tv_title_image_asseted_one);

        //icon label image asseted
        iv_icon_button_asseted_one = findViewById(R.id.iv_icon_button_asseted_one);

        iv_profile_user_form = findViewById(R.id.iv_profile_user_form);
        card_content_image_profile_form = findViewById(R.id.card_content_image_profile_form);

        iv_icon_user_plus_form = findViewById(R.id.iv_icon_user_plus_form);
        layout_button_submit = findViewById(R.id.layout_button_submit);
        progress_submit_form = findViewById(R.id.progress_submit_form);
        tv_title_button_submit = findViewById(R.id.tv_title_button_submit);

        cv_card_count_assets = findViewById(R.id.cv_card_count_assets);
        tv_label_count_asset = findViewById(R.id.tv_label_count_asset);

        layout_list_category = findViewById(R.id.layout_list_category);

        tv_category_button = findViewById(R.id.tv_category_button);

        button_cancel_login = findViewById(R.id.button_cancel_login);
        button_view_login = findViewById(R.id.button_view_login);

        view_include_category = findViewById(R.id.layout_category);
        view_include_assets = findViewById(R.id.layout_asstes);

    }

    public void setCategory(String category) {
        CATEGORY_SELECTED = category;
        tv_category_button.setText(category);
    }

    public void newActSucces() {
        Intent intent = new Intent(MainActivity.this, HomeScreen.class);
        startActivity(intent);
        MainActivity.super.finish();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
            }
        }
        return false;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perms : permissionsToRequest) {
                if (hasPermission(perms)) {
                    permissionsRejected.add(perms);
                }
            }
            if (permissionsRejected.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(permissionsRejected
                                                .toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                });
                    }
                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getData() != null) {
                String path = getPathFromURI(data.getData());
                createReadFileUri(data.getData(), requestCode, path);
                Log.d(TAG, "Result Request code gallery: " + requestCode);
                Log.d(TAG, "Result uri cursor: " + path);
            }

        }

    }


    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        String index = cursor.getString(column_index);
        cursor.close();
        return index;
    }

    public void dialogAssetOpen(List<ModelAsset> list) {
        DialogAssets assets = new DialogAssets(this, context, list);
        assets.show();
    }


    private void createReadFileUri(Uri data, int code, String filePath) {
        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
        String filename = getNameImage(filePath);

        setImageActiveBackground(code, filename, selectedImage, data);
    }

    private String getNameImage(String picturePath) {
        File fileName = new File(picturePath);
        return fileName.getName();
    }

    public void generateOBJ(int index, Uri uri, Bitmap bitmap) {
        ModelAsset parser = new ModelAsset();
        parser.setImage(index, uri, true, bitmap);
        imageListOBJ.add(parser);
    }

    public void updateOBJ(int position) {
        int sizeList = imageListOBJ.size();
        CONT_ASSET = position;

        if (position < 3) cv_add_new_image.setVisibility(View.VISIBLE);

        if (sizeList == 1) {
            String path = getPathFromURI(imageListOBJ.get(0).getUri());
            String name = getNameImage(path);
            tv_title_image_asseted_one.setText(name);
        }
        tv_label_count_asset.setText(String.valueOf(CONT_ASSET));


        Log.d(TAG, "Remove cont: " + CONT_ASSET);
        Log.d(TAG, "Remove position: " + position);
        Log.d(TAG, "Size list: " + sizeList);

    }

    public void removeLabelAsset() {
        cv_image_one.setVisibility(View.GONE);
        cv_card_count_assets.setVisibility(View.GONE);
    }

    private ModelReadFile createProfile() {
        ModelReadFile readFile = new ModelReadFile();
        readFile.setTarget("profile");
        readFile.setUri(path_asset_profile_pic);
        return readFile;
    }

    private void updateLabelSeeMoreAssets(String filename, int code) {
        int count = imageListOBJ.size();

        if (CONT_ASSET == 0) {
            cv_card_count_assets.setVisibility(View.VISIBLE);
            cv_image_one.setVisibility(View.VISIBLE);
            tv_title_image_asseted_one.setText(filename);

        } else if (count == 2) {
            tv_title_image_asseted_one.setText(R.string.txt_see_more);

        } else if (count == 3) {
            cv_add_new_image.setVisibility(View.GONE);
        }

        if (code != 204) CONT_ASSET = imageListOBJ.size();
        int counter = CONT_ASSET;
        tv_label_count_asset.setText(String.valueOf(counter));

        Log.d(TAG, "Returned asset count: " + counter);
        Log.d(TAG, "Count asset: " + count);
    }

    private void setImageActiveBackground(int code, String filename, Bitmap bitmap, Uri uri) {
        assert uri != null;
        switch (code) {
            case 201:
                generateOBJ(0, uri, bitmap);
                break;
            case 202:
                generateOBJ(1, uri, bitmap);
                break;
            case 203:
                generateOBJ(2, uri, bitmap);
                break;
            case 204:
                iv_profile_user_form.setImageBitmap(bitmap);
                iv_icon_user_plus_form.setVisibility(View.GONE);
                path_asset_profile_pic = uri;
                PROFILE_ASSETED = true;
                break;
        }
        updateLabelSeeMoreAssets(filename, code);
    }

    public void copyToFileAsset(boolean valid) {
        if (valid) {
            ArrayList<ModelReadFile> uris = new ArrayList<>();

            for (int i = 0; i < imageListOBJ.size(); i++) {

                int countTarget = i + 1;
                String target = "_" + i + "_";

                ModelReadFile readFile = new ModelReadFile();
                readFile.setUri(imageListOBJ.get(i).getUri());
                readFile.setTarget(target);
                uris.add(readFile);

                Log.d(TAG, "Uri target name: " + target);
                Log.d(TAG, "Count target: " + i);


                if (countTarget == imageListOBJ.size()) {
                    progressSubmit(true);
                    uris.add(createProfile());
                    new ProcessImages(MainActivity.this, uris).execute();
                }
            }
        }
    }

    private void disableInputs(boolean activi) {
        if (!activi) {
            edit_text_name_add.setFocusable(false);
            edit_text_tile_add.setFocusable(false);
            edit_text_body_content_add.setFocusable(false);

            edit_text_name_add.setClickable(false);
            edit_text_tile_add.setClickable(false);
            edit_text_body_content_add.setClickable(false);
            text_input_layout_name_add.setError(null);
            text_input_layout_name_add.clearFocus();
        } else {

            edit_text_name_add.setFocusable(true);
            edit_text_tile_add.setFocusable(true);
            edit_text_body_content_add.setFocusable(true);

            edit_text_name_add.setClickable(true);
            edit_text_tile_add.setClickable(true);
            edit_text_body_content_add.setClickable(true);
        }

    }

    public void progressSubmit(boolean activate) {
        if (activate) {
            tv_title_button_submit.setVisibility(View.GONE);
            layout_button_submit.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDark080));
            progress_submit_form.setVisibility(View.VISIBLE);
            button_save_project.setClickable(false);
            button_save_project.setFocusable(false);

            disableInputs(false);
        } else {
            tv_title_button_submit.setVisibility(View.VISIBLE);
            layout_button_submit.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondary));
            progress_submit_form.setVisibility(View.GONE);
            button_save_project.setClickable(true);
            button_save_project.setFocusable(true);

            disableInputs(true);
        }
    }

    private void intentGallery(int item) {
        switch (item) {
            case 0:
                SELECTED_RESULT_IMG = 201;
                break;
            case 1:
                SELECTED_RESULT_IMG = 202;
                break;
            case 2:
                SELECTED_RESULT_IMG = 203;
                break;
            case 3:
                SELECTED_RESULT_IMG = 204;
                break;
        }
        int RESULT_GALLERY = SELECTED_RESULT_IMG;
        Log.d(TAG, "Intent gallery result: " + RESULT_GALLERY);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_GALLERY);

    }

    public boolean validateForm() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "",
                Snackbar.LENGTH_LONG);

        if (edit_text_tile_add.getText().toString().isEmpty()) {
            edit_text_tile_add.requestFocus();
            return false;
        } else if (edit_text_body_content_add.getText().toString().isEmpty()) {
            edit_text_body_content_add.requestFocus();
            return false;
        } else if (edit_text_name_add.getText().toString().isEmpty()) {
            edit_text_name_add.requestFocus();
            text_input_layout_name_add.setError("Resquested");
            return false;
        } else if (CATEGORY_SELECTED.isEmpty()) {
            snackbar.setText("Request category select.").show();
            return false;
        } else if (!PROFILE_ASSETED) {
            snackbar.setText("Request profile and one asset.").show();
            return false;
        } else {
            return true;
        }
    }

    public Project getContentProject(String uid) {
        String title = edit_text_tile_add.getText().toString();
        String content = edit_text_body_content_add.getText().toString();
        String type = CATEGORY_SELECTED;
        String name = edit_text_name_add.getText().toString();

        return new Project(uid, name, type, title, content);
    }


    public void uploadIMGS(ArrayList<ModelReadFile> listIMG) {
        new UploadImages(MainActivity.this, listIMG).execute();
    }

    private void dialogModal() {
        Bottom_sheet_dialog bottomSheetFragment = new Bottom_sheet_dialog();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void dialogModalAsset(List<ModelAsset> imageList) {
        Bottom_sheet_dialog_assets bottomSheetFragment = new Bottom_sheet_dialog_assets(imageList, MainActivity.this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_project:
                copyToFileAsset(validateForm());
                break;
            case R.id.cv_add_new_image:
                intentGallery(CONT_ASSET);
                break;
            case R.id.cv_image_one:
                dialogModalAsset(imageListOBJ);
                break;
            case R.id.card_content_image_profile_form:
                intentGallery(3);
                break;
            case R.id.layout_list_category:
                dialogModal();
                break;
            case R.id.button_cancel_login:
                MainActivity.super.finish();
                break;
            case R.id.button_view_login:
                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                startActivity(intent);
                break;

        }
    }
}
