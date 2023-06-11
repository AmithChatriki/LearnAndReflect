package com.example.flashcards;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.Utils.MyDBHelper;

import java.util.ArrayList;

public class RecyclerGratitudeAdapter extends RecyclerView.Adapter<RecyclerGratitudeAdapter.ViewHolder> {

    Context context;
    ArrayList<GratitudesModel> arrGrats = new ArrayList<>();
    MyDBHelper myDBHelper;


    RecyclerGratitudeAdapter(Context context, ArrayList<GratitudesModel> arrGrats, MyDBHelper myDBHelper) {
        this.context = context;
        this.arrGrats = arrGrats;
        this.myDBHelper = myDBHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.gratitude_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textTitle.setText(arrGrats.get(position).getTitle());
        holder.textContent.setText(arrGrats.get(position).getContent());

        holder.llrow.setOnLongClickListener(v -> {
            String title = arrGrats.get(position).getTitle();
            deleteGratitude(title);
            return true;
        });
    }

    private void deleteGratitude(String title) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure want to delete?")
                .setPositiveButton("Yes", (dialog1, which) -> {
                    myDBHelper.deleteGrats(title);
                    Gratitude fragment = (Gratitude) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.container);
                    fragment.showGrats();
                })
                .setNegativeButton("No", (dialog12, which) -> {

                }).show();

    }

    @Override
    public int getItemCount() {
        return arrGrats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textContent;
        LinearLayout llrow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.txtTitle);
            textContent = itemView.findViewById(R.id.txtContent);
            llrow = itemView.findViewById(R.id.llgratrow);
        }
    }

}
