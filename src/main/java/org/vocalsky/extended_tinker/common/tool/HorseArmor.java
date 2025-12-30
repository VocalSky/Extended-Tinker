package org.vocalsky.extended_tinker.common.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.util.IArmorModel;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.SlotStackModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.ModifiableArrowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HorseArmor extends HorseArmorItem implements Equipable, IModifiableDisplay, IArmorModel {
    public static UUID HorseArmorUUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected @NotNull ItemStack execute(@NotNull BlockSource p_40408_, @NotNull ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };

    private static final ResourceLocation[] textures = new ResourceLocation[]{Extended_tinker.getResource("textures/tinker_armor/horse_armor/maille1_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/shield1_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/maille2_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/shield2_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/head_armor.png")};

    @Override
    public int textureSize() {
        return textures.length;
    }

    @Override
    public ResourceLocation getModelTexture(int partIndex) {
        return textures[partIndex];
    }

    @Getter
    protected final ArmorItem.Type type;
    @Getter
    private final int defense;
    @Getter
    private final float toughness;
    @Getter
    protected final float knockbackResistance;
    @Getter
    protected final ArmorMaterial material;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public static boolean dispenseArmor(BlockSource p_40399_, ItemStack p_40400_) {
        BlockPos blockpos = p_40399_.getPos().relative(p_40399_.getBlockState().getValue(DispenserBlock.FACING));
        List<LivingEntity> list = p_40399_.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(p_40400_)));
        if (list.isEmpty()) {
            return false;
        } else {
            LivingEntity livingentity = list.get(0);
            EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(p_40400_);
            ItemStack itemstack = p_40400_.split(1);
            livingentity.setItemSlot(equipmentslot, itemstack);
            if (livingentity instanceof Mob) {
                ((Mob)livingentity).setDropChance(equipmentslot, 2.0F);
                ((Mob)livingentity).setPersistenceRequired();
            }

            return true;
        }
    }

    public HorseArmor(ArmorMaterial materialIn, ArmorItem.Type type, Item.Properties builderIn, ToolDefinition toolDefinition) {
        super(0, "", builderIn.defaultDurability(materialIn.getDurabilityForType(type)));
        this.material = materialIn;
        this.type = type;
        this.defense = materialIn.getDefenseForType(type);
        this.toughness = materialIn.getToughness();
        this.knockbackResistance = materialIn.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = HorseArmorUUID;
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }

        this.defaultModifiers = builder.build();
        this.toolForRendering = null;
        this.toolDefinition = toolDefinition;
    }

    public HorseArmor(ModifiableArmorMaterial material, ArmorItem.Type type, Item.Properties properties) {
        this(material, type, properties, Objects.requireNonNull(material.getArmorDefinition(type), "Missing tool definition for " + type.getName()));
    }

    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

