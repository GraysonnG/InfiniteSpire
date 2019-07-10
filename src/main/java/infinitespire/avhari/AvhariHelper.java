package infinitespire.avhari;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import infinitespire.InfiniteSpire;
import infinitespire.helpers.CardHelper;
import infinitespire.helpers.QuestHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static infinitespire.patches.CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;

public class AvhariHelper {

	private static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	private static final float SPIN_SPEED = 10f;
	private static final float FADE_ALPHA_SPEED = 3f;
	private static Random relicRng = new Random();

	private static final Texture shardTexture = InfiniteSpire.Textures.getUITexture("topPanel/avhari/voidShard.png");

	private static final UIStrings strings = CardCrawlGame.languagePack.getUIString(InfiniteSpire.createID("Avhari"));

	public static class SpinningCardItems {
		private ArrayList<CardItem> cardItems = new ArrayList<>();

		private float deltaSpeed = 1f;

		public SpinningCardItems(float cX, float cY) {

			Vector2 rotationPoint = new Vector2(cX, cY);

			ArrayList<AbstractCard> cards = CardHelper.getBlackRewardCards(5);

			float dist = Settings.HEIGHT / 3.5f;

			cardItems.add(new CardItem(cards.get(0), rotationPoint, dist, 0f));
			cardItems.add(new CardItem(cards.get(1), rotationPoint, dist, 72f));
			cardItems.add(new CardItem(cards.get(2), rotationPoint, dist, 72f * 2));
			cardItems.add(new CardItem(cards.get(3), rotationPoint, dist, 72f * 3));
			cardItems.add(new CardItem(cards.get(4), rotationPoint, dist, 72f * 4));
		}

		public void render(SpriteBatch sb) {
			for(CardItem cardItem : cardItems) {
				cardItem.render(sb);
			}
		}

		public void update() {
			boolean shouldRotate = true;
			for(CardItem cardItem : cardItems) {
				cardItem.update();
				if(cardItem.isHovered()) {
					cardItem.card.drawScale = MathUtils.lerp(0.4f, .85f, cardItem.card.drawScale);
					shouldRotate = false;
				} else {
					cardItem.card.drawScale = MathUtils.lerp(0.85f, 0.4f, cardItem.card.drawScale);
				}
			}

			if(shouldRotate) {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 1.0f);
			}else {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 0.0f);
			}

			float deltaAngle = (-15 * deltaSpeed) * Gdx.graphics.getDeltaTime();

			for(CardItem cardItem : cardItems) {
				cardItem.rotateByAmount(deltaAngle);
			}

