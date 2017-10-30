package org.drulabs.pixelr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.dto.PictureDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaushald on 15/02/17.
 */

public class DBHandler {

    private static DBHandler handler = null;

    private Context context;
    private SQLiteDatabase db;

    private DBHandler(@NonNull Context cxt) {
        this.context = cxt;
        ArtifactsDB picsDB = new ArtifactsDB(context);
        db = picsDB.getWritableDatabase();
    }

    public static DBHandler getHandle(Context cxt) {
        if (handler == null) {
            synchronized (DBHandler.class) {
                if (handler == null) {
                    handler = new DBHandler(cxt);
                }
            }
        }
        return handler;
    }

    public long addPic(String picKey, PictureDTO picture) {

        ContentValues cv = new ContentValues();
        cv.put(ArtifactsDB.PIC_KEY, picKey);
        cv.put(ArtifactsDB.PIC_COMMENTS_COUNT, picture.getCommentsCount());
        cv.put(ArtifactsDB.PIC_CREDIT, picture.getPhotoCredit());
        cv.put(ArtifactsDB.PIC_DATE, picture.getDateTaken());
        cv.put(ArtifactsDB.PIC_LIKES_COUNT, picture.getLikesCount());
        cv.put(ArtifactsDB.PIC_NAME, picture.getPicName());
        cv.put(ArtifactsDB.PIC_THUMB_URL, picture.getThumbURL());
        cv.put(ArtifactsDB.PIC_URL, picture.getPicURL());

        long rowId = 0;

        if (!picExists(picKey)) {
            rowId = db.insert(ArtifactsDB.TABLE_PICS, null, cv);
        } else {
            rowId = db.update(ArtifactsDB.TABLE_PICS, cv, ArtifactsDB.PIC_KEY + "=?",
                    new String[]{picKey});
        }
        return rowId;

    }

    public void addPic(@NonNull Map<String, PictureDTO> pictureMap) {
        for (Map.Entry<String, PictureDTO> row : pictureMap.entrySet()) {
            addPic(row.getKey(), row.getValue());
        }
    }

    public int setLikedForPic(@NonNull String picKey, boolean liked) {
        ContentValues values = new ContentValues();
        values.put(ArtifactsDB.PIC_IS_LIKED, liked);

        int updateCount = db.update(ArtifactsDB.TABLE_PICS, values, ArtifactsDB.PIC_KEY + "=?"
                , new String[]{picKey});
        return updateCount;
    }

    public void setLikedForPic(@NonNull Map<String, PictureDTO> picMap) {
        for (Map.Entry<String, PictureDTO> row : picMap.entrySet()) {
            setLikedForPic(row.getKey(), true);
        }
    }

    public synchronized void updateLikedArtifacts(List<LikeDTO> likeList) {
        for (LikeDTO like : likeList) {
            setLikedForPic(like.getArtifactId(), true);
        }
    }

