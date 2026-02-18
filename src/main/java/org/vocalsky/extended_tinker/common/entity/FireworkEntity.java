package org.vocalsky.extended_tinker.common.entity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FireworkStarModifier;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.OptionalInt;

public class FireworkEntity extends Projectile {
    private int life;
    private int lifetime;
    private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET;
    private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE;
    private LivingEntity attachedToEntity;

    public @Nullable LivingEntity getLivingOwner() {
        return this.getOwner() instanceof LivingEntity living ? living : null;
    }
    static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;

    public void setItem(ItemStack itemStack) {
        if (!itemStack.is(this.getDefaultItem()) || itemStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(itemStack.copy(), (stack) -> stack.setCount(1)));
        }
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public @NotNull ItemStack getItem() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemStack;
    }

    public FireworkEntity(EntityType<? extends FireworkEntity> fireworkEntityEntityType, Level level) {
        super(fireworkEntityEntityType, level);
    }

    public FireworkEntity(EntityType<? extends FireworkEntity> entityType, Level level, ItemStack itemStack) {
        this(entityType, level);
        this.setItem(itemStack);
    }

    public FireworkEntity(Level level, Entity entity, ItemStack itemStack) {
        this(ModCore.Entities.firecrackEntity.get(), level, itemStack);
        this.setOwner(entity);
    }

    public FireworkEntity(Level level, @Nullable Entity entity, double x, double y, double z, ItemStack itemStack) {
        this(level, entity, itemStack);
        ToolStack tool = ToolStack.from(itemStack);
        this.life = 0;
        this.setPos(x, y, z);
        this.setDeltaMovement(this.random.triangle(0.0, 0.002297), 0.05, this.random.triangle(0.0, 0.002297));
        this.lifetime = 10 * tool.getModifierLevel(ModCore.Modifiers.Ids.firework_flight) + this.random.nextInt(6) + this.random.nextInt(7);
    }

    public FireworkEntity(Level level, @Nullable Entity entity, double x, double y, double z, ItemStack itemStack, boolean isShotAtAngle) {
        this(level, entity, x, y, z, itemStack);
        this.entityData.set(DATA_SHOT_AT_ANGLE, isShotAtAngle);
    }

    public FireworkEntity(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        this(level, livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),itemStack);
        this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(livingEntity.getId()));
        this.attachedToEntity = livingEntity;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
        this.getEntityData().define(DATA_SHOT_AT_ANGLE, false);
    }

    protected Item getDefaultItem() {
        return Items.AIR;
    }

    private boolean isAttachedToEntity() {
        return this.entityData.get(DATA_ATTACHED_TO_TARGET).isPresent();
    }

    public boolean isShotAtAngle() {
        return this.entityData.get(DATA_SHOT_AT_ANGLE);
    }

    public boolean shouldRenderAtSqrDistance(double d) {
        return d < (double)4096.0F && !this.isAttachedToEntity();
    }

    public boolean shouldRender(double x, double y, double z) {
        return super.shouldRender(x, y, z) && !this.isAttachedToEntity();
    }

    private CompoundTag getStarTag(ToolStack tool) {
        if (tool.getModifierLevel(ModCore.Modifiers.Ids.firework_star) == 0) return new CompoundTag();
        return FireworkStarModifier.getStar(tool);
    }

    private void dealExplosionDamage() {
        float f = 0.0F;
        ItemStack itemstack = this.getItem();
        ToolStack tool = ToolStack.from(itemstack);
        CompoundTag compoundTag = getStarTag(tool);
        ListTag listtag = compoundTag.getList("Explosions", Tag.TAG_COMPOUND);
        if (!listtag.isEmpty()) {
            f = 5.0F + (float)(listtag.size() * 2);
        }

        if (f > 0.0F) {
            if (this.attachedToEntity != null && (tool.getModifierLevel(ModCore.Modifiers.Ids.safety_firework) == 0 || this.attachedToEntity != this.getLivingOwner()))
                this.attachedToEntity.hurt(this.damageSources().source(DamageTypes.FIREWORKS, this, this.getOwner()), 5.0F + (float)(listtag.size() * 2));

            Vec3 vec3 = this.position();

            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5.0))) {
                if (livingentity != this.attachedToEntity && (tool.getModifierLevel(ModCore.Modifiers.Ids.safety_firework) == 0 || livingentity != this.getLivingOwner()) && !(this.distanceToSqr(livingentity) > 25.0)) {
                    boolean flag = false;

                    for(int i = 0; i < 2; ++i) {
                        Vec3 vec31 = new Vec3(livingentity.getX(), livingentity.getY(0.5 * (double)i), livingentity.getZ());
                        HitResult hitresult = this.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        if (hitresult.getType() == HitResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        float f1 = f * (float)Math.sqrt((5.0 - (double)this.distanceTo(livingentity)) / 5.0);
                        livingentity.hurt(this.damageSources().source(DamageTypes.FIREWORKS, this, this.getOwner()), f1);
                    }
                }
            }
        }
    }

    public void handleEntityEvent(byte eid) {
        if (eid == 17 && this.level().isClientSide) {
            if (!this.hasExplosion()) {
                for(int i = 0; i < this.random.nextInt(3) + 2; ++i) {
                    this.level().addParticle(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, 0.005, this.random.nextGaussian() * 0.05);
                }
            } else {
                ItemStack itemstack = this.getItem();
                ToolStack tool = ToolStack.from(itemstack);
                CompoundTag compoundTag = getStarTag(tool);
                Vec3 vec3 = this.getDeltaMovement();
                this.level().createFireworks(this.getX(), this.getY(), this.getZ(), vec3.x, vec3.y, vec3.z, compoundTag);
            }
        }
        super.handleEntityEvent(eid);
    }

    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.MISS || !ForgeEventFactory.onProjectileImpact(this, result)) {
            super.onHit(result);
        }
    }

    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level().isClientSide) {
            this.explode();
        }

    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        BlockPos blockpos = new BlockPos(blockHitResult.getBlockPos());
        this.level().getBlockState(blockpos).entityInside(this.level(), blockpos, this);
        if (!this.level().isClientSide() && this.hasExplosion()) {
            this.explode();
        }

        super.onHitBlock(blockHitResult);
    }

    private boolean hasExplosion() {
        ItemStack itemstack = this.getItem();
        ToolStack tool = ToolStack.from(itemstack);
        CompoundTag compoundTag = getStarTag(tool);
        ListTag listtag = compoundTag.getList("Explosions", 10);
        return !listtag.isEmpty();
    }

    private void explode() {
        this.level().broadcastEntityEvent(this, (byte)17);
        this.gameEvent(GameEvent.EXPLODE, this.getOwner());
        this.dealExplosionDamage();
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAttachedToEntity()) {
            if (this.attachedToEntity == null) {
                this.entityData.get(DATA_ATTACHED_TO_TARGET).ifPresent((id) -> {
                    Entity entity = this.level().getEntity(id);
                    if (entity instanceof LivingEntity) {
                        this.attachedToEntity = (LivingEntity)entity;
                    }
                });
            }
            if (this.attachedToEntity != null) {
                Vec3 vec3;
                if (this.attachedToEntity.isFallFlying()) {
                    Vec3 vec31 = this.attachedToEntity.getLookAngle();
                    Vec3 vec32 = this.attachedToEntity.getDeltaMovement();
                    this.attachedToEntity.setDeltaMovement(vec32.add(vec31.x * 0.1 + (vec31.x * 1.5 - vec32.x) * 0.5, vec31.y * 0.1 + (vec31.y * 1.5 - vec32.y) * 0.5, vec31.z * 0.1 + (vec31.z * 1.5 - vec32.z) * 0.5));
                    vec3 = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
                } else {
                    vec3 = Vec3.ZERO;
                }
                this.setPos(this.attachedToEntity.getX() + vec3.x, this.attachedToEntity.getY() + vec3.y, this.attachedToEntity.getZ() + vec3.z);
                this.setDeltaMovement(this.attachedToEntity.getDeltaMovement());
            }
        } else {
            if (!this.isShotAtAngle()) {
                double d2 = this.horizontalCollision ? 1.0 : 1.15;
                this.setDeltaMovement(this.getDeltaMovement().multiply(d2, 1.0, d2).add(0.0, 0.04, 0.0));
            }
            Vec3 vec33 = this.getDeltaMovement();
            this.move(MoverType.SELF, vec33);
            this.setDeltaMovement(vec33);
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (!this.noPhysics) {
            this.onHit(hitresult);
            this.hasImpulse = true;
        }

        this.updateRotation();
        if (this.life == 0 && !this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
        }

        ++this.life;
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getDeltaMovement().y * (double)0.5F, this.random.nextGaussian() * 0.05);
        }

        if (!this.level().isClientSide && this.life > this.lifetime) {
            this.explode();
        }
    }

    static {
        DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(FireworkEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
        DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(FireworkEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_ITEM_STACK = SynchedEntityData.defineId(FireworkEntity.class, EntityDataSerializers.ITEM_STACK);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag data) {
        super.addAdditionalSaveData(data);
        data.putInt("Life", this.life);
        data.putInt("LifeTime", this.lifetime);
        data.putBoolean("ShotAtAngle", this.entityData.get(DATA_SHOT_AT_ANGLE));
        ItemStack itemStack = this.getItemRaw();
        if (!itemStack.isEmpty()) data.put("Item", itemStack.save(new CompoundTag()));
    }

    public void readAdditionalSaveData(@NotNull CompoundTag data) {
        super.readAdditionalSaveData(data);
        this.life = data.getInt("Life");
        this.lifetime = data.getInt("LifeTime");
        if (data.contains("ShotAtAngle")) this.entityData.set(DATA_SHOT_AT_ANGLE, data.getBoolean("ShotAtAngle"));
        ItemStack itemStack = ItemStack.of(data.getCompound("Item"));
        this.setItem(itemStack);
    }
}