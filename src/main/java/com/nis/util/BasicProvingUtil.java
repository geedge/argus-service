package com.nis.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class BasicProvingUtil {
	public static void main(String[] args) {
		// String ip = "::";
		String ip = " fe80::d025:864c:3151:daa0";
		System.out.println(ip + "=" + isIpv6(ip));

	}

	/**
	 * 判断是否是Integer类型
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isIntType(Object obj) {
		if (obj == null) {
			return false;
		}
		String num = obj.toString().trim();
		if (num.length() == 0) {
			return false;
		}
		try {
			Integer.parseInt(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断是否是Long类型
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isLongType(Object obj) {
		if (obj == null) {
			return false;
		}
		String num = obj.toString().trim();
		if (num.length() == 0) {
			return false;
		}
		try {
			Long.parseLong(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断是否是Double类型
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isDoubleType(Object obj) {
		if (obj == null) {
			return false;
		}
		String num = obj.toString().trim();
		if (num.length() == 0) {
			return false;
		}
		try {
			Double.parseDouble(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断ip或ip掩码格式是否正确(ipv4或ipv6) true代表格式正确 false代表格式不正确
	 * 
	 * @param ip
	 * @param ipType
	 *            ipv4还是ipv6(4,6)
	 * @return
	 */
	public static boolean isIpOrIpMask(String ip, Integer ipType) {
		// boolean ipv4Bool = val_ipv4(ip);
		// boolean ipv6Bool = val_ipv6(ip);
		// if (ipv6Bool) {

		if (null != ip && !ip.equals("")) {
			if (null != ipType && ipType == 4) {
				boolean ipv4 = isIpv4(ip.trim());
				if (ipv4) {
					return true;
				}
			} else if (null != ipType && ipType == 6) {
				boolean ipv6 = isIpv6(ip.trim());
				if (ipv6) {
					return true;
				}

			} else {// ipType不等于4或6时不验证与ipType是否一致,仅验证是否是ip格式
				boolean ipv6 = isIpv6(ip.trim());
				boolean ipv4 = isIpv4(ip.trim());
				if (ipv6 || ipv4) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断端口或端口掩码格式是否正确(0-65535) true代表格式正确 false代表格式不正确
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isPortOrPortMask(String port) {
		try {
			if (null != port && !port.equals("")) {
				int parseInt = Integer.parseInt(port.trim());
				if (parseInt >= 0 && parseInt <= 65535) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isIpv4(String ipAddress) {
		try {
			String ipv4 = "^(0|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(0|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(0|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(0|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			return Pattern.compile(ipv4).matcher(ipAddress).matches();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isIpv6(String ipAddress) {
		try {

			String ipv6 = "^((::)|(([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))$";
			return Pattern.compile(ipv6).matcher(ipAddress).matches();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean val_ipv4(String host) {
		InetAddress addressIpv4 = null;

		try {
			addressIpv4 = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			return false;
		}
		if (addressIpv4 instanceof Inet6Address) {
			return false;
		}
		if (addressIpv4 instanceof Inet4Address) {
			return true;
		}
		return false;
	}

	public static boolean val_ipv6(String host) {
		InetAddress addressIpv6 = null;
		try {
			addressIpv6 = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		if (addressIpv6 instanceof Inet6Address) {
			return true;
		}
		if (addressIpv6 instanceof Inet4Address) {
			return false;
		}
		return false;
	}
}
