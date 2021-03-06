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
import net.minecraft.network.handler.NetworkGameHandlerServer;
import net.minecraft.network.packet.server.SPacketCustomPayload;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkGameHandlerServer.class, remap = false)
public abstract class SPacketHandler {

	@Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/server/SPacketCustomPayload;)V", at = @At("RETURN"))
	public void onCustomPayload(SPacketCustomPayload packet, CallbackInfo info) {
		if (packet.getChannel().startsWith(NetworkManager.CHANNEL_PREFIX)) {
			PacketByteBuf buf = packet.getData();
			AbstractChannel channel = NetworkManager.getChannel(packet.getChannel().substring(NetworkManager.CHANNEL_PREFIX.length()));
			AbstractPacket thePacket = channel.read(buf);
			NetworkHelper.handle(channel, thePacket);
		}
	}

}
