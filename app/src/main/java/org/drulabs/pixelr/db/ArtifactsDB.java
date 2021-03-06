package org.drulabs.pixelr.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaushald on 14/02/17.
 */

class ArtifactsDB extends SQLiteOpenHelper {

    static final String TABLE_PICS = "pictures";
    static final String PIC_ID = "pic_id";
    static final String PIC_KEY = "pic_key";
    static final String PIC_NAME = "pic_name";
    static final String PIC_DATE = "pic_date";
    static final String PIC_COMMENTS_COUNT = "comments_count";
    static final String PIC_LIKES_COUNT = "likes_count";
    static final String PIC_CREDIT = "pic_credit";
    static final String PIC_RELATIONSHIP = "relation_with_photographer";
    static final String PIC_URL = "pic_url";
    static final String PIC_THUMB_URL = "thumb_url";
    static final String PIC_IS_LIKED = "is_liked";
    private static final int DB_VERSION_1 = 1;
    private static final int DB_VERSION_2 = 2;
    private static final String DB_NAME = "pics.db";
    private static final String CREATE_PIC_TABLE = "CREATE TABLE " + TABLE_PICS
            + " (" + PIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PIC_KEY + " TEXT NOT NULL, "
            + PIC_NAME + " TEXT NOT NULL, "
            + PIC_DATE + " INTEGER DEFAULT 0, "
            + PIC_COMMENTS_COUNT + " INTEGER DEFAULT 0, "
            + PIC_LIKES_COUNT + " INTEGER DEFAULT 0, "
            + PIC_CREDIT + " TEXT NOT NULL, "
            + PIC_URL + " TEXT, "
            + PIC_THUMB_URL + " TEXT, "
            + PIC_IS_LIKED + " INTEGER DEFAULT 0 "
            + ");";

    public ArtifactsDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION_1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case DB_VERSION_1:
                // code for upgrading DB from v1 to v2
                break;
            case DB_VERSION_2:
                // code for upgrading DB from v2 to v3
                break;
            default:
                break;
        }

    }
}
