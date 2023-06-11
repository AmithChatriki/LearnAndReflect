package com.example.flashcards;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcards.Adapter.CardsRVAdapter;
import com.example.flashcards.Model.CardsModel;
import com.example.flashcards.Utils.MyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cards extends Fragment implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private MyDBHelper myDBHelper;

    private List<CardsModel> mList;
    private CardsRVAdapter adapter;


    public Cards() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_cards, null);


        recyclerView = root.findViewById(R.id.recyclerCards);
        fab = root.findViewById(R.id.fabAddCards);
        myDBHelper = new MyDBHelper(getActivity());
        mList = new ArrayList<>();
        adapter = new CardsRVAdapter(getActivity(), mList, myDBHelper);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        try {
            mList = myDBHelper.getAllGoals();
            Collections.reverse(mList);
            adapter.setGoals(mList);
        } catch (Exception e) {
            Log.d("hell", "No Cards");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewGoal.newInstance().show(requireActivity().getSupportFragmentManager(), AddNewGoal.TAG);
                showCards();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RVTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDBHelper.getAllGoals();
        Collections.reverse(mList);
        adapter.setGoals(mList);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void showCards() {

        MyDBHelper dbHelper = new MyDBHelper(getContext());

        List<CardsModel> data = (List<CardsModel>) dbHelper.getAllGoals();
        Collections.reverse(data);

        if (data.size()>0) {
//            llnonodes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(new CardsRVAdapter(getActivity(), (ArrayList<CardsModel>) data, dbHelper));


        } else {
//            llnonodes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

}