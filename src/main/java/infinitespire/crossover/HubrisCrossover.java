package infinitespire.crossover;

import com.evacipated.cardcrawl.mod.hubris.cards.black.InfiniteBlow;

public class HubrisCrossover {

	public static boolean isInfiniteBlowLoaded = false;

	public static void loadInfiniteBlow() {
		if(!isInfiniteBlowLoaded) {
			InfiniteBlow.load();
			isInfiniteBlowLoaded = true;
		}
	}
}
