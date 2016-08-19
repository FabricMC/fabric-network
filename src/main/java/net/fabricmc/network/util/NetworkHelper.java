package net.fabricmc.network.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.base.Fabric;
import net.fabricmc.network.*;
import net.minecraft.util.PacketByteBuf;

public class NetworkHelper {

	public static <T extends AbstractPacket> PacketByteBuf createPacketBuf(AbstractChannel<T> channel, T packet) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		channel.write(buf, packet);
		return buf;
	}

	public static <T extends AbstractPacket> void handle(AbstractChannel<T> channel, T packet) {
		if (packet.getClass().isAnnotationPresent(Asynchronous.class)) {
			packet.handle();
		} else {
			Fabric.getSidedHandler().runOnMainThread(packet::handle);
		}
	}

}
