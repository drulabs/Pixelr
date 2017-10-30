package org.drulabs.pixelr.screens.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.NoteDTO;
import org.drulabs.pixelr.utils.Store;

import java.util.Date;

public class UserNotes extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference notesRef;
    private RecyclerView rvUserNotes;
    private EditText etUserNote;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        notesRef = FirebaseDatabase.getInstance().getReference().child("users").child(Store
                .getInstance(this).getMyKey()).child("notes");
        initializeUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private void initializeUI() {
        rvUserNotes = findViewById(R.id.notes_list);
        etUserNote = findViewById(R.id.txt_user_note);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_note);
        fabAdd.setOnClickListener(this);

        rvUserNotes.setLayoutManager(new LinearLayoutManager(UserNotes.this));
        adapter = createFirebaseUiAdapter();
        rvUserNotes.setAdapter(adapter);
    }

    FirebaseRecyclerAdapter createFirebaseUiAdapter() {
        Query query = notesRef.limitToLast(20);
        FirebaseRecyclerOptions<NoteDTO> options = new FirebaseRecyclerOptions.Builder<NoteDTO>()
                .setQuery(query, NoteDTO.class)
                .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<NoteDTO, NoteHolder>(options) {

            @Override
            protected void onBindViewHolder(NoteHolder holder, int position, NoteDTO model) {
                holder.bind(model);
            }

            @Override
            public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,
                        parent, false);
                return new NoteHolder(view);
            }
        };

        return adapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_note:

                String noteText = etUserNote.getText().toString();
                if (noteText.isEmpty()) {
                    return;
                }

                NoteDTO noteDTO = new NoteDTO();
                noteDTO.setNoteText(noteText);
                noteDTO.setTimestamp(System.currentTimeMillis());
                noteDTO.setAddedOn((new Date()).toString());
                notesRef.push().setValue(noteDTO).addOnSuccessListener(aVoid -> etUserNote
                        .setText(""));

                break;
            default:
                break;
        }
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView txtNote;

        NoteHolder(View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.note_text);
        }

        void bind(NoteDTO note) {
            txtNote.setText(note.getNoteText());
        }
    }
}
