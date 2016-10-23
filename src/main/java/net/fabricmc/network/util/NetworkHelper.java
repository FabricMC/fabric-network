/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.network.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.base.Fabric;
import net.fabricmc.network.AbstractChannel;
import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.Asynchronous;
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
