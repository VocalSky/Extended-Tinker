package org.vocalsky.extended_tinker.common.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
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
import slimeknights.tconstruct.library.modifiers.hook.interaction.SlotStackModifierHook;
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
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HorseArmor extends HorseArmorItem implements Equipable, IModifiableDisplay {
    private static final EnumMap<ArmorItem.Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295"));
    });
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };
    protected final ArmorItem.Type type;
    private final int defense;
    private final float toughness;
    protected final float knockbackResistance;
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
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double)this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }

        this.defaultModifiers = builder.build();
        this.toolForRendering = null;
        this.toolDefinition = toolDefinition;
    }

    public HorseArmor(ModifiableArmorMaterial material, ArmorItem.Type type, Item.Properties properties) {
        this(material, type, properties, (ToolDefinition) Objects.requireNonNull(material.getArmorDefinition(type), "Missing tool definition for " + type.getName()));
    }

    public ArmorItem.Type getType() {
        return this.type;
    }

    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public ArmorMaterial getMaterial() {
        return this.material;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

    public int getDefense() {
        return this.defense;
    }

    public float getToughness() {
        return this.toughness;
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }

    public static enum Type {
        HELMET(EquipmentSlot.HEAD, "helmet"),
        CHESTPLATE(EquipmentSlot.CHEST, "chestplate"),
        LEGGINGS(EquipmentSlot.LEGS, "leggings"),
        BOOTS(EquipmentSlot.FEET, "boots");

        private final EquipmentSlot slot;
        private final String name;

        private Type(EquipmentSlot p_266754_, String p_266886_) {
            this.slot = p_266754_;
            this.name = p_266886_;
        }

        public EquipmentSlot getSlot() {
            return this.slot;
        }

        public String getName() {
            return this.name;
        }
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
        return this.type == ArmorItem.Type.BOOTS && ModifierUtil.checkVolatileFlag(stack, SNOW_BOOTS);
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

    public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        InteractionResult result = ToolInventoryCapability.tryOpenContainer(stack, (IToolStackView)null, this.getToolDefinition(), playerIn, slimeknights.tconstruct.library.utils.Util.getSlotType(handIn));
        return result.consumesAction() ? new InteractionResultHolder(result, stack) : super.use(levelIn, playerIn, handIn);
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
        if (slot != this.getEquipmentSlot()) {
            return ImmutableMultimap.of();
        } else {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            if (!tool.isBroken()) {
                StatsNBT statsNBT = tool.getStats();
                UUID uuid = (UUID)ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
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
        return (Multimap<Attribute, AttributeModifier>)(slot == this.getEquipmentSlot() && nbt != null ? this.getAttributeModifiers((IToolStackView)ToolStack.from(stack), (EquipmentSlot)slot) : ImmutableMultimap.of());
    }

    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return this.type == ArmorItem.Type.CHESTPLATE && !ToolDamageUtil.isBroken(stack) && ModifierUtil.checkVolatileFlag(stack, ELYTRA);
    }

    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (this.getEquipmentSlot() == EquipmentSlot.CHEST) {
            ToolStack tool = ToolStack.from(stack);
            if (!tool.isBroken()) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    if (((ElytraFlightModifierHook)entry.getHook(ModifierHooks.ELYTRA_FLIGHT)).elytraFlightTick(tool, entry, entity, flightTicks)) {
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
                boolean isCorrectSlot = living.getItemBySlot(this.getEquipmentSlot()) == stack;

                for(ModifierEntry entry : modifiers) {
                    ((InventoryTickModifierHook)entry.getHook(ModifierHooks.INVENTORY_TICK)).onInventoryTick(tool, entry, levelIn, living, itemSlot, isSelected, isCorrectSlot, stack);
                }
            }
        }

    }

    public boolean overrideStackedOnOther(ItemStack held, Slot slot, ClickAction action, Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }

    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack held, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
    }

    public Component getName(ItemStack stack) {
        return TooltipUtil.getDisplayName(stack, this.getToolDefinition());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = TooltipUtil.getArmorStats(tool, player, tooltips, key, tooltipFlag);
        TooltipUtil.addAttributes(this, tool, player, tooltips, TooltipUtil.SHOW_ARMOR_ATTRIBUTES, new EquipmentSlot[]{this.getEquipmentSlot()});
        return tooltips;
    }

    public int getDefaultTooltipHideFlags(ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
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
}