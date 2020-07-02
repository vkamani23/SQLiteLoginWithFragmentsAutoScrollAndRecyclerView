package com.example.loginregistration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SignUpActivity extends AppCompatActivity {

    private static final int READ_PERMISSION_CODE = 100;
    private static final int WRITE_PERMISSION_CODE = 101;
    private final String checkEmailQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COLUMN_EMAIL + " = ?";
    SQLiteOpenHelper openHelper;
    SQLiteDatabase sqLiteDatabase, db;
    Cursor cursorEmailValidation;
    private EditText inputFirstName, inputLastName, inputDOB, inputEmail, inputPhone, inputPassword, inputConfirmPassword;
    private Button btnRegister;
    private TextView gotoLogin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        findViews();
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_CODE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_CODE);
        inputDOB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DatePicker datePicker = new DatePicker(inputDOB);
                    datePicker.show(getSupportFragmentManager(), "");
                }
                return false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase = openHelper.getWritableDatabase();
                String first_name = inputFirstName.getText().toString().trim();
                String last_name = inputLastName.getText().toString().trim();
                String dob = inputDOB.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirm_password = inputConfirmPassword.getText().toString().trim();
                if (!checkEmailExits(email)) {
                    if (password.equals(confirm_password)) {
                        insertData(first_name, last_name, dob, email, phone, password);
                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_LONG).show();
                        Intent startLoginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(startLoginIntent);
                    } else
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Email Already Exists!", Toast.LENGTH_LONG).show();
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(SignUpActivity.this,
                    new String[]{permission},
                    requestCode);
        }
        /*else {
            Toast.makeText(SignUpActivity.this,"Permission already granted",Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SignUpActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                Log.d("PermissionResults", "onRequestPermissionsResult: Read Permission Granted");
            } else {
                //Toast.makeText(SignUpActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                Log.d("PermissionResults", "onRequestPermissionsResult: Read Permission Denied");
            }
        } else if (requestCode == WRITE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SignUpActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                Log.d("PermissionResults", "onRequestPermissionsResult: Write Permission Granted");
            } else {
                //Toast.makeText(SignUpActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                Log.d("PermissionResults", "onRequestPermissionsResult: Write Permission Denied");
            }
        }
    }

    private boolean checkEmailExits(String email) {
        cursorEmailValidation = db.rawQuery(checkEmailQuery, new String[]{email});
        if (cursorEmailValidation != null) {
            if (cursorEmailValidation.getCount() > 0) {
                cursorEmailValidation.moveToNext();
                return true;
            }
        }
        return false;
    }

    public void insertData(String first_name, String last_name, String dob, String email, String phone, String password) {
        /*String picture = "iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAQAAABIkb+zAAACSElEQVR4Ae2aT0tUYRSHHwhyXAkF" +
                "/SElCCToS9QmcGvMpihq0z9CzMFN0FeIoPoMbQKjbSBtBxwFZWgT5TIYoqHxasLIPbkYwmQuB+6957wE53m274XfAzKLg5SmSY" +
                "/vzFOVe/TY5iru/EAQhjSpwgNyBGELd3pI5YSHo/nCJu7MM6yY8Ojv/D2ukYBmQcI0d3jDJ7bJyMnJ+MYqr7jFuYL51" +
                "/FCSTjFUzpIoTltnjAFPPafrye8ZRfRZcA7//lKQgmV+Z48Kx1wn/TQZLd0QJ85ErNAjlRwyN2086WyebqEG+RIDR" +
                "4wRwKukCE12ecSzpxkE6nRNidw5TlSsy0cOT/mp5MRolj0bsAZ3HiN1B4gvMCJ0/w2CciYwoUWYhIgLODCOmJkGwcuIGbmnMWc24ihN11" +
                "/gQx8iTmrpgEfMeeracAXzBmYBvzCnAPTgCFBENTAJMtssH/oOi0aHEWMVXfoXOQzcsQuM44Byg6dyX8" +
                "/G3064Reg7FBZRsa45Beg7FDZQMbY8QtQdqjsF9yR3QKUHSpSoF+AsiMCDIyACIiACIiACIiACIiACIiACIiACIiACIiACIiA" +
                "/K4q/8zzZpvgLKjxL8zLfoFKDtUGnSRY24x4Rig7NCZoXvss2lwDVB26DRYosPeoWssMgH+AeqO0mSIoTuY88E0YAVzLtNHjPzJ" +
                "LHgkvGfH4I9nhVkSIAWWfacTAREQAREQAREQAREQARGg32/KvXNHv9/o75Ki32/0d0nR7zf6u8To9xv9XWL0+43ZnecPKogex63Vp08AAAAASUVORK5CYII=";*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_FIRST_NAME, first_name);
        contentValues.put(DatabaseHelper.COLUMN_LAST_NAME, last_name);
        contentValues.put(DatabaseHelper.COLUMN_DOB, dob);
        contentValues.put(DatabaseHelper.COLUMN_EMAIL, email);
        contentValues.put(DatabaseHelper.COLUMN_PHONE, phone);
        contentValues.put(DatabaseHelper.COLUMN_PASSWORD, password);
//        contentValues.put(DatabaseHelper.COLUMN_PICTURE, null);
        long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    private void findViews() {
        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputDOB = findViewById(R.id.inputDOB);
        inputEmail = findViewById(R.id.inputEmail);
        inputPhone = findViewById(R.id.inputPhone);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        gotoLogin = findViewById(R.id.gotoLogin);
    }

}