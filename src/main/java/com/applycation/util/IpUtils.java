/**
 *
 */
package com.applycation.util;


import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by gaorui on 16/12/26.
 */
public class IpUtils {
    private static Logger logger = Logger.getLogger(IpUtils.class);


    private static class IpUtilsHolder {
        private static final InetAddress localAddr = new IpUtils().init();
    }

    private IpUtils() {

    }

    public static InetAddress getLocalAddr() {
        return IpUtilsHolder.localAddr;
    }

    private InetAddress init() {
        try {
            return InetAddress.getLocalHost(); //直接获取IP地址，适应于Windows机器
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Enumeration<NetworkInterface> enumeration = null; //遍历所有的网络接口获取
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            logger.error(e.getMessage());
        }
        while (enumeration != null && enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            Enumeration<InetAddress> addr = networkInterface.getInetAddresses();
            while (addr.hasMoreElements()) {
                InetAddress localAddr = addr.nextElement();
                Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
                if (localAddr.getHostAddress() != null && IPV4_PATTERN.matcher(localAddr.getHostAddress()).matches()) {
                    return localAddr;
                }
            }
        }
        return null;
    }
}
