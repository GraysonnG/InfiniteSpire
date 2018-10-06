package infinitespire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

@Deprecated
public class TopPanelRenderPatch{
	
	@SpirePatch(cls="com.megacrit.cardcrawl.ui.panels.TopPanel", method = "renderDeckIcon")
	public static class RenderDeckIcon{
		public static void Postfix(TopPanel __instance, SpriteBatch sb) {
			//QuestLogButtonDeprecated.renderQuestLogButton(sb);
		}
	}
	
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.ui.panels.TopPanel", method = "updateButtons")
	public static class UpdateButtons {
		public static void Postfix(TopPanel __instance) {
			//QuestLogButtonDeprecated.updateQuestLogButton();
		}
	}
}
