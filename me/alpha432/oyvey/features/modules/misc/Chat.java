package me.alpha432.oyvey.features.modules.misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chat extends Module {

    private static Chat INSTANCE = new Chat();
    private final Timer timer = new Timer();
    private Setting custom = this.register(new Setting("Custom", "custom suffix"));
    public Setting suffix;
    public Setting clean;
    public Setting infinite;
    public Setting autoQMain;
    public Setting qNotification;
    public Setting qDelay;
    public Setting timeStamps;
    public Setting rainbowTimeStamps;
    public Setting bracket;
    public Setting space;
    public Setting all;
    public Setting shrug;
    public Setting disability;

    public Chat() {
        super("Chat", "Modifies your chat", Module.Category.MISC, true, false, false);
        this.suffix = this.register(new Setting("Suffix", Chat.Suffix.ONE, "Your Suffix."));
        this.clean = this.register(new Setting("CleanChat", Boolean.valueOf(false), "Cleans your chat"));
        this.infinite = this.register(new Setting("Infinite", Boolean.valueOf(false), "Makes your chat infinite."));
        this.autoQMain = this.register(new Setting("AutoQMain", Boolean.valueOf(false), "Spams AutoQMain"));
        this.qNotification = this.register(new Setting("QNotification", Boolean.valueOf(false), test<invokedynamic>(this)));
        this.qDelay = this.register(new Setting("QDelay", Integer.valueOf(9), Integer.valueOf(1), Integer.valueOf(90), test<invokedynamic>(this)));
        this.timeStamps = this.register(new Setting("Time", TextUtil.Color.NONE));
        this.rainbowTimeStamps = this.register(new Setting("RainbowTimeStamps", Boolean.valueOf(false), test<invokedynamic>(this)));
        this.bracket = this.register(new Setting("Bracket", TextUtil.Color.WHITE, test<invokedynamic>(this)));
        this.space = this.register(new Setting("Space", Boolean.valueOf(true), test<invokedynamic>(this)));
        this.all = this.register(new Setting("All", Boolean.valueOf(false), test<invokedynamic>(this)));
        this.shrug = this.register(new Setting("Shrug", Boolean.valueOf(false)));
        this.disability = this.register(new Setting("Disability", Boolean.valueOf(false)));
        this.setInstance();
    }

    public static Chat getInstance() {
        if (Chat.INSTANCE == null) {
            Chat.INSTANCE = new Chat();
        }

        return Chat.INSTANCE;
    }

    private void setInstance() {
        Chat.INSTANCE = this;
    }

    public void onUpdate() {
        if (((Boolean) this.shrug.getValue()).booleanValue()) {
            Chat.mc.player.sendChatMessage(TextUtil.shrug);
            this.shrug.setValue(Boolean.valueOf(false));
        }

        if (((Boolean) this.autoQMain.getValue()).booleanValue()) {
            if (!this.shouldSendMessage(Chat.mc.player)) {
                return;
            }

            if (((Boolean) this.qNotification.getValue()).booleanValue()) {
                Command.sendMessage("awa");
            }

            Chat.mc.player.sendChatMessage("waw");
            this.timer.reset();
        }

    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            String s = packet.getMessage();

            if (s.startsWith("/")) {
                return;
            }

            switch ((Chat.Suffix) this.suffix.getValue()) {
            case ONE:
                s = s + " : á´?Ê?á´?á´˜á´? á´?ÊŸÉªá´?É´á´?";

            default:
                if (s.length() >= 256) {
                    s = s.substring(0, 256);
                }

                packet.message = s;
            }
        }

    }

    @SubscribeEvent
    public void onChatPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketChat) {
            ;
        }

    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && this.timeStamps.getValue() != TextUtil.Color.NONE && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat) event.getPacket()).isSystem()) {
                return;
            }

            String originalMessage = ((SPacketChat) event.getPacket()).chatComponent.getFormattedText();
            String message = this.getTimeString(originalMessage) + originalMessage;

            ((SPacketChat) event.getPacket()).chatComponent = new TextComponentString(message);
        }

    }

    public String getTimeString(String message) {
        String date = (new SimpleDateFormat("k:mm")).format(new Date());

        if (((Boolean) this.rainbowTimeStamps.getValue()).booleanValue()) {
            String timeString = "<" + date + ">" + (((Boolean) this.space.getValue()).booleanValue() ? " " : "");
            StringBuilder builder = new StringBuilder(timeString);

            builder.insert(0, "Â§+");
            return builder.toString();
        } else {
            return (this.bracket.getValue() == TextUtil.Color.NONE ? "" : TextUtil.coloredString("<", (TextUtil.Color) this.bracket.getValue())) + TextUtil.coloredString(date, (TextUtil.Color) this.timeStamps.getValue()) + (this.bracket.getValue() == TextUtil.Color.NONE ? "" : TextUtil.coloredString(">", (TextUtil.Color) this.bracket.getValue())) + (((Boolean) this.space.getValue()).booleanValue() ? " " : "") + "Â§r";
        }
    }

    private boolean shouldSendMessage(EntityPlayer player) {
        return player.dimension != 1 ? false : (!this.timer.passedS((double) ((Integer) this.qDelay.getValue()).intValue()) ? false : player.getPosition().equals(new Vec3i(0, 240, 0)));
    }

    private boolean lambda$new$5(Object v) {
        return this.timeStamps.getValue() != TextUtil.Color.NONE;
    }

    private boolean lambda$new$4(Object v) {
        return this.timeStamps.getValue() != TextUtil.Color.NONE;
    }

    private boolean lambda$new$3(Object v) {
        return this.timeStamps.getValue() != TextUtil.Color.NONE;
    }

    private boolean lambda$new$2(Object v) {
        return this.timeStamps.getValue() != TextUtil.Color.NONE;
    }

    private boolean lambda$new$1(Object v) {
        return ((Boolean) this.autoQMain.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Object v) {
        return ((Boolean) this.autoQMain.getValue()).booleanValue();
    }

    public static enum Suffix {

        NONE, ONE, TWO, THREE, CLAW;
    }
}