    public synchronized boolean isPicLiked(String picKey) {

        Cursor cursor = null;

        try {
            cursor = db.query(ArtifactsDB.TABLE_PICS, new String[]{ArtifactsDB.PIC_IS_LIKED},
                    ArtifactsDB.PIC_KEY + "=?", new String[]{picKey}, null, null, null);

            boolean isLiked = false;

            if (cursor.moveToNext()) {
                isLiked = cursor.getInt(cursor.getColumnIndex(ArtifactsDB.PIC_IS_LIKED)) == 1;
            }

            return isLiked;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return false;

    }

    public Map<String, PictureDTO> getLikedPics() {
        Cursor cursor = null;

        try {
            cursor = db.query(ArtifactsDB.TABLE_PICS, null, ArtifactsDB.PIC_IS_LIKED + "=1", null,
                    null, null, ArtifactsDB.PIC_DATE + " DESC");

            int picKeyIndex = cursor.getColumnIndex(ArtifactsDB.PIC_KEY);
            int commentCountIndex = cursor.getColumnIndex(ArtifactsDB.PIC_COMMENTS_COUNT);
            int picCreditIndex = cursor.getColumnIndex(ArtifactsDB.PIC_CREDIT);
            int picDateIndex = cursor.getColumnIndex(ArtifactsDB.PIC_DATE);
            int likesCountIndex = cursor.getColumnIndex(ArtifactsDB.PIC_LIKES_COUNT);
            int picNameIndex = cursor.getColumnIndex(ArtifactsDB.PIC_NAME);
            int picUrlIndex = cursor.getColumnIndex(ArtifactsDB.PIC_URL);
            int thumbIndex = cursor.getColumnIndex(ArtifactsDB.PIC_THUMB_URL);

            Map<String, PictureDTO> pictureMap = new HashMap<>();

            while (cursor.moveToNext()) {

                String picKey = cursor.getString(picKeyIndex);
                int commentsCount = cursor.getInt(commentCountIndex);
                String picCredit = cursor.getString(picCreditIndex);
                long date = cursor.getLong(picDateIndex);
                int likesCount = cursor.getInt(likesCountIndex);
                String picName = cursor.getString(picNameIndex);
                String picUrl = cursor.getString(picUrlIndex);
                String thumbUrl = cursor.getString(thumbIndex);

                PictureDTO soloPic = new PictureDTO();
                soloPic.setLikesCount(likesCount);
                soloPic.setPicName(picName);
                soloPic.setPhotoCredit(picCredit);
                soloPic.setCommentsCount(commentsCount);
                soloPic.setDateTaken(date);
                soloPic.setPicURL(picUrl);
                soloPic.setThumbURL(thumbUrl);

                pictureMap.put(picKey, soloPic);
            }

            return pictureMap;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }

    public Map<String, PictureDTO> getAllPics() {
        Cursor cursor = null;

        try {
            cursor = db.query(ArtifactsDB.TABLE_PICS, null, null, null, null, null, ArtifactsDB
                    .PIC_DATE + " DESC");

            int picKeyIndex = cursor.getColumnIndex(ArtifactsDB.PIC_KEY);
            int commentCountIndex = cursor.getColumnIndex(ArtifactsDB.PIC_COMMENTS_COUNT);
            int picCreditIndex = cursor.getColumnIndex(ArtifactsDB.PIC_CREDIT);
            int picDateIndex = cursor.getColumnIndex(ArtifactsDB.PIC_DATE);
            int likesCountIndex = cursor.getColumnIndex(ArtifactsDB.PIC_LIKES_COUNT);
            int picNameIndex = cursor.getColumnIndex(ArtifactsDB.PIC_NAME);
            int picUrlIndex = cursor.getColumnIndex(ArtifactsDB.PIC_URL);
            int thumbIndex = cursor.getColumnIndex(ArtifactsDB.PIC_THUMB_URL);

            Map<String, PictureDTO> pictureMap = new HashMap<>();

            while (cursor.moveToNext()) {

                String picKey = cursor.getString(picKeyIndex);
                int commentsCount = cursor.getInt(commentCountIndex);
                String picCredit = cursor.getString(picCreditIndex);
                long date = cursor.getLong(picDateIndex);
                int likesCount = cursor.getInt(likesCountIndex);
                String picName = cursor.getString(picNameIndex);
                String picUrl = cursor.getString(picUrlIndex);
                String thumbUrl = cursor.getString(thumbIndex);

                PictureDTO soloPic = new PictureDTO();
                soloPic.setLikesCount(likesCount);
                soloPic.setPicName(picName);
                soloPic.setPhotoCredit(picCredit);
                soloPic.setCommentsCount(commentsCount);
                soloPic.setDateTaken(date);
                soloPic.setPicURL(picUrl);
                soloPic.setThumbURL(thumbUrl);

                pictureMap.put(picKey, soloPic);
            }

            return pictureMap;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }

    public PictureDTO getPic(String picKey) {
        Cursor cursor = null;

        try {
            cursor = db.query(ArtifactsDB.TABLE_PICS, null, ArtifactsDB.PIC_KEY + "=?",
                    new String[]{picKey}, null, null, ArtifactsDB.PIC_DATE + " DESC");

            int picKeyIndex = cursor.getColumnIndex(ArtifactsDB.PIC_KEY);
            int commentCountIndex = cursor.getColumnIndex(ArtifactsDB.PIC_COMMENTS_COUNT);
            int picCreditIndex = cursor.getColumnIndex(ArtifactsDB.PIC_CREDIT);
            int picDateIndex = cursor.getColumnIndex(ArtifactsDB.PIC_DATE);
            int likesCountIndex = cursor.getColumnIndex(ArtifactsDB.PIC_LIKES_COUNT);
            int picNameIndex = cursor.getColumnIndex(ArtifactsDB.PIC_NAME);
            int picUrlIndex = cursor.getColumnIndex(ArtifactsDB.PIC_URL);
            int thumbIndex = cursor.getColumnIndex(ArtifactsDB.PIC_THUMB_URL);

            PictureDTO soloPic = null;

            if (cursor.moveToNext()) {
                int commentsCount = cursor.getInt(commentCountIndex);
                String picCredit = cursor.getString(picCreditIndex);
                long date = cursor.getLong(picDateIndex);
                int likesCount = cursor.getInt(likesCountIndex);
                String picName = cursor.getString(picNameIndex);
                String picUrl = cursor.getString(picUrlIndex);
                String thumbUrl = cursor.getString(thumbIndex);

                soloPic = new PictureDTO();
                soloPic.setLikesCount(likesCount);
                soloPic.setPicName(picName);
                soloPic.setPhotoCredit(picCredit);
                soloPic.setCommentsCount(commentsCount);
                soloPic.setDateTaken(date);
                soloPic.setPicURL(picUrl);
                soloPic.setThumbURL(thumbUrl);
            }

            return soloPic;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }


    public boolean picExists(String picKey) {
        boolean exists = false;

        Cursor cursor = db.query(ArtifactsDB.TABLE_PICS, null, ArtifactsDB.PIC_KEY + "=?", new
                String[]{picKey}, null, null, ArtifactsDB.PIC_NAME);

        exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

}
