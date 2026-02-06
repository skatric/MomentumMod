package net.skatric.nyris.procedures;

import net.skatric.nyris.network.NyrisModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GainMomentumByKillingProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(Entity entity, Entity sourceentity) {
		execute(null, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("minecraft:hostile")))) {
			if ((sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum < 100) {
				{
					double _setval = (sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum + 10;
					sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.momentum = _setval;
						capability.syncPlayerVariables(sourceentity);
					});
				}
			}
		}
	}
}
