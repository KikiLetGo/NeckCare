package com.elexlab.neckcare.exercise;

import android.content.Context;
import android.content.SharedPreferences;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.facetrack.FacePose;

import java.util.ArrayDeque;
import java.util.Deque;

public class Exercise {
    public static int EXERCISE_PERIOD = 1000*60*60;//exercise every 1 hour
    //public final static int EXERCISE_PERIOD = 1000*5;//exercise every 1 min
    static {
        SharedPreferences sharedPreferences = NeckCareApplication.getContext().getSharedPreferences("NeckCare",Context.MODE_PRIVATE);
        int exercisePeriod = sharedPreferences.getInt("exercisePeriod",EXERCISE_PERIOD);
        EXERCISE_PERIOD = exercisePeriod;

    }

    public interface ExerciseListener{
        void onComplete();
    }
    private ExerciseListener exerciseListener;
    private Deque<Action> actions = new ArrayDeque<>();
    private Action currentAction;

    public static Exercise newInstance(){
        Exercise exercise = new Exercise();
        return exercise;
    }

    private Exercise() {
        actions.add(new LeftWaveAction());
        actions.add(new ResetAction());
        actions.add(new LeftWaveAction());
        actions.add(new ResetAction());
        actions.add(new LeftWaveAction());
        actions.add(new ResetAction());
        actions.add(new LeftWaveAction());
        actions.add(new ResetAction());

        actions.add(new RightWaveAction());
        actions.add(new ResetAction());
        actions.add(new RightWaveAction());
        actions.add(new ResetAction());
        actions.add(new RightWaveAction());
        actions.add(new ResetAction());
        actions.add(new RightWaveAction());
        actions.add(new ResetAction());

        actions.add(new UpWaveAction());
        actions.add(new ResetAction());
        actions.add(new UpWaveAction());
        actions.add(new ResetAction());
        actions.add(new UpWaveAction());
        actions.add(new ResetAction());
        actions.add(new UpWaveAction());
        actions.add(new ResetAction());

        actions.add(new DownWaveAction());
        actions.add(new ResetAction());
        actions.add(new DownWaveAction());
        actions.add(new ResetAction());
        actions.add(new DownWaveAction());
        actions.add(new ResetAction());
        actions.add(new DownWaveAction());
        actions.add(new ResetAction());

        actions.add(new LeftRotateAction());
        actions.add(new ResetAction());
        actions.add(new LeftRotateAction());
        actions.add(new ResetAction());
        actions.add(new LeftRotateAction());
        actions.add(new ResetAction());
        actions.add(new LeftRotateAction());
        actions.add(new ResetAction());

        actions.add(new RightRotateAction());
        actions.add(new ResetAction());
        actions.add(new RightRotateAction());
        actions.add(new ResetAction());
        actions.add(new RightRotateAction());
        actions.add(new ResetAction());
        actions.add(new RightRotateAction());
        actions.add(new ResetAction());

    }

    public void setExerciseListener(ExerciseListener exerciseListener) {
        this.exerciseListener = exerciseListener;
    }

    public Action action(FacePose facePose){
        if(currentAction == null){
            currentAction = actions.poll();
        }
        if(currentAction == null){// all actions acted
            if(this.exerciseListener != null){
                this.exerciseListener.onComplete();
            }
            return null;
        }
        boolean complete = currentAction.action(facePose);
        if(complete){
            currentAction = actions.poll();
        }
        return currentAction;
    }
}
