package com.elexlab.neckcare.phonemonitor;

public class GlobalState extends BaseState{
    @Override
    public void enter(UserAvatar userAvatar) {

    }

    @Override
    public void exit(UserAvatar userAvatar) {

    }

    @Override
    public void execute(UserAvatar userAvatar) {

    }

    @Override
    public boolean onMessage(UserAvatar userAvatar, Telegram telegram) {
        if(telegram.getMsgType() == Telegram.MsgType.COMPUTER_EXERCISE){
            BaseState state = new ComputerExerciseState();
            userAvatar.getFSM().changeState(state);
            return true;
        }
        return false;
    }
}
