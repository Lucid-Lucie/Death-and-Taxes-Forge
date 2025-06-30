package lucie.deathtaxes.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import lucie.deathtaxes.DeathTaxes;
import lucie.deathtaxes.client.model.ScavengerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;

public class LostToast implements Toast
{
    private final List<ItemStack> items;
    private final ScavengerModel model;

    private static final ResourceLocation BUNDLE_FRONT = DeathTaxes.withModNamespace("textures/gui/toasts/scavenger_bundle_open_front.png");
    private static final ResourceLocation BUNDLE_BACK = DeathTaxes.withModNamespace("textures/gui/toasts/scavenger_bundle_open_back.png");

    public LostToast(List<ItemStack> items)
    {
        this.items = items;
        this.model = new ScavengerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ScavengerModel.LAYER_LOCATION));
    }

    @Override
    @Nonnull
    public Visibility render(@Nonnull GuiGraphics guiGraphics, @Nonnull ToastComponent toastComponent, long timeSinceLastVisible)
    {
        Minecraft minecraft = toastComponent.getMinecraft();
        Font font = minecraft.font;

        // Render head for 5 seconds and each item for 2 seconds.
        int time = 2000 * this.items.size() + 5000;

        if (!this.items.isEmpty())
        {
            // Render background.
            guiGraphics.blit(Toast.TEXTURE, 0, 0, 0, 0, this.width(), this.height());

            if (timeSinceLastVisible <= 5000)
            {
                // Render head for the first 5 seconds.
                this.renderHead(guiGraphics.pose());
            }
            else
            {
                // Render items for the remaining time.
                this.renderItem(guiGraphics, timeSinceLastVisible);
            }

            // Render information.
            this.renderText(guiGraphics, font, I18n.get("toast." + DeathTaxes.MODID + ".info.title"), 31, 7, 0x863a4a, timeSinceLastVisible, time);
            this.renderText(guiGraphics, font, I18n.get("toast." + DeathTaxes.MODID + ".info.description"), 31, 18, 0xFFFFFF, timeSinceLastVisible, time);
            return timeSinceLastVisible <= time ? Visibility.SHOW : Visibility.HIDE;
        }

        return Visibility.HIDE;
    }

    private void renderText(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color, long time, long stop)
    {
        int textWidth = font.width(text);
        int availableWidth = this.width() - 10 - x;

        if (textWidth > availableWidth)
        {
            int overflowWidth = textWidth - availableWidth;
            double scrollRange = Math.max((double) overflowWidth * 0.5D, 3.0D);
            double scrollOffset = (time / 300.0D) - (scrollRange / 2);
            double scrollProgress = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * scrollOffset / scrollRange)) / 2.0D + 0.5D;
            double scrollAmount = Mth.lerp(scrollProgress, 0.0D, overflowWidth);
            double currentOffset = time > 300 && time < stop ? scrollAmount : 0;
            guiGraphics.enableScissor(guiGraphics.guiWidth() - this.width() + x, 0, guiGraphics.guiWidth() - 10, guiGraphics.guiHeight());
            guiGraphics.drawString(font, text, x - (int) currentOffset, y, color);
            guiGraphics.disableScissor();
        }
        else
        {
            guiGraphics.drawString(font, text, x, y, color);
        }
    }

    private void renderItem(GuiGraphics guiGraphics, long timeSinceLastVisible)
    {
        int index = (int) ((timeSinceLastVisible / 2000) % this.items.size());
        ItemStack itemStack = this.items.get(index);
        guiGraphics.blit(LostToast.BUNDLE_BACK, 8, 8, 0, 0, 16, 16, 16, 16);
        guiGraphics.renderItem(itemStack, 8, 8);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 500);
        guiGraphics.blit(LostToast.BUNDLE_FRONT, 8, 8, 0, 0, 16, 16, 16, 16);
        guiGraphics.pose().popPose();
    }
    
    private void renderHead(PoseStack poseStack)
    {
        poseStack.pushPose();
        poseStack.translate(16, 24, 100);
        poseStack.scale(20.0F, 20.0F, 20.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-155));
        Vector3f light0 = new Vector3f(0.8F, 0.7F, -0.6F).normalize();
        Vector3f light1 = new Vector3f(-0.8F, -0.7F, 0.6F).normalize();
        RenderSystem.setupGui3DDiffuseLighting(light0, light1);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(DeathTaxes.withModNamespace("textures/entity/scavenger.png")));
        model.getHead().render(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}