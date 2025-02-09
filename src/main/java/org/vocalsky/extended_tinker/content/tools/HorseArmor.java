package org.vocalsky.extended_tinker.content.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ElytraFlightModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HorseArmor extends HorseArmorItem implements Wearable, IModifiableDisplay {
//    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };
    protected final EquipmentSlot slot;
    private final int defense;
    private final float toughness;
    protected final float knockbackResistance;
    protected final ArmorMaterial material;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final ResourceLocation texture;

    public static boolean dispenseArmor(BlockSource p_40399_, ItemStack p_40400_) {
        BlockPos blockpos = p_40399_.getPos().relative((Direction)p_40399_.getBlockState().getValue(DispenserBlock.FACING));
        List<LivingEntity> list = p_40399_.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(p_40400_)));
        if (list.isEmpty()) {
            return false;
        } else {
            LivingEntity livingentity = (LivingEntity)list.get(0);
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

    public HorseArmor(ArmorMaterial materialIn, EquipmentSlot slot, Item.Properties builderIn, ToolDefinition toolDefinition) {
        super(0, "tcon", builderIn.defaultDurability(materialIn.getDurabilityForSlot(slot)));
        texture = Extended_tinker.getResource("textures/entity/horse/armor/horse_armor_tcon.png");
        this.material = materialIn;
        this.slot = slot;
        this.defense = materialIn.getDefenseForSlot(slot);
        this.toughness = materialIn.getToughness();
        this.knockbackResistance = materialIn.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID;
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double)this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0.0F) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        this.defaultModifiers = builder.build();
        this.toolForRendering = null;
        this.toolDefinition = toolDefinition;
    }