			cardItems.removeIf((cardItem) -> {
				if(cardItem.isHovered() && InputHelper.justClickedLeft) {
					if(cardItem.canBuy()) {
						CardCrawlGame.sound.play("SHOP_PURCHASE");
						InfiniteSpire.voidShardCount -= cardItem.price;
						AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(cardItem.card, cardItem.card.current_x, cardItem.card.current_y));
						return true;
					}
					CardCrawlGame.sound.play("UI_CLICK_2");
				}
				return false;
			});
		}
	}

	public static class CardItem {

		private Texture locked = InfiniteSpire.Textures.getCardTexture("ui/512/card-locked.png");
		private Color lockAlpha = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		public int price = -1;
		private AbstractCard card;
		private Vector2 position;
		private Vector2 center;
		private float angleFromCenter;
		private Hitbox hb;

		public CardItem(AbstractCard card, Vector2 centerOfCircle, float distanceFromCenter, float angleFromCenter) {
			this.card = card;
			switch (card.rarity) {
				case COMMON:
					price = 1;
					break;
				case UNCOMMON:
					price = 2;
					break;
				case RARE:
					price = 3;
					break;
				default:
					price = 99;
			}

			if(card.color.equals(INFINITE_BLACK)) price = InfiniteSpire.AVHARI_CARD_PRICE;

			float xPos = MathUtils.cosDeg(angleFromCenter) * distanceFromCenter;
			float yPos = MathUtils.sinDeg(angleFromCenter) * distanceFromCenter;

			position = new Vector2(centerOfCircle.x + xPos, centerOfCircle.y + yPos);
			center = centerOfCircle;

			card.drawScale = 0.4f;
			card.current_x = centerOfCircle.x;
			card.current_y = centerOfCircle.y;

			hb = new Hitbox(position.x, position.y ,card.hb.width, card.hb.height);
		}

		public void render(SpriteBatch sb) {
			sb.setColor(Color.WHITE.cpy());
			card.render(sb);
			renderLock(sb);
			renderPrice(sb);
			this.hb.render(sb);
		}

		public void renderPrice(SpriteBatch sb) {
			float scale = Settings.scale;
			float shardCX = shardTexture.getWidth() / 2f;
			float shardCY = shardTexture.getHeight() / 2f;
			float width = shardTexture.getWidth();
			float height = shardTexture.getHeight();
			Color fontColor = Color.WHITE.cpy();

			sb.setColor(lockAlpha);
			sb.draw(shardTexture,
				position.x - shardCX,
				position.y - shardCY,
				width/2,
				height/2,
				width,
				height,
				scale,
				scale,
				1.0f,
				0,
				0,
				shardTexture.getWidth(),
				shardTexture.getHeight(),
				false,
				false);

			if(!this.canBuy()) {
				fontColor = Color.RED.cpy();
			}

			fontColor.a = lockAlpha.a;

			FontHelper.cardTitleFont.getData().setScale(1.0f);
			FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "" + this.price, position.x, position.y - (16f * Settings.scale), fontColor);

		}

		public void renderLock(SpriteBatch sb) {
			float offset = 256f;

			try {
				Method method = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, Texture.class, float.class, float.class, float.class);
				method.setAccessible(true);
				method.invoke(card,
					sb,
					lockAlpha,
					locked,
					card.current_x - offset,
					card.current_y - offset,
					1.0f);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

			sb.setColor(Color.WHITE.cpy());
		}

		public void update() {
			card.target_x = position.x;
			card.target_y = position.y;
			card.update();
			this.hb.update();
			if(hb.hovered) {
				lockAlpha.a = MathHelper.fadeLerpSnap(lockAlpha.a, 0.2f);
			}else {
				lockAlpha.a = MathHelper.fadeLerpSnap(lockAlpha.a, 1.0f);
			}
		}

		public void rotateByAmount(float angle) {
			float angleR = angle * (float)(Math.PI / 180d);
			float newX = MathUtils.cos(angleR) * (position.x - center.x) - MathUtils.sin(angleR) * (position.y - center.y) + center.x;
			float newY = MathUtils.sin(angleR) * (position.x - center.x) + MathUtils.cos(angleR) * (position.y - center.y) + center.y;

			this.position.set(newX, newY);
			this.hb.update(newX - (hb.width / 2f), newY - (hb.height / 2f));
		}

		public AbstractCard getCard() {
			return card.makeStatEquivalentCopy();
		}

		public float getAngleFromCenter() {
			return angleFromCenter;
		}

		public Vector2 getPosition() {
			return position;
		}

		public void setAngleFromCenter(float angleFromCenter) {
			this.angleFromCenter = angleFromCenter;
		}

		public boolean isHovered() {
			return hb.hovered;
		}

		public boolean canBuy() {
			return InfiniteSpire.voidShardCount >= this.price;
		}
	}

	public static class SpinningRelicItems {
		public ArrayList<RelicItem> relicItems = new ArrayList<>();

		private float deltaSpeed = 1f;

		public SpinningRelicItems(float cX, float cY) {
			Vector2 rotationPoint = new Vector2(cX, cY);
			float dist = 90f * Settings.scale;

			relicItems.add(new RelicItem(getRandomBossRelic(), rotationPoint, dist, 0f));
			relicItems.add(new RelicItem(getRandomBossRelic(), rotationPoint, dist, 120f));
			relicItems.add(new RelicItem(getRandomBossRelic(), rotationPoint, dist, 240f));
		}

		public void render(SpriteBatch sb) {
			for (RelicItem relicItem : relicItems) {
				relicItem.render(sb);
			}
		}

		public void update() {
			boolean shouldRotate = true;
			boolean hasPurchaced = false;
			for(RelicItem relicItem : relicItems) {
				relicItem.update();
				if(relicItem.isHovered()) {
					shouldRotate = false;
				}
			}

			if(shouldRotate) {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 1.0f);
			}else {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 0.0f);
			}

			final float deltaAngle = (-15 * deltaSpeed) * Gdx.graphics.getDeltaTime();

			for(RelicItem relicItem : relicItems) {
				relicItem.rotateByAmount(deltaAngle);
				if(relicItem.isHovered() && InputHelper.justClickedLeft) {
					hasPurchaced = relicItem.purchace();
					CardCrawlGame.sound.play("UI_CLICK_2");
				}
			}

			if(hasPurchaced) relicItems.clear();
		}
	}

	public static AbstractRelic getRandomBossRelic() {
		return AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
	}

	public static class RelicItem {
		public int price = InfiniteSpire.AVHARI_RELIC_PRICE;
		private Vector2 center, position;
		private AbstractRelic relic;
		private Hitbox hb;

		public RelicItem(AbstractRelic relic, Vector2 centerOfCircle, float distanceFromCenter, float angleFromCenter) {
			this.relic = relic;

			float xPos = MathUtils.cosDeg(angleFromCenter) * distanceFromCenter;
			float yPos = MathUtils.sinDeg(angleFromCenter) * distanceFromCenter;

			position = new Vector2(centerOfCircle.x + xPos, centerOfCircle.y + yPos);
			center = centerOfCircle;

			hb = new Hitbox(position.x, position.y, relic.hb.width, relic.hb.height);
		}

		public void render(SpriteBatch sb) {
			sb.setColor(Color.WHITE.cpy());
			relic.renderOutline(sb, false);
			relic.renderWithoutAmount(sb, new Color(0.0f, 0.0f, 0.0f, 0.25f));
			renderPrice(sb);

			if(this.isHovered()) {
				relic.renderTip(sb);
			}

			this.hb.render(sb);
		}

		private void renderPrice(SpriteBatch sb) {

			float width = shardTexture.getWidth();
			float height = shardTexture.getHeight();
			float xPos = this.relic.currentX - (width / 2f);
			float yPos = this.relic.currentY - height - (10f * Settings.scale);

			TextureRegion region = new TextureRegion(shardTexture);

			sb.draw(region,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				Settings.scale, Settings.scale,
				1.0f);

			Color fontColor = Color.WHITE.cpy();
			if(!this.canBuy()) {
				fontColor = Color.RED.cpy();
			}

			FontHelper.cardTitleFont.getData().setScale(1.0f);
			FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "" + this.price,
				relic.currentX,
				yPos + (16f * Settings.scale * Settings.scale), fontColor);
		}

		public void update() {
			relic.currentX = position.x;
			relic.currentY = position.y;
			this.hb.update();
			//hover logic
		}

		public void rotateByAmount(float angle) {
			float angleR = angle * (float)(Math.PI / 180d);
			float newX = MathUtils.cos(angleR) * (position.x - center.x) - MathUtils.sin(angleR) * (position.y - center.y) + center.x;
			float newY = MathUtils.sin(angleR) * (position.x - center.x) + MathUtils.cos(angleR) * (position.y - center.y) + center.y;

			this.position.set(newX, newY);
			this.hb.update(newX - (hb.width / 2f), newY - (hb.height / 2f));
		}

		public boolean isHovered() {
			return this.hb.hovered;
		}

		public boolean canBuy() {
			return InfiniteSpire.voidShardCount >= this.price;
		}

		public boolean purchace() {
			if(this.canBuy()) {
				CardCrawlGame.sound.play("SHOP_PURCHASE");
				this.relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
				InfiniteSpire.voidShardCount -= this.price;
				return true;
			}
			return false;
		}
	}

	public static class RandomRelicItem {
		private static final float MAX_DURATION = 0.1f;
		private Hitbox hb;
		private float duration;
		private float fadeAlpha;
		private float spin = (new Random()).random(-1000f, 1000f);
		private AbstractRelic relic;
		private boolean isDone;
		private Color bgBlackAlpha = WHITE.cpy();
		private Color bgHoverAlpha = new Color(1f, 1f, 1f, 0.0f);
		private Interpolation hoverInterp = Interpolation.circleIn;

		public RandomRelicItem(Vector2 position) {
			relic = QuestHelper.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
			hb = new Hitbox(position.x, position.y, relic.hb.width, relic.hb.height);
			duration = MAX_DURATION;

		}

		public void update() {
			spin += Gdx.graphics.getRawDeltaTime() * SPIN_SPEED;

			// after item is bought fade cool bg thing
			if(isDone) {
				fadeAlpha -= Gdx.graphics.getRawDeltaTime() * FADE_ALPHA_SPEED;
				if(fadeAlpha > 1.0f) fadeAlpha = 1.0f;
				if(fadeAlpha < 0.0f) fadeAlpha = 0.0f;
				bgBlackAlpha.a = Interpolation.circleOut.apply(0.0f, 1.0f, fadeAlpha);
				bgHoverAlpha.a = Interpolation.circleIn.apply(0.0f, 1.0f, fadeAlpha);
				return;
			}

			hb.update();
			duration -= Gdx.graphics.getRawDeltaTime();
			if(duration <= 0.0f) {
				duration = MAX_DURATION;
				relic = getUniqueRandomRelic(relic);
			}

			relic.currentX = hb.x + (hb.width / 2f);
			relic.currentY = hb.y + (hb.height / 2f);

			if(hb.hovered) {
				hoverInterp = Interpolation.circleIn;
				fadeAlpha += Gdx.graphics.getRawDeltaTime() * FADE_ALPHA_SPEED;
				if(fadeAlpha > 1f) fadeAlpha = 1.0f;
				if (InputHelper.justClickedLeft) {
					if (InfiniteSpire.voidShardCount >= InfiniteSpire.AVHARI_RANDOM_RELIC_PRICE) {
						CardCrawlGame.sound.play("SHOP_PURCHASE");
						this.relic.makeCopy().instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
						InfiniteSpire.voidShardCount -= InfiniteSpire.AVHARI_RANDOM_RELIC_PRICE;
						this.isDone = true;
					} else {
						CardCrawlGame.sound.play("UI_CLICK_2");
					}
				}

			} else {
				hoverInterp = Interpolation.circleOut;
				fadeAlpha -= Gdx.graphics.getRawDeltaTime() * FADE_ALPHA_SPEED;
				if(fadeAlpha < 0f) fadeAlpha = 0f;
			}
			bgHoverAlpha.a = hoverInterp.apply(0.0f, 1.0f, fadeAlpha);
		}

		public AbstractRelic getUniqueRandomRelic(AbstractRelic currentRelic) {
			AbstractRelic newRelic = returnRandomRelic(returnRandomRelicTier());

			if(newRelic.relicId.equals(currentRelic.relicId)) {
				return getUniqueRandomRelic(currentRelic);
			}

			return newRelic;
		}

		public void render(SpriteBatch sb) {
			sb.setColor(WHITE.cpy());

			renderRelicSpinBg(sb);

			if(isDone) return;
			if(hb.hovered) renderTip();
			relic.renderOutline(sb, false);
			relic.renderWithoutAmount(sb, new Color(0.0f, 0.0f, 0.0f, 0.25f));

			renderPrice(sb);
			this.hb.render(sb);
		}

		private void renderTip() {
			TipHelper.renderGenericTip(
				InputHelper.mX + 50.0F * Settings.scale,
				InputHelper.mY + 50.0F * Settings.scale,
				strings.TEXT[2],
				strings.TEXT[3]);
		}

		private static Texture texture = InfiniteSpire.Textures.getUITexture("avhari/relicSpinBG.png");
		private static Texture hover = InfiniteSpire.Textures.getUITexture("avhari/relicSpinBG_hover.png");
		private static TextureRegion region = new TextureRegion(texture);
		private static TextureRegion hoverRegion = new TextureRegion(hover);

		private void renderRelicSpinBg(SpriteBatch sb) {
			float width = texture.getWidth();
			float height = texture.getHeight();
			float xPos = this.relic.currentX - (width / 2f);
			float yPos = this.relic.currentY - (height / 2f);

			sb.setColor(bgBlackAlpha.cpy());
			sb.setBlendFunction(770,771);
			sb.draw(region,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				Settings.scale * bgBlackAlpha.a,
				Settings.scale * bgBlackAlpha.a,
				spin);
			sb.setColor(bgHoverAlpha.cpy());
			sb.draw(hoverRegion,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				Settings.scale * bgHoverAlpha.a,
				Settings.scale * bgHoverAlpha.a,
				spin * -3);
		}


		private void renderPrice(SpriteBatch sb) {
			float width = shardTexture.getWidth();
			float height = shardTexture.getHeight();
			float xPos = this.relic.currentX - (width / 2f);
			float yPos = this.relic.currentY - height - (10f * Settings.scale);

			TextureRegion region = new TextureRegion(shardTexture);

			sb.draw(region,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				Settings.scale, Settings.scale,
				1.0f);

			Color fontColor = Color.WHITE.cpy();
			if(!(InfiniteSpire.voidShardCount >= InfiniteSpire.AVHARI_RANDOM_RELIC_PRICE)) {
				fontColor = Color.RED.cpy();
			}

			FontHelper.cardTitleFont.getData().setScale(1.0f);
			FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "" + InfiniteSpire.AVHARI_RANDOM_RELIC_PRICE,
				relic.currentX,
				yPos + (16f * Settings.scale * Settings.scale), fontColor);
		}
	}

	public static class RemoveCardItem {
		private static final float WIDTH = 64f;
		private static final float HEIGHT = 64f;
		private Color bgBlackAlpha = WHITE.cpy();
		private Color bgHoverAlpha = new Color(1f, 1f, 1f, 0.0f);
		private Vector2 position;
		private Hitbox hb;
		private float fadeAlpha;
		private float spin = (new Random()).random(-1000f, 1000f);
		private boolean isDone;
		private Interpolation hoverInterp = Interpolation.circleIn;
		private static Texture texture = InfiniteSpire.Textures.getUITexture("avhari/relicSpinBG.png");
		private static Texture hover = InfiniteSpire.Textures.getUITexture("avhari/relicSpinBG_hover.png");
		private static TextureRegion region = new TextureRegion(texture);
		private static TextureRegion hoverRegion = new TextureRegion(hover);

		public RemoveCardItem(Vector2 position) {
			this.position = position;
			this.hb = new Hitbox(position.x, position.y, WIDTH * Settings.scale, HEIGHT * Settings.scale);

		}

		public void update() {
			hb.update();
			if(isDone) {
				fadeAlpha -= Gdx.graphics.getRawDeltaTime() * FADE_ALPHA_SPEED;
				if(fadeAlpha > 1.0f) fadeAlpha = 1.0f;
				if(fadeAlpha < 0.0f) fadeAlpha = 0.0f;
				bgBlackAlpha.a = Interpolation.circleOut.apply(0.0f, 1.0f, fadeAlpha);
				bgHoverAlpha.a = Interpolation.circleIn.apply(0.0f, 1.0f, fadeAlpha);
				return;
			}
			spin -= Gdx.graphics.getRawDeltaTime() * SPIN_SPEED;

			updateCardRemoval();

			if(hb.hovered) {
				hoverInterp = Interpolation.circleIn;
				fadeAlpha += Gdx.graphics.getRawDeltaTime() * FADE_ALPHA_SPEED;
				if (fadeAlpha > 1f) fadeAlpha = 1.0f;
				if (InputHelper.justClickedLeft) {
					if(this.canPurchace()) {
						AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, ShopScreen.NAMES[13], false, false, true, true);
					} else {
						CardCrawlGame.sound.play("UI_CLICK_2");
					}
				}

			} else {
				hoverInterp = Interpolation.circleOut;
				fadeAlpha -= Gdx.graphics.getRawDeltaTime() * FADE_ALPHA_SPEED;
				if(fadeAlpha < 0f) fadeAlpha = 0.0f;
			}

			bgHoverAlpha.a = hoverInterp.apply(0.0f, 1.0f, fadeAlpha);
		}

		public void updateCardRemoval() {
			if(!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
				InfiniteSpire.voidShardCount -= InfiniteSpire.AVHARI_REMOVE_CARD_PRICE;
				CardCrawlGame.sound.play("SHOP_PURCHASE");
				for(AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
					CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
					AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
					AbstractDungeon.player.masterDeck.removeCard(card);
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.isDone = true;
			}
		}

		public void render(SpriteBatch sb) {
			renderBg(sb);
			if(this.isDone) return;
			sb.setColor(WHITE.cpy());
			Texture texture = InfiniteSpire.Textures.getUITexture("avhari/cardRemoval.png");
			TextureRegion textRegion = new TextureRegion(texture);

			if(this.hb.hovered) {
				renderTip();
			}

			sb.draw(textRegion,
				position.x,
				position.y,
				hb.width / 2,
				hb.height / 2,
				hb.width,
				hb.height,
				1.0f,
				1.0f,
				0f);

			this.renderPrice(sb);
			hb.render(sb);
		}

		private void renderTip() {
			TipHelper.renderGenericTip(
				InputHelper.mX + 50.0F * Settings.scale,
				InputHelper.mY + 50.0F * Settings.scale,
				strings.TEXT[0],
				strings.TEXT[1]);
		}

		private void renderBg(SpriteBatch sb) {
			float width = texture.getWidth();
			float height = texture.getHeight();
			float xPos = position.x - width / 2f + hb.width / 2f;
			float yPos = position.y - height / 2f + hb.height / 2f;

			sb.setColor(bgBlackAlpha.cpy());
			sb.setBlendFunction(770,771);
			sb.draw(region,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				 -1 * Settings.scale * bgBlackAlpha.a,
				Settings.scale * bgBlackAlpha.a,
				spin);
			sb.setColor(bgHoverAlpha.cpy());
			sb.draw(hoverRegion,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				-1 * Settings.scale * bgHoverAlpha.a,
				Settings.scale * bgHoverAlpha.a,
				spin * -3);
		}

		private void renderPrice(SpriteBatch sb) {
			float width = shardTexture.getWidth();
			float height = shardTexture.getHeight();
			float xPos = this.position.x + (hb.width / 2f) - (width / 2f);
			float yPos = this.position.y - (height / 2f + (10f * Settings.scale));

			TextureRegion region = new TextureRegion(shardTexture);

			sb.draw(region,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				Settings.scale, Settings.scale,
				1.0f);

			Color fontColor = Color.WHITE.cpy();
			if(!(InfiniteSpire.voidShardCount >= InfiniteSpire.AVHARI_RANDOM_RELIC_PRICE)) {
				fontColor = Color.RED.cpy();
			}

			FontHelper.cardTitleFont.getData().setScale(1.0f);
			FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "" + InfiniteSpire.AVHARI_REMOVE_CARD_PRICE,
				position.x + (hb.width / 2f),
				yPos + (22f * Settings.scale * Settings.scale), fontColor);
		}

		public boolean canPurchace() {
			return InfiniteSpire.voidShardCount >= InfiniteSpire.AVHARI_REMOVE_CARD_PRICE;
		}
	}

	public static AbstractRelic returnRandomRelic(AbstractRelic.RelicTier tier) {
		String key = Circlet.ID;
		AbstractRelic retVal = new Circlet();
		switch(tier) {
			case BOSS:
				int bossPoolSize = AbstractDungeon.bossRelicPool.size() - 1;
				if(bossPoolSize > 0)
					key = AbstractDungeon.bossRelicPool.get(relicRng.random(bossPoolSize));
				break;
			case COMMON:
				int commonPoolSize = AbstractDungeon.commonRelicPool.size() - 1;
				if(commonPoolSize > 0)
					key = AbstractDungeon.commonRelicPool.get(relicRng.random(commonPoolSize));
				break;
			case RARE:
				int rarePoolSize = AbstractDungeon.rareRelicPool.size() - 1;
				if(rarePoolSize > 0)
					key = AbstractDungeon.rareRelicPool.get(relicRng.random(rarePoolSize));
				break;
			case UNCOMMON:
				int uncommonPoolSize = AbstractDungeon.uncommonRelicPool.size() - 1;
				if(uncommonPoolSize > 0)
					key = AbstractDungeon.uncommonRelicPool.get(relicRng.random(uncommonPoolSize));
				break;
			case SHOP:
				int shopPoolSize = AbstractDungeon.shopRelicPool.size() - 1;
				if(shopPoolSize > 0) {
					key = AbstractDungeon.shopRelicPool.get(relicRng.random(shopPoolSize));
				}
				break;
			default:
				key = Circlet.ID;
				break;
		}

		retVal = RelicLibrary.getRelic(key);

		return retVal.makeCopy();
	}

	public static AbstractRelic.RelicTier returnRandomRelicTier() {
		int roll = relicRng.random(0,3);
		switch (roll) {
			case 0:
				return AbstractRelic.RelicTier.COMMON;
			case 1:
				return AbstractRelic.RelicTier.UNCOMMON;
			case 2:
				return AbstractRelic.RelicTier.RARE;
			case 3:
				return AbstractRelic.RelicTier.SHOP;
		}

		return AbstractRelic.RelicTier.COMMON;
	}
}
