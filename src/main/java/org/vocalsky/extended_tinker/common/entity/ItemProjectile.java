package org.vocalsky.extended_tinker.common.entity;


import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ItemProjectile extends Projectile implements ItemSupplier {
    public @Nullable LivingEntity getLivingOwner() {
        return this.getOwner() instanceof LivingEntity living ? living : null;
    }
    static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;
    protected ItemProjectile(EntityType<? extends ItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public void setItem(ItemStack itemStack) {
        if (!itemStack.is(this.getDefaultItem()) || itemStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(itemStack.copy(), (stack) -> stack.setCount(1)));
        }

    }

    protected abstract Item getDefaultItem();

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public @NotNull ItemStack getItem() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemStack;
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack itemStack = this.getItemRaw();
        if (!itemStack.isEmpty()) {
            tag.put("Item", itemStack.save(new CompoundTag()));
        }

    }

    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ItemStack itemStack = ItemStack.of(tag.getCompound("Item"));
        this.setItem(itemStack);
    }

    static {
        DATA_ITEM_STACK = SynchedEntityData.defineId(ItemProjectile.class, EntityDataSerializers.ITEM_STACK);
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitresult.getType() == HitResult.Type.ENTITY){
            this.onHitEntity((EntityHitResult) hitresult);
        }
    }
}