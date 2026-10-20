package sircow.sleeplessness.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.sleeplessness.item.ModItems;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Shadow @Final private DataSlot cost;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void sleeplessness$disableRepairs(CallbackInfo ci) {
        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) self;
        ItemStack left = accessor.getInputSlots().getItem(0);
        ItemStack right = accessor.getInputSlots().getItem(1);

        if ((left.is(ModItems.DREAMCATCHER.get()) || right.is(ModItems.DREAMCATCHER.get())) && !right.isEmpty()) {
            accessor.getResultSlots().setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            ci.cancel();
        }
    }
}
