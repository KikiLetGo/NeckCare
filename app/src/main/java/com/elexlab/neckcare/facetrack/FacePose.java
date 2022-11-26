package com.elexlab.neckcare.facetrack;

import com.elexlab.neckcare.misc.EasyLog;

public class FacePose {

    private Vector3 noseVec;
    private Vector3 forwardVec;
    private Vector3 headVec;

    public FacePose(Vector3 noseVec, Vector3 forwardVec) {
        this.noseVec = noseVec;
        this.forwardVec = forwardVec;
    }

    public Vector3 getNoseVec() {
        return noseVec;
    }

    public void setNoseVec(Vector3 noseVec) {
        this.noseVec = noseVec;
    }

    public Vector3 getForwardVec() {
        return forwardVec;
    }

    public void setForwardVec(Vector3 forwardVec) {
        this.forwardVec = forwardVec;
    }

    public Euler getAngle(){
        Vector3 startVec = new Vector3(0,0,-1);

        Vector3 forwardVec = this.forwardVec.sub(this.noseVec).normalize();

        double xAngle = Math.atan(forwardVec.getY()/forwardVec.getZ());
        double yAngle = Math.atan(forwardVec.getX()/forwardVec.getZ());
        double zAngle = Math.atan(forwardVec.getX()/forwardVec.getY());
        Euler euler = new Euler(xAngle,yAngle,zAngle);

        EasyLog.i("findFacePose","angle:"+ forwardVec.angleTo(startVec));

        return euler;
    }

    public Euler getHeadAngle(){
        Vector3 startVec = new Vector3(0,1,0);

        Vector3 headVec = this.headVec.sub(this.noseVec).normalize();

        double xAngle = Math.atan(headVec.getY()/headVec.getZ());
        double yAngle = Math.atan(headVec.getX()/headVec.getZ());
        double zAngle = Math.atan(headVec.getX()/headVec.getY());
        Euler euler = new Euler(xAngle,yAngle,zAngle);

        EasyLog.i("getHeadAngle","angle:"+ headVec.angleTo(startVec));

        return euler;
    }

    public Vector3 getHeadVec() {
        return headVec;
    }

    public void setHeadVec(Vector3 headVec) {
        this.headVec = headVec;
    }
}
