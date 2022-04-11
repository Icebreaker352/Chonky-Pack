package net.mcreator.chonkypack.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.Entity;

import net.mcreator.chonkypack.item.HairBallItem;
import net.mcreator.chonkypack.ChonkyPackModElements;
import net.mcreator.chonkypack.ChonkyPackMod;

import java.util.Map;
import java.util.HashMap;

@ChonkyPackModElements.ModElement.Tag
public class HairBallSpawnProcedure extends ChonkyPackModElements.ModElement {
	public HairBallSpawnProcedure(ChonkyPackModElements instance) {
		super(instance, 2);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				ChonkyPackMod.LOGGER.warn("Failed to load dependency entity for procedure HairBallSpawn!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				ChonkyPackMod.LOGGER.warn("Failed to load dependency x for procedure HairBallSpawn!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				ChonkyPackMod.LOGGER.warn("Failed to load dependency y for procedure HairBallSpawn!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				ChonkyPackMod.LOGGER.warn("Failed to load dependency z for procedure HairBallSpawn!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				ChonkyPackMod.LOGGER.warn("Failed to load dependency world for procedure HairBallSpawn!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		if ((entity instanceof CatEntity)) {
			new Object() {
				private int ticks = 0;
				private float waitTicks;
				private IWorld world;
				public void start(IWorld world, int waitTicks) {
					this.waitTicks = waitTicks;
					MinecraftForge.EVENT_BUS.register(this);
					this.world = world;
				}

				@SubscribeEvent
				public void tick(TickEvent.ServerTickEvent event) {
					if (event.phase == TickEvent.Phase.END) {
						this.ticks += 1;
						if (this.ticks >= this.waitTicks)
							run();
					}
				}

				private void run() {
					if (world instanceof World && !world.isRemote()) {
						ItemEntity entityToSpawn = new ItemEntity((World) world, x, y, z, new ItemStack(HairBallItem.block, (int) (1)));
						entityToSpawn.setPickupDelay((int) 10);
						world.addEntity(entityToSpawn);
					}
					MinecraftForge.EVENT_BUS.unregister(this);
				}
			}.start(world, (int) 20);
		}
	}

	@SubscribeEvent
	public void onEntityTick(LivingEvent.LivingUpdateEvent event) {
		Entity entity = event.getEntityLiving();
		World world = entity.world;
		double i = entity.getPosX();
		double j = entity.getPosY();
		double k = entity.getPosZ();
		Map<String, Object> dependencies = new HashMap<>();
		dependencies.put("x", i);
		dependencies.put("y", j);
		dependencies.put("z", k);
		dependencies.put("world", world);
		dependencies.put("entity", entity);
		dependencies.put("event", event);
		this.executeProcedure(dependencies);
	}
}
