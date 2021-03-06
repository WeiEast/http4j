/**
 * Copyright (C) 2010 Zhang, Guilin <guilin.zhang@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.http4j.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLSocket;

import com.google.code.http4j.HTTP;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public final class IOUtils {
	
	private IOUtils(){}
	
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}

	public static void close(Socket socket) {
		if (null != socket && !socket.isClosed()) {
			try {
				if(!(socket instanceof SSLSocket)) {
					socket.shutdownInput();
					socket.shutdownOutput();
				}
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Extract data from stream by given end expression.e.g. 
	 * <li>http4j [4j] -&gt; http</li>
	 * <li>http4j [4] -&gt; http</li>
	 * <li>http4j [ttp4j] -&gt; h</li>
	 * <li>http4j [http4j] -&gt; ""</li>
	 * <li>http4j [4g] -&gt; "http4j"</li>
	 * 
	 * @param in
	 * @param endExpression
	 *            minimum length of 1
	 * @return bytes
	 */
	public static byte[] extractByEnd(InputStream in, byte... endExpression) throws IOException {
		ByteArrayOutputStream bf = new ByteArrayOutputStream();
		int count = 0;
		int b;
		while (count < endExpression.length && (b = in.read()) != -1) {
			count = (b == endExpression[count]) ? count + 1 : 0;
			bf.write(b);
		}
		
		byte[] bs = bf.toByteArray();
		return count == 0 ? bs : Arrays.copyOf(bs, bs.length - count);
	}

	/**
	 * @param in
	 * @return next chunk, <code>null</code> if reaches the end of chunk body.
	 * @throws IOException 
	 */
	public static byte[] getNextChunk(InputStream in) throws IOException {
		int size = getNextChunkSize(in);
		return size > 0 ? getNextChunk(in, size) : null;
	}

	/**
	 * @see DataInputStream#readFully(byte[])
	 * @param in
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(InputStream in, int size) throws IOException {
		byte[] data = new byte[size];
		new DataInputStream(in).readFully(data);
		return data;
	}
	
	static byte[] getNextChunk(InputStream in, int size) throws IOException {
		byte[] next = read(in, size);
		read(in, 2);// CRLF
		return next;
	}

	static int getNextChunkSize(InputStream in) throws IOException {
		byte[] chunkSize = extractByEnd(in, HTTP.CR, HTTP.LF);
		String s = new String(chunkSize);
		int end = s.indexOf(';');
		s = end < 0 ? s : s.substring(0, end);
		return Integer.parseInt(s.trim(), 16);
	}

	public static byte[] unGzip(byte[] original) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(original.length << 2);
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(original), original.length);
		byte[] buffer = new byte[original.length];
		int read;
		while((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		return out.toByteArray();
	}
}
