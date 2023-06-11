package com.example.flashcards.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.AddNewGoal;
import com.example.flashcards.Model.CardsModel;
import com.example.flashcards.Utils.MyDBHelper;
import com.example.flashcards.R;

import java.util.List;

public class CardsRVAdapter extends RecyclerView.Adapter<CardsRVAdapter.MyViewHolder> {

    private List<CardsModel> list;
    Context context;
    MyDBHelper myDBHelper;

    public CardsRVAdapter(Context context, List<CardsModel> list, MyDBHelper myDBHelper) {
        this.context = context;
        this.list = list;
        this.myDBHelper = myDBHelper;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CardsModel item = list.get(position);
        holder.checkBox.setText(item.getGoal());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myDBHelper.updateStatus(item.getId(), 1);
                } else {
                    myDBHelper.updateStatus(item.getId(), 0);
                }

            }
        });
    }

    private boolean toBoolean(int num) {
        return num!=0;
    }

    public void setGoals(List<CardsModel> mlist) {
        this.list = mlist;

        notifyDataSetChanged();
    }

    public void deleteGoal(int position) {
        CardsModel item = list.get(position);
        myDBHelper.deleteGoal(item.getId());
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void editGoal(int position) {
        CardsModel card = list.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", card.getId());
        bundle.putString("goal", card.getGoal());

        AddNewGoal goal = new AddNewGoal();
        goal.setArguments(bundle);
        goal.show(((AppCompatActivity)context).getSupportFragmentManager(), goal.getTag());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Context getContext() {
        return context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.mCheckBox);
        }
    }

}
