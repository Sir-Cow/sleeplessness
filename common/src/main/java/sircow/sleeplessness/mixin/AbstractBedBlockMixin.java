package sircow.sleeplessness.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Unit;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AbstractBedBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sircow.sleeplessness.other.ModPlayerLogic;

@Mixin(AbstractBedBlock.class)
public abstract class AbstractBedBlockMixin extends HorizontalDirectionalBlock {
    protected AbstractBedBlockMixin(Properties properties) {
        super(properties);
    }

    @WrapOperation(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startSleepInBed(Lnet/minecraft/world/level/block/AbstractBedBlock;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/attribute/BedRule;Lnet/minecraft/core/BlockPos;)Lcom/mojang/datafixers/util/Either;"))
    private Either<Player.BedSleepingProblem, Unit> sleeplessness$damageDreamcatcherOnSleep(Player player, AbstractBedBlock bedBlock, BlockState state, BedRule rule, BlockPos pos, Operation<Either<Player.BedSleepingProblem, Unit>> original) {
        Either<Player.BedSleepingProblem, Unit> result = original.call(player, bedBlock, state, rule, pos);

        if (result.right().isPresent()) ModPlayerLogic.damageDreamcatcher(player);
        return result;
    }
}
