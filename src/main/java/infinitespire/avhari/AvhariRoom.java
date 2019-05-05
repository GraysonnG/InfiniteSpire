package infinitespire.avhari;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import infinitespire.InfiniteSpire;

public class AvhariRoom extends AbstractRoom {

	public static Vector2 vector2 = new Vector2((Settings.WIDTH / 3f) * 2f , Settings.HEIGHT / 2f);
	public static AvhariHelper.SpinningCardItems cards;
	public static AvhariHelper.SpinningRelicItems relics;
	public static final Texture portal = InfiniteSpire.Textures.getUITexture("avhari/portal.png");
	private float portalRotation = 0.0f;
	private static final Avhari avhari = new Avhari(0,0);

	public AvhariRoom() {
		this.phase = RoomPhase.COMPLETE;
		this.mapSymbol = "AVR";
		this.mapImg = ImageMaster.MAP_NODE_MERCHANT;
		this.mapImgOutline = ImageMaster.MAP_NODE_MERCHANT_OUTLINE;
	}

	@Override
	public void onPlayerEntry() {
		cards = new AvhariHelper.SpinningCardItems(vector2.x, vector2.y);
		relics = new AvhariHelper.SpinningRelicItems(vector2.x, vector2.y);
		this.playBGM("SHOP");
		AbstractDungeon.overlayMenu.proceedButton.setLabel(ShopRoom.TEXT[0]);
	}

	@Override
	public AbstractCard.CardRarity getCardRarity(int roll) {
		int rareRate;
		// Start Nloths Gift Effect
		if (AbstractDungeon.player.hasRelic(NlothsGift.ID)) {
			rareRate = (int) (Settings.NORMAL_RARE_DROP_RATE * NlothsGift.MULTIPLIER);
		} else {
			rareRate = Settings.NORMAL_RARE_DROP_RATE;
		}
		// End Nloths Gift Effect

		if (roll < rareRate) {
			return AbstractCard.CardRarity.RARE;
		} else if (roll < Settings.NORMAL_UNCOMMON_DROP_RATE) {
			return AbstractCard.CardRarity.UNCOMMON;
		}
		return AbstractCard.CardRarity.COMMON;

	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		renderPortal(sb);
		avhari.render(sb);
		if(cards != null) cards.render(sb);
		if(relics != null) relics.render(sb);

	}

	public void renderPortal(SpriteBatch sb) {
		sb.setColor(Color.WHITE.cpy());
		sb.setBlendFunction(770, 1);
		sb.draw(portal,
			(Settings.WIDTH / 3f * 2f) - 500f * Settings.scale,
			(Settings.HEIGHT / 2f) - 500f * Settings.scale,
			500f,
			500f,
			1000f,
			1000f,
			Settings.scale,
			Settings.scale,
			portalRotation,
			0,
			0,
			1000,
			1000,
			false,
			false
		);
		sb.setBlendFunction(770, 771);
	}

	@Override
	public void update() {
		super.update();
		if(cards != null) cards.update();
		if(relics != null) relics.update();

		portalRotation += 8 * Gdx.graphics.getDeltaTime();

		if(portalRotation >= 360f) {
			portalRotation = 0.0f;
		}
	}
}
