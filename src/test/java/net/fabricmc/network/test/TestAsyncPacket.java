package net.fabricmc.network.test;

import net.fabricmc.network.Asynchronous;

@Asynchronous
public class TestAsyncPacket extends TestPacket {

	public TestAsyncPacket(int value) {
		super(value);
	}

	public TestAsyncPacket() {
	}

}
