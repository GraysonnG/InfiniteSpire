package infinitespire.interfaces;

import basemod.interfaces.ISubscriber;
import infinitespire.abstracts.Quest;

public interface OnQuestRemovedSubscriber extends ISubscriber{
    void receiveQuestRemoved(Quest quest);
}
