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

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btn2;
    EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.loginBtn);
        btn2 = findViewById(R.id.loginBtn2);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);

        MyDBHelper dbHelper = new MyDBHelper(LoginActivity.this);

//        dbHelper.addAccounts("amithchatriki@gmail.com", "7004");
//        dbHelper.addAccounts("anuragn@gmail.com", "5245");

        ArrayList<AccountsModel> arrAccounts = dbHelper.fetchAccounts();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = email.getText().toString();
                String password = pass.getText().toString();

                int login = dbHelper.Login(emailId, password);

                if(login!=0) {
                    SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor =pref.edit();
                    editor.putBoolean("flag", true);
                    editor.apply();

                    Intent iHome = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(iHome);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn2.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        });

    }

}