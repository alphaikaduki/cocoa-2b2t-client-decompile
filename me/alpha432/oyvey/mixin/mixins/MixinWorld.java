package me.alpha432.oyvey.mixin.mixins;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ World.class})
public class MixinWorld {

    @Redirect(
        method = { "getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"
            )
    )
    public void getEntitiesOfTypeWithinAABBHook(Chunk chunk, Class entityClass, AxisAlignedBB aabb, List listToFill, Predicate filter) {
        try {
            chunk.getEntitiesOfTypeWithinAABB(entityClass, aabb, listToFill, filter);
        } catch (Exception exception) {
            ;
        }

    }

    @Inject(
        method = { "onEntityAdded"},
        at = {             @At("HEAD")}
    )
    private void onEntityAdded(Entity entityIn, CallbackInfo ci) {}
}
