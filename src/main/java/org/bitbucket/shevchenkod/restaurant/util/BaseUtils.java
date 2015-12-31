package org.bitbucket.shevchenkod.restaurant.util;

import org.apache.commons.codec.digest.DigestUtils;



public class BaseUtils {

	public static String md5(String s) {
		if (s == null) {
			return null;
		}
		return DigestUtils.md5Hex(s);
	}
}
