package com.wangdaye.mysplash.common.data.service.network;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.gson.GsonBuilder;
import com.wangdaye.mysplash.Mysplash;
import com.wangdaye.mysplash.common.basic.TLSCompactService;
import com.wangdaye.mysplash.common.data.api.PhotoApi;
import com.wangdaye.mysplash.common.data.entity.unsplash.LikePhotoResult;
import com.wangdaye.mysplash.common.data.entity.unsplash.Photo;
import com.wangdaye.mysplash.common.data.entity.unsplash.PhotoStats;
import com.wangdaye.mysplash.common.utils.widget.interceptor.AuthInterceptor;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Photo service.
 * */

public class PhotoService extends TLSCompactService {

    private Call call;
    private PhotoNodeService nodeService;

    private PhotoService() {
        call = null;
        nodeService = PhotoNodeService.getService();
    }

    public static PhotoService getService() {
        return new PhotoService();
    }

    private OkHttpClient buildClient() {
        return getClientBuilder()
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    private PhotoApi buildApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Mysplash.UNSPLASH_API_BASE_URL)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setDateFormat(Mysplash.DATE_FORMAT)
                                        .create()))
                .build()
                .create((PhotoApi.class));
    }

    public void requestPhotos(@Mysplash.PageRule int page,
                              @Mysplash.PerPageRule int per_page,
                              String order_by,
                              final OnRequestPhotosListener l) {
        Call<List<Photo>> getPhotos = buildApi(buildClient()).getPhotos(page, per_page, order_by);
        getPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getPhotos;
    }

    public void requestCuratePhotos(@Mysplash.PageRule int page,
                                    @Mysplash.PerPageRule int per_page,
                                    String order_by,
                                    final OnRequestPhotosListener l) {
        Call<List<Photo>> getCuratePhotos = buildApi(buildClient()).getCuratedPhotos(page, per_page, order_by);
        getCuratePhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getCuratePhotos;
    }

    public void setLikeForAPhoto(String id, boolean like, final OnSetLikeListener l) {
        Call<LikePhotoResult> setLikeForAPhoto = like ?
                buildApi(buildClient()).likeAPhoto(id) : buildApi(buildClient()).unlikeAPhoto(id);
        setLikeForAPhoto.enqueue(new Callback<LikePhotoResult>() {
            @Override
            public void onResponse(Call<LikePhotoResult> call, Response<LikePhotoResult> response) {
                if (l != null) {
                    l.onSetLikeSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<LikePhotoResult> call, Throwable t) {
                if (l != null) {
                    l.onSetLikeFailed(call, t);
                }
            }
        });
        call = setLikeForAPhoto;
    }

    public void requestAPhoto(String id, final OnRequestSinglePhotoListener l) {
        if (nodeService == null) {
            Call<Photo> getAPhoto = buildApi(buildClient()).getAPhoto(id);
            getAPhoto.enqueue(new Callback<Photo>() {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response) {
                    if (l != null) {
                        l.onRequestSinglePhotoSuccess(call, response);
                    }
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
                    if (l != null) {
                        l.onRequestSinglePhotoFailed(call, t);
                    }
                }
            });
        } else {
            nodeService.requestAPhoto(id, l);
        }
    }

    public void requestUserPhotos(String username,
                                  @Mysplash.PageRule int page,
                                  @Mysplash.PerPageRule int per_page,
                                  String order_by,
                                  final OnRequestPhotosListener l) {
        Call<List<Photo>> getUserPhotos = buildApi(buildClient())
                .getUserPhotos(username, page, per_page, order_by);
        getUserPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getUserPhotos;
    }

    public void requestUserLikes(String username,
                                 @Mysplash.PageRule int page,
                                 @Mysplash.PerPageRule int per_page,
                                 String order_by, final
                                 OnRequestPhotosListener l) {
        Call<List<Photo>> getUserLikes = buildApi(buildClient())
                .getUserLikes(username, page, per_page, order_by);
        getUserLikes.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getUserLikes;
    }

    public void requestCollectionPhotos(int collectionId,
                                        @Mysplash.PageRule int page,
                                        @Mysplash.PerPageRule int per_page,
                                        final OnRequestPhotosListener l) {
        Call<List<Photo>> getCollectionPhotos = buildApi(buildClient())
                .getCollectionPhotos(collectionId, page, per_page);
        getCollectionPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getCollectionPhotos;
    }

    public void requestCuratedCollectionPhotos(int collectionId,
                                               @Mysplash.PageRule int page,
                                               @Mysplash.PerPageRule int per_page,
                                               final OnRequestPhotosListener l) {
        Call<List<Photo>> getCuratedCollectionPhotos = buildApi(buildClient())
                .getCuratedCollectionPhotos(collectionId, page, per_page);
        getCuratedCollectionPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getCuratedCollectionPhotos;
    }

    public void requestRandomPhotos(List<Integer> collectionIdList,
                                    Boolean featured, String username, String query, String orientation,
                                    final OnRequestPhotosListener l) {
        StringBuilder collections = new StringBuilder();
        if (collectionIdList != null && collectionIdList.size() > 0) {
            collections.append(collectionIdList.get(0));
        }
        for (int i = 1; collectionIdList != null && i < collectionIdList.size(); i ++) {
            collections.append(",").append(collectionIdList.get(i));
        }

        Call<List<Photo>> getRandomPhotos = buildApi(buildClient()).getRandomPhotos(
                collections.toString(), featured, username, query, orientation, Mysplash.DEFAULT_PER_PAGE);
        getRandomPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (l != null) {
                    l.onRequestPhotosSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                if (l != null) {
                    l.onRequestPhotosFailed(call, t);
                }
            }
        });
        call = getRandomPhotos;
    }

    @WorkerThread
    @Nullable
    public List<Photo> requestRandomPhotos(@Nullable List<Integer> collectionIdList,
                                           Boolean featured,
                                           String username, String query, String orientation) {
        StringBuilder collections = new StringBuilder();
        if (collectionIdList != null && collectionIdList.size() > 0) {
            collections.append(collectionIdList.get(0));
        }
        for (int i = 1; collectionIdList != null && i < collectionIdList.size(); i ++) {
            collections.append(",").append(collectionIdList.get(i));
        }

        Call<List<Photo>> getRandomPhotos = buildApi(buildClient()).getRandomPhotos(
                collections.toString(), featured, username, query, orientation, Mysplash.DEFAULT_PER_PAGE);
        try {
            Response<List<Photo>> response = getRandomPhotos.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadPhoto(String url) {
        buildApi(buildClient()).downloadPhoto(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // do nothing.
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // do nothing.
            }
        });
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

    // interface.

    public interface OnRequestPhotosListener {
        void onRequestPhotosSuccess(Call<List<Photo>> call, retrofit2.Response<List<Photo>> response);
        void onRequestPhotosFailed(Call<List<Photo>> call, Throwable t);
    }

    public interface OnRequestSinglePhotoListener {
        void onRequestSinglePhotoSuccess(Call<Photo> call, retrofit2.Response<Photo> response);
        void onRequestSinglePhotoFailed(Call<Photo> call, Throwable t);
    }

    public interface OnSetLikeListener {
        void onSetLikeSuccess(Call<LikePhotoResult> call, retrofit2.Response<LikePhotoResult> response);
        void onSetLikeFailed(Call<LikePhotoResult> call, Throwable t);
    }
}
