package org.vocalsky.extended_tinker.common.tool;

import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.entity.FirecrackEntity;
import org.vocalsky.extended_tinker.common.entity.FireworkRocket;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableArrowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.entity.ModifiableArrow;
import slimeknights.tconstruct.tools.entity.ThrownShuriken;

import java.util.Objects;

public class FireworkRocketItem extends ModifiableArrowItem {
    public FireworkRocketItem(Properties props, ToolDefinition toolDefinition) {
        super(props, toolDefinition);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        FireworkRocket rocket = new FireworkRocket(level, shooter);
        rocket.onCreate(stack, shooter);
        return rocket;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack1 = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack stack2 = player.getItemInHand(InteractionHand.OFF_HAND);
        if (hand.equals(InteractionHand.MAIN_HAND) && stack1.is(ModCore.Tools.FIREWORK_ROCKET.get()) && stack2.is(ModCore.Tags.FIREWORK_FLINT)) {
//            level.playSound(null, player.getX(), player.getY(), player.getZ(), Sounds.SHURIKEN_THROW.getSound(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
//            player.getCooldowns().addCooldown(stack1.getItem(), 10);
            if (!level.isClientSide()) {
                FireworkRocket rocket = new FireworkRocket(level, player);
                IToolStackView tool = rocket.onCreate(stack1, player);
                float velocity = ConditionalStatModifierHook.getModifiedStat(tool, player, ToolStats.VELOCITY);
//                rocket.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 1.0F);
                double x = player.getLookAngle().x;
                double y = player.getLookAngle().y;
                double z = player.getLookAngle().z;
                rocket.shoot(player.getX() + x * 1.5,player.getY() + 0.6 * player.getBbHeight() + y * 1.5, player.getZ() + z * 1.5, velocity, 1.0F);
//                FireworkRocket rocket = new FireworkRocket(level, player, player.getX() + x * 1.5,player.getY() + 0.6 * player.getBbHeight() + y * 1.5, player.getZ() + z * 1.5, stack1, true);
//                rocket.setOwner(player);
//                rocket.setDeltaMovement(player.getLookAngle().scale(tool.getModifierLevel(ModifierIds.reach) + 1));
                level.addFreshEntity(rocket);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack1.shrink(1);
                stack2.hurtAndBreak(1, Objects.requireNonNull(player), (p) -> p.broadcastBreakEvent(hand));
            }

            return InteractionResultHolder.sidedSuccess(stack1, level.isClientSide());
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
    }
}
