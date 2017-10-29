package org.drulabs.pixelr.screens.comment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.CommentDTO;
import org.drulabs.pixelr.screens.PresenterCreator;
import org.drulabs.pixelr.ui.NotificationToast;
import org.drulabs.pixelr.utils.Store;

import java.util.List;

public class CommentsActivity extends AppCompatActivity implements CommentContract.View, View
        .OnClickListener {

    public static final String KEY_TITLE = "title";
    public static final String KEY_ARTIFACT_ID = "artifactId";
    public static final String KEY_ARTIFACT_TYPE = "artifactType";

    private CommentContract.Presenter mPresenter;

    private RecyclerView recyclerView;
    private View loaderView;
    private View commentBase;
    private View fabSend;
    private EditText etComment;


    private CommentsAdapter commentsAdapter;

    private String artifactId;
    private String artifactType;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(KEY_TITLE) || !extras.containsKey
                (KEY_ARTIFACT_ID) || !extras.containsKey(KEY_ARTIFACT_TYPE)) {
            NotificationToast.showToast(this, getString(R.string.invalid_args_msg));
            this.finish();
            return;
        }

        artifactId = extras.getString(KEY_ARTIFACT_ID);
        title = extras.getString(KEY_TITLE);
        artifactType = extras.getString(KEY_ARTIFACT_TYPE);

        setToolBarTitle(title);

        initializeUI();

        PresenterCreator.createCommentsPresenter(this, this, artifactId, artifactType);
        mPresenter.start();
    }

    private void initializeUI() {

        loaderView = findViewById(R.id.comment_layout_loader);

        commentBase = findViewById(R.id.comment_layout_base);
        recyclerView = findViewById(R.id.comment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etComment = findViewById(R.id.txt_comment);
        fabSend = findViewById(R.id.fab_send);
        fabSend.setOnClickListener(this);

        commentsAdapter = new CommentsAdapter(this);
        recyclerView.setAdapter(commentsAdapter);

        loaderView.setVisibility(View.VISIBLE);
        commentBase.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void showLoading() {
        if (loaderView != null) {
            loaderView.setVisibility(View.VISIBLE);
            commentBase.setAlpha(0.3f);
        }
    }

    @Override
    public void hideLoading() {
        if (loaderView != null) {
            commentBase.setAlpha(1.0f);
            loaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadComments(List<CommentDTO> comments) {
        commentsAdapter.append(comments);

        if (commentBase != null) {
            commentBase.setVisibility(View.VISIBLE);
        }

        if (loaderView != null) {
            loaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadComment(CommentDTO comment) {
        commentsAdapter.append(comment);
        if (commentBase != null) {
            commentBase.setVisibility(View.VISIBLE);
        }

        if (loaderView != null) {
            loaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadError(String message) {
        NotificationToast.showToast(this, getString(R.string.no_comments_found));
        if (commentBase != null) {
            commentBase.setVisibility(View.VISIBLE);
        }

        if (loaderView != null) {
            loaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCommentSaved() {
        NotificationToast.showToast(this, getString(R.string.comment_saved_successfully));
        etComment.setText("");
    }

    void setToolBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_send:
                CommentDTO comment = new CommentDTO();
                comment.setArtifactId(artifactId);
                comment.setCommenterPic(Store.getInstance(this).getUserPicUrl());
                comment.setCommenter(Store.getInstance(this).getMyName());
                comment.setCommenterId(Store.getInstance(this).getMyKey());
                comment.setTimestamp(System.currentTimeMillis());
                comment.setText(etComment.getText().toString());
                mPresenter.addComment(comment);
                break;
            default:
                break;
        }
    }
}
