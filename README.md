# Create Sync Matrix

A Forge 1.20.1 addon that converts Create's rotational power into Sync's piggawatts for the Shell Constructor. The mod depends on Forge 47.4.10, Create 6.0.8 (Forge), and Sync (Forge) 0.1.8.

## Building
1. Ensure JDK 17+ is installed.
2. Because binary files are excluded from this repo, regenerate the Gradle wrapper jar with your system Gradle (8.x recommended): `gradle wrapper --gradle-version 8.14.3`.
3. Drop your exact mod jars into `libs/` so Gradle resolves Create 6.0.8 and Sync 0.1.8 locally (see `libs/README.txt` for filenames). Use the exact names `create-1.20.1-6.0.8.jar` and `Sync-0.1.8.jar`.
4. Run `./gradlew build` to produce the mod jar under `build/libs`.

## Gameplay overview
Place the Piggawatt Converter so its output face points at a Sync Shell Constructor. Connect a Create shaft to any side along the block's axis (front/back) to feed rotational speed. The block stores a configurable buffer of piggawatts, pushes energy toward its facing each tick, and exposes Create goggle tooltips for live diagnostics.

## Configuration
- `converter.maxBuffer`: internal piggawatt buffer size.
- `converter.maxOutputPerTick`: maximum piggawatts sent to neighbors per tick.
- `converter.piggawattPerSpeed`: piggawatts produced per point of rotational speed each tick.

All values live in the synced common config generated under the `config` folder after first launch.
