package com.elexlab.neckcare.phonemonitor;

public class UserAvatar {
    private static UserAvatar instance = new UserAvatar();
    public static UserAvatar getInstance(){
        return instance;
    }
    private UserAvatar() {
        phoneStateMachine = new PhoneStateMachine(this);
    }
    private PhoneStateMachine phoneStateMachine;
    private long usingTime=0;

    public long getUsingTime() {
        return usingTime;
    }

    public void setUsingTime(long usingTime) {
        this.usingTime = usingTime;
    }



    public void update(){
        phoneStateMachine.update();
    }

    public PhoneStateMachine getFSM() {
        return phoneStateMachine;
    }
}
