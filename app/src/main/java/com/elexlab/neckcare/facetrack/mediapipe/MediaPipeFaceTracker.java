package com.elexlab.neckcare.facetrack.mediapipe;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.View;
import com.elexlab.neckcare.facetrack.AbstractFaceTracker;
import com.elexlab.neckcare.facetrack.Face;
import com.elexlab.neckcare.facetrack.FacePose;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.facemesh.FaceMesh;
import com.google.mediapipe.solutions.facemesh.FaceMeshOptions;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;


public class MediaPipeFaceTracker extends AbstractFaceTracker {
    private static final boolean RUN_ON_GPU = true;
    private CameraInput cameraInput;
    private FaceMesh faceMesh;
    private SolutionGlSurfaceView<FaceMeshResult> glSurfaceView ;

    public MediaPipeFaceTracker(Activity activity) {
        super(activity);
        createTracker();
        createGlSurfaceView();
    }

    private void createTracker(){
        faceMesh = new FaceMesh(
                        activity,
                        FaceMeshOptions.builder()
                            .setStaticImageMode(true)
                            .setRefineLandmarks(true)
                            .setRunOnGpu(RUN_ON_GPU)
                            .build());
        cameraInput = new CameraInput(activity);
    }

    private void createGlSurfaceView(){
        glSurfaceView =
                new SolutionGlSurfaceView<>(activity, faceMesh.getGlContext(), faceMesh.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new FaceMeshResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);

    }

    private void initCamera(){
        cameraInput = new CameraInput(activity);
        cameraInput.setNewFrameListener(textureFrame -> faceMesh.send(textureFrame));
        glSurfaceView.post(()->{
            startCamera(activity,glSurfaceView.getWidth(),glSurfaceView.getHeight());
        });
        glSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void startTrack() {
        cleanCameraInput();
        initCamera();
        faceMesh.setResultListener(
                faceMeshResult -> {
                    if(faceTrackListener != null){
                        Face face = new Face();
                        FacePose facePose = Utils.findFacePose(faceMeshResult,0);
                        face.setFacePose(facePose);
                        faceTrackListener.onFaceFrame(face);
                    }
                    glSurfaceView.setRenderData(faceMeshResult);
                    glSurfaceView.requestRender();
                });

    }

    @Override
    public void stopTrack() {
        cameraInput.setNewFrameListener(null);
        cameraInput.close();
    }

    @Override
    public FacePose getFacePose() {
        return null;
    }

    @Override
    public GLSurfaceView getGLSurfaceView() {
        return glSurfaceView;
    }


    private void cleanCameraInput(){
        if(cameraInput != null){
            cameraInput.close();
        }
    }

    public void startCamera(Activity activity, int width, int height) {
        cameraInput.start(
                activity,
                faceMesh.getGlContext(),
                CameraInput.CameraFacing.FRONT,
                width,
                height);
    }

}
