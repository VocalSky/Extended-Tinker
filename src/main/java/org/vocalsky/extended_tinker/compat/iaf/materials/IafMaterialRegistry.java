package org.vocalsky.extended_tinker.compat.iaf.materials;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonPlatingMaterialStats;
import org.vocalsky.extended_tinker.util.MaterialRegistryImpl;
import slimeknights.mantle.network.packet.ISimplePacket;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.events.MaterialsLoadedEvent;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.materials.definition.UpdateMaterialsPacket;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsManager;
import slimeknights.tconstruct.library.materials.stats.UpdateMaterialStatsPacket;
import slimeknights.tconstruct.library.materials.traits.MaterialTraitsManager;
import slimeknights.tconstruct.library.materials.traits.UpdateMaterialTraitsPacket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class IafMaterialRegistry {
    public static final MaterialStatsId MELEE_HARVEST = new MaterialStatsId(TConstruct.getResource("melee_harvest"));
    public static final MaterialStatsId RANGED = new MaterialStatsId(TConstruct.getResource("ranged"));
    public static final MaterialStatsId ARMOR = new MaterialStatsId(TConstruct.getResource("armor"));
    static IafMaterialRegistry INSTANCE;
    private static final Map<MaterialStatsId, IMaterial> FIRST_MATERIALS = new HashMap();
    private final MaterialManager materialManager;
    private final MaterialStatsManager materialStatsManager;
    private final MaterialTraitsManager materialTraitsManager;
    private final IMaterialRegistry registry;
    private static boolean materialsLoaded = false;
    private static boolean statsLoaded = false;
    private static boolean traitsLoaded = false;
    @VisibleForTesting
    static boolean fullyLoaded = false;
    private static final Function<MaterialStatsId, IMaterial> FIRST_LOADER = (statsId) -> {
        IMaterialRegistry instance = getInstance();

        for(IMaterial material : instance.getVisibleMaterials()) {
            if (!material.isHidden() && instance.getMaterialStats(material.getIdentifier(), statsId).isPresent()) {
                return material;
            }
        }

        return IMaterial.UNKNOWN;
    };

    public static IMaterialRegistry getInstance() {
        return INSTANCE.registry;
    }

    public static void init() {
        INSTANCE = new IafMaterialRegistry();
        IEventBus var10000 = MinecraftForge.EVENT_BUS;
        IafMaterialRegistry var10001 = INSTANCE;
        Objects.requireNonNull(var10001);
        var10000.addListener(var10001::addDataPackListeners);
        var10000 = MinecraftForge.EVENT_BUS;
        var10001 = INSTANCE;
        Objects.requireNonNull(var10001);
        var10000.addListener(var10001::onDatapackSync);
    }

    public static boolean isFullyLoaded() {
        return INSTANCE != null && fullyLoaded;
    }

    public IafMaterialRegistry() {
        this.materialManager = new MaterialManager(() -> {
            materialsLoaded = true;
            checkAllLoaded();
        });
        this.materialStatsManager = new MaterialStatsManager(() -> {
            statsLoaded = true;
            checkAllLoaded();
        });
        this.materialTraitsManager = new MaterialTraitsManager(() -> {
            traitsLoaded = true;
            checkAllLoaded();
        });
        this.registry = new MaterialRegistryImpl(this.materialManager, this.materialStatsManager, this.materialTraitsManager);

        for(MaterialStatType<?> type : DragonPlatingMaterialStats.TYPES) {
            this.registry.registerStatType(type, ARMOR);
        }

//        this.registry.registerStatType(SkullStats.TYPE);
    }

    @VisibleForTesting
    IafMaterialRegistry(IMaterialRegistry registry) {
        this.registry = registry;
        this.materialManager = null;
        this.materialStatsManager = null;
        this.materialTraitsManager = null;
    }

    public static void updateMaterialsFromServer(UpdateMaterialsPacket packet) {
        INSTANCE.materialManager.updateMaterialsFromServer(packet.getMaterials(), packet.getRedirects(), packet.getTags());
    }

    public static void updateMaterialStatsFromServer(UpdateMaterialStatsPacket packet) {
        INSTANCE.materialStatsManager.updateMaterialStatsFromServer(packet.getMaterialToStats());
    }

    public static void updateMaterialTraitsFromServer(UpdateMaterialTraitsPacket packet) {
        INSTANCE.materialTraitsManager.updateFromServer(packet.getMaterialToTraits());
    }

    public static IMaterial getMaterial(MaterialId id) {
        return getInstance().getMaterial(id);
    }

    public static Collection<IMaterial> getMaterials() {
        return INSTANCE.registry.getVisibleMaterials();
    }

    public static IMaterial firstWithStatType(MaterialStatsId id) {
        return (IMaterial)FIRST_MATERIALS.computeIfAbsent(id, FIRST_LOADER);
    }

    private static void checkAllLoaded() {
        if (materialsLoaded && statsLoaded && traitsLoaded) {
            materialsLoaded = false;
            statsLoaded = false;
            traitsLoaded = false;
            fullyLoaded = true;
            FIRST_MATERIALS.clear();
            MinecraftForge.EVENT_BUS.post(new MaterialsLoadedEvent());
        } else {
            fullyLoaded = false;
        }

    }

    private void addDataPackListeners(AddReloadListenerEvent event) {
        event.addListener(this.materialManager);
        this.materialManager.setConditionContext(event.getConditionContext());
        event.addListener(this.materialStatsManager);
        event.addListener(this.materialTraitsManager);
    }

    private void sendPackets(ServerPlayer player, ISimplePacket[] packets) {
        if (player.connection.connection.isMemoryConnection()) {
            fullyLoaded = true;
            MinecraftForge.EVENT_BUS.post(new MaterialsLoadedEvent());
        } else {
            TinkerNetwork network = TinkerNetwork.getInstance();
            PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> player);

            for(ISimplePacket packet : packets) {
                network.send(target, packet);
            }
        }

    }

    private void onDatapackSync(OnDatapackSyncEvent event) {
        ISimplePacket[] packets = new ISimplePacket[]{this.materialManager.getUpdatePacket(), this.materialStatsManager.getUpdatePacket(), this.materialTraitsManager.getUpdatePacket()};
        ServerPlayer targetedPlayer = event.getPlayer();
        if (targetedPlayer != null) {
            this.sendPackets(targetedPlayer, packets);
        } else {
            for(ServerPlayer player : event.getPlayerList().getPlayers()) {
                this.sendPackets(player, packets);
            }
        }

    }
}
