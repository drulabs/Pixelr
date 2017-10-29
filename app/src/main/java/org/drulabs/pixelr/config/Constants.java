package org.drulabs.pixelr.config;

/**
 * Created by kaushald on 22/01/17.
 */

public interface Constants {

    String FILE_PROVIDER_AUTHORITY = "org.drulabs.pixelr.provider";

    // google constants
    String GOOGLE_CLIENT_ID = "560496245142-j6rabe001bm25r159m1ifft22iuc34lo.apps.googleusercontent.com";

    String IMAGE_BUCKET = "pixelr-gdgpune.appspot.com";

    // Image processing related
    String PICS_DB = "photos";
    String PICS_DATE_TAKEN = "dateTaken";

    String COMMENTS_DB = "comments";
    String COMMENTS_TIMESTAMP = "timestamp";
    String COMMENTS_ARTIFACT_ID = "artifactId";
    String KEY_COMMENTS_COUNT = "commentsCount";

    String LIKES_DB = "likes";
    String LIKES_ARTIFACT_ID = "artifactId";
    String LIKES_LIKEDBY_ID = "likedById";
    String LIKES_LIKEDBY_NAME = "likedByName";
    String LIKES_LIKEDBY_PIC = "likerPic";
    String LIKES_LIKEDBY_DATE = "likedOn";
    String LIKES_ARTIFACT_UNIQUEID = "likesUniqueIdentifier";
    int THUMB_SIZE = 144;
}
