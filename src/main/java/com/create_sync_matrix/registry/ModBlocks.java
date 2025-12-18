package com.create_sync_matrix.registry;

import com.create_sync_matrix.CreateSyncMatrix;
import com.create_sync_matrix.block.PiggawattConverterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Central location for block and block-item registrations.
 */
@Mod.EventBusSubscriber(modid = CreateSyncMatrix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateSyncMatrix.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateSyncMatrix.MOD_ID);

	/**
	 * The kinetic → piggawatt converter block. This is a kinetic block so it
	 * will properly interact with shafts and gearboxes from Create.
	 */
	public static final RegistryObject<Block> PIGGAWATT_CONVERTER = BLOCKS.register(
			"piggawatt_converter",
			() -> new PiggawattConverterBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.strength(3.0F, 6.0F)
					.requiresCorrectToolForDrops()
					.noOcclusion()));

	public static final RegistryObject<Item> PIGGAWATT_CONVERTER_ITEM = ITEMS.register(
			"piggawatt_converter",
			() -> new BlockItem(PIGGAWATT_CONVERTER.get(), new Item.Properties()));

	/**
	 * If you want to insert the block into an existing creative tab, listen to
	 * the BuildCreativeModeTabContentsEvent. We place it in Redstone & Gears by
	 * default so players can find it near other Create machinery.
	 */
	@SubscribeEvent
	public static void onBuildCreativeTab(net.minecraftforge.event.BuildCreativeModeTabContentsEvent event) {
		// The key is a registry entry; we can directly compare against known constants.
		if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			event.accept(PIGGAWATT_CONVERTER_ITEM);
		}
	}
}
