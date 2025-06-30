package lucie.deathtaxes.network;

import lucie.deathtaxes.network.clientbound.LostItemsPacket;
import lucie.deathtaxes.toast.LostToast;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class Packets
{
    public static void pocketPackage(LostItemsPacket message, NetworkEvent.Context context)
    {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().getToasts().addToast(new LostToast(message.items))));
    }
}
