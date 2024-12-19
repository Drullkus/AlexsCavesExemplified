package org.crimsoncrips.alexscavesexemplified.networking;

import com.crimsoncrips.alexsmobsinteraction.AlexsMobsInteraction;
import com.crimsoncrips.alexsmobsinteraction.networking.FarseerPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            AlexsCavesExemplified.prefix("farseer"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, FarseerPacket.class, FarseerPacket::encode, FarseerPacket::decode, FarseerPacket::handle);
    }
}
