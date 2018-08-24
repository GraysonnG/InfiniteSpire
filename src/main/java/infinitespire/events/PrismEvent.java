package infinitespire.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.relics.CubicDiamond;

public class PrismEvent extends AbstractImageEvent {

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Prism of Light");
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String ID = eventStrings.NAME;

    private State state;

    public enum State{
        CHOOSING,
        LEAVING
    }

    public PrismEvent() {
        super(ID, DESCRIPTIONS[0], "img/infinitespire/events/prismoflight.jpg");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
        this.state = State.CHOOSING;
    }

    @Override
    protected void buttonEffect(int i) {
        switch(state) {
            case CHOOSING:
                switch (i) {
                    case 0:
                        AbstractRelic relic = new CubicDiamond();
                        relic.instantObtain();
                        relic.playLandingSFX();
                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        break;
                    case 1:
                        this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                        break;
                }
                this.imageEventText.updateDialogOption(0, OPTIONS[1]);
                this.imageEventText.clearRemainingOptions();
                this.state = State.LEAVING;
                break;
            case LEAVING:
                this.openMap();
                break;
        }
    }
}
