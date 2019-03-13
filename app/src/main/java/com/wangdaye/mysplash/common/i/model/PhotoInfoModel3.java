package com.wangdaye.mysplash.common.i.model;

import com.wangdaye.mysplash.common.data.entity.unsplash.Photo;
import com.wangdaye.mysplash.common.data.service.network.PhotoService;
import com.wangdaye.mysplash.common.ui.adapter.PhotoInfoAdapter3;

/**
 * Photo info model.
 *
 * Model for {@link com.wangdaye.mysplash.common.i.view.PhotoInfoView}.
 *
 * */

public interface PhotoInfoModel3 {

    PhotoService getPhotoService();
    PhotoInfoAdapter3 getAdapter();

    Photo getPhoto();
    void setPhoto(Photo p, boolean init);

    /** The flag to mark if loading photo details failed. */
    boolean isFailed();
    void setFailed(boolean b);
}
