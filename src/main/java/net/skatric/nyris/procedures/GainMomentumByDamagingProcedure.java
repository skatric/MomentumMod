package net.skatric.nyris.procedures;

import net.skatric.nyris.network.NyrisModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GainMomentumByDamagingProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getAmount());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		execute(null, damagesource, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		if (damagesource == null || entity == null || sourceentity == null)
			return;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("minecraft:hostile")))) {
			if ((sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum < 100) {
				{
					double _setval = (sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum + 5;
					sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.momentum = _setval;
						capability.syncPlayerVariables(sourceentity);
					});
				}
			}
		}
		if ((sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum == 100) {
			{
				double _setval = 60;
				sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.momentum = _setval;
					capability.syncPlayerVariables(sourceentity);
				});
			}
			entity.hurt(damagesource, (float) amount);
		}
		if ((sourceentity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum >= 60) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, false));
		}
		if (sourceentity instanceof Player) {
			sourceentity.getPersistentData().putBoolean("Combat", true);
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, false));
		}
	}
}
