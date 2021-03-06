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

package com.google.code.http4j.impl.conn;

import java.io.IOException;

import com.google.code.http4j.Connection;
import com.google.code.http4j.Host;
import com.google.code.http4j.utils.IOUtils;

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class SingleConnectionManager extends AbstractConnectionManager {

	public SingleConnectionManager() {
		super();
	}

	@Override
	protected void doShutdown() {}

	@Override
	public boolean doRelease(Connection connection) {
		IOUtils.close(connection);
		return false;
	}

	@Override
	protected Connection getConnection(Host host) throws InterruptedException,
			IOException {
		return host.newConnection();
	}
}
