package org.drulabs.pixelr.dto;

/**
 * Created by kaushald on 10/02/17.
 */

public class CommentDTO implements Comparable<CommentDTO> {

    private String commentId;
    private String artifactId;
    private String commenter;
    private String commenterPic;
    private String commenterId;
    private String text;
    private long timestamp;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommenterPic() {
        return commenterPic;
    }

    public void setCommenterPic(String commenterPic) {
        this.commenterPic = commenterPic;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentDTO commentDTO = (CommentDTO) o;

        return commentId.equals(commentDTO.commentId);

    }

    @Override
    public int hashCode() {
        return commentId.hashCode();
    }

    @Override
    public int compareTo(CommentDTO commentDTO) {
        if (this.timestamp > commentDTO.getTimestamp()) {
            return -1;
        } else if (this.timestamp < commentDTO.getTimestamp()) {
            return 1;
        } else {
            return 0;
        }
    }
}
