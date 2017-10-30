package org.drulabs.pixelr.dto;

import java.io.Serializable;

/**
 * Created by kaushald on 05/02/17.
 */

public class PictureDTO implements Comparable<PictureDTO>, Serializable {

    private String picName;
    private long dateTaken;
    private int commentsCount;
    private int likesCount;
    private String photoCredit;
    private String picURL;
    private String uploaderId;
    private String thumbURL;

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getPhotoCredit() {
        return photoCredit;
    }

    public void setPhotoCredit(String photoCredit) {
        this.photoCredit = photoCredit;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PictureDTO that = (PictureDTO) o;

        if (dateTaken != that.dateTaken) return false;
        if (commentsCount != that.commentsCount) return false;
        if (likesCount != that.likesCount) return false;
        if (!picName.equals(that.picName)) return false;
        if (photoCredit != null ? !photoCredit.equals(that.photoCredit) : that.photoCredit != null)
            return false;
        if (!picURL.equals(that.picURL)) return false;
        return thumbURL != null ? thumbURL.equals(that.thumbURL) : that.thumbURL == null;
    }

    @Override
    public int hashCode() {
        int result = picName.hashCode();
        result = 31 * result + (int) (dateTaken ^ (dateTaken >>> 32));
        result = 31 * result + commentsCount;
        result = 31 * result + likesCount;
        result = 31 * result + (photoCredit != null ? photoCredit.hashCode() : 0);
        result = 31 * result + picURL.hashCode();
        result = 31 * result + (thumbURL != null ? thumbURL.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(PictureDTO pictureDTO) {
        if (dateTaken > pictureDTO.getDateTaken()) {
            return -1;
        } else if (dateTaken < pictureDTO.getDateTaken()) {
            return 1;
        }
        return 0;
    }
}
