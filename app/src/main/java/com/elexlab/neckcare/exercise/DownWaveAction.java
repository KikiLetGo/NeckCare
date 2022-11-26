package com.elexlab.neckcare.exercise;

import com.elexlab.neckcare.facetrack.Euler;
import com.elexlab.neckcare.facetrack.FacePose;
import com.elexlab.neckcare.misc.EasyLog;

public class DownWaveAction extends Action{
    @Override
    public boolean action(FacePose facePose) {
        if(facePose!=null){
            Euler euler = facePose.getAngle();
            float rotateX = (float) (180 / Math.PI * euler.getX());
            if(rotateX < -25){
                EasyLog.i("DownWaveAction","finish");
                return true;
            }
        }
        return false;
    }
}
