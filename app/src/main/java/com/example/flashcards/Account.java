package com.example.flashcards;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcards.Utils.MyDBHelper;


public class Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn;

    public Account() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_account, null);
        Button btn = root.findViewById(R.id.logoutBtn);
        TextView userMailId = root.findViewById(R.id.accountEmail);

        MyDBHelper dbHelper = new MyDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("accounts",
                new String[] {"emailID"},
                "isCurrent" + " = ? ",
                new String[] {"1"},
                null, null, null);
        cursor.moveToFirst();
        int x = cursor.getColumnIndex("emailID");
        String currentusermail = cursor.getString(x);
        userMailId.setText(currentusermail);

        btn.setOnClickListener(v -> {
            SharedPreferences pref = getActivity().getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("flag", false);
            editor.apply();

            db.execSQL("UPDATE accounts SET isCurrent = 0");

            Intent i = new Intent(getActivity(), SplashActivity.class);
            startActivity(i);
            getActivity().finish();

        });
        return root;
    }
}