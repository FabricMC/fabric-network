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

package net.fabricmc.network.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.api.Side;
import net.fabricmc.api.Sided;
import net.fabricmc.base.Fabric;
import net.fabricmc.network.AbstractChannel;
import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.NetworkManager;
import net.fabricmc.network.util.NetworkHelper;
import net.minecraft.entity.player.EntityPlayerClient;
import net.minecraft.entity.player.EntityPlayerServer;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IndexedChannel extends AbstractChannel<AbstractPacket> {

	private BiMap<Class<? extends AbstractPacket>, Integer> ids = HashBiMap.create();
	private int nextId = 0;

	public void register(Class<? extends AbstractPacket> clazz) {
		ids.put(clazz, nextId++);
	}

	@Override
	public void write(PacketByteBuf buf, AbstractPacket packet) {
		buf.writeInt(ids.get(packet.getClass()));
		packet.write(buf);
	}

	@Override
	public AbstractPacket read(PacketByteBuf buf) {
		try {
			AbstractPacket packet = ids.inverse().get(buf.readInt()).newInstance();
			packet.read(buf);
			return packet;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private PacketByteBuf createPacketBuf(AbstractPacket packet) {
		return NetworkHelper.createPacketBuf(this, packet);
	}

	private String getChannel() {
		return NetworkManager.CHANNEL_PREFIX + NetworkManager.getChannelName(this);
	}

//	Client -> Server
	@Sided(Side.CLIENT)
	public void sendToServer(AbstractPacket packet) {
		((EntityPlayerClient) Fabric.getSidedHandler().getClientPlayer()).networkHandler.a(new SPacketCustomPayload(getChannel(), createPacketBuf(packet)));
	}

//	Server -> Client(s)
	public void sendToPlayer(AbstractPacket packet, EntityPlayerServer player) {
		player.networkHandler.sendPacket(new CPacketCustomPayload(getChannel(), createPacketBuf(packet)));
	}

	public void sendToAll(AbstractPacket packet, List<EntityPlayerServer> players) {
		CPacketCustomPayload wrapper = new CPacketCustomPayload(getChannel(), createPacketBuf(packet));
		players.forEach(player -> player.networkHandler.sendPacket(wrapper));
	}

	public void sendToAll(AbstractPacket packet, List<EntityPlayerServer> players, Predicate<EntityPlayerServer> predicate) {
		sendToAll(packet, players.stream()
				.filter(predicate)
				.collect(Collectors.toList()));
	}

	public void sendToAllInRadius(AbstractPacket packet, WorldServer world, Vec3d pos, double radius) {
		double maxDist = radius * radius + radius * radius + radius * radius;
		sendToAll(packet, world.b(EntityPlayerServer.class,
				player -> player.e(pos.x, pos.y, pos.z) <= maxDist));
	}

	public void sendToAllInRadius(AbstractPacket packet, WorldServer world, Vec3i pos, double radius) {
		sendToAllInRadius(packet, world, new Vec3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5), radius);
	}

	public void sendToAllInRadius(AbstractPacket packet, int dimension, Vec3d pos, double radius) {
		sendToAllInRadius(packet, Fabric.getSidedHandler().getServerInstance().getWorld(dimension), pos, radius);
	}

	public void sendToAllInRadius(AbstractPacket packet, int dimension, Vec3i pos, double radius) {
		sendToAllInRadius(packet, Fabric.getSidedHandler().getServerInstance().getWorld(dimension), pos, radius);
	}

}
