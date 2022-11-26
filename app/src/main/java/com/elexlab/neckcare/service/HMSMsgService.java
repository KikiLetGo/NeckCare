package com.elexlab.neckcare.service;

import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.phonemonitor.Telegram;
import com.elexlab.neckcare.phonemonitor.UserAvatar;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class HMSMsgService extends HmsMessageService {
    private static final String TAG = HMSMsgService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage message) {
        EasyLog.i(TAG, "onMessageReceived is called");

        // 判断消息是否为空
        if (message == null) {
            EasyLog.e(TAG, "Received message entity is null!");
            return;
        }

        // 获取消息内容
        EasyLog.i(TAG, "get Data: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSentTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getToken: " + message.getToken());


        Telegram telegram = new Telegram(Telegram.MsgType.COMPUTER_EXERCISE);
        UserAvatar.getInstance()
                .getFSM()
                .handleMessage(telegram);
    }

}
