package org.vocalsky.extended_tinker.golem.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.modulargolems.content.item.equipments.GolemEquipmentItem;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import lombok.Getter;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
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
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GolemArmor extends GolemEquipmentItem implements IModifiableDisplay {
    /** Volatile modifier tag to make piglins neutal when worn */
    public static final ResourceLocation PIGLIN_NEUTRAL = TConstruct.getResource("piglin_neutral");
    /** Volatile modifier tag to make this item an elytra */
    public static final ResourceLocation ELYTRA = TConstruct.getResource("elyta");
    /** Volatile flag for a boot item to walk on powdered snow. Cold immunity is handled through a tag */
    public static final ResourceLocation SNOW_BOOTS = TConstruct.getResource("snow_boots");

    @Getter
    private final ToolDefinition toolDefinition;
    /** Cache of the tool built for rendering */
    private ItemStack toolForRendering = null;

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected @NotNull ItemStack execute(@NotNull BlockSource p_40408_, @NotNull ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };
    @Getter
    protected final ArmorItem.Type type;
    @Getter
    private final int defense;
    @Getter
    private final float toughness;
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

    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }

    public GolemArmor(ArmorMaterial materialIn, ArmorItem.Type type, Properties builderIn, ToolDefinition toolDefinition) {
        super(builderIn.defaultDurability(materialIn.getDurabilityForType(type)), type.getSlot(), GolemTypes.ENTITY_GOLEM::get, builder -> {});
        this.material = materialIn;
        this.type = type;
        this.defense = materialIn.getDefenseForType(type);
        this.toughness = materialIn.getToughness();
        this.knockbackResistance = materialIn.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.get(type.getSlot());
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }

        this.defaultModifiers = builder.build();
        this.toolDefinition = toolDefinition;
    }

    public GolemArmor(ModifiableArmorMaterial material, ArmorItem.Type type, Properties properties) {
        this(material, type, properties, Objects.requireNonNull(material.getArmorDefinition(type), "Missing tool definition for " + type.getName()));
    }

    /* Basic properties */

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return ModifierUtil.checkVolatileFlag(stack, PIGLIN_NEUTRAL);
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return type == ArmorItem.Type.BOOTS && ModifierUtil.checkVolatileFlag(stack, SNOW_BOOTS);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }


    /* Enchantments */

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public Map<Enchantment,Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }


    /* Loading */

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ToolCapabilityProvider(stack);
    }

    @Override
    public void verifyTagAfterLoad(@NotNull CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, getToolDefinition());
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Player playerIn) {
        ToolStack.ensureInitialized(stack, getToolDefinition());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level levelIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        InteractionResult result = ToolInventoryCapability.tryOpenContainer(stack, null, getToolDefinition(), playerIn, Util.getSlotType(handIn));
        if (result.consumesAction()) {
            return new InteractionResultHolder<>(result, stack);
        }
        return super.use(levelIn, playerIn, handIn);
    }


    /* Display */

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        // we use enchantments to handle some modifiers, so don't glow from them
        // however, if a modifier wants to glow let them
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        int rarity = ModifierUtil.getVolatileInt(stack, RARITY);
        return Rarity.values()[Mth.clamp(rarity, 0, 3)];
    }


    /* Indestructible items */

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, INDESTRUCTIBLE_ENTITY);
    }

    @Override
    public Entity createEntity(Level level, Entity original, ItemStack stack) {
        if (ModifierUtil.checkVolatileFlag(stack, INDESTRUCTIBLE_ENTITY)) {
            IndestructibleItemEntity entity = new IndestructibleItemEntity(level, original.getX(), original.getY(), original.getZ(), stack);
            entity.setPickupDelayFrom(original);
            return entity;
        }
        return null;
    }


    /* Damage/Durability */

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        // handle in the tinker station
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ToolDamageUtil.getFakeMaxDamage(stack);
    }

    @Override
    public int getDamage(ItemStack stack) {
        if (!canBeDepleted()) {
            return 0;
        }
        return ToolStack.from(stack).getDamage();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (canBeDepleted()) {
            ToolStack.from(stack).setDamage(damage);
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
        // We basically emulate Itemstack.damageItem here. We always return 0 to skip the handling in ItemStack.
        // If we don't tools ignore our damage logic
        if (canBeDepleted() && ToolDamageUtil.damage(ToolStack.from(stack), amount, damager, stack)) {
            onBroken.accept(damager);
        }

        return 0;
    }


    /* Durability display */

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.showDurabilityBar(pStack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }

    @Override
    protected @NotNull Multimap<Attribute, AttributeModifier> getDefaultGolemModifiers(@NotNull ItemStack stack, @NotNull EquipmentSlot slot) {
        return getAttributeModifiers(slot, stack);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getGolemModifiers(@NotNull ItemStack stack, @org.jetbrains.annotations.Nullable Entity entity, @NotNull EquipmentSlot slot) {
        return getSlot() != slot || entity == null || !isFor(entity.getType()) ? ImmutableMultimap.of() : this.getDefaultGolemModifiers(stack, slot);
    }

    /* Armor properties */

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return false;
    }


    @Override
    public @NotNull Multimap<Attribute,AttributeModifier> getAttributeModifiers(@NotNull IToolStackView tool, @NotNull EquipmentSlot slot) {
        if (slot != getEquipmentSlot()) {
            return ImmutableMultimap.of();
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (!tool.isBroken()) {
            // base stats
            StatsNBT statsNBT = tool.getStats();
            UUID uuid = UUID.get(type.getSlot());
            builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "tconstruct.armor.armor", statsNBT.get(ToolStats.ARMOR), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "tconstruct.armor.toughness", statsNBT.get(ToolStats.ARMOR_TOUGHNESS), AttributeModifier.Operation.ADDITION));
            double knockbackResistance = statsNBT.get(ToolStats.KNOCKBACK_RESISTANCE);
            if (knockbackResistance != 0) {
                builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "tconstruct.armor.knockback_resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
            }
            // grab attributes from modifiers
            BiConsumer<Attribute,AttributeModifier> attributeConsumer = builder::put;
            for (ModifierEntry entry : tool.getModifierList()) {
                entry.getHook(ModifierHooks.ATTRIBUTES).addAttributes(tool, entry, slot, attributeConsumer);
            }
        }

        return builder.build();
    }

    @Override
    public Multimap<Attribute,AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (slot != getEquipmentSlot() || nbt == null) {
            return ImmutableMultimap.of();
        }
        return getAttributeModifiers(ToolStack.from(stack), slot);
    }


    /* Elytra */

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return type == ArmorItem.Type.CHESTPLATE && !ToolDamageUtil.isBroken(stack) && ModifierUtil.checkVolatileFlag(stack, ELYTRA);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (getEquipmentSlot() == EquipmentSlot.CHEST) {
            ToolStack tool = ToolStack.from(stack);
            if (!tool.isBroken()) {
                // if any modifier says stop flying, stop flying
                for (ModifierEntry entry : tool.getModifierList()) {
                    if (entry.getHook(ModifierHooks.ELYTRA_FLIGHT).elytraFlightTick(tool, entry, entity, flightTicks)) {
                        return false;
                    }
                }
                // damage the tool and keep flying
                if (!entity.level().isClientSide && (flightTicks + 1) % 20 == 0) {
                    ToolDamageUtil.damageAnimated(tool, 1, entity, EquipmentSlot.CHEST);
                }
                return true;
            }
        }
        return false;
    }


    /* Ticking */

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);

        // don't care about non-living, they skip most tool context
        if (entityIn instanceof LivingEntity living) {
            ToolStack tool = ToolStack.from(stack);
            if (!levelIn.isClientSide) {
                tool.ensureHasData();
            }
            List<ModifierEntry> modifiers = tool.getModifierList();
            if (!modifiers.isEmpty()) {
                boolean isCorrectSlot = living.getItemBySlot(getEquipmentSlot()) == stack;
                // we pass in the stack for most custom context, but for the sake of armor its easier to tell them that this is the correct slot for effects
                for (ModifierEntry entry : modifiers) {
                    entry.getHook(ModifierHooks.INVENTORY_TICK).onInventoryTick(tool, entry, levelIn, living, itemSlot, isSelected, isCorrectSlot, stack);
                }
            }
        }
    }

    @Override
    public boolean overrideStackedOnOther(@NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack slotStack, @NotNull ItemStack held, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return TooltipUtil.getDisplayName(stack, getToolDefinition());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    @Override
    public @NotNull List<Component> getStatInformation(@NotNull IToolStackView tool, @Nullable Player player, @NotNull List<Component> tooltips, @NotNull TooltipKey key, @NotNull TooltipFlag tooltipFlag) {
        tooltips = TooltipUtil.getArmorStats(tool, player, tooltips, key, tooltipFlag);
        TooltipUtil.addAttributes(this, tool, player, tooltips, TooltipUtil.SHOW_ARMOR_ATTRIBUTES, getEquipmentSlot());
        return tooltips;
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(getToolDefinition());
    }

    @Override
    public @NotNull ItemStack getRenderTool() {
        if (toolForRendering == null) {
            toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }
        return toolForRendering;
    }
}