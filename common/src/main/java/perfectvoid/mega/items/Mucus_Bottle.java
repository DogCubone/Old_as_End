package perfectvoid.mega.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import perfectvoid.mega.Registers.ExtraRegister;

public class Mucus_Bottle extends BlockItem {
    public Mucus_Bottle(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) user.addStatusEffect(new StatusEffectInstance(ExtraRegister.GRAVITY.get(), 200));

        //Gives a glass bottle to the player as remainder, also avoids giving glass bottles if in creative mode.
        if (user instanceof PlayerEntity  player && !player.getAbilities().creativeMode) {
            if(!player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE)))
                player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        if (player != null && !player.getAbilities().creativeMode) player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
        return true;
    }
}
