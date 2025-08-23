package org.vocalsky.extended_tinker.compat.iaf.tool;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import lombok.Getter;
import net.minecraft.ChatFormatting;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ElytraFlightModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.SlotStackModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
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
import java.util.regex.Matcher;

public class DragonArmor extends ItemDragonArmor implements IModifiableDisplay {
    protected static final EnumMap<EquipmentSlot, UUID> UUID = new EnumMap<>(EquipmentSlot.class);

    static {
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            UUID.put(slot, java.util.UUID.randomUUID());
        }
    }

    public static final ResourceLocation PIGLIN_NEUTRAL = TConstruct.getResource("piglin_neutral");

    @Getter
    private final ToolDefinition toolDefinition;

    private ItemStack toolForRendering = null;

//    private static final EnumObject<ArmorItem.Type, ResourceLocation[]> textures = new EnumObject.Builder<ArmorItem.Type, ResourceLocation[]>(ArmorItem.Type.class)
//            .put(ArmorItem.Type.HELMET, new ResourceLocation[]{Extended_tinker.getResource("textures/tinker_armor/golem_armor/helmet0.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/helmet1.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/helmet2.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/helmet3.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/helmet4.png")})
//            .put(ArmorItem.Type.CHESTPLATE, new ResourceLocation[]{Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate0.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate1.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate2.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate3.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate4.png")})
//            .put(ArmorItem.Type.LEGGINGS, new ResourceLocation[]{Extended_tinker.getResource("textures/tinker_armor/golem_armor/leggings0.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/leggings1.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/leggings2.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/leggings3.png"), Extended_tinker.getResource("textures/tinker_armor/golem_armor/leggings4.png")})
//            .build();
//
//    @Override
//    public int textureSize() {
//        return textures.get(type).length;
//    }
//
//    @Override
//    public ResourceLocation getModelTexture(int partIndex) {
//        return textures.get(type)[partIndex];
//    }

    @Override
    public @NotNull String getDescriptionId() {
        return super.getDescriptionId().replace(IceAndFire.MODID, Extended_tinker.MODID);
    }

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected @NotNull ItemStack execute(@NotNull BlockSource p_40408_, @NotNull ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };
    @Getter
    protected final Type type;
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

    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    public DragonArmor(DragonArmorType base, ArmorMaterial materialIn, Type type, Properties builderIn, ToolDefinition toolDefinition) {
        super(base, type.getOrder());
        this.material = materialIn;
        this.type = type;
        this.defense = materialIn.getDefenseForType(type.ArmorType());
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
//        System.out.println("SDKJSNDJOIS");
//        System.out.println(super.getDescriptionId());
    }

    public DragonArmor(DragonArmorType base, ModifiableArmorMaterial material, Type type, Properties properties) {
        this(base, material, type, properties, Objects.requireNonNull(material.getArmorDefinition(type.ArmorType()), "Missing tool definition for " + type.getName()));
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

    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return RarityModule.getRarity(stack);
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    public Entity createEntity(Level level, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(level, original, stack);
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
        super.appendHoverText(stack, level, tooltip, flag);
//        String var10000;
//        switch (this.dragonSlot) {
//            case 1 -> var10000 = "dragon.armor_neck";
//            case 2 -> var10000 = "dragon.armor_body";
//            case 3 -> var10000 = "dragon.armor_tail";
//            default -> var10000 = "dragon.armor_head";
//        }
//
//        String words = var10000;
//        tooltip.add(Component.translatable(words).withStyle(ChatFormatting.GRAY));
//        System.out.println("DASDWDASDS");
//        System.out.println(super.getDescriptionId());
        System.out.println("RUNISGOOD");
        System.out.println(tooltip);
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

    public static String fullNameOfArmorType(DragonArmorType armorType) {
        boolean is_steel = armorType == DragonArmorType.FIRE || armorType == DragonArmorType.ICE || armorType ==DragonArmorType.LIGHTNING;
        return (is_steel ? "DRAGONSTEEL_" : "") + armorType.name();
    }

    @Getter
    public static enum Type {
        HEAD(EquipmentSlot.HEAD, "head", 0),
        BODY(EquipmentSlot.CHEST, "body", 2),
        NECK(EquipmentSlot.LEGS, "neck", 1),
        TAIL(EquipmentSlot.FEET, "tail", 3);

        private final EquipmentSlot slot;
        private final String name;
        private final int order;

        Type(EquipmentSlot slot, String name, int order) {
            this.slot = slot;
            this.name = name;
            this.order = order;
        }

        public ArmorItem.Type ArmorType() {
            return switch (this) {
                case HEAD -> ArmorItem.Type.HELMET;
                case BODY -> ArmorItem.Type.CHESTPLATE;
                case NECK -> ArmorItem.Type.LEGGINGS;
                case TAIL -> ArmorItem.Type.BOOTS;
            };
        }

    }
}
