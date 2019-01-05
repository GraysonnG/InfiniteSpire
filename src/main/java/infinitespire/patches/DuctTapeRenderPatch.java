package infinitespire.patches;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.crossover.HubrisCrossover;
import javafx.util.Pair;

import java.util.ArrayList;

public class DuctTapeRenderPatch {
	public static final String DUCT_TAPE_CARD = "com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard";

	private static ArrayList<BlackCard.BlackParticle> particles = new ArrayList<>();
	private static final float FPS_SCALE = (240f / Settings.MAX_FPS);

	@SpirePatch(cls = DUCT_TAPE_CARD, method=SpirePatch.CLASS, optional = true)
	public static class Fields {
		public static SpireField<ArrayList<BlackCard.BlackParticle>> particles = new SpireField<>(ArrayList::new);
		public static SpireField<Integer> particleSize = new SpireField<>(() -> 150);
	}

	@SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = {SpriteBatch.class, boolean.class})
	public static class Render {
		@SpirePrefixPatch
		public static void renderBlackCardParticles(AbstractCard card, SpriteBatch sb, boolean bool) {
			if(InfiniteSpire.isHubrisLoaded && HubrisCrossover.isDuctTapeBlackCard(card)) {
				sb.setColor(Color.WHITE.cpy());
				for (BlackCard.BlackParticle p : particles) {
					p.render(sb);
				}
			}
		}
	}

	@SpirePatch(cls = DUCT_TAPE_CARD, method = "calculateCard", optional = true)
	public static class CalculateFrame {

		@SpirePostfixPatch
		public static void changeFrameToBlackCard(CustomCard card){
			if(InfiniteSpire.isHubrisLoaded && HubrisCrossover.isDuctTapeBlackCard(card)){
				card.setBannerTexture("img/infinitespire/cards/ui/512/boss-banner.png", "img/infinitespire/cards/ui/1024/boss-banner.png");
			}

			if(InfiniteSpire.isHubrisLoaded && HubrisCrossover.isDuctTapeBlackCard(card) && HubrisCrossover.getIndexsOfHubrisBlackCard(card).size() > 1) {
				Fields.particleSize.set(card, 75);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "update")
	public static class Update {
		@SpirePostfixPatch
		public static void updateBlackCardParticles(AbstractCard card) {
			if(InfiniteSpire.isHubrisLoaded && HubrisCrossover.isDuctTapeBlackCard(card)) {
				for(BlackCard.BlackParticle p : particles){
					p.update();
				}
				particles.removeIf(BlackCard.BlackParticle::isDead);

				if(Fields.particles.get(card).size() < Fields.particleSize.get(card) && InfiniteSpire.shouldDoParticles && !Settings.DISABLE_EFFECTS){
					for(int i = 0; i < 2 * FPS_SCALE; i++){
						Vector2 point = generateRandomPointAlongEdgeOfHitbox(card);
						particles.add(new BlackCard.BlackParticle(point.x, point.y, card.drawScale, card.upgraded));
					}
				}
			}
		}

		public static Vector2 generateRandomPointAlongEdgeOfHitbox(AbstractCard card){
			Vector2 result = new Vector2();
			Random random = new Random();
			ArrayList<Integer> sides = HubrisCrossover.getIndexsOfHubrisBlackCard(card);
			boolean topOrBottom = random.randomBoolean();
			boolean isRandom = HubrisCrossover.getIndexsOfHubrisBlackCard(card).size() > 1;
			boolean leftOrRight = isRandom ? random.randomBoolean() : sides.get(0) == 0;
            Pair<Float, Float> topBotXBounds = getTopBotXBounds(card, leftOrRight, isRandom);


			boolean tbOrLr = random.randomBoolean();

			if(tbOrLr){
				result.x = random.random(topBotXBounds.getKey(), topBotXBounds.getValue());
				result.y = topOrBottom ? card.hb.cY + (card.hb.height / 2f) : card.hb.cY - (card.hb.height / 2f);
			} else {
				result.x = leftOrRight ? card.hb.cX - (card.hb.width / 2f): card.hb.cX + (card.hb.width / 2f) ;
				result.y = random.random(card.hb.cY - (card.hb.height / 2f), card.hb.cY + (card.hb.height / 2f));
			}

			return result;
		}

		private static Pair<Float, Float> getTopBotXBounds(AbstractCard card , boolean leftOrRight, boolean isRandom) {
			Pair<Float, Float> pair;
			float farLeft = card.hb.cX - (card.hb.width / 2f);
			float farRight = card.hb.cX + (card.hb.width / 2f);
			float center = card.hb.cX;

			if(isRandom) {
				pair = new Pair<>(farLeft, farRight);
			} else {
				pair = leftOrRight ?
					new Pair<>(farLeft, center) :
					new Pair<>(center, farRight);
			}

			return pair;
		}
	}
}