//    public HorseArmor(ModifiableArmorMaterial material, ArmorSlotType slotType, Item.Properties properties) {
//        this(material, slotType.getEquipmentSlot(), properties, (ToolDefinition)Objects.requireNonNull(material.getArmorDefinition(slotType), "Missing tool definition for " + slotType));
//    }

    public int getProtection() {
        return this.getDefense();
    }
    public ResourceLocation getTexture() {
        return this.texture;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public ArmorMaterial getMaterial() {
        return this.material;
    }

    public InteractionResultHolder<ItemStack> super_use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
        ItemStack itemstack = p_40396_.getItemInHand(p_40397_);
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = p_40396_.getItemBySlot(equipmentslot);
        if (itemstack1.isEmpty()) {
            p_40396_.setItemSlot(equipmentslot, itemstack.copy());
            if (!p_40395_.isClientSide()) {
                p_40396_.awardStat(Stats.ITEM_USED.get(this));
            }

            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, p_40395_.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        InteractionResult result = ToolInventoryCapability.tryOpenContainer(stack, (IToolStackView)null, this.getToolDefinition(), playerIn, Util.getSlotType(handIn));
        return result.consumesAction() ? new InteractionResultHolder(result, stack) : super_use(levelIn, playerIn, handIn);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

    public int getDefense() { return this.defense; }

    public float getToughness() {
        return this.toughness;
    }

    @Nullable
    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }
    public static final ResourceLocation PIGLIN_NEUTRAL = TConstruct.getResource("piglin_neutral");
    public static final ResourceLocation ELYTRA = TConstruct.getResource("elyta");
    public static final ResourceLocation SNOW_BOOTS = TConstruct.getResource("snow_boots");
    private final ToolDefinition toolDefinition;
    private ItemStack toolForRendering;

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return ModifierUtil.checkVolatileFlag(stack, PIGLIN_NEUTRAL);
    }

    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return this.slot == EquipmentSlot.FEET && ModifierUtil.checkVolatileFlag(stack, SNOW_BOOTS);
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    public boolean isEnchantable(ItemStack stack) {
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

    public void verifyTagAfterLoad(CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, this.getToolDefinition());
    }

    public void onCraftedBy(ItemStack stack, Level levelIn, Player playerIn) {
        ToolStack.ensureInitialized(stack, this.getToolDefinition());
    }

    public boolean isFoil(ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    public Rarity getRarity(ItemStack stack) {
        int rarity = ModifierUtil.getVolatileInt(stack, RARITY);
        return Rarity.values()[Mth.clamp(rarity, 0, 3)];
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, INDESTRUCTIBLE_ENTITY);
    }

    public Entity createEntity(Level level, Entity original, ItemStack stack) {
        if (ModifierUtil.checkVolatileFlag(stack, INDESTRUCTIBLE_ENTITY)) {
            IndestructibleItemEntity entity = new IndestructibleItemEntity(level, original.getX(), original.getY(), original.getZ(), stack);
            entity.setPickupDelayFrom(original);
            return entity;
        } else {
            return null;
        }
    }

    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    public boolean canBeDepleted() {
        return true;
    }

    public int getMaxDamage(ItemStack stack) {
        if (!this.canBeDepleted()) {
            return 0;
        } else {
            ToolStack tool = ToolStack.from(stack);
            int durability = tool.getStats().getInt(ToolStats.DURABILITY);
            return tool.isBroken() ? durability + 1 : durability;
        }
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

    public boolean isBarVisible(ItemStack pStack) {
        return DurabilityDisplayModifierHook.showDurabilityBar(pStack);
    }

    public int getBarColor(ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
    }

    public int getBarWidth(ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        if (slot != this.getSlot()) {
            return ImmutableMultimap.of();
        } else {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            if (!tool.isBroken()) {
                StatsNBT statsNBT = tool.getStats();
                UUID uuid = ARMOR_MODIFIER_UUID;
                builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "tconstruct.armor.armor", (double)(Float)statsNBT.get(ToolStats.ARMOR), AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "tconstruct.armor.toughness", (double)(Float)statsNBT.get(ToolStats.ARMOR_TOUGHNESS), AttributeModifier.Operation.ADDITION));
                double knockbackResistance = (double)(Float)statsNBT.get(ToolStats.KNOCKBACK_RESISTANCE);
                if (knockbackResistance != (double)0.0F) {
                    builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "tconstruct.armor.knockback_resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
                }

                Objects.requireNonNull(builder);
                BiConsumer<Attribute, AttributeModifier> attributeConsumer = builder::put;

                for(ModifierEntry entry : tool.getModifierList()) {
                    ((AttributesModifierHook)entry.getHook(ModifierHooks.ATTRIBUTES)).addAttributes(tool, entry, slot, attributeConsumer);
                }
            }

            return builder.build();
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return (Multimap<Attribute, AttributeModifier>)(slot == this.getSlot() && nbt != null ? this.getAttributeModifiers((IToolStackView)ToolStack.from(stack), (EquipmentSlot)slot) : ImmutableMultimap.of());
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return this.slot == EquipmentSlot.CHEST && !ToolDamageUtil.isBroken(stack) && ModifierUtil.checkVolatileFlag(stack, ELYTRA);
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (this.slot == EquipmentSlot.CHEST) {
            ToolStack tool = ToolStack.from(stack);
            if (!tool.isBroken()) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    if (((ElytraFlightModifierHook)entry.getHook(ModifierHooks.ELYTRA_FLIGHT)).elytraFlightTick(tool, entry, entity, flightTicks)) {
                        return false;
                    }
                }

                if (!entity.level.isClientSide && (flightTicks + 1) % 20 == 0) {
                    ToolDamageUtil.damageAnimated(tool, 1, entity, EquipmentSlot.CHEST);
                }

                return true;
            }
        }

        return false;
    }

    public void inventoryTick(ItemStack stack, Level levelIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity) {
            ToolStack tool = ToolStack.from(stack);
            if (!levelIn.isClientSide) {
                tool.ensureHasData();
            }

            List<ModifierEntry> modifiers = tool.getModifierList();
            if (!modifiers.isEmpty()) {
                LivingEntity living = (LivingEntity)entityIn;
                boolean isCorrectSlot = living.getItemBySlot(this.slot) == stack;

                for(ModifierEntry entry : modifiers) {
                    ((InventoryTickModifierHook)entry.getHook(ModifierHooks.INVENTORY_TICK)).onInventoryTick(tool, entry, levelIn, living, itemSlot, isSelected, isCorrectSlot, stack);
                }
            }
        }

    }

    public Component getName(ItemStack stack) {
        return TooltipUtil.getDisplayName(stack, this.getToolDefinition());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = TooltipUtil.getArmorStats(tool, player, tooltips, key, tooltipFlag);
        TooltipUtil.addAttributes(this, tool, player, tooltips, TooltipUtil.SHOW_ARMOR_ATTRIBUTES, new EquipmentSlot[]{this.getSlot()});
        return tooltips;
    }

    public int getDefaultTooltipHideFlags(ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
    }

    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowedIn(group)) {
            ToolBuildHandler.addDefaultSubItems(this, items);
        }

    }

    public ItemStack getRenderTool() {
        if (this.toolForRendering == null) {
            this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }

        return this.toolForRendering;
    }

    public ToolDefinition getToolDefinition() {
        return this.toolDefinition;
    }
};