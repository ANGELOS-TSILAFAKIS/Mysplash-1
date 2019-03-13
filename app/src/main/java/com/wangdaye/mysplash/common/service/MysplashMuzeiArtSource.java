package com.wangdaye.mysplash.common.service;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import com.wangdaye.mysplash.R;
import com.wangdaye.mysplash.common.data.entity.unsplash.Photo;
import com.wangdaye.mysplash.common.utils.helper.MuzeiUpdateHelper;
import com.wangdaye.mysplash.common.utils.manager.MuzeiOptionManager;
import com.wangdaye.mysplash.photo3.view.activity.PhotoActivity3;

import java.util.ArrayList;
import java.util.List;

/**
 * Muzei source photoService.
 *
 * This photoService is used to provide source of wallpapers for Muzei.
 *
 * */

public class MysplashMuzeiArtSource extends RemoteMuzeiArtSource
        implements MuzeiUpdateHelper.OnUpdateCallback {

    private static final String SOURCE_NAME = "Mysplash";

    private static final long HOUR = 60 * 60 * 1000;
    private static final long FIFTEEN_MINUTES = 15 * 60 * 1000;

    public MysplashMuzeiArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    protected void onTryUpdate(int reason) {
        if (MuzeiOptionManager.getInstance(this).isUpdateOnlyInWifi()
                && !MuzeiUpdateHelper.isWifi(this)) {
            return;
        }
        MuzeiUpdateHelper.update(this, this);
    }

    private void publishPhoto(@NonNull Photo photo) {
        Intent intent = new Intent(this, PhotoActivity3.class);
        intent.putExtra(PhotoActivity3.KEY_PHOTO_ACTIVITY_2_ID, photo.id);

        publishArtwork(
                new Artwork.Builder()
                        .title(getString(R.string.by) + " " + photo.user.name)
                        .byline(getString(R.string.on) + " " + photo.created_at.split("T")[0])
                        .imageUri(Uri.parse(photo.getWallpaperSizeUrl(this)))
                        .token(photo.id)
                        .viewIntent(intent)
                        .build());

        List<UserCommand> commands = new ArrayList<>();
        commands.add(new UserCommand(BUILTIN_COMMAND_ID_NEXT_ARTWORK));
        setUserCommands(commands);
    }

    // interface.

    @Override
    public void onUpdateSucceed(@NonNull List<Photo> photoList) {
        Artwork art = getCurrentArtwork();
        String lastPhotoId = art == null ? null : art.getToken();
        for (Photo photo : photoList) {
            if (!photo.id.equals(lastPhotoId)) {
                publishPhoto(photo);
                scheduleUpdate(System.currentTimeMillis()
                        + MuzeiOptionManager.getInstance(this).getUpdateInterval() * HOUR);
                return;
            }
        }
        onUpdateFailed();
    }

    @Override
    public void onUpdateFailed() {
        scheduleUpdate(System.currentTimeMillis() + FIFTEEN_MINUTES);
    }
}