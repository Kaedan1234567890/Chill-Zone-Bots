package com.chillzone.bots;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BotRoster {
    private BotRoster() {}

    // V3 identities: intentionally uncommon names to reduce collision risk with real accounts.
    // All names are <= 16 characters.
    public static final BotProfile MAPLE_CRATE =
            new BotProfile("MapleCrate73", "team1", "surface_gatherer");

    public static final List<BotProfile> ALL = List.of(
            MAPLE_CRATE,
            new BotProfile("CopperFox271", "team1", "miner"),
            new BotProfile("RiftKnight82", "team1", "fighter"),

            new BotProfile("PixelMoth47", "team2", "surface_gatherer"),
            new BotProfile("MossyPickaxe91", "team2", "miner"),
            new BotProfile("VexRaptor63", "team2", "fighter"),

            new BotProfile("BirchByte58", "team3", "surface_gatherer"),
            new BotProfile("FrostAxolotl72", "team3", "miner"),
            new BotProfile("RedstoneRily84", "team3", "fighter")
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
