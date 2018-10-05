package infinitespire.interfaces;

import basemod.interfaces.ISubscriber;
import infinitespire.abstracts.Quest;

public interface OnQuestAddedSubscriber extends ISubscriber{
    void receiveQuestAdded(Quest quest);
}
