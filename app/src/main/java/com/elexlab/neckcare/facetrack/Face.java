package com.elexlab.neckcare.facetrack;

import com.google.mediapipe.solutions.facemesh.FaceMeshResult;

public class Face {
    private FacePose facePose;

    public FacePose getFacePose() {
        return facePose;
    }

    public void setFacePose(FacePose facePose) {
        this.facePose = facePose;
    }
}
