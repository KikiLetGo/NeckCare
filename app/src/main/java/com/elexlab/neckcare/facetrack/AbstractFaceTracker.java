package com.elexlab.neckcare.facetrack;

import android.app.Activity;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLContext;

public abstract class AbstractFaceTracker {
    protected Activity activity;
    protected FaceTrackListener faceTrackListener;
    public AbstractFaceTracker(Activity activity) {
        this.activity = activity;
    }

    public void setFaceTrackListener(FaceTrackListener faceTrackListener) {
        this.faceTrackListener = faceTrackListener;
    }

    public abstract void startTrack();
    public abstract void stopTrack();
    public abstract FacePose getFacePose();

    public abstract GLSurfaceView getGLSurfaceView();
}
