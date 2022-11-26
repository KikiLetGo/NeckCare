package com.elexlab.neckcare.exercise;

import com.elexlab.neckcare.facetrack.Euler;
import com.elexlab.neckcare.facetrack.FacePose;
import com.elexlab.neckcare.misc.EasyLog;

public class ResetAction extends Action{
    @Override
    public boolean action(FacePose facePose) {
        if(facePose!=null){
            Euler euler = facePose.getAngle();
            Euler headEuler = facePose.getHeadAngle();

            float rotateX = (float) (180 / Math.PI * euler.getX());
            float rotateY = (float) (180 / Math.PI * euler.getY());
            float rotateZ = (float) (180 / Math.PI * headEuler.getZ());

            if(Math.abs(rotateX)<10 && Math.abs(rotateY)<10 && Math.abs(rotateZ)<10 ){
                EasyLog.i("ResetAction","已归位");
                return true;
            }

        }
        return false;
    }
}
