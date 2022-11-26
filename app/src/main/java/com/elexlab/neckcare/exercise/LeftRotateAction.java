package com.elexlab.neckcare.exercise;

import com.elexlab.neckcare.facetrack.Euler;
import com.elexlab.neckcare.facetrack.FacePose;
import com.elexlab.neckcare.facetrack.Vector3;
import com.elexlab.neckcare.misc.EasyLog;

public class LeftRotateAction extends Action{

    @Override
    public boolean action(FacePose facePose) {

        if(facePose!=null){
            Euler euler = facePose.getHeadAngle();
            float rotateZ = (float) (180 / Math.PI * euler.getZ());
            if(rotateZ>40){
                EasyLog.i("LeftRotateAction","finish");
                return true;
            }
        }
        return false;
    }
}
