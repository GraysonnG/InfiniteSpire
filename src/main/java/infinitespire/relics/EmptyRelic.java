package infinitespire.relics;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class EmptyRelic extends Relic {
	public static final String ID = InfiniteSpire.createID("EmptyRelic");

	public EmptyRelic(){
		super(ID, "empty", RelicTier.DEPRECATED, LandingSound.FLAT);
	}

	public void update(){}
}
