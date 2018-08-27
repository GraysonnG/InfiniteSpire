package infinitespire.interfaces;

import basemod.interfaces.ISubscriber;
import infinitespire.abstracts.Quest;

public interface OnQuestIncrementSubscriber extends ISubscriber{
    void receiveQuestIncrement(Quest quest);
}
