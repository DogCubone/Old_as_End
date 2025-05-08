package perfectvoid.mega.items;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import perfectvoid.mega.old_as_end;

public class ChorusHoney extends Item {
    private static final int USE_TIME = 40;

    public ChorusHoney(Settings settings) {
        super(settings);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity player)
            if (user.removeStatusEffect(StatusEffects.WITHER)) {
              Advancement firstAid = player.server.getAdvancementLoader().get(new Identifier(old_as_end.MOD_ID + ':' + "first_aid"));
              if (!player.getAdvancementTracker().getProgress(firstAid).isDone()){
                  player.getAdvancementTracker().grantCriterion(firstAid, "first_aid");
              }
            }

        //Gives a glass bottle to the player as remainder, also avoids giving glass bottles if in creative mode.
        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            if(!player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE)))
                player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return super.finishUsing(stack, world, user);
    }

    public static int getUseTime() {
        return USE_TIME;
    }
}
