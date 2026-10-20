package sircow.sleeplessness.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.objectweb.asm.Opcodes;
import sircow.sleeplessness.effect.ModEffects;
import sircow.sleeplessness.other.ModPlayerLogic;
import sircow.sleeplessness.other.TempInventoryStorage;
import sircow.sleeplessness.trigger.ModTriggers;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;resetStat(Lnet/minecraft/stats/Stat;)V", ordinal = 1))
    private void sleeplessness$preventTimeSinceRestResetOnDeath(ServerPlayer instance, Stat<?> stat) {
        if (stat != Stats.CUSTOM.get(Stats.TIME_SINCE_REST)) {
            instance.resetStat(stat);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void sleeplessness$saveInventory(DamageSource source, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        TempInventoryStorage.savePlayerInventory(player);
    }

    @ModifyConstant(method = {"startSleepInBed", "lambda$startSleepInBed$0"}, constant = @Constant(doubleValue = 5.0))
    private double sleeplessness$modifyDoubleValue(double original) {
        return 3.0;
    }

    @Inject(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startSleepInBed(Lnet/minecraft/world/level/block/AbstractBedBlock;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/attribute/BedRule;Lnet/minecraft/core/BlockPos;)Lcom/mojang/datafixers/util/Either;"), cancellable = true)
    private void sleeplessness$dreamcatcherRequired(AbstractBedBlock bedBlock, BlockState bedBlockState, BedRule rule, BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
        Player.BedSleepingProblem problem = ModPlayerLogic.allowSleep((ServerPlayer)(Object)this, pos);
        if (problem != null) {
            cir.setReturnValue(Either.left(problem));
        }
    }

    @Inject(method = "setRespawnPosition", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerPlayer;respawnConfig:Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;", opcode = Opcodes.PUTFIELD))
    private void sleeplessness$triggerBedSpawnPoint(ServerPlayer.RespawnConfig respawnConfig, boolean showMessage, CallbackInfo ci) {
        if (showMessage && respawnConfig != null) {
            ModTriggers.BED_SPAWN_POINT.trigger((ServerPlayer)(Object)this);
        }
    }

    @WrapOperation(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gamerules/GameRules;get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"))
    private Object sleeplessness$modifyKeepInventoryRule(GameRules instance, GameRule<?> key, Operation<Object> original) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        Object result = original.call(instance, key);

        if (key == GameRules.KEEP_INVENTORY) {
            boolean ruleValue = false;
            if (result instanceof Boolean) ruleValue = (Boolean) result;
            return ruleValue || self.hasEffect(ModEffects.wellRestedHolder());
        }

        return result;
    }
}
