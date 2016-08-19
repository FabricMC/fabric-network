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

import net.minecraft.util.PacketByteBuf;

import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BufferHelper {

	public static <T> void writeArray(PacketByteBuf buf, T[] value, BiConsumer<PacketByteBuf, T> writer) {
		buf.writeInt(value.length);
		for (T it : value) {
			writer.accept(buf, it);
		}
	}

	public static <T> T[] readArray(PacketByteBuf buf, Class<T> clazz, Function<PacketByteBuf, T> reader) {
		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(clazz, buf.readInt());
		for (int i = 0; i < array.length; i++) {
			array[i] = reader.apply(buf);
		}
		return array;
	}

	public static void writeString(PacketByteBuf buf, String value) {
		buf.writeInt(value.length());
		buf.writeString(value);
	}

	public static String readString(PacketByteBuf buf) {
		return buf.readString(buf.readInt());
	}

}
