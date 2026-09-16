package com.chillzone.bots;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public final class BotManager {
    private BotManager() {}

    public static boolean isOnline(MinecraftServer server, BotProfile profile) {
        return server.getPlayerList().getPlayerByName(profile.username()) != null;
    }

    public static boolean spawnAt(MinecraftServer server, ServerPlayer anchor, BotProfile profile) {
        if (isOnline(server, profile)) {
            return false;
        }

        Vec3 pos = anchor.position();
        return EntityPlayerMPFake.createFake(
                profile.username(),
                server,
                pos,
                anchor.getYRot(),
                anchor.getXRot(),
                anchor.level().dimension(),
                GameType.SURVIVAL,
                false
        );
    }

    public static boolean remove(MinecraftServer server, BotProfile profile) {
        ServerPlayer player = server.getPlayerList().getPlayerByName(profile.username());
        if (!(player instanceof EntityPlayerMPFake fake)) {
            return false;
        }

        // Carpet's disconnect path saves normal player data. That means inventory,
        // equipment and location are persisted using Minecraft's normal player save.
        fake.kill(Component.literal("Chill Zone bot removed"));
        return true;
    }
}
