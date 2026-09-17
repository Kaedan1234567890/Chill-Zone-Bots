package com.chillzone.bots.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.chillzone.bots.BotRoster;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

/**
 * Gives only Chill Zone managed Carpet fake players an explicit TAB display name.
 * Vanilla normally returns null here and lets the client derive the text from the
 * profile.  Returning the literal managed username prevents other profile/team
 * presentation data from producing a strange TAB entry.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerTabNameMixin {
    @Inject(method = "getTabListDisplayName", at = @At("HEAD"), cancellable = true)
    private void chillzonebots$managedBotTabName(CallbackInfoReturnable<Component> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (!(player instanceof EntityPlayerMPFake)) {
            return;
        }

        String profileName = player.getGameProfile().name();
        if (profileName != null && BotRoster.BY_NAME.containsKey(profileName.toLowerCase(Locale.ROOT))) {
            cir.setReturnValue(Component.literal(profileName));
        }
    }
}
