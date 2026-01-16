package com.create_sync_matrix.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Configuration entries for the Create ⇄ Sync bridge.
 *
 * <p>All values live in the server/common config so worlds and servers are
 * deterministic. Clients will read the synced values from servers.</p>
 */
public class CSMCommonConfig {
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.IntValue MAX_BUFFER;
	public static final ForgeConfigSpec.IntValue MAX_OUTPUT_PER_TICK;
	public static final ForgeConfigSpec.DoubleValue PIGGAWATT_PER_SPEED;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.comment(
				"Kinetic→piggawatt conversion settings.",
				"Values are intentionally generous by default so you can test the block quickly.",
				"Everything is per tick unless otherwise stated.")
				.push("converter");

		MAX_BUFFER = builder
				.comment("How many piggawatts the converter can hold internally.")
				.defineInRange("maxBuffer", 50000, 1000, 10_000_000);

		MAX_OUTPUT_PER_TICK = builder
				.comment("Maximum piggawatts sent to adjacent blocks per tick.")
				.defineInRange("maxOutputPerTick", 2000, 10, 50_000);

		PIGGAWATT_PER_SPEED = builder
				.comment(
					"How many piggawatts are generated each tick per point of Create rotational speed.",
					"Speed is the absolute RPM-like speed value Create exposes on kinetic blocks.",
					"Higher values make the block more efficient; lower values encourage bigger gear ratios.")
				.defineInRange("piggawattPerSpeed", 25.0D, 0.1D, 10_000D);

		builder.pop();

		SPEC = builder.build();
	}

	private CSMCommonConfig() {
		// Utility class; no instances.
	}
}
