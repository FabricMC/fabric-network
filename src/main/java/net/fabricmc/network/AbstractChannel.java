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

import net.minecraft.util.PacketByteBuf;

/**
 * The base class for a channel receiving and sending packets.
 * Implementers are encouraged to use impl.SimpleAbstractChannel instead,
 * as it includes a lot of useful helper functions.
 *
 * @param <T> The base packet type.
 */
public abstract class AbstractChannel<T extends AbstractPacket> {
    /**
     * Write a packet to a byte buffer.
     * @param buf
     * @param packet
     */
    public abstract void write(PacketByteBuf buf, T packet);

    /**
     * Read a packet from a byte buffer.
     * @param buf
     * @return
     */
    public abstract T read(PacketByteBuf buf);

}
