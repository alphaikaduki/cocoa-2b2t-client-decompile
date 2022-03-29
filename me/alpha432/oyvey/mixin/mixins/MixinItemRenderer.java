package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.event.events.RenderItemEvent;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.render.HandChams;
import me.alpha432.oyvey.features.modules.render.NoRender;
import me.alpha432.oyvey.features.modules.render.SmallShield;
import me.alpha432.oyvey.features.modules.render.ViewModel;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ItemRenderer.class})
public abstract class MixinItemRenderer {

    public Minecraft mc;
    private boolean injection = true;

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer abstractclientplayer, float f, float f1, EnumHand enumhand, float f2, ItemStack itemstack, float f3);

    @Inject(
        method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderItemInFirstPersonHooks(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0F;
            float yOffset = 0.0F;

            this.injection = false;
            if (hand == EnumHand.MAIN_HAND && offset.isOn() && player.getHeldItemMainhand() != ItemStack.EMPTY) {
                xOffset = ((Float) offset.mainX.getValue()).floatValue();
                yOffset = ((Float) offset.mainY.getValue()).floatValue();
            }

            this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            this.injection = true;
        }

    }

    @Inject(
        method = { "transformSideFirstPerson"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo cancel) {
        RenderItemEvent event = new RenderItemEvent(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D);

        MinecraftForge.EVENT_BUS.post(event);
        if (ViewModel.getInstance().isEnabled()) {
            boolean bob = ViewModel.getInstance().isDisabled() || ((Boolean) ViewModel.getInstance().doBob.getValue()).booleanValue();
            int i = hand == EnumHandSide.RIGHT ? 1 : -1;

            GlStateManager.translate((float) i * 0.56F, -0.52F + (bob ? p_187459_2_ : 0.0F) * -0.6F, -0.72F);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(event.getMainX(), event.getMainY(), event.getMainZ());
                RenderUtil.rotationHelper((float) event.getMainRotX(), (float) event.getMainRotY(), (float) event.getMainRotZ());
            } else {
                GlStateManager.translate(event.getOffX(), event.getOffY(), event.getOffZ());
                RenderUtil.rotationHelper((float) event.getOffRotX(), (float) event.getOffRotY(), (float) event.getOffRotZ());
            }

            cancel.cancel();
        }

    }

    @Redirect(
        method = { "renderArmFirstPerson"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
                ordinal = 0
            )
    )
    public void translateHook(float x, float y, float z) {
        SmallShield offset = SmallShield.getINSTANCE();
        boolean shiftPos = Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getHeldItemMainhand() != ItemStack.EMPTY && offset.isOn();

        GlStateManager.translate(x + (shiftPos ? ((Float) offset.mainX.getValue()).floatValue() : 0.0F), y + (shiftPos ? ((Float) offset.mainY.getValue()).floatValue() : 0.0F), z);
    }

    @Inject(
        method = { "renderFireInFirstPerson"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && ((Boolean) NoRender.getInstance().fire.getValue()).booleanValue()) {
            info.cancel();
        }

    }

    @Inject(
        method = { "transformEatFirstPerson"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo cancel) {
        if (ViewModel.getInstance().isEnabled()) {
            if (!((Boolean) ViewModel.getInstance().noEatAnimation.getValue()).booleanValue()) {
                float f = (float) Minecraft.getMinecraft().player.getItemInUseCount() - p_187454_1_ + 1.0F;
                float f1 = f / (float) stack.getMaxItemUseDuration();
                float f3;

                if (f1 < 0.8F) {
                    f3 = MathHelper.abs(MathHelper.cos(f / 4.0F * 3.1415927F) * 0.1F);
                    GlStateManager.translate(0.0F, f3, 0.0F);
                }

                f3 = 1.0F - (float) Math.pow((double) f1, 27.0D);
                int i = hand == EnumHandSide.RIGHT ? 1 : -1;

                GlStateManager.translate((double) (f3 * 0.6F * (float) i) * ((Double) ViewModel.getInstance().eatX.getValue()).doubleValue(), (double) (f3 * 0.5F) * -((Double) ViewModel.getInstance().eatY.getValue()).doubleValue(), 0.0D);
                GlStateManager.rotate((float) i * f3 * 90.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate((float) i * f3 * 30.0F, 0.0F, 0.0F, 1.0F);
            }

            cancel.cancel();
        }

    }

    @Inject(
        method = { "renderSuffocationOverlay"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && ((Boolean) NoRender.getInstance().blocks.getValue()).booleanValue()) {
            ci.cancel();
        }

    }

    @Shadow
    protected abstract void renderArmFirstPerson(float f, float f1, EnumHandSide enumhandside);

    @Inject(
        method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0F;
            float yOffset = 0.0F;

            this.injection = false;
            if (hand == EnumHand.MAIN_HAND) {
                if (offset.isOn()) {
                    xOffset = ((Float) offset.mainX.getValue()).floatValue();
                    yOffset = ((Float) offset.mainY.getValue()).floatValue();
                }
            } else if (offset.isOn()) {
                xOffset = ((Float) offset.offX.getValue()).floatValue();
                yOffset = ((Float) offset.offY.getValue()).floatValue();
            }

            if (HandChams.getINSTANCE().isOn() && hand == EnumHand.MAIN_HAND && stack.isEmpty()) {
                if (((HandChams.RenderMode) HandChams.getINSTANCE().mode.getValue()).equals(HandChams.RenderMode.WIREFRAME)) {
                    this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                }

                GlStateManager.pushMatrix();
                if (((HandChams.RenderMode) HandChams.getINSTANCE().mode.getValue()).equals(HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPushAttrib(1048575);
                } else {
                    GlStateManager.pushAttrib();
                }

                if (((HandChams.RenderMode) HandChams.getINSTANCE().mode.getValue()).equals(HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPolygonMode(1032, 6913);
                }

                GL11.glDisable(3553);
                GL11.glDisable(2896);
                if (((HandChams.RenderMode) HandChams.getINSTANCE().mode.getValue()).equals(HandChams.RenderMode.WIREFRAME)) {
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                }

                GL11.glColor4f(((Boolean) ClickGui.getInstance().rainbow.getValue()).booleanValue() ? (float) ColorUtil.rainbow(((Integer) ClickGui.getInstance().rainbowHue.getValue()).intValue()).getRed() / 255.0F : (float) ((Integer) HandChams.getINSTANCE().red.getValue()).intValue() / 255.0F, ((Boolean) ClickGui.getInstance().rainbow.getValue()).booleanValue() ? (float) ColorUtil.rainbow(((Integer) ClickGui.getInstance().rainbowHue.getValue()).intValue()).getGreen() / 255.0F : (float) ((Integer) HandChams.getINSTANCE().green.getValue()).intValue() / 255.0F, ((Boolean) ClickGui.getInstance().rainbow.getValue()).booleanValue() ? (float) ColorUtil.rainbow(((Integer) ClickGui.getInstance().rainbowHue.getValue()).intValue()).getBlue() / 255.0F : (float) ((Integer) HandChams.getINSTANCE().blue.getValue()).intValue() / 255.0F, (float) ((Integer) HandChams.getINSTANCE().alpha.getValue()).intValue() / 255.0F);
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }

            if (SmallShield.getINSTANCE().isOn() && (!stack.isEmpty || HandChams.getINSTANCE().isOff())) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            } else if (!stack.isEmpty || HandChams.getINSTANCE().isOff()) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
            }

            this.injection = true;
        }

    }
}
