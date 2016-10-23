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
import net.fabricmc.network.AbstractPacket;
import net.minecraft.util.PacketByteBuf;

public class IndexedChannel extends SimpleAbstractChannel<AbstractPacket> {

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
}
