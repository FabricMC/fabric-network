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

package net.fabricmc.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.buffer.Unpooled;
import net.fabricmc.base.Fabric;
import net.minecraft.util.PacketByteBuf;

import java.lang.reflect.Method;

public class NetworkManager {

	public static final String CHANNEL_PREFIX = "Fabric|";

	private static final BiMap<String, AbstractChannel> channels = HashBiMap.create();

	public static void registerChannel(String name, AbstractChannel<?> channel) {
		if (channelExists(name)) throw new IllegalArgumentException("Channel " + name + " is already registered");
		channels.put(name, channel);
	}

	public static <T extends AbstractPacket> AbstractChannel<T> getChannel(String name) {
		return channels.get(name);
	}

	public static String getChannelName(AbstractChannel<?> channel) {
		return channels.inverse().get(channel);
	}

	public static boolean channelExists(String name) {
		return channels.containsKey(name);
	}

}
