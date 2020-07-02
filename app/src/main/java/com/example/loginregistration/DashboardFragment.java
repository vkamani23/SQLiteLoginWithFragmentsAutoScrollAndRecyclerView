package com.example.loginregistration;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private static final String SHARED_PREF_NAME = "MySharedPrefs";
    private static final String KEY_IMAGE_PATH = "ImagePath";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EMAIL = "email";
    private static final String TAG = "DashboardFragment";
    private final String fetchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE "
            + DatabaseHelper.COLUMN_EMAIL + " = ?";
    //private boolean isPictureUpdated = false;
    private final int ACTION_CODE = 100;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase sqLiteDatabase, db;
    Cursor cursor;
    CircleImageView profile_picture;
    EditText edtFirstName, edtLastName, edtDob, edtEmail, edtPhone;
    List<Person> personList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mEmail;
    private String picture_path;

    public DashboardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String email) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);

        return fragment;
    }

    public void saveLocally(String data) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IMAGE_PATH, data);
        editor.apply();
    }

    public String getLocalData(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IMAGE_PATH, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveLocally(null);
        if (getArguments() != null) {
            mEmail = getArguments().getString(ARG_EMAIL);
            openHelper = new DatabaseHelper(getActivity());
            sqLiteDatabase = openHelper.getReadableDatabase();
            db = openHelper.getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery(fetchQuery, new String[]{mEmail});
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        Person person = new Person(
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DOB)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)),
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD)),
                                //convertToBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_PICTURE)))
                                null
                        );

                        if (cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_PICTURE)) != null) {
                            Bitmap img = convertToBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_PICTURE)));
                            person.setProfile_picture(img);
                        }
                        personList.add(person);
                    }
                }
            }
        }
    }

    private Bitmap convertToBitmap(byte[] blob) {
        ByteArrayInputStream stream = new ByteArrayInputStream(blob);
        Bitmap myBitmap = BitmapFactory.decodeStream(stream);
        return myBitmap;
    }

    public void getImage() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, ACTION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_CODE && resultCode == getActivity().RESULT_OK) {
            Uri uri = data.getData();
            String[] column = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, column, null, null, null);
            cursor.moveToFirst();
            picture_path = cursor.getString(cursor.getColumnIndex(column[0]));
//            Log.d("---------->", "onActivityResult: Picture Path: "+picture_path);
            cursor.close();
            profile_picture.setImageBitmap(BitmapFactory.decodeFile(picture_path));
            saveLocally(picture_path);
//            Log.d(TAG, "onActivityResult: "+getLocalData(KEY_IMAGE_PATH));
            saveImageToDB(profile_picture);
        }
    }

    private void saveImageToDB(CircleImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        byte[] imagesBytes = convertToImageByte(bitmap);
        String updateImageQuery = "UPDATE " + DatabaseHelper.TABLE_NAME + " SET " + DatabaseHelper.COLUMN_PICTURE + " =? WHERE "
                + DatabaseHelper.COLUMN_EMAIL + " = " + mEmail + "";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_PICTURE, imagesBytes);
        db.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.COLUMN_EMAIL + " = ?", new String[]{mEmail});
    }

    private byte[] convertToImageByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        profile_picture = view.findViewById(R.id.img_profile);

        if (picture_path != null)
            profile_picture.setImageBitmap(BitmapFactory.decodeFile(picture_path));
        else if (personList.get(0).getProfile_picture() != null) {
            profile_picture.setImageBitmap(personList.get(0).getProfile_picture());
        }

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        edtFirstName = view.findViewById(R.id.first_name_profile);
        edtFirstName.setText(personList.get(0).getFirst_name());
        edtLastName = view.findViewById(R.id.last_name_profile);
        edtLastName.setText(personList.get(0).getLast_name());
        edtDob = view.findViewById(R.id.dob_profile);
        edtDob.setText(personList.get(0).getDob());
        edtEmail = view.findViewById(R.id.email_profile);
        edtEmail.setText(personList.get(0).getEmail());
        edtPhone = view.findViewById(R.id.phone_profile);
        edtPhone.setText(String.valueOf(personList.get(0).getPhone()));
        return view;
    }
}