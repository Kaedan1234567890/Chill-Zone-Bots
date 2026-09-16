package com.chillzone.bots;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChillZoneBots implements ModInitializer {
    public static final String MOD_ID = "chillzonebots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Chill Zone Bots V1 loading: MapleCrate foundation test.");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("bots")
                    .requires(source -> source.hasPermission(2))

                    .then(Commands.literal("spawn")
                            .then(Commands.literal("MapleCrate")
                                    .executes(context -> {
                                        ServerPlayer caller;
                                        try {
                                            caller = context.getSource().getPlayerOrException();
                                        } catch (Exception ex) {
                                            context.getSource().sendFailure(
                                                    Component.literal("Run this command in-game for V1 so MapleCrate has a spawn position.")
                                            );
                                            return 0;
                                        }

                                        boolean started = BotManager.spawnAt(
                                                context.getSource().getServer(),
                                                caller,
                                                BotRoster.MAPLE_CRATE
                                        );

                                        if (!started) {
                                            context.getSource().sendFailure(
                                                    Component.literal("MapleCrate is already online or Carpet could not start the spawn.")
                                            );
                                            return 0;
                                        }

                                        context.getSource().sendSuccess(
                                                () -> Component.literal("Spawning MapleCrate — Team 1 Surface Gatherer."),
                                                true
                                        );
                                        return Command.SINGLE_SUCCESS;
                                    })))

                    .then(Commands.literal("remove")
                            .then(Commands.literal("MapleCrate")
                                    .executes(context -> {
                                        boolean removed = BotManager.remove(
                                                context.getSource().getServer(),
                                                BotRoster.MAPLE_CRATE
                                        );

                                        if (!removed) {
                                            context.getSource().sendFailure(
                                                    Component.literal("MapleCrate is not currently online as a Carpet fake player.")
                                            );
                                            return 0;
                                        }

                                        context.getSource().sendSuccess(
                                                () -> Component.literal("MapleCrate removed. Minecraft/Carpet player data will be reused next spawn."),
                                                true
                                        );
                                        return Command.SINGLE_SUCCESS;
                                    })))

                    .then(Commands.literal("status")
                            .executes(context -> {
                                boolean online = BotManager.isOnline(
                                        context.getSource().getServer(),
                                        BotRoster.MAPLE_CRATE
                                );
                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "MapleCrate | Team 1 | Surface Gatherer | " +
                                                        (online ? "ONLINE" : "OFFLINE")
                                        ),
                                        false
                                );
                                return Command.SINGLE_SUCCESS;
                            }))
            );
        });
    }
}
