package lucie.deathtaxes.network.clientbound;

import lucie.deathtaxes.network.Packets;
import lucie.deathtaxes.toast.LostToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LostItemsPacket
{
    public final List<ItemStack> items;

    public LostItemsPacket(List<ItemStack> items)
    {
        this.items = items;
    }

    public LostItemsPacket(FriendlyByteBuf buffer)
    {
        List<ItemStack> items = new ArrayList<>();

        int size = buffer.readInt();

        for (int i = 0; i < size; i++)
        {
            items.add(buffer.readItem());
        }

        this.items = items;
    }

    public void toBytes(FriendlyByteBuf buffer)
    {
        buffer.writeInt(items.size());

        for (ItemStack itemStack : items)
        {
            buffer.writeItem(itemStack);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier)
    {
        contextSupplier.get().enqueueWork(() -> {
            Packets.pocketPackage(this, contextSupplier.get());
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
