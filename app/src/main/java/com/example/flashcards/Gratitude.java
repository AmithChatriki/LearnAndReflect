package com.example.flashcards;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.flashcards.Utils.MyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Gratitude extends Fragment {

    Button createGrat;
    FloatingActionButton fabAdd;
    RecyclerView recyclerView;
    LinearLayout llnonodes;

    public Gratitude() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_gratitude, null);

        createGrat = root.findViewById(R.id.createNew);
        fabAdd = root.findViewById(R.id.fabAdd);
        recyclerView = root.findViewById(R.id.recyclerGrats);
        llnonodes = root.findViewById(R.id.emptyGratsLL);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        showGrats();

        fabAdd.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_layout);

            EditText edtTitle,edtContent;
            Button btnAdd;

            edtTitle = dialog.findViewById(R.id.edtTitle);
            edtContent = dialog.findViewById(R.id.edtContent);
            btnAdd = dialog.findViewById(R.id.btnAdd);

            MyDBHelper dbHelper = new MyDBHelper(getContext());

            btnAdd.setOnClickListener(v1 -> {
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();

                if (!content.equals("")) {

                    dbHelper.addGratitudes(title, content);
                    showGrats();

                    dialog.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Please enter Something", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();

        });

        createGrat.setOnClickListener(v -> fabAdd.performClick());

        return root;
    }

    public void showGrats() {

        MyDBHelper dbHelper = new MyDBHelper(getContext());

        List<GratitudesModel> data = (List<GratitudesModel>) dbHelper.getGrats();

        if (data.size()>0) {
            llnonodes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(new RecyclerGratitudeAdapter(getContext(), (ArrayList<GratitudesModel>) data, dbHelper));


        } else {
            llnonodes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

}