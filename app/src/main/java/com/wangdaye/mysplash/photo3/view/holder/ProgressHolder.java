package com.wangdaye.mysplash.photo3.view.holder;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.wangdaye.mysplash.Mysplash;
import com.wangdaye.mysplash.R;
import com.wangdaye.mysplash.common.basic.activity.MysplashActivity;
import com.wangdaye.mysplash.common.data.entity.unsplash.Photo;
import com.wangdaye.mysplash.common.ui.adapter.PhotoInfoAdapter3;
import com.wangdaye.mysplash.common.utils.AnimUtils;
import com.wangdaye.mysplash.photo3.view.activity.PhotoActivity3;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Progress holder.
 *
 * This view holder is used to show the progress.
 *
 * */

public class ProgressHolder extends PhotoInfoAdapter3.ViewHolder {

    @BindView(R.id.item_photo_3_progress_container)
    RelativeLayout container;

    @BindView(R.id.item_photo_3_progress_progressView)
    CircularProgressView progress;

    @BindView(R.id.item_photo_3_progress_button)
    Button button;

    private boolean failed;
    public static final int TYPE_PROGRESS = 1;

    public ProgressHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void onBindView(PhotoActivity3 a, Photo photo) {
        failed = a.isLoadFailed();
        if (failed) {
            progress.setAlpha(0f);
            button.setAlpha(1f);
            button.setVisibility(View.VISIBLE);
        } else {
            progress.setAlpha(1f);
            button.setAlpha(0f);
            button.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRecycled() {
        // do nothing.
    }

    public void setFailedState() {
        if (!failed) {
            failed = true;
            AnimUtils.animShow(button, 150, button.getAlpha(), 1f);
            AnimUtils.animHide(progress, 150, progress.getAlpha(), 0f, false);
        }
    }

    private void setProgressState() {
        if (failed) {
            failed = false;
            AnimUtils.animShow(progress, 150, progress.getAlpha(), 1f);
            AnimUtils.animHide(button, 150, button.getAlpha(), 0f, true);
        }
    }

    @OnClick(R.id.item_photo_3_progress_button) void retryRefresh() {
        MysplashActivity activity =  Mysplash.getInstance().getTopActivity();
        if (activity instanceof PhotoActivity3) {
            setProgressState();
            ((PhotoActivity3) activity).initRefresh();
        }
    }
}
