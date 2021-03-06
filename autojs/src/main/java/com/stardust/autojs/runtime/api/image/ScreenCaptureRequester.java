package com.stardust.autojs.runtime.api.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import com.stardust.app.OnActivityResultDelegate;

/**
 * Created by Stardust on 2017/5/17.
 */

public interface ScreenCaptureRequester {

    interface Callback {

        void onRequestResult(int result, Intent data);

    }

    void request();

    void setOnActivityResultCallback(Callback callback);

    abstract class AbstractScreenCaptureRequester implements ScreenCaptureRequester {

        protected Callback mCallback;

        @Override
        public void setOnActivityResultCallback(Callback callback) {
            mCallback = callback;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class ActivityScreenCaptureRequester extends AbstractScreenCaptureRequester implements ScreenCaptureRequester, OnActivityResultDelegate {

        private static final int REQUEST_CODE_MEDIA_PROJECTION = "Eating...Today is 17.5.20 yet...The 90 days、、、".hashCode() >> 16;
        private OnActivityResultDelegate.Mediator mMediator;
        private Activity mActivity;

        public ActivityScreenCaptureRequester(Mediator mediator, Activity activity) {
            mMediator = mediator;
            mActivity = activity;
            mMediator.addDelegate(REQUEST_CODE_MEDIA_PROJECTION, this);
        }

        @Override
        public void request() {
            mActivity.startActivityForResult(((MediaProjectionManager) mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE)).createScreenCaptureIntent(), REQUEST_CODE_MEDIA_PROJECTION);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            mMediator.removeDelegate(this);
            mCallback.onRequestResult(resultCode, data);
        }
    }

}
