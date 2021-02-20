package chronoMods.ui.lobby;

import chronoMods.*;
import chronoMods.steam.*;
import chronoMods.ui.deathScreen.*;
import chronoMods.ui.hud.*;
import chronoMods.ui.lobby.*;
import chronoMods.ui.mainMenu.*;
import chronoMods.utilities.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.PatchNotesScreen;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import com.codedisaster.steamworks.*;

import java.util.ArrayList;

public class PlayerListWidgetItem
{
    public RemotePlayer player;
    public Texture ownerCrown;
    public Texture kickBoot;

    // Position
    public float x;
    public float y;
    public float scroll;

    public Hitbox kickbox = new Hitbox(36f * Settings.scale, 24f * Settings.scale);
    public float ks;

    public PlayerListWidgetItem(RemotePlayer player) {
        this.player = player;
        ownerCrown = ImageMaster.getRelicImg("Busted Crown");
        kickBoot = ImageMaster.getRelicImg("Boot");
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void scroll(float y) {
        this.scroll = y;
    }

    public void update(int i) {

        // if (TogetherManager.currentLobby != null && player != null && !(player.isUser(TogetherManager.currentLobby.ownerID)) && TogetherManager.getCurrentUser().isUser(TogetherManager.currentLobby.ownerID)) {
        if (TogetherManager.currentLobby != null && player != null && TogetherManager.getCurrentUser().isUser(TogetherManager.currentLobby.ownerID)) {
            kickbox.move(this.x - (464 / 2f) * Settings.scale + 36f * Settings.scale, this.y + this.scroll - (i * 75f * Settings.scale) - 24f * Settings.scale);

            kickbox.update();
            this.ks = 1.0f;
            if (kickbox.hovered) {
                this.ks = 1.2f;
            }
            if (kickbox.hovered && InputHelper.justClickedLeft) {
              kickbox.clickStarted = true;
              NewGameScreen.kick = player;
              NetworkHelper.sendData(NetworkHelper.dataType.Kick);
            } 
        }
    }

    public void clear() {
        player = null;
    }

    private static final Color EMPTY_PLAYER_SLOT = new Color(1f, 1f, 1f, 0.3f);

    public void render(SpriteBatch sb, int i) {
        // BG Panel        
        sb.setColor(Color.WHITE.cpy());

        if (player == null) {
            sb.setColor(EMPTY_PLAYER_SLOT);
            // Background
            sb.draw(
                ImageMaster.REWARD_SCREEN_ITEM,
                this.x - 464 / 2f,
                this.y + this.scroll - (i * 75f * Settings.scale) - 98 / 2f,
                464 / 2f, 98 / 2f,
                464, 98,
                Settings.scale,Settings.scale*0.75f,
                0f,
                0, 0, 464, 98,
                false, false);
            sb.setColor(Color.WHITE);
        } else {

            sb.draw(
                ImageMaster.REWARD_SCREEN_ITEM,
                this.x - 464 / 2f,
                this.y + this.scroll - (i * 75f * Settings.scale) - 98 / 2f,
                464 / 2f, 98 / 2f,
                464, 98,
                Settings.scale,Settings.scale*0.75f,
                0f,
                0, 0, 464, 98,
                false, false);

            // Player Portrait
            if (player.portraitImg != null) {
                sb.draw(
                    player.portraitImg,
                    this.x - 56 / 2f - 164f * Settings.scale,
                    this.y + this.scroll - (i * 75f * Settings.scale) - 56 / 2f - 2f * Settings.scale,
                    56 / 2f, 56 / 2f,
                    56, 56,
                    Settings.scale, Settings.scale,
                    0f, 0, 0,
                    player.portraitImg.getWidth(), player.portraitImg.getHeight(),
                    false, false); }

            // Portrait Frame
            sb.draw(TogetherManager.portraitFrames.get(0), 
                this.x - (64 / 2f) * Settings.scale - 164f * Settings.scale    - 184.0F * Settings.scale, 
                this.y + this.scroll - (i * 75f * Settings.scale) - (64 / 2f) * Settings.scale - 2f * Settings.scale    - 104.0F * Settings.scale, 
                0.0F, 0.0F, 432.0F, 243.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1920, 1080, false, false);

            // Owner Crown
            if (TogetherManager.currentLobby != null && player.isUser(TogetherManager.currentLobby.ownerID)) {
                sb.draw(
                    ownerCrown,
                    this.x - 164f * Settings.scale,
                    this.y + this.scroll - (i * 75f * Settings.scale) - 12f * Settings.scale,
                    64 / 2f, 64 / 2f,
                    64, 64,
                    Settings.scale, Settings.scale,
                    -60f, 0, 0,
                    ownerCrown.getWidth(), ownerCrown.getHeight(),
                    false, false); }
            
            // Kick Icon
            else if (TogetherManager.getCurrentUser().isUser(TogetherManager.currentLobby.ownerID)) {
                sb.draw(
                    kickBoot,
                    kickbox.x - 12f * Settings.scale,
                    kickbox.y - 12f * Settings.scale,
                    48 / 2f, 48 / 2f,
                    48, 48,
                    Settings.scale * this.ks, Settings.scale * this.ks,
                    0f, 0, 0,
                    kickBoot.getWidth(), kickBoot.getHeight(),
                    false, false);

                kickbox.render(sb);
            }


            // Player Name
            Color color = Settings.CREAM_COLOR;

            if (TogetherManager.gameMode == TogetherManager.mode.Versus) {
                FontHelper.renderSmartText(
                    sb,
                    FontHelper.cardDescFont_N,
                    player.userName,
                    this.x - 112f * Settings.scale,
                    this.y + this.scroll - (i * 75f * Settings.scale) + 5f * Settings.scale,
                    1000f * Settings.scale,
                    0f,
                    color);
            } else if (TogetherManager.gameMode == TogetherManager.mode.Coop) {
                FontHelper.renderSmartText(
                    sb,
                    FontHelper.cardDescFont_N,
                    player.userName,
                    this.x - 112f * Settings.scale,
                    this.y + this.scroll - (i * 75f * Settings.scale) + 12f * Settings.scale,
                    1000f * Settings.scale,
                    0f,
                    color);

                FontHelper.renderSmartText(
                    sb,
                    FontHelper.cardTypeFont,
                    player.character,
                    this.x - 100f * Settings.scale,
                    this.y + this.scroll - (i * 75f * Settings.scale) - 10f * Settings.scale,
                    1000f * Settings.scale,
                    0f,
                    Color.DARK_GRAY);
            }

            // Ready Tick
            if (player.ready) {
                sb.draw(
                    ImageMaster.TICK,
                    this.x - 64 / 2f + 164f * Settings.scale,
                    this.y + this.scroll - (i * 75f * Settings.scale) - 64 / 2f - 2f * Settings.scale,
                    64 / 2f, 64 / 2f,
                    64, 64,
                    Settings.scale, Settings.scale,
                    0f, 0, 0, 64, 64,
                    false, false);
            }
        }
    }
}