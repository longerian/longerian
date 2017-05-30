package me.longerian.abc.chapter3;

import java.io.IOException;
import java.io.OutputStream;

public interface Framer {

	void frameMsg(byte[] message, OutputStream out) throws IOException;
	byte[] nextMsg() throws IOException;
	
}
