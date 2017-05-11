package com.bjtu.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Created by gimling on 17-4-19.
 */
//ip类
public class IPHelper {

    public static String getRealIp(HttpServletRequest request){
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String getIpLocation(String ip) throws IOException {
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        ObjectMapper om = new ObjectMapper();
        Map map = om.readValue(new URL(urlStr+"?ip="+ip),Map.class);
        Map data = (Map) map.get("data");
        return data.get("country").toString()+data.get("region").toString()+data.get("city")+" "+data.get("isp");
    }

}
