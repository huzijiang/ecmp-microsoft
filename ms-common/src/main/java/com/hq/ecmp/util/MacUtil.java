package com.hq.ecmp.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * SELECT INET_ATON('127.0.0.1'), INET_ATON('127.1'); ip to int
 * SELECT INET_NTOA(3520061480); int to ip
 *
 * @author sxf
 *         2011-1-6
 */
public class MacUtil {
    /**
     * 取客户端的真实ip，考虑了反向代理等因素的干扰
     */
    public static String getIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        String ip = resolveClientIPFromXFF(xff);
        if (isValidIP(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIP(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIP(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 从X-Forwarded-For头部中获取客户端的真实IP。
     * X-Forwarded-For并不是RFC定义的标准HTTP请求Header，可以参考http://en.wikipedia.org/wiki/X-Forwarded-For
     *
     * @param xff X-Forwarded-For头部的值
     * @return 如果能够解析到client IP，则返回表示该IP的字符串，否则返回null
     */
    private static String resolveClientIPFromXFF(String xff) {
        if (xff == null || xff.length() == 0) {
            return null;
        }
        String[] ss = xff.split(",");
        for (String ip : ss) {
            ip = ip.trim();
            if (isValidIP(ip))
                return ip;
        }
        return null;
    }

    // long ip to string
    public static String iplongToIp(long ipaddress) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipaddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        return sb.toString();
    }

    //string ip to long
    public static long ipStrToLong(String ipaddress) {
        long[] ip = new long[4];
        int position1 = ipaddress.indexOf(".");
        int position2 = ipaddress.indexOf(".", position1 + 1);
        int position3 = ipaddress.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(ipaddress.substring(0, position1));
        ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 检查是否是一个合格的ipv4 ip
     *
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            return false;
        return ipPattern.matcher(ip).matches();
    }

    /**
     * 取得一个合格的ipv4 ip,外网ip
     *
     * @return
     */
    public static String getLocalIP() {
        for (String a : getAllNoLoopbackAddresses()) {
            if (isValidIP(a)) {
                return a;
            }
        }
        return "";
    }

    /**
     * 获取本地地址
     * 内网ip
     *
     * @return 本机ipv4地址
     */
    public static final String getLocalAddress() {
        try {
            for (Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces(); ni.hasMoreElements(); ) {
                NetworkInterface eth = ni.nextElement();
                for (Enumeration<InetAddress> add = eth.getInetAddresses(); add.hasMoreElements(); ) {
                    InetAddress i = add.nextElement();
                    if (i instanceof Inet4Address) {
                        if (i.isSiteLocalAddress()) {
                            return i.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得本地所有的ipv4列表
     *
     * @return
     */
    public static List<String> getLocalIPs() {
        List<String> localIps = new ArrayList<String>();
        for (String a : getAllNoLoopbackAddresses()) {
            if (isValidIP(a)) {
                localIps.add(a);
            }
        }
        return localIps;
    }

    private static final Pattern ipPattern = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");

    private static Collection<InetAddress> getAllHostAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Collection<InetAddress> addresses = new ArrayList<InetAddress>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    addresses.add(inetAddress);
                }
            }

            return addresses;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Collection<String> getAllNoLoopbackAddresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostAddress();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }

    /**
     * 通过HttpServletRequest返回IP地址
     * @param request HttpServletRequest
     * @return ip String
     * @throws Exception
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }



    /**
     * 通过IP地址获取MAC地址
     * @param ip String,127.0.0.1格式
     * @return mac String
     * @throws Exception
     */
    public static String getMACAddress(String ip) throws Exception {
        String line = "";
        String macAddress = "";
        final String MAC_ADDRESS_PREFIX = "MAC Address = ";
        final String LOOPBACK_ADDRESS = "127.0.0.1";
        //如果为127.0.0.1,则获取本地MAC地址。
        if (LOOPBACK_ADDRESS.equals(ip)) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            //貌似此方法需要JDK1.6。
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            //下面代码是把mac地址拼装成String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            //把字符串所有小写字母改为大写成为正规的mac地址并返回
            macAddress = sb.toString().trim().toUpperCase();
            return macAddress;
        }
        //获取非本地IP的MAC地址
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line != null) {
                    int index = line.indexOf(MAC_ADDRESS_PREFIX);
                    if (index != -1) {
                        macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddress;
    }
    }
