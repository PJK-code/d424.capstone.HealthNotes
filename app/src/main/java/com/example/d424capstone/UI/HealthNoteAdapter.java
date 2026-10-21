package com.example.d424capstone.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424capstone.R;
import com.example.d424capstone.entities.HealthNote;

import java.util.ArrayList;
import java.util.List;

// adapter for binding HealthNote objects to cards
public class HealthNoteAdapter extends RecyclerView.Adapter<HealthNoteAdapter.HealthNoteViewHolder> {

    private List<HealthNote> healthNotes = new ArrayList<>();
    private OnItemClickListener listener;

    // handling click events on note cards
    public interface OnItemClickListener {
        void onItemClick(HealthNote healthNote);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HealthNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_note, parent, false);
        return new HealthNoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthNoteViewHolder holder, int position) {
        HealthNote currentNote = healthNotes.get(position);
        holder.textTitle.setText(currentNote.getTitle());
        holder.textContent.setText(currentNote.getContent());
        holder.textType.setText(currentNote.getNoteType());
        holder.textLocation.setText(currentNote.getBodyLocation());
        holder.textDate.setText(currentNote.getDate());
    }

    @Override
    public int getItemCount() {
        return healthNotes.size();
    }

    // updates the list of health notes
    public void setHealthNotes(List<HealthNote> notes) {
        this.healthNotes = notes;
        notifyDataSetChanged();
    }

    // ViewHolder class holding UI views for each note card.
    class HealthNoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textContent, textType, textLocation, textDate;

        public HealthNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textNoteTitle);
            textContent = itemView.findViewById(R.id.textNoteContent);
            textType = itemView.findViewById(R.id.textNoteType);
            textLocation = itemView.findViewById(R.id.textBodyLocation);
            textDate = itemView.findViewById(R.id.textNoteDate);

            // handler for click event on card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(healthNotes.get(position));
                    }
                }
            });
        }
    }
}
