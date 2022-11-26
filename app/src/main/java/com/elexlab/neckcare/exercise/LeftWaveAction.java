package com.elexlab.neckcare.exercise;

import com.elexlab.neckcare.facetrack.Euler;
import com.elexlab.neckcare.facetrack.FacePose;
import com.elexlab.neckcare.misc.EasyLog;

public class LeftWaveAction extends Action{
    @Override
    public boolean action(FacePose facePose) {
        if(facePose!=null){
            Euler euler = facePose.getAngle();
            float rotateY = (float) (180 / Math.PI * euler.getY());
            if(rotateY>40){
                EasyLog.i("LeftWaveAction","finish");
                return true;
            }
        }
        return false;
    }
}
