package infinitespire.abstracts;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public abstract class InfiniteRelic extends Relic {
    public InfiniteRelic(String setId, String textureID, LandingSound sfx) {
        super(setId, textureID, RelicTier.UNCOMMON, sfx);
        this.counter = 1;
        if(AbstractDungeon.player != null)
            this.updateDescription(AbstractDungeon.player.chosenClass);
    }

    @Override
    public void onVictory() {
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            this.counter ++;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
