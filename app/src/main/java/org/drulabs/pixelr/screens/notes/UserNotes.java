package org.drulabs.pixelr.screens.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.NoteDTO;

import java.util.Date;

public class UserNotes extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView rvUserNotes;
    private EditText etUserNote;
    private FirebaseRecyclerAdapter adapter;
    // DatabaseReference notesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);
        // Initiate notes db ref here
        initializeUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening to real time database
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to real time database
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
        // create firebase adapter
        return null;
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
                //Push the note to database
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
