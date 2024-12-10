package dev.snowsylph.zerotick.mixin;

import net.minecraft.util.math.random.Random;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.snowsylph.zerotick.config.Config;

@Mixin(BambooBlock.class)
public class BambooTickMixin {
    @Shadow
    protected void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {}

    @Inject(at = @At("TAIL"), method = "scheduledTick")
    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random, CallbackInfo info) {
        if (!Config.config().isPluginEnabled) {
            return;
        }

        if (!Config.config().tickableBamboo) {
            return;
        }

        if (world.isAir(pos.down())) {
            return;
        }

        this.randomTick(state, world, pos, random);
    }
}