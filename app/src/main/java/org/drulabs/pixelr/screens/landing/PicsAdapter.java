package org.drulabs.pixelr.screens.landing;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.db.DBHandler;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.firebase.FirebaseImageHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaushald on 05/02/17.
 */
public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.PicsVH> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd, yyyy " +
            "(HH:mm)");
    private List<PictureDTO> photos;
    private Map<String, PictureDTO> photoMap;
    private PicsContract.Presenter mPresenter;
    private Context mContext;
    private DBHandler dbHandler;

    public PicsAdapter(Context context, PicsContract.Presenter picsPresenter) {
        this.photos = new ArrayList<>();
        this.photoMap = new HashMap<>();
        this.mContext = context;
        this.mPresenter = picsPresenter;
        this.dbHandler = DBHandler.getHandle(mContext);
    }

    @Override
    public PicsVH onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View picView = LayoutInflater.from(context).inflate(R.layout
                .item_layout, parent, false);
        return new PicsVH(picView);
    }

    @Override
    public void onBindViewHolder(PicsVH holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    public void append(HashMap<String, PictureDTO> photos) {
        if (this.photos != null && this.photos.size() >= 0 && photos != null) {
            photoMap.putAll(photos);
            this.photos.addAll(getValueListFromMap(photos));
            this.notifyDataSetChanged();
        }
    }

    public void append(String key, PictureDTO photo) {
        if (photos != null && photos.size() >= 0 && photo != null) {
            this.photos.add(photo);
            this.photoMap.put(key, photo);
            this.notifyDataSetChanged();
            dbHandler.addPic(key, photo);
        }
    }

    public void reset() {
        this.photos.clear();
        this.photoMap.clear();
        notifyDataSetChanged();
    }

    private List<PictureDTO> getValueListFromMap(Map<String, PictureDTO> picMap) {

        List<PictureDTO> localPics = new ArrayList<>();

        for (Map.Entry<String, PictureDTO> map : picMap.entrySet()) {
            PictureDTO singlePic = map.getValue();
            if (!photos.contains(singlePic) && !localPics.contains(singlePic)) {
                localPics.add(singlePic);
            }
        }

        Collections.sort(localPics);

        return localPics;
    }

    @Nullable
    private String getKeyForPicture(PictureDTO picture) {
        if (photoMap == null || !photoMap.containsValue(picture)) {
            return null;
        }

        for (Map.Entry<String, PictureDTO> map : photoMap.entrySet()) {
            if (map.getValue() == picture) {
                return map.getKey();
            }
        }

        return null;
    }

    class PicsVH extends RecyclerView.ViewHolder {

        ImageView image;
        TextView likes;
        TextView comments;
        TextView share;
        TextView download;
        TextView imageDetails;
        private boolean isLiked = false;

        public PicsVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            imageDetails = itemView.findViewById(R.id.txt_image_credit);
            likes = itemView.findViewById(R.id.txt_like_count);
            comments = itemView.findViewById(R.id.txt_comment_count);
            share = itemView.findViewById(R.id.txt_share_btn);
            download = itemView.findViewById(R.id.txt_download_btn);
        }

        public void bind(int index) {

            final PictureDTO pic = photos.get(index);
            final String key = getKeyForPicture(pic);

            likes.setText(String.valueOf(pic.getLikesCount()));
            comments.setText(String.valueOf(pic.getCommentsCount()));
            imageDetails.setText(pic.getPhotoCredit() + " (" + dateFormat.format(new Date(pic
                    .getDateTaken())) + ")");

            FirebaseImageHelper.loadFBStorageImageIn(mContext, pic.getPicURL(), image);

            image.setOnClickListener(view -> mPresenter.onPicClicked(key, pic));

            isLiked = dbHandler.isPicLiked(key);
            likes.setCompoundDrawablesWithIntrinsicBounds(isLiked ? R.mipmap.ic_like : R.mipmap
                    .ic_unlike, 0, 0, 0);

            likes.setOnClickListener(view -> {
                isLiked = !isLiked;
                likes.setCompoundDrawablesWithIntrinsicBounds(isLiked ? R.mipmap.ic_like : R.mipmap
                        .ic_unlike, 0, 0, 0);

                if (isLiked) {
                    pic.setLikesCount(pic.getLikesCount() + 1);

                    likes.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_like, 0, 0, 0);
                } else {
                    pic.setLikesCount(pic.getLikesCount() - 1);
                    likes.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_unlike, 0, 0, 0);
                }
                mPresenter.onLikeClicked(key, pic, isLiked);
                likes.setText(String.valueOf(pic.getLikesCount()));
            });

            comments.setOnClickListener(view -> mPresenter.onCommentsClicked(key, pic));

            share.setOnClickListener(view -> mPresenter.onShareClicked(key, pic));

            download.setOnClickListener(view -> mPresenter.onDownloadClicked(key, pic));
        }
    }
}
