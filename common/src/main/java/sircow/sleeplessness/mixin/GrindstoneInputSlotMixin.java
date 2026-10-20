package sircow.sleeplessness.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.sleeplessness.item.ModItems;

@Mixin(targets = {"net.minecraft.world.inventory.GrindstoneMenu$2", "net.minecraft.world.inventory.GrindstoneMenu$3"})
public class GrindstoneInputSlotMixin {
    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void sleeplessness$dreamcatcherCannotBePlaced(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ModItems.DREAMCATCHER.get())) {
            cir.setReturnValue(false);
        }
    }
}
