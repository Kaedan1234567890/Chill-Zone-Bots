package com.chillzone.bots;

import carpet.CarpetSettings;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChillZoneBots implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("chillzonebots");

    @Override public void onInitialize() {
        LOGGER.info("Chill Zone Bots V3 loading: identity/presentation test.");
        // Carpet 26.2 normally hides fake players from the multiplayer server-list sample.
        // V3 deliberately enables Carpet's supported listing path so the server can expose
        // the fake-player GameProfile names instead of anonymous placeholders.
        CarpetSettings.allowListingFakePlayers = true;
        LOGGER.info("Carpet allowListingFakePlayers enabled by Chill Zone Bots.");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            var root=Commands.literal("bots");
            var spawn=Commands.literal("spawn");
            var leave=Commands.literal("leave");
            var remove=Commands.literal("remove");

            for (BotProfile bot: BotRoster.ALL) {
                spawn.then(Commands.literal(bot.username()).executes(c -> spawnOne(c,bot)));
                leave.then(Commands.literal(bot.username()).executes(c -> removeOne(c,bot)));
                remove.then(Commands.literal(bot.username()).executes(c -> removeOne(c,bot)));
            }
            for (String team: new String[]{"team1","team2","team3"}) {
                spawn.then(Commands.literal(team).executes(c -> spawnGroup(c,team)));
                leave.then(Commands.literal(team).executes(c -> removeGroup(c,team)));
                remove.then(Commands.literal(team).executes(c -> removeGroup(c,team)));
            }
            spawn.then(Commands.literal("all").executes(this::spawnAll));
            leave.then(Commands.literal("all").executes(this::removeAll));
            remove.then(Commands.literal("all").executes(this::removeAll));
            root.then(spawn); root.then(leave); root.then(remove);
            root.then(Commands.literal("status").executes(this::status));
            dispatcher.register(root);
        });
    }

    private int spawnOne(CommandContext<CommandSourceStack> c, BotProfile bot) {
        ServerPlayer caller;
        try { caller=c.getSource().getPlayerOrException(); }
        catch(Exception e){ c.getSource().sendFailure(Component.literal("Run spawn commands in-game for V2.")); return 0; }
        if(!BotManager.spawnAt(c.getSource().getServer(),caller,bot)){
            c.getSource().sendFailure(Component.literal(bot.username()+" is already online or could not spawn.")); return 0;
        }
        c.getSource().sendSuccess(() -> Component.literal("Spawned "+bot.username()+" | "+team(bot.team())+" | "+role(bot.role())), false);
        return Command.SINGLE_SUCCESS;
    }

    private int spawnGroup(CommandContext<CommandSourceStack> c,String team) {
        ServerPlayer caller;
        try { caller=c.getSource().getPlayerOrException(); }
        catch(Exception e){ c.getSource().sendFailure(Component.literal("Run spawn commands in-game for V2.")); return 0; }
        int n=0;
        for(BotProfile b:BotRoster.ALL) if(b.team().equals(team)&&BotManager.spawnAt(c.getSource().getServer(),caller,b)) n++;
        int x=n; c.getSource().sendSuccess(() -> Component.literal("Spawned "+x+" bot(s) from "+team(team)+"."),false);
        return n>0?Command.SINGLE_SUCCESS:0;
    }

    private int spawnAll(CommandContext<CommandSourceStack> c) {
        ServerPlayer caller;
        try { caller=c.getSource().getPlayerOrException(); }
        catch(Exception e){ c.getSource().sendFailure(Component.literal("Run /bots spawn all in-game for V2.")); return 0; }
        int n=0; for(BotProfile b:BotRoster.ALL) if(BotManager.spawnAt(c.getSource().getServer(),caller,b)) n++;
        int x=n; c.getSource().sendSuccess(() -> Component.literal("Spawned "+x+" Chill Zone bot(s)."),false);
        return n>0?Command.SINGLE_SUCCESS:0;
    }

    private int removeOne(CommandContext<CommandSourceStack> c,BotProfile b) {
        if(!BotManager.remove(c.getSource().getServer(),b)){ c.getSource().sendFailure(Component.literal(b.username()+" is not online as a managed Carpet bot.")); return 0; }
        c.getSource().sendSuccess(() -> Component.literal(b.username()+" left the server."),false);
        return Command.SINGLE_SUCCESS;
    }

    private int removeGroup(CommandContext<CommandSourceStack> c,String team) {
        int n=0; for(BotProfile b:BotRoster.ALL) if(b.team().equals(team)&&BotManager.remove(c.getSource().getServer(),b)) n++;
        int x=n; c.getSource().sendSuccess(() -> Component.literal("Removed "+x+" bot(s) from "+team(team)+"."),false);
        return n>0?Command.SINGLE_SUCCESS:0;
    }

    private int removeAll(CommandContext<CommandSourceStack> c) {
        int n=0; for(BotProfile b:BotRoster.ALL) if(BotManager.remove(c.getSource().getServer(),b)) n++;
        int x=n; c.getSource().sendSuccess(() -> Component.literal("Removed "+x+" Chill Zone bot(s)."),false);
        return Command.SINGLE_SUCCESS;
    }

    private int status(CommandContext<CommandSourceStack> c) {
        StringBuilder s=new StringBuilder("Chill Zone Bots: "); boolean first=true;
        for(BotProfile b:BotRoster.ALL){ if(!first)s.append(" | "); first=false; s.append(b.username()).append(":").append(BotManager.isOnline(c.getSource().getServer(),b)?"ON":"OFF"); }
        c.getSource().sendSuccess(() -> Component.literal(s.toString()),false); return Command.SINGLE_SUCCESS;
    }
    private static String team(String s){return switch(s){case "team1"->"Team 1";case "team2"->"Team 2";case "team3"->"Team 3";default->s;};}
    private static String role(String s){return switch(s){case "surface_gatherer"->"Surface Gatherer";case "miner"->"Miner";case "fighter"->"Fighter";default->s;};}
}
