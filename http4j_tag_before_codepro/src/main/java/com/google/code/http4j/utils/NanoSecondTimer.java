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

/**
 * @author <a href="mailto:guilin.zhang@hotmail.com">Zhang, Guilin</a>
 */
public class NanoSecondTimer implements Timer {
	
	protected long start;
	
	protected long stop;
	
	@Override
	public long getDuration() {
		return stop - start;
	}
	
	@Override
	public void start() {
		start = getCurrentTime();
	}

	@Override
	public void stop() {
		stop = getCurrentTime();
	}

	public long getStart() {
		return start;
	}

	public long getStop() {
		return stop;
	}
	
	@Override
	public void reset() {
		start = 0;
		stop = 0;
	}

	protected long getCurrentTime() {
		return System.nanoTime();
	}
}
