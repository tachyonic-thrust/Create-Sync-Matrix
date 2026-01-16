package com.create_sync_matrix;

import com.create_sync_matrix.config.CSMCommonConfig;
import com.create_sync_matrix.registry.ModBlockEntities;
import com.create_sync_matrix.registry.ModBlocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Entry point for the Create-Sync compatibility mod.
 *
 * <p>This class wires the Forge event bus, registers content, and exposes
 * the config that lets players tune how much rotational power is turned
 * into piggawatts per tick. Almost every method here is intentionally
 * verbose and heavily commented to make it clear which lifecycle hook
 * does what and how to extend or adjust behavior later.</p>
 */
@Mod(CreateSyncMatrix.MOD_ID)
public class CreateSyncMatrix {
	/**
	 * The mod id is used everywhere Forge needs a namespace. If you need to
	 * rename the mod, update this constant as well as the values inside
	 * {@code mods.toml} and the resource folder layout under
	 * {@code src/main/resources/assets}.
	 */
	public static final String MOD_ID = "create_sync_matrix";

	public CreateSyncMatrix() {
		// Forge splits events between the mod-specific bus (content loading)
		// and the global bus (runtime events like world ticks). Most content
		// registrations must happen on the mod bus.
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register everything that needs to be available in registries.
		ModBlocks.BLOCKS.register(modBus);
		ModBlocks.ITEMS.register(modBus);
		ModBlockEntities.BLOCK_ENTITIES.register(modBus);

		// Hook into the global event bus if you need gameplay events later.
		MinecraftForge.EVENT_BUS.register(this);

		// Expose the common config so players/server owners can tune
		// piggawatt production numbers and internal buffer sizes.
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CSMCommonConfig.SPEC);
	}
}
