# Adam Camera

A simple client-side freecam for Minecraft (Fabric, 1.21.11).

## Controls
| Key | Action |
|---|---|
| **F4** | Toggle Adam Camera (rebindable in Options > Controls > Key Binds) |
| W A S D | Fly around |
| Space | Up |
| Ctrl | Down |
| **Shift** | Move faster (hold) |
| Mouse | Look around |

While active, your real player stands still and can't attack, mine or interact.
It switches off automatically if you die, change dimension or leave the world.

## Build
You need **JDK 25 (the mod itself still targets Java 21)** installed.

    ./gradlew build        (Windows: gradlew.bat build)

The mod jar is created at `build/libs/adam-camera-1.0.0.jar`.

## Install
Put the jar in `.minecraft/mods` together with **Fabric Loader 0.19.3+** and **Fabric API** for Minecraft 1.21.11.

## Test in a dev client
    ./gradlew runClient

## Tweaking
- Speeds: `NORMAL_SPEED` and `FAST_SPEED` in `AdamCamera.java` (blocks per tick, 20 ticks = 1 second).
- Versions: change `gradle.properties` (values from https://fabricmc.net/develop).
  This branch targets 1.21.11 (JDK 25 (the mod itself still targets Java 21), remapped Loom).
