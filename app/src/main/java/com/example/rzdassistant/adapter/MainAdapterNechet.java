package com.example.rzdassistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.rzdassistant.DBHelper;
import com.example.rzdassistant.DBHelperNechet;
import com.example.rzdassistant.EditActivityChet;
import com.example.rzdassistant.EditActivityNechet;
import com.example.rzdassistant.R;
import com.example.rzdassistant.db.MyDbManagerNechet;

import java.util.ArrayList;
import java.util.List;

public class MainAdapterNechet extends RecyclerView.Adapter<MainAdapterNechet.MyViewHolder> {

    private Context context;
//    private List<ListItemNechet> mainArrayNechet;

    public MainAdapterNechet(Context context){
        this.context = context;
//        mainArrayNechet = new ArrayList<>();
    }


    private final SortedList<ListItemNechet> mainArrayNechet = new SortedList<>(ListItemNechet.class, new SortedList.Callback<ListItemNechet>() {
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
        public int compare(ListItemNechet contact, ListItemNechet contact2) {
            return contact.getTitle_nechet().compareTo(contact2.getTitle_nechet());
        }

        @Override
        public void onChanged(int pos, int count) {
            notifyItemRangeChanged(pos, count);
        }

        @Override
        public boolean areContentsTheSame(ListItemNechet contact, ListItemNechet contact2) {
            return contact2.equals(contact);
        }

        @Override
        public boolean areItemsTheSame(ListItemNechet contact, ListItemNechet contact2) {
            return false;
        }
    });

//    contact2.getTitle_nechet().equals(contact.getTitle_nechet())

    @NonNull
    @Override
    public MainAdapterNechet.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout_nechet, parent, false);
        return new MyViewHolder(view, context, mainArrayNechet);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(mainArrayNechet.get(position).getTitle_nechet());
        holder.setDataPiketStartNechet(mainArrayNechet.get(position).getPiket_start_nechet());
        holder.setDataFinish(mainArrayNechet.get(position).getTitle_finish_nechet());
        holder.setDataPiketFinishNechet(mainArrayNechet.get(position).getPiket_finish_nechet());
        holder.setDataSpeed(mainArrayNechet.get(position).getSpeed_nechet());
    }

    @Override
    public int getItemCount() {
        return mainArrayNechet.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvtTitleNechet;
        private final TextView tvtPiketStartNechet;
        private final TextView tvtTitleFinishNechet;
        private final TextView tvtPiketFinishNechet;
        private final TextView tvtSpeedNechet;
        private Context context;
        private SortedList<ListItemNechet> mainArrayNechet;

        public MyViewHolder(@NonNull View itemView, Context context, SortedList<ListItemNechet> mainArrayNechet) {
            super(itemView);
            this.context = context;
            this.mainArrayNechet = mainArrayNechet;
            tvtTitleNechet = itemView.findViewById(R.id.tvtTitleNechet);
            tvtPiketStartNechet = itemView.findViewById(R.id.tvtPiketStartNechet);
            tvtTitleFinishNechet = itemView.findViewById(R.id.tvtTitleFinishNechet);
            tvtPiketFinishNechet = itemView.findViewById(R.id.tvtPiketFinishNechet);
            tvtSpeedNechet = itemView.findViewById(R.id.tvtSpeedNechet);
            itemView.setOnClickListener(this);
        }

        public void setData(String titleNechet){
            tvtTitleNechet.setText(titleNechet);
        }

        public void setDataPiketStartNechet(String piketStartNechet){
            tvtPiketStartNechet.setText(piketStartNechet);
        }

        public void setDataFinish(String titlefinishNechet){
            tvtTitleFinishNechet.setText(titlefinishNechet);
        }

        public void setDataPiketFinishNechet(String piketFinishNechet){
            tvtPiketFinishNechet.setText(piketFinishNechet);
        }

        public void setDataSpeed(String speedNechet){
            tvtSpeedNechet.setText(speedNechet);
        }

        @Override
        public void onClick(View view) {

            Intent i_nechet_redactor = new Intent(context, EditActivityNechet.class);
            i_nechet_redactor.putExtra(DBHelperNechet.LIST_ITEM_INTENT_NECHET, mainArrayNechet.get(getAdapterPosition()));
            i_nechet_redactor.putExtra(DBHelperNechet.EDIT_STATE_NECHET, false);
            context.startActivity(i_nechet_redactor);
        }
    }

    public void updateAdapterNechet(List<ListItemNechet> newListNechet){
        mainArrayNechet.clear();
        mainArrayNechet.addAll(newListNechet);
        notifyDataSetChanged();

    }

    public void removeItemNechet(int pos, MyDbManagerNechet dbManagerNechet) {
        dbManagerNechet.deleteNechet(mainArrayNechet.get(pos).getId_nechet());
        mainArrayNechet.removeItemAt(pos);
        notifyItemRangeChanged(0, mainArrayNechet.size());
        notifyItemRemoved(pos);
    }
}
