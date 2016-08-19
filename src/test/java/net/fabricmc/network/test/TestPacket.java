package net.fabricmc.network.test;

import net.fabricmc.network.AbstractPacket;
import net.fabricmc.network.Asynchronous;
import net.minecraft.util.PacketByteBuf;

public class TestPacket extends AbstractPacket {

	private int value;

	public TestPacket(int value) {
		this.value = value;
	}

	public TestPacket() {
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(value);
	}

	@Override
	public void read(PacketByteBuf buf) {
		value = buf.readInt();
	}

	@Override
	public void handle() {
		System.out.println(String.format("Value: %d, Thread: %s", value, Thread.currentThread().getName()));
	}

}
