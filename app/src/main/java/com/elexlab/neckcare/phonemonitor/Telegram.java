package com.elexlab.neckcare.phonemonitor;

import java.util.Map;

public class Telegram {
    public enum MsgType{
        SUSPEND,
        COMPUTER_EXERCISE,
        EXERCISE_COMPLETE

    }
    private MsgType msgType;

    private Map<String,Object> extraInfo;

    public Telegram(MsgType msgType) {
        this.msgType = msgType;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }
}