//    public @NotNull SoundEvent getEquipSound() {
//        return this.getMaterial().getEquipSound();
//    }

    public static final ResourceLocation PIGLIN_NEUTRAL = TConstruct.getResource("piglin_neutral");
    public static final ResourceLocation ELYTRA = TConstruct.getResource("elyta");
    public static final ResourceLocation SNOW_BOOTS = TConstruct.getResource("snow_boots");
    @Getter
    private final ToolDefinition toolDefinition;
    private ItemStack toolForRendering;

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return ModifierUtil.checkVolatileFlag(stack, PIGLIN_NEUTRAL);
    }

    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return this.type == ArmorItem.Type.BOOTS && ModifierUtil.checkVolatileFlag(stack, SNOW_BOOTS);
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ToolCapabilityProvider(stack);
    }

    public void verifyTagAfterLoad(@NotNull CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, this.getToolDefinition());
    }

    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Player playerIn) {
        ToolStack.ensureInitialized(stack, this.getToolDefinition());
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level levelIn, Player playerIn, @NotNull InteractionHand handIn) {
        if (playerIn.isCrouching()) {
            ItemStack stack = playerIn.getItemInHand(handIn);
            InteractionResult result = ToolInventoryCapability.tryOpenContainer(stack, null, this.getToolDefinition(), playerIn, Util.getSlotType(handIn));
            if (result.consumesAction()) {
                return new InteractionResultHolder<>(result, stack);
            }
        }

        return super.use(levelIn, playerIn, handIn);
    }

    public boolean isFoil(@NotNull ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return RarityModule.getRarity(stack);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    public Entity createEntity(Level level, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(level, original, stack);
    }

    public boolean isRepairable(@NotNull ItemStack stack) {
        return false;
    }

    public boolean canBeDepleted() {
        return true;
    }

    public int getMaxDamage(ItemStack stack) {
        return ToolDamageUtil.getFakeMaxDamage(stack);
    }

    public int getDamage(ItemStack stack) {
        return !this.canBeDepleted() ? 0 : ToolStack.from(stack).getDamage();
    }

    public void setDamage(ItemStack stack, int damage) {
        if (this.canBeDepleted()) {
            ToolStack.from(stack).setDamage(damage);
        }

    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
        if (this.canBeDepleted() && ToolDamageUtil.damage(ToolStack.from(stack), amount, damager, stack)) {
            onBroken.accept(damager);
        }

        return 0;
    }

    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.showDurabilityBar(pStack);
    }

    public int getBarColor(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
    }

    public int getBarWidth(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }

    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return false;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull IToolStackView tool, @NotNull EquipmentSlot slot) {
        if (slot != this.getEquipmentSlot()) {
            return ImmutableMultimap.of();
        } else {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            if (!tool.isBroken()) {
                StatsNBT statsNBT = tool.getStats();
                UUID uuid = HorseArmorUUID;
//                builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Horse armor bonus", (double) statsNBT.get(ToolStats.ARMOR), AttributeModifier.Operation.ADDITION));
//                builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Horse armor bonus", (double) statsNBT.get(ToolStats.ARMOR_TOUGHNESS), AttributeModifier.Operation.ADDITION));
//                double knockbackResistance = (double) statsNBT.get(ToolStats.KNOCKBACK_RESISTANCE);
//                if (knockbackResistance != (double)0.0F) {
//                    builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Horse armor bonus", knockbackResistance, AttributeModifier.Operation.ADDITION));
//                }
                builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "tconstruct.armor.armor", (double) statsNBT.get(ToolStats.ARMOR), AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "tconstruct.armor.toughness", (double) statsNBT.get(ToolStats.ARMOR_TOUGHNESS), AttributeModifier.Operation.ADDITION));
                double knockbackResistance = (double) statsNBT.get(ToolStats.KNOCKBACK_RESISTANCE);
                if (knockbackResistance != (double)0.0F) {
                    builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "tconstruct.armor.knockback_resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
                }

                Objects.requireNonNull(builder);
                BiConsumer<Attribute, AttributeModifier> attributeConsumer = builder::put;

                for(ModifierEntry entry : tool.getModifierList()) {
                    entry.getHook(ModifierHooks.ATTRIBUTES).addAttributes(tool, entry, slot, attributeConsumer);
                }
            }

            return builder.build();
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return slot == this.getEquipmentSlot() && nbt != null ? this.getAttributeModifiers(ToolStack.from(stack), slot) : ImmutableMultimap.of();
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return this.type == ArmorItem.Type.CHESTPLATE && !ToolDamageUtil.isBroken(stack) && ModifierUtil.checkVolatileFlag(stack, ELYTRA);
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (this.getEquipmentSlot() == EquipmentSlot.CHEST) {
            ToolStack tool = ToolStack.from(stack);
            if (!tool.isBroken()) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    if (entry.getHook(ModifierHooks.ELYTRA_FLIGHT).elytraFlightTick(tool, entry, entity, flightTicks)) {
                        return false;
                    }
                }

                if (!entity.level().isClientSide && (flightTicks + 1) % 20 == 0) {
                    ToolDamageUtil.damageAnimated(tool, 1, entity, EquipmentSlot.CHEST);
                }

                return true;
            }
        }

        return false;
    }

    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity living) {
            ToolStack tool = ToolStack.from(stack);
            if (!levelIn.isClientSide) {
                tool.ensureHasData();
            }

            List<ModifierEntry> modifiers = tool.getModifierList();
            if (!modifiers.isEmpty()) {
                boolean isCorrectSlot = living.getItemBySlot(this.getEquipmentSlot()) == stack;

                for(ModifierEntry entry : modifiers) {
                    entry.getHook(ModifierHooks.INVENTORY_TICK).onInventoryTick(tool, entry, levelIn, living, itemSlot, isSelected, isCorrectSlot, stack);
                }
            }
        }

    }

    public boolean overrideStackedOnOther(@NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }

    public boolean overrideOtherStackedOnMe(@NotNull ItemStack slotStack, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
    }

    public @NotNull Component getName(@NotNull ItemStack stack) {
        return ToolNameHook.getName(this.getToolDefinition(), stack);
    }


    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    public @NotNull List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, @NotNull List<Component> tooltips, @NotNull TooltipKey key, @NotNull TooltipFlag tooltipFlag) {
        tooltips = TooltipUtil.getArmorStats(tool, player, tooltips, key, tooltipFlag);
        TooltipUtil.addAttributes(this, tool, player, tooltips, TooltipUtil.SHOW_ARMOR_ATTRIBUTES, this.getEquipmentSlot());
        return tooltips;
    }

    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
    }

    public @NotNull ItemStack getRenderTool() {
        if (this.toolForRendering == null) {
            this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }

        return this.toolForRendering;
    }
}