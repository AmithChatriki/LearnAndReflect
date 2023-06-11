package com.example.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcards.Utils.MyDBHelper;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister, btn2;
    EditText email, pass, cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.RegisterBtn);
        btn2 = findViewById(R.id.loginBtn2);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        cpass = findViewById(R.id.password2);

        MyDBHelper dbHelper = new MyDBHelper(RegisterActivity.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = email.getText().toString();
                String password = pass.getText().toString();
                String cpassword = cpass.getText().toString();

                if (password.equals(cpassword)) {

                    SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor =pref.edit();
                    editor.putBoolean("flag", true);
                    editor.apply();

                    dbHelper.addAccounts(emailId, password);
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords are not same", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}