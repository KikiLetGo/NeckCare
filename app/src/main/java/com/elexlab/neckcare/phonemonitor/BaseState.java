package com.elexlab.neckcare.phonemonitor;

public abstract class BaseState {
    protected BaseState() {
    }



    abstract public void enter(UserAvatar userAvatar);
    abstract public void exit(UserAvatar userAvatar);
    abstract public void execute(UserAvatar userAvatar);
    abstract public boolean onMessage(UserAvatar userAvatar, Telegram telegram);
}
