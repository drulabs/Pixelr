package org.drulabs.pixelr.firebase;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.config.Constants;
import org.drulabs.pixelr.dto.PictureDTO;

/**
 * Created by kaushald on 05/02/17.
 */

public class FirebaseImageHelper {

    private static final String TAG = "ImageHelper";

    public static void loadFBStorageImageIn(Context context, StorageReference storageRef, ImageView img) {
        // Load the image using Glide
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.ic_face_black_800dp)
                .error(R.mipmap.ic_launcher)
                .animate(R.anim.fade_in)
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, StorageReference model,
                                               Target<GlideDrawable> target, boolean
                                                       isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, StorageReference
                            model, Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        Log.d(TAG, "Image downloaded");
                        return false;
                    }
                })
                .into(img);
    }

    public static void loadFBStorageImageIn(Context context, String imageURL, ImageView img) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(("gs://"
                + Constants.IMAGE_BUCKET)).child(imageURL);

        loadFBStorageImageIn(context, storageRef, img);
    }

    public static void loadImageFromUrlInto(Context context, String imageUri, ImageView img) {
        Glide.with(context)
                .load(imageUri.toString())
                .error(R.drawable.ic_face_black_800dp)
                .placeholder(R.drawable.ic_face_black_800dp)
                .animate(R.anim.fade_in)
                .into(img);
    }

    public static void loadLatestImageIn(final Context context, final ImageView img, final
    DownloadURIListener listener) {
        Query picsQuery = FirebaseDatabase.getInstance().getReference().child(Constants
                .PICS_DB).orderByChild(Constants.PICS_DATE_TAKEN).limitToLast(1);
        picsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                    PictureDTO pic = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        pic = snapshot.getValue(PictureDTO.class);
                        pic.setPicURL(Constants.PICS_DB + "/" + pic.getPicName() + ".jpg");
                        pic.setThumbURL(Constants.PICS_DB + "/" + pic.getPicName() + "_thumb.jpg");
                        break;
                    }

                    if (pic != null) {
                        //loadFBStorageImageIn(context, pic.getPicURL(), img);
                        getDownloadURI(pic, listener);
                    } else {
                        img.setImageResource(R.drawable.ic_face_black_800dp);
                    }

                } else {
                    img.setImageResource(R.drawable.ic_face_black_800dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ImageHelper", "latest image fetch error: " + databaseError.toString());
                img.setImageResource(R.drawable.ic_face_black_800dp);
            }
        });
    }

    public static void getDownloadURI(PictureDTO picture, final DownloadURIListener listener) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(("gs://"
                + Constants.IMAGE_BUCKET)).child(picture.getPicURL());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUri) {
                listener.onDownloadURIFetched(downloadUri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e);
            }
        });

    }

    public interface DownloadURIListener {
        void onDownloadURIFetched(Uri downloadUri);

        void onError(Exception e);
    }

}
