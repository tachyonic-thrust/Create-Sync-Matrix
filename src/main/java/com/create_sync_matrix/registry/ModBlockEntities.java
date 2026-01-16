package com.create_sync_matrix.registry;

import com.create_sync_matrix.CreateSyncMatrix;
import com.create_sync_matrix.block.entity.PiggawattConverterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Block entity registrations live here to keep the main mod class clean.
 */
public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateSyncMatrix.MOD_ID);

	public static final RegistryObject<BlockEntityType<PiggawattConverterBlockEntity>> PIGGAWATT_CONVERTER =
			BLOCK_ENTITIES.register("piggawatt_converter", () ->
					BlockEntityType.Builder.of(PiggawattConverterBlockEntity::new, ModBlocks.PIGGAWATT_CONVERTER.get())
						.build(null));
}
