package com.example.loginregistration;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private final String loginQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE "
            + DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
    SQLiteOpenHelper openHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private TextView gotoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        openHelper = new DatabaseHelper(this);
        sqLiteDatabase = openHelper.getReadableDatabase();
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                cursor = sqLiteDatabase.rawQuery(loginQuery, new String[]{email, password});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(), "Logged In Successfully!", Toast.LENGTH_LONG).show();
                        Intent landingIntent = new Intent(LoginActivity.this, LandingActivity.class);
                        landingIntent.putExtra("email", email);
                        startActivity(landingIntent);
                    } else
                        Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findViews() {
        inputEmail = findViewById(R.id.inputLoginEmail);
        inputPassword = findViewById(R.id.inputLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        gotoRegister = findViewById(R.id.gotoRegister);
    }
}