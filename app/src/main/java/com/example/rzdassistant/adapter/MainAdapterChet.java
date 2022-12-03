package com.example.rzdassistant.adapter;

import static java.util.Collections.*;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.rzdassistant.DBHelper;
import com.example.rzdassistant.EditActivityChet;
import com.example.rzdassistant.R;
import com.example.rzdassistant.db.MyDbManagerChet;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainAdapterChet extends RecyclerView.Adapter<MainAdapterChet.MyViewHolder> {

    private Context context;
//    public SortedList<ListItemChet> mainArrayChet;

    public MainAdapterChet(Context context){
        this.context = context;
//        mainArrayChet = new ArrayList<>();
    }


    public final SortedList<ListItemChet> mainArrayChet = new SortedList<>(ListItemChet.class, new SortedList.Callback<ListItemChet>() {
        @Override
        public void onInserted(int pos, int count) {
            notifyItemRangeInserted(pos, count);
        }

        @Override
        public void onRemoved(int pos, int count) {
            notifyItemRangeRemoved(pos, -count);
        }

        @Override
        public void onMoved(int pos, int count) {
            notifyItemMoved(pos, count);
        }

        @Override
        public int compare(ListItemChet contact, ListItemChet contact2) {
            return contact.getTitle_chet().compareTo(contact2.getTitle_chet());
        }

        @Override
        public void onChanged(int pos, int count) {
            notifyItemRangeChanged(pos, count);
        }

        @Override
        public boolean areContentsTheSame(ListItemChet contact, ListItemChet contact2) {
            return contact2.equals(contact);
        }

        @Override
        public boolean areItemsTheSame(ListItemChet contact, ListItemChet contact2) {
            return false;
        }
    });

//    contact2.getTitle_chet().equals(contact.getTitle_chet())


    @NonNull
    @Override
    public MainAdapterChet.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout_chet, parent, false);
        return new MyViewHolder(view, context, mainArrayChet);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapterChet.MyViewHolder holder, int position) {
        holder.setData(mainArrayChet.get(position).getTitle_chet());
        holder.setDataPiketStart(mainArrayChet.get(position).getPiket_start_chet());
        holder.setDataFinish(mainArrayChet.get(position).getTitle_finish_chet());
        holder.setDataPiketFinish(mainArrayChet.get(position).getPiket_finish_chet());
        holder.setDataSpeed(mainArrayChet.get(position).getSpeed_chet());
    }

    @Override
    public int getItemCount() {
        return mainArrayChet.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvtTitleChet;
        private final TextView tvtPiketStartChet;
        private final TextView tvtTitleFinishChet;
        private final TextView tvtPiketFinishChet;
        private final TextView tvtSpeedChet;
        private Context context;
        private SortedList<ListItemChet> mainArrayChet;

        public MyViewHolder(@NonNull View itemView, Context context, SortedList<ListItemChet> mainArrayChet) {
            super(itemView);
            this.context = context;
            this.mainArrayChet = mainArrayChet;
            tvtTitleChet = itemView.findViewById(R.id.tvtTitleChet);
            tvtPiketStartChet = itemView.findViewById(R.id.tvtPiketStartChet);
            tvtTitleFinishChet = itemView.findViewById(R.id.tvtTitleFinishChet);
            tvtPiketFinishChet = itemView.findViewById(R.id.tvtPiketFinishChet);
            tvtSpeedChet = itemView.findViewById(R.id.tvtSpeedChet);
            itemView.setOnClickListener(this);
        }

        public void setData(String titleChet){
            tvtTitleChet.setText(titleChet);
        }

        public void setDataPiketStart(String piketStartChet){
            tvtPiketStartChet.setText(piketStartChet);
        }

        public void setDataFinish(String titlefinishChet){
            tvtTitleFinishChet.setText(titlefinishChet);
        }

        public void setDataPiketFinish(String piketFinishChet){
            tvtPiketFinishChet.setText(piketFinishChet);
        }

        public void setDataSpeed(String speedChet){
            tvtSpeedChet.setText(speedChet);
        }

        @Override
        public void onClick(View view) {

            Intent i_chet_redactor = new Intent(context, EditActivityChet.class);
            i_chet_redactor.putExtra(DBHelper.LIST_ITEM_INTENT_CHET, mainArrayChet.get(getAdapterPosition()));
            i_chet_redactor.putExtra(DBHelper.EDIT_STATE_CHET, false);
            context.startActivity(i_chet_redactor);
        }
    }

    public void updateAdapterChet(List<ListItemChet> newListChet){
        mainArrayChet.clear();
        mainArrayChet.addAll(newListChet);
        notifyDataSetChanged();

    }

    public void removeItemChet(int pos, MyDbManagerChet dbManagerChet) {
        dbManagerChet.deleteChet(mainArrayChet.get(pos).getId_chet());
        mainArrayChet.removeItemAt(pos);
        notifyItemRangeChanged(0, mainArrayChet.size());
        notifyItemRemoved(pos);
    }
}





