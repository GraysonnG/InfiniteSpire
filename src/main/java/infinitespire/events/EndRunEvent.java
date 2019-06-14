package infinitespire.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import infinitespire.InfiniteSpire;
import infinitespire.rooms.EndRunRoom;

public class EndRunEvent extends AbstractImageEvent {
	public static final String ID = InfiniteSpire.createID("EndRunEvent");
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String NAME = eventStrings.NAME;
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;
	private static final String TEXTURE = "img/infinitespire/events/emptyrestsite.jpg";

	public static boolean shouldEndRun;

	public EndRunEvent() {
		super(NAME, DESCRIPTIONS[0], TEXTURE);
		imageEventText.setDialogOption(OPTIONS[0]);
		imageEventText.setDialogOption(OPTIONS[1]);
		shouldEndRun = false;
	}

	@Override
	protected void buttonEffect(int buttonPressed) {
		switch (buttonPressed) {
			case 0:
				CardCrawlGame.nextDungeon = Exordium.ID;
				shouldEndRun = false;
				break;
			case 1:
				CardCrawlGame.nextDungeon = TheEnding.ID;
				shouldEndRun = true;
				break;
		}

		AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
		if(AbstractDungeon.currMapNode.room instanceof EndRunRoom) {
			AbstractDungeon.currMapNode.room = ((EndRunRoom) AbstractDungeon.currMapNode.room).originalRoom;
			GenericEventDialog.hide();
			AbstractDungeon.fadeOut();
			AbstractDungeon.isDungeonBeaten = true;
		}

	}
}
