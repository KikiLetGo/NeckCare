package com.elexlab.neckcare.phonemonitor;

public class PhoneStateMachine {
    private BaseState previousState;
    private BaseState globalState;

    private BaseState currentState;
    private UserAvatar userAvatar;
    public PhoneStateMachine(UserAvatar userAvatar) {
        this.userAvatar = userAvatar;
        changeState(RestState.getInstance());
        globalState = new GlobalState();
    }

    public void update() {
        if(currentState != null) {
            currentState.execute(userAvatar);
        }
    }

    public void changeState(BaseState state) {
        assert (state != null) : "PhoneStateMachine-changeState:Do not set null state to current";
        previousState = currentState;
        if(currentState != null) {
            currentState.exit(userAvatar);
        }
        currentState = state;
        currentState.enter(userAvatar);
    }

    public BaseState getCurrentState() {
        return currentState;
    }

    public void revertToPreviousState() {
        changeState(previousState);
    }

    public boolean handleMessage(Telegram msg)
    {
        //first see if the current state is valid and that it can handle
        //the message
        if (currentState!=null && currentState.onMessage(userAvatar, msg))
        {
            return true;
        }

        //if not, and if a global state has been implemented, send
        //the message to the global state
        if (globalState!=null && globalState.onMessage(userAvatar, msg))
        {
            return true;
        }

        return false;
    }
}
