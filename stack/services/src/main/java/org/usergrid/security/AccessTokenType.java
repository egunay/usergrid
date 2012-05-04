/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.usergrid.security;

import static org.usergrid.utils.CodecUtils.base64;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum AccessTokenType {
	ACCESS("ac", true), REFRESH("re", false), OFFLINE("of", false), EMAIL("em",
			false);

	public static final int PREFIX_LENGTH = 3;
	public static final int BASE64_PREFIX_LENGTH = 4;

	private final String prefix;
	private final String base64Prefix;
	private final boolean expires;
	private static Map<String, AccessTokenType> prefixes;
	private static Map<String, AccessTokenType> base64Prefixes;

	private synchronized static void register(AccessTokenType type) {
		if (prefixes == null) {
			prefixes = new ConcurrentHashMap<String, AccessTokenType>();
		}
		if (base64Prefixes == null) {
			base64Prefixes = new ConcurrentHashMap<String, AccessTokenType>();
		}
		prefixes.put(type.getPrefix(), type);
		base64Prefixes.put(type.getBase64Prefix(), type);
	}

	AccessTokenType(String prefix, boolean expires) {
		this.prefix = prefix;
		this.expires = expires;
		base64Prefix = base64(prefix + "-");
		register(this);
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean getExpires() {
		return expires;
	}

	public String getBase64Prefix() {
		return base64Prefix;
	}

	public static AccessTokenType getFromBase64String(String token) {
		if (token == null) {
			return null;
		}
		if (token.length() >= 4) {
			return base64Prefixes.get(token.substring(0, 4));
		}
		return null;
	}

}
