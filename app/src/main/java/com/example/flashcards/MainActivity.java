package com.example.flashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.flashcards.Adapter.CardsRVAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnView = findViewById(R.id.bnView);

        bnView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment(), false);
            } else if (id == R.id.nav_flashcards) {
                loadFragment(new Cards(), false);
            } else if (id == R.id.nav_gratitude) {
                loadFragment(new Gratitude(), false);
            } else { //Account
                loadFragment(new Account(), false);
            }

            return true;
        });

        bnView.setSelectedItemId(R.id.nav_home);

    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.container, fragment);
        else
            ft.replace(R.id.container, fragment);
        ft.commit();
    }

}