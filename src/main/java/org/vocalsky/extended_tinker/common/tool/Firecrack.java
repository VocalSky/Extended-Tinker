package org.vocalsky.extended_tinker.common.tool;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.entity.FirecrackEntity;
import org.vocalsky.extended_tinker.network.PacketHandler;
import org.vocalsky.extended_tinker.network.packet.FirecrackShotPacket;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Firecrack extends ModifiableItem {

    public Firecrack(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClickEmpty);
    }

    private void LeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity() != null && event.getEntity().getMainHandItem().getItem() instanceof Firecrack)
            PacketHandler.INSTANCE.sendToServer(new FirecrackShotPacket(event.getEntity().getId()));
        else if (event.getEntity() != null && event.getEntity().getOffhandItem().getItem() instanceof Firecrack)
            PacketHandler.INSTANCE.sendToServer(new FirecrackShotPacket(event.getEntity().getId()));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer) fireworkRocketShot(serverPlayer);
        return super.onLeftClickEntity(stack, player, target);
    }

    public static void fireworkRocketShot(ServerPlayer player) {
        Level level = player.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = player.getMainHandItem();
            InteractionHand hand;
            if (!(stack.getItem() instanceof Firecrack)) {
                stack = player.getOffhandItem();
                hand = InteractionHand.OFF_HAND;
            } else
                hand = InteractionHand.MAIN_HAND;
            if (!(stack.getItem() instanceof Firecrack)) return;
            ToolStack tool = ToolStack.from(stack);
            if (tool.isBroken()) return;
            double x = player.getLookAngle().x;
            double y = player.getLookAngle().y;
            double z = player.getLookAngle().z;
            FirecrackEntity FirecrackEntity = new FirecrackEntity(level, player, player.getX() + x * 1.5,player.getY() + 0.6 * player.getBbHeight() + y * 1.5, player.getZ() + z * 1.5, stack, true);
            FirecrackEntity.setOwner(player);
            FirecrackEntity.setDeltaMovement(player.getLookAngle().scale(tool.getModifierLevel(ModifierIds.reach) + 1));
            level.addFreshEntity(FirecrackEntity);
            stack.hurtAndBreak(1, Objects.requireNonNull(player), (player1) -> player1.broadcastBreakEvent(hand));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();
            ToolStack tool = ToolStack.from(stack);
            if (!tool.isBroken()) {
                Vec3 clickLocation = context.getClickLocation();
                Direction clickedFace = context.getClickedFace();
                FirecrackEntity firecrackEntity = new FirecrackEntity(level, context.getPlayer(), clickLocation.x + (double) clickedFace.getStepX() * 0.15, clickLocation.y + (double) clickedFace.getStepY() * 0.15, clickLocation.z + (double) clickedFace.getStepZ() * 0.15, stack);
                level.addFreshEntity(firecrackEntity);
                stack.hurtAndBreak(1, Objects.requireNonNull(context.getPlayer()), (player) -> player.broadcastBreakEvent(context.getHand()));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        if (player.isFallFlying()) {
            ItemStack stack = player.getItemInHand(hand);
            ToolStack tool = ToolStack.from(stack);
            if (!level.isClientSide && !tool.isBroken()) {
                FirecrackEntity firecrackEntity = new FirecrackEntity(level, stack, player);
                level.addFreshEntity(firecrackEntity);
                if (!player.getAbilities().instabuild)
                    stack.hurtAndBreak(1, Objects.requireNonNull(player), (player1) -> player1.broadcastBreakEvent(hand));
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        } else {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
    }
}