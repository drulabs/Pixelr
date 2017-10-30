package org.drulabs.pixelr.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.drulabs.pixelr.config.Constants;
import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.utils.Store;

import java.util.LinkedList;
import java.util.List;

/**
 * This handler is only responsible for updating the Likes database. Individual artifacts should
 * update likes count by them selves
 * Created by kaushald on 23/02/17.
 */
public class LikesHandler {

    private Context mContext;
    private Callback mListener;
    private String artifactId;

    private boolean hasMoreItems = true;

    private Store store;

    // Firebase variables
    private DatabaseReference likesDB;

    public LikesHandler(@NonNull Context context, Callback mListener, @NonNull String
            artifactId) {
        this.mContext = context;
        this.mListener = mListener;
        this.artifactId = artifactId;

        store = Store.getInstance(mContext);

        likesDB = FirebaseDatabase.getInstance().getReference().child(Constants.LIKES_DB);
    }

    static void fetchLikedArtifacts(Context context, final IArtifactsCallback callback) {
        DatabaseReference likesDB = FirebaseDatabase.getInstance().getReference().child(Constants
                .LIKES_DB);
        Query likedArtifactsQuery = likesDB.orderByChild(Constants.LIKES_LIKEDBY_ID).equalTo
                (Store.getInstance(context).getMyKey());
        likedArtifactsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.hasChildren()) {

                    List<LikeDTO> likesList = new LinkedList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LikeDTO like = snapshot.getValue(LikeDTO.class);
                        likesList.add(like);
                    }

                    callback.onArtifactsFetched(likesList);

                } else {
                    callback.onError();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError();
            }
        });
    }

    public void fetchLikes() {
        if (hasMoreItems && mListener != null) {
            Query likesQuery = likesDB.orderByChild(Constants.LIKES_ARTIFACT_ID).equalTo
                    (artifactId);
            likesQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        LikeDTO singleLike = dataSnapshot.getValue(LikeDTO.class);
                        mListener.onLikeFetched(singleLike);
                    } else {
                        mListener.onNoMoreLikes();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mListener.onNoMoreLikes();
                }
            });
        }
    }

    public void fetchPic(String picKey) {
        FirebaseDatabase.getInstance().getReference().child(Constants.PICS_DB).child(picKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot != null) {
                            PictureDTO pic = snapshot.getValue(PictureDTO.class);
                            pic.setPicURL(Constants.PICS_DB + "/" + pic.getPicName() + ".jpg");
                            pic.setThumbURL(Constants.PICS_DB + "/" + pic.getPicName() + "_thumb" +
                                    ".jpg");
                            mListener.onPicFetched(snapshot.getKey(), pic);
                        } else {
                            mListener.onError();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mListener.onError();
                    }
                });
    }

    void updateLikeStatus(final boolean isLiked, long fireDate) {

        String likesKey = artifactId + "_" + store.getMyKey();

        if (isLiked) {

            LikeDTO likeObj = new LikeDTO();
            likeObj.setArtifactId(artifactId);
            likeObj.setLikedById(store.getMyKey());
            likeObj.setLikesUniqueIdentifier(likesKey);
            likeObj.setLikedByName(store.getMyName());
            likeObj.setLikerPic(store.getUserPicUrl());
            likeObj.setLikedOn(fireDate);

            likesDB.child(likesKey).setValue(likeObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (mListener != null) {
                        mListener.onLikeUpdated(isLiked, true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (mListener != null) {
                        mListener.onLikeUpdated(isLiked, false);
                    }
                }
            });
        } else {
            likesDB.child(likesKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (mListener != null) {
                        mListener.onLikeUpdated(isLiked, true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (mListener != null) {
                        mListener.onLikeUpdated(isLiked, false);
                    }
                }
            });
        }
    }

    public interface Callback {
        void onLikeUpdated(boolean isLiked, boolean isSuccess);

        void onLikeFetched(LikeDTO like);

        void onNoMoreLikes();
        //TODO implement pagination for likes

        void onPicFetched(String key, PictureDTO picture);

        void onError();
    }

    public interface IArtifactsCallback {
        void onArtifactsFetched(List<LikeDTO> myLikes);

        void onError();
    }

}
