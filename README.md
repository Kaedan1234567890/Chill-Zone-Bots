# Chill Zone Bots — V1

Target:
- Minecraft 26.2
- Fabric Loader 0.19.3
- Java 25
- Fabric API 0.160.0+26.2
- Carpet 26.2
- server-side only

V1 deliberately tests only the foundation.

## Test bot

MapleCrate
- Team: Team 1
- Role: Surface Gatherer

The complete nine-bot roster is already recorded in `BotRoster.java`, but V1 only allows MapleCrate to spawn. This keeps the first server test small.

## Commands

`/bots spawn MapleCrate`

Spawns MapleCrate at the command-running player's current location as a Carpet fake player.

`/bots remove MapleCrate`

Disconnects MapleCrate through Carpet.

`/bots status`

Shows whether MapleCrate is online.

Commands currently require vanilla permission level 2 (OP). LuckPerms nodes can be added after the foundation is proven.

## Persistence

Carpet fake players use Minecraft player data. V1 intentionally relies on that existing save path rather than inventing a second inventory format.

Test:
1. Spawn MapleCrate.
2. Give MapleCrate a few recognizable items.
3. Move MapleCrate somewhere else.
4. Remove MapleCrate with `/bots remove MapleCrate`.
5. Spawn MapleCrate again.
6. Verify inventory/equipment and saved player state behave correctly on the real server.

Important: V1 is a source build. GitHub Actions is the authoritative compile test. It has NOT been claimed as compile-confirmed before Actions is green.

## Not in V1

No AI, mining, pathfinding, fighting, building, schedules, team backup, automatic restart restoration, or relationship memory yet.
