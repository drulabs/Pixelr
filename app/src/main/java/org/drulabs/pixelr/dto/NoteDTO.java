package org.drulabs.pixelr.dto;

/**
 * Created by kaushald on 30/10/17.
 */

public class NoteDTO {

    private String noteText;
    private String addedOn;
    private long timestamp;

    public NoteDTO() {
        //empty constructor required by forebase
    }

    public NoteDTO(String noteText, String addedOn, long timestamp) {
        this.noteText = noteText;
        this.addedOn = addedOn;
        this.timestamp = timestamp;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteDTO noteDTO = (NoteDTO) o;

        if (timestamp != noteDTO.timestamp) return false;
        if (noteText != null ? !noteText.equals(noteDTO.noteText) : noteDTO.noteText != null)
            return false;
        return addedOn != null ? addedOn.equals(noteDTO.addedOn) : noteDTO.addedOn == null;
    }

    @Override
    public int hashCode() {
        int result = noteText != null ? noteText.hashCode() : 0;
        result = 31 * result + (addedOn != null ? addedOn.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
