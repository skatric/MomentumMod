package net.skatric.nyris.procedures;

import net.skatric.nyris.network.NyrisModVariables;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class MomentumTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum > 100) {
			{
				double _setval = 100;
				entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.momentum = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
		if ((entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum < 0) {
			{
				double _setval = 0;
				entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.momentum = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal((Math.round((entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum) + "/100")), true);
		if ((entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum >= 60) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 2, false, false));
		}
		if (entity.getPersistentData().getBoolean("Combat")) {
			entity.getPersistentData().putDouble("Combat_timer", (entity.getPersistentData().getDouble("Combat_timer") + 1));
			if (entity.getPersistentData().getDouble("Combat_timer") >= 60) {
				entity.getPersistentData().putDouble("Combat_timer", 0);
				entity.getPersistentData().putBoolean("Combat", false);
			}
		}
		if (!entity.getPersistentData().getBoolean("Combat")) {
			entity.getPersistentData().putDouble("timer", (entity.getPersistentData().getDouble("timer") + 1));
			if (entity.getPersistentData().getDouble("timer") >= 60) {
				entity.getPersistentData().putDouble("timer", 0);
				{
					double _setval = (entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new NyrisModVariables.PlayerVariables())).momentum - 1;
					entity.getCapability(NyrisModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.momentum = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
			}
		} else {
			entity.getPersistentData().putDouble("timer", 0);
		}
	}
}
