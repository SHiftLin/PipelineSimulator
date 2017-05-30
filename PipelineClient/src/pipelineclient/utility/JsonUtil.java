/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineclient.utility;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author lsh
 */
public class JsonUtil {

    public static String jsonMapToString(Map<String, Object> json) {
        String str = "{";
        Set set = json.entrySet();
        int n = set.size(), count = 0;
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            /*
            if (value instanceof Integer) {
                str += key + ":i_" + value;
            } else {
                str += key + ":s_" + value;
            }
             */
            str += key + ":" + value.toString();
            count++;
            if (count < n) {
                str += "#";
            }
        }
        str += "}";
        return str;
    }

    public static Map<String, String> jsonStringToMap(String str) {
        Map<String, String> json = new HashMap<>();
        json.clear();
        str = str.substring(1, str.length() - 1);
        String[] item = str.split("#");
        int n = item.length;
        for (int i = 0; i < n; i++) {
            int p = item[i].indexOf(':');
            String key = item[i].substring(0, p);
            String value = item[i].substring(p + 1);
            /*
            String key = item[i].substring(0, p);
            String value_str = item[i].substring(p + 3);
            Object value;
            if (item[i].charAt(p + 1) == 'i') {
                value = Integer.parseInt(value_str);
            } else {
                value = value_str;
            }
             */
            json.put(key, value);
        }
        return json;
    }

}
