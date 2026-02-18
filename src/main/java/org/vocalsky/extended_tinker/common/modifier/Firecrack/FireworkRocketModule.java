package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileShootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ScheduledProjectileTaskModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.utils.Schedule;

import java.util.List;

public enum FireworkRocketModule implements ModifierModule, ProjectileShootModifierHook, ProjectileHitModifierHook, ScheduledProjectileTaskModifierHook, ModifierRemovalHook {
    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<FireworkRocketModule>defaultHooks(ModifierHooks.PROJECTILE_SHOT, ModifierHooks.PROJECTILE_HIT, ModifierHooks.SCHEDULE_PROJECTILE_TASK);
    public static final RecordLoadable<FireworkRocketModule> LOADER = new SingletonLoader<>(INSTANCE);
    private static final ResourceLocation KEY_LIFETIME = Extended_tinker.getResource("lifetime");
    private static final ResourceLocation KEY_SAFETY = Extended_tinker.getResource("safety");

    @Override
    public @NotNull RecordLoadable<? extends ModifierModule> getLoader() { return LOADER; }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public void onProjectileShoot(@NotNull IToolStackView iToolStackView, @NotNull ModifierEntry modifierEntry, @Nullable LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull Projectile projectile, @Nullable AbstractArrow abstractArrow, @NotNull ModDataNBT modDataNBT, boolean b) {
        int lifetime = iToolStackView.getModifierLevel(ModCore.Modifiers.Ids.firework_flight) * 10 + projectile.random.nextInt(6) + projectile.random.nextInt(7);
        modDataNBT.putInt(KEY_LIFETIME, lifetime);
        modDataNBT.putBoolean(KEY_SAFETY, iToolStackView.getModifierLevel(ModCore.Modifiers.Ids.safety_firework) > 0);
        modDataNBT.put(FireworkStarModifier.fireworks, FireworkStarModifier.getStar(iToolStackView));
        projectile.setNoGravity(true);
    }

    @Override
    public boolean onProjectileHitEntity(@NotNull ModifierNBT modifiers, @NotNull ModDataNBT persistentData, @NotNull ModifierEntry modifier, @NotNull Projectile projectile, @NotNull EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        explode(projectile, persistentData);
        return projectile.isRemoved();
    }

    @Override
    public boolean onProjectileHitsBlock(@NotNull ModifierNBT modifiers, @NotNull ModDataNBT persistentData, @NotNull ModifierEntry modifier, @NotNull Projectile projectile, @NotNull BlockHitResult hit, @Nullable LivingEntity owner) {
        explode(projectile, persistentData);
        return projectile.isRemoved();
    }

    @Override
    public @Nullable Component onRemoved(@NotNull IToolStackView iToolStackView, @NotNull Modifier modifier) {
        iToolStackView.getPersistentData().remove(KEY_LIFETIME);
        iToolStackView.getPersistentData().remove(KEY_SAFETY);
        return null;
    }

    @Override
    public void scheduleProjectileTask(@NotNull IToolStackView iToolStackView, @NotNull ModifierEntry modifierEntry, @NotNull ItemStack itemStack, @NotNull Projectile projectile, @Nullable AbstractArrow abstractArrow, @NotNull ModDataNBT modDataNBT, Schedule.@NotNull Scheduler scheduler) {
        int lifetime = modDataNBT.getInt(KEY_LIFETIME);
        scheduler.add(0, lifetime);
        for (int i = 1; i <= lifetime; ++i) scheduler.add(i, i);
    }

    @Override
    public void onScheduledProjectileTask(@NotNull IToolStackView iToolStackView, @NotNull ModifierEntry modifierEntry, @NotNull ItemStack itemStack, @NotNull Projectile projectile, @Nullable AbstractArrow arrow, @NotNull ModDataNBT modDataNBT, int task) {
        if (arrow == null || arrow.level().isClientSide || arrow.isRemoved()) return;
        if (task == 0) {
            explode(arrow, modDataNBT);
        } else {
            if (arrow.level() instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.FIREWORK, arrow.getX(), arrow.getY(), arrow.getZ(), 1, arrow.random.nextGaussian() * 0.05D, -arrow.getDeltaMovement().y * 0.5D, arrow.random.nextGaussian() * 0.05D, 0.0);
            }
        }
    }

    private static void explode(Projectile arrow, ModDataNBT dataNBT) {
        arrow.level().broadcastEntityEvent(arrow, (byte)17);
        arrow.gameEvent(GameEvent.EXPLODE, arrow.getOwner());
        dealExplosionDamage(arrow, dataNBT);
        arrow.discard();
    }

    private static void dealExplosionDamage(Projectile arrow, ModDataNBT dataNBT) {
        float f = 0.0F;
        CompoundTag compoundtag = dataNBT.getCompound(FireworkStarModifier.fireworks);
        ListTag listtag = compoundtag.getList("Explosions", 10);
        if (!listtag.isEmpty())
            f = 5.0F + (float)(listtag.size() * 2);
        if (f > 0.0F) {
            Vec3 vec3 = arrow.position();
            for(LivingEntity livingentity : arrow.level().getEntitiesOfClass(LivingEntity.class, arrow.getBoundingBox().inflate(5.0D))) {
                if ((!dataNBT.getBoolean(KEY_SAFETY) || livingentity != arrow.getOwner()) && !(arrow.distanceToSqr(livingentity) > 25.0D)) {
                    boolean flag = false;
                    for(int i = 0; i < 2; ++i) {
                        Vec3 vec31 = new Vec3(livingentity.getX(), livingentity.getY(0.5D * (double)i), livingentity.getZ());
                        HitResult hitresult = arrow.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, arrow));
                        if (hitresult.getType() == HitResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        float f1 = f * (float)Math.sqrt((5.0D - (double)arrow.distanceTo(livingentity)) / 5.0D);
                        livingentity.hurt(arrow.damageSources().source(DamageTypes.FIREWORKS, arrow, arrow.getOwner()), f1);
                    }
                }
            }
        }
    }
}
