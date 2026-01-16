package com.create_sync_matrix.block;

import com.create_sync_matrix.block.entity.PiggawattConverterBlockEntity;
import com.create_sync_matrix.registry.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

/**
 * A kinetic block that consumes Create rotational energy and exposes a custom
 * energy capability oriented toward a single side.
 */
public class PiggawattConverterBlock extends DirectionalKineticBlock implements IBE<PiggawattConverterBlockEntity> {
	public PiggawattConverterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState state) {
		// Use the axis of the facing direction so the block can spin when coupled
		// to shafts in-line with its output face.
		return state.getValue(FACING).getAxis();
	}

	@Override
	public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
		// Point the output side toward the player by default. Adjust here if you
		// prefer it to face away from the player instead.
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		// Treat like other machinery: pistons cannot move it to prevent odd
		// behavior with shaft/rotation calculations.
		return PushReaction.BLOCK;
	}

	@Override
	public Class<PiggawattConverterBlockEntity> getBlockEntityClass() {
		return PiggawattConverterBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends PiggawattConverterBlockEntity> getBlockEntityType() {
		return ModBlockEntities.PIGGAWATT_CONVERTER.get();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return IBE.super.newBlockEntity(pos, state);
	}

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		// Permit shafts to connect along the axis so rotational power flows
		// directly into the converter. We allow both front and back to keep
		// setups flexible.
		return face.getAxis() == getRotationAxis(state);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		// When the block is placed we want to immediately notify neighbors so
		// energy capability connections update right away.
		super.onPlace(state, level, pos, oldState, isMoving);
		if (!level.isClientSide) {
			level.updateNeighborsAt(pos, this);
		}
	}
}
