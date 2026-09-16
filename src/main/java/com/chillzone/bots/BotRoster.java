package com.chillzone.bots;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BotRoster {
    private BotRoster() {}

    public static final BotProfile MAPLE_CRATE =
            new BotProfile("MapleCrate", "team1", "surface_gatherer");

    public static final List<BotProfile> ALL = List.of(
            MAPLE_CRATE,
            new BotProfile("CopperFox27", "team1", "miner"),
            new BotProfile("RiftKnight", "team1", "fighter"),
            new BotProfile("PixelMoth", "team2", "surface_gatherer"),
            new BotProfile("MossyPickaxe", "team2", "miner"),
            new BotProfile("VexRaptor", "team2", "fighter"),
            new BotProfile("BirchByte", "team3", "surface_gatherer"),
            new BotProfile("FrostyAxolotl", "team3", "miner"),
            new BotProfile("RedstoneRiley", "team3", "fighter")
    );

    public static final Map<String, BotProfile> BY_NAME = makeMap();

    private static Map<String, BotProfile> makeMap() {
        Map<String, BotProfile> map = new LinkedHashMap<>();
        for (BotProfile profile : ALL) {
            map.put(profile.username().toLowerCase(), profile);
        }
        return Map.copyOf(map);
    }
}
