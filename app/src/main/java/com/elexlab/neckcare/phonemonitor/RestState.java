package com.elexlab.neckcare.phonemonitor;

import com.elexlab.neckcare.exercise.Exercise;
import com.elexlab.neckcare.managers.ScreenStatusManager;
import com.elexlab.neckcare.misc.EasyLog;

public class RestState extends BaseState{
    private long startTime;
    private final static String TAG = RestState.class.getSimpleName();

    private static final RestState instance = new RestState();
    public static RestState getInstance(){
        return instance;
    }
    private RestState() {
        super();
    }

    @Override
    public void enter(UserAvatar userAvatar) {
        EasyLog.d(TAG,"enter RestState");
        startTime = System.currentTimeMillis();
    }

    @Override
    public void exit(UserAvatar userAvatar) {
        EasyLog.d(TAG,"exit RestState");

    }

    @Override
    public void execute(UserAvatar userAvatar) {
        long nowTime = System.currentTimeMillis();
        long restTime = nowTime - startTime;
        if(restTime >= Exercise.EXERCISE_PERIOD){//如果用户已经超过一个锻炼周期没有使用手机，则重置用户累积的使用时间
            userAvatar.setUsingTime(0);
        }
        if(ScreenStatusManager.getInstance().getScreenState() == ScreenStatusManager.ScreenState.SCREEN_UNLOCKED){
            userAvatar.getFSM().changeState(new UsingState());
        }
    }

    @Override
    public boolean onMessage(UserAvatar userAvatar, Telegram telegram) {
        return false;
    }
}
