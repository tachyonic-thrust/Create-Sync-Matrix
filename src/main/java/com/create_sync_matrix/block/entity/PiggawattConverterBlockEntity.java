package com.create_sync_matrix.block.entity;

import com.create_sync_matrix.block.PiggawattConverterBlock;
import com.create_sync_matrix.config.CSMCommonConfig;
import com.create_sync_matrix.registry.ModBlockEntities;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Block entity that performs the rotational → piggawatt conversion.
 */
public class PiggawattConverterBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
	/**
	 * Internal buffer used to avoid losing generated piggawatts before we have
	 * somewhere to send them.
	 */
	private final ConverterEnergyStorage energy;

	/**
	 * Capability wrapper so adjacent blocks can pull power using Forge's
	 * standard energy capability. Sync currently uses piggawatts internally;
	 * we expose them through this capability so the Shell Constructor can pull
	 * the charge without any custom API calls.
	 */
	private LazyOptional<IEnergyStorage> energyCapability;

	public PiggawattConverterBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.PIGGAWATT_CONVERTER.get(), pos, state);
		this.energy = new ConverterEnergyStorage(
				CSMCommonConfig.MAX_BUFFER.get(),
				CSMCommonConfig.MAX_OUTPUT_PER_TICK.get());
		this.energyCapability = LazyOptional.of(() -> energy);
	}

	@Override
	public void tick() {
		// Only run conversion server-side so we do not double-generate power.
		if (level == null || level.isClientSide) {
			return;
		}

		super.tick();

		// Generate piggawatts from the current rotational speed. Speed is signed
		// in Create, so we use the absolute value and ignore direction.
		float speed = Math.abs(getSpeed());
		if (speed > 0) {
			int generated = (int) Math.round(speed * CSMCommonConfig.PIGGAWATT_PER_SPEED.get());
			int accepted = energy.receiveEnergy(generated, false);
			if (accepted > 0) {
				setChanged();
			}
		}

		// Try to push power out of the block each tick so adjacent blocks can
		// grab power even if the buffer is filling faster than they drain it.
		ejectEnergy();
	}

	private void ejectEnergy() {
		Direction outputDirection = getBlockState().getValue(PiggawattConverterBlock.FACING);
		if (level == null) {
			return;
		}

		BlockPos targetPos = worldPosition.relative(outputDirection);
		if (!level.isLoaded(targetPos)) {
			return;
		}

		var targetEntity = level.getBlockEntity(targetPos);
		if (targetEntity == null) {
			return;
		}

		targetEntity.getCapability(ForgeCapabilities.ENERGY, outputDirection.getOpposite())
				.ifPresent(target -> {
					int transferable = Math.min(energy.getMaxExtract(), energy.getEnergyStored());
					if (transferable <= 0) {
						return;
					}

					int extracted = energy.extractEnergy(transferable, true);
					int received = target.receiveEnergy(extracted, false);
					energy.extractEnergy(received, false);
					if (received > 0) {
						setChanged();
					}
				});
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		energyCapability.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		energyCapability = LazyOptional.of(() -> energy);
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ENERGY && (side == null || side == getBlockState().getValue(PiggawattConverterBlock.FACING))) {
			return energyCapability.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putInt("Energy", energy.getEnergyStored());
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		energy.setEnergy(tag.getInt("Energy"));
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		// Display current buffer contents and the configured conversion rate so
		// players can understand performance without opening a GUI.
		LangBuilder header = Lang.builder()
				.text("Piggawatt Buffer");
		tooltip.add(header.component());

		LangBuilder bufferLine = Lang.builder()
				.text(energy.getEnergyStored() + " / " + energy.getMaxEnergyStored() + " pw stored");
		tooltip.add(bufferLine.component());

		LangBuilder rateLine = Lang.builder()
				.text("Per speed point: ")
				.text(String.format("%.2f pw/t", CSMCommonConfig.PIGGAWATT_PER_SPEED.get()));
		tooltip.add(rateLine.component());
		return true;
	}

	/**
	 * Helper energy storage that exposes setters and configurable per-tick
	 * limits to keep the math centralized.
	 */
	private static class ConverterEnergyStorage extends EnergyStorage {
		public ConverterEnergyStorage(int capacity, int maxTransfer) {
			// Allow large bursts of incoming generation but restrict outbound flow
			// via the configured max transfer to avoid overwhelming neighbors.
			super(capacity, capacity, maxTransfer);
		}

		public void setEnergy(int energy) {
			this.energy = Math.min(energy, getMaxEnergyStored());
		}

		public int getMaxExtract() {
			return maxExtract;
		}
	}
}
