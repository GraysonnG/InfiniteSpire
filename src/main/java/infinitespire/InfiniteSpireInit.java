package infinitespire;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import infinitespire.util.TextureLoader;

public class InfiniteSpireInit implements PostInitializeSubscriber {

	@Override
	public void receivePostInitialize() {
		InfiniteSpire.loadData();
		Texture modBadge = TextureLoader.getTexture("img/infinitespire/modbadge.png");

		ModPanel settingsPanel = new ModPanel();

		ModLabeledToggleButton nightmareQuestDefault = new ModLabeledToggleButton("Start Game with Endless Quest",
			350f, 500f, Settings.CREAM_COLOR, FontHelper.buttonLabelFont, InfiniteSpire.startWithEndlessQuest, settingsPanel,
			(me) -> {},
			(me) -> {
				InfiniteSpire.startWithEndlessQuest = me.enabled;
				InfiniteSpire.saveData();
			});

		ModLabeledToggleButton shouldDoParticles = new ModLabeledToggleButton("Black Cards have Particle Effects",
			350f, 450f, Settings.CREAM_COLOR, FontHelper.buttonLabelFont, InfiniteSpire.shouldDoParticles, settingsPanel,
			(me) -> {},
			(me) -> {
				InfiniteSpire.shouldDoParticles = me.enabled;
				InfiniteSpire.saveData();
			});

		nightmareQuestDefault.toggle.enabled = InfiniteSpire.startWithEndlessQuest;
		shouldDoParticles.toggle.enabled = InfiniteSpire.shouldDoParticles;

		settingsPanel.addUIElement(nightmareQuestDefault);
		settingsPanel.addUIElement(shouldDoParticles);

		BaseMod.registerModBadge(modBadge, "Infinite Spire", "Blank The Evil",
			"Adds a new way to play Slay the Spire, no longer stop after the 3rd boss. Keep fighting and gain perks as you climb.", settingsPanel);
	}
}
