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

package net.fabricmc.network.mixin;

import net.fabricmc.network.AbstractChannel;
import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.NetworkManager;
import net.fabricmc.network.util.NetworkHelper;
import net.minecraft.client.network.NetworkHandlerClient;
import net.minecraft.network.packet.client.CPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkHandlerClient.class, remap = false)
public abstract class CPacketHandler {

	@Inject(method = "a(Lnet/minecraft/network/packet/client/CPacketCustomPayload;)V", at = @At("RETURN"))
	public void a(CPacketCustomPayload packet, CallbackInfo info) {
		if (packet.channel.startsWith(NetworkManager.CHANNEL_PREFIX)) {
			PacketByteBuf buf = packet.getData();
			AbstractChannel channel = NetworkManager.getChannel(packet.channel.substring(NetworkManager.CHANNEL_PREFIX.length()));
			AbstractPacket thePacket = channel.read(buf);
			NetworkHelper.handle(channel, thePacket);
		}
	}

}