//    private Context context;
//    public List<ListItemChet> mainArrayChet;
//
//    public MainAdapterChet(Context context){
//        this.context = context;
//        mainArrayChet = new ArrayList<>();
//    }
//
//
//    private final SortedList<ListItemChet> contacts = new SortedList<>(ListItemChet.class, new SortedList.Callback<ListItemChet>() {
//        @Override
//        public void onInserted(int pos, int count) {
//            notifyItemRangeInserted(pos, count);
//        }
//
//        @Override
//        public void onRemoved(int pos, int count) {
//            notifyItemRangeRemoved(pos, count);
//        }
//
//        @Override
//        public void onMoved(int pos, int count) {
//            notifyItemMoved(pos, count);
//        }
//
//        @Override
//        public int compare(ListItemChet contact, ListItemChet contact2) {
//            return contact.getTitle_chet().compareTo(contact2.getTitle_chet());
//        }
//
//        @Override
//        public void onChanged(int pos, int count) {
//            notifyItemRangeChanged(pos, count);
//        }
//
//        @Override
//        public boolean areContentsTheSame(ListItemChet contact, ListItemChet contact2) {
//            return contact2.equals(contact);
//        }
//
//        @Override
//        public boolean areItemsTheSame(ListItemChet contact, ListItemChet contact2) {
//            return contact2.getTitle_chet().equals(contact.getTitle_chet());
//        }
//    });
//
//
//    @NonNull
//    @Override
//    public MainAdapterChet.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout_chet, parent, false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MainAdapterChet.MyViewHolder holder, int position) {
//        holder.setData(mainArrayChet.get(position).getTitle_chet());
//        holder.setDataSpeed(mainArrayChet.get(position).getSpeed_chet());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mainArrayChet.size();
//    }
//
//static class MyViewHolder extends RecyclerView.ViewHolder{
//    private final TextView tvtTitleChet;
//    private final TextView tvtSpeedChet;
//
//    public MyViewHolder(@NonNull View itemView) {
//        super(itemView);
//        tvtTitleChet = itemView.findViewById(R.id.tvtTitleChet);
//        tvtSpeedChet = itemView.findViewById(R.id.tvtSpeedChet);
//    }
//
//    public void setData(String titleChet){
//        tvtTitleChet.setText(titleChet);
//    }
//
//    public void setDataSpeed(String speedChet){
//        tvtSpeedChet.setText(speedChet);
//    }
//}
//
//    public void updateAdapterChet(List<ListItemChet> newListChet){
//        mainArrayChet.clear();
//        mainArrayChet.addAll(newListChet);
//        notifyDataSetChanged();
//
//    }
//
//    public void removeItemChet(int pos, MyDbManagerChet dbManagerChet) {
//        dbManagerChet.deleteChet(mainArrayChet.get(pos).getId_chet());
//        mainArrayChet.remove(pos);
//        notifyItemRangeChanged(0, mainArrayChet.size());
//        notifyItemRemoved(pos);
//    }
