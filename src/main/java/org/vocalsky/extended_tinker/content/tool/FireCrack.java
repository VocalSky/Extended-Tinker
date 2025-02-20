package org.vocalsky.extended_tinker.content.tool;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FireCrack extends ModifiableItem {
    public static final String TAG_FIREWORKS = "Fireworks";
    public static final String TAG_EXPLOSION = "Explosion";
    public static final String TAG_EXPLOSIONS = "Explosions";
    public static final String TAG_FLIGHT = "Flight";
    public static final String TAG_EXPLOSION_TYPE = "Type";
    public static final String TAG_EXPLOSION_TRAIL = "Trail";
    public static final String TAG_EXPLOSION_FLICKER = "Flicker";
    public static final String TAG_EXPLOSION_COLORS = "Colors";
    public static final String TAG_EXPLOSION_FADECOLORS = "FadeColors";
    public static final double ROCKET_PLACEMENT_OFFSET = 0.15;

    public FireCrack(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();
            Vec3 clickLocation = context.getClickLocation();
            Direction clickedFace = context.getClickedFace();
            FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(level, context.getPlayer(), clickLocation.x + (double)clickedFace.getStepX() * 0.15, clickLocation.y + (double)clickedFace.getStepY() * 0.15, clickLocation.z + (double)clickedFace.getStepZ() * 0.15, stack);
            level.addFreshEntity(fireworkRocketEntity);
            stack.hurtAndBreak(1, Objects.requireNonNull(context.getPlayer()), (player) -> player.broadcastBreakEvent(context.getHand()));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (player.isFallFlying()) {
            ItemStack stack = player.getItemInHand(hand);
            if (!level.isClientSide) {
                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(level, stack, player);
                level.addFreshEntity(fireworkRocketEntity);
                if (!player.getAbilities().instabuild)
                    stack.hurtAndBreak(1, Objects.requireNonNull(player), (player1) -> player1.broadcastBreakEvent(hand));
                player.awardStat(Stats.ITEM_USED.get(this));
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level p_41212_, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        CompoundTag tag = stack.getTagElement(TAG_FIREWORKS);
        if (tag != null) {
            if (tag.contains(TAG_FLIGHT, 99)) {
                tooltip.add(Component.translatable("item.minecraft.firework_rocket.flight").append(" ").append(String.valueOf(tag.getByte(TAG_FLIGHT))).withStyle(ChatFormatting.GRAY));
            }
            ListTag explosions = tag.getList(TAG_EXPLOSIONS, 10);
            if (!explosions.isEmpty()) {
                for(int i = 0; i < explosions.size(); ++i) {
                    CompoundTag compound = explosions.getCompound(i);
                    List<Component> starTooltip = Lists.newArrayList();
                    FireworkStarItem.appendHoverText(compound, starTooltip);
                    if (!starTooltip.isEmpty()) {
                        for(int j = 1; j < starTooltip.size(); ++j)
                            starTooltip.set(j, Component.literal("  ").append(starTooltip.get(j)).withStyle(ChatFormatting.GRAY));
                        tooltip.addAll(starTooltip);
                    }
                }
            }

        }
    }

    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putByte(TAG_FLIGHT, (byte)1);
        return stack;
    }

    public enum Shape {
        SMALL_BALL(0, "small_ball"),
        LARGE_BALL(1, "large_ball"),
        STAR(2, "star"),
        CREEPER(3, "creeper"),
        BURST(4, "burst");

        private static final Shape[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt((p_41240_) -> p_41240_.id)).toArray(Shape[]::new);
        private final int id;
        private final String name;

        Shape(int p_41234_, String p_41235_) {
            this.id = p_41234_;
            this.name = p_41235_;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static Shape byId(int id) {
            return id >= 0 && id < BY_ID.length ? BY_ID[id] : SMALL_BALL;
        }
    }
}