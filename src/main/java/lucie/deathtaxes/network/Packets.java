package lucie.deathtaxes.network;

import lucie.deathtaxes.network.clientbound.LostItemsPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packets
{
    public static void pocketPackage(LostItemsPacket message, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> message.handle(context)));
        context.get().setPacketHandled(true);
    }
}
