package dev.snowsylph.zerotick.mixin;

import dev.snowsylph.zerotick.config.Config;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractPlantPartBlock.class)
interface AbstractPlantPartBlockAccessor {
    @Accessor("growthDirection")
    Direction getGrowthDirection();
}

@Mixin(AbstractPlantStemBlock.class)
interface AbstractPlantStemBlockAccessor {
    @Accessor("AGE")
    IntProperty getAge();

    @Accessor("growthChance")
    double getGrowthChance();
}

@Mixin(AbstractPlantStemBlock.class)
public abstract class AbstractPlantStemMixin extends AbstractPlantPartBlock {
    protected AbstractPlantStemMixin(Settings settings, Direction growthDirection, VoxelShape outlineShape, boolean tickWater) {
        super(settings, growthDirection, outlineShape, tickWater);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        }

        if (!Config.config().isPluginEnabled)
        {
            return;
        }

        if (!Config.config().tickableAbstract)
        {
            return;
        }

        try
        {
            Block block = state.getBlock();

            Direction growthDirection = ((AbstractPlantPartBlockAccessor) block).getGrowthDirection();
            IntProperty age = ((AbstractPlantStemBlockAccessor) block).getAge();
            double growthChance = ((AbstractPlantStemBlockAccessor) block).getGrowthChance();

            switch (((AbstractPlantPartBlockAccessor) block).getGrowthDirection())
            {
                case Direction.UP -> {
                    if (world.isAir(pos.down()))
                        return;
                }
                case Direction.DOWN -> {
                    if (world.isAir(pos.up()))
                        return;
                }
                default -> {}
            }

            if (state.get(age) < 25 && random.nextDouble() < growthChance)
            {
                BlockPos blockPos = pos.offset(((AbstractPlantPartBlockAccessor) block).getGrowthDirection());
                if (world.getBlockState(blockPos).isAir())
                {
                    world.setBlockState(blockPos, state.cycle(age));
                }
            }
        }
        catch (ClassCastException ignored) {}
    }
}
