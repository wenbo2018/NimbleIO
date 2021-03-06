package com.generallycloud.nio.buffer;

import java.nio.ByteBuffer;

public class HeapMemoryPoolV2 extends MemoryPoolV2{

	public HeapMemoryPoolV2(int capacity) {
		super(capacity);
	}

	protected ByteBuffer allocateMemory(int capacity) {
		return ByteBuffer.allocate(capacity);
	}

	public void freeMemory() {
		this.memory.clear();
	}
	
}
