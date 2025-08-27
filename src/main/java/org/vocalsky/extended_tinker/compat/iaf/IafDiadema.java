package org.vocalsky.extended_tinker.compat.iaf;

import com.csdy.tcondiadema.frames.CsdyRegistries;
import com.csdy.tcondiadema.frames.diadema.DiademaType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.diadema.MagneticStormSurge.MagneticStormSurgeDiadema;

public class IafDiadema {
    public static final DeferredRegister<DiademaType> DIADEMA_TYPES = DeferredRegister.create(CsdyRegistries.DIADEMA_TYPE, Extended_tinker.MODID);

    public static final RegistryObject<DiademaType> MagneticStormSurge =
            DIADEMA_TYPES.register("magnetic_storm_surge", () -> DiademaType.create(MagneticStormSurgeDiadema::new));
}
