package lucie.deathtaxes.network;

import lucie.deathtaxes.DeathTaxes;
import lucie.deathtaxes.network.clientbound.LostItemsPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network
{
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(DeathTaxes.withModNamespace("main"), () -> "1", s -> true, s -> true);

    private static int packetId = 0;

    private static int id()
    {
        return packetId++;
    }

    public static void register()
    {
        INSTANCE.registerMessage(
                id(),
                LostItemsPacket.class,
                LostItemsPacket::toBytes,
                LostItemsPacket::new,
                LostItemsPacket::handle);
    }
}
