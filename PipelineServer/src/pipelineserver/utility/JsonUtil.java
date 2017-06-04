/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pipelineserver.utility;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import static pipelineserver.stage.Var.*;
import static pipelineserver.utility.Convert.*;
/**
 *
 * @author lsh
 */
public class JsonUtil {

    public static String HALT() {
        return "{status:HALT}";
    }
    
    public static String Process() {
        Map<String, Object> json = new HashMap<>();
        json.clear();
        
        json.put("status","OK");
        json.put("Cycle",Cycle);
        json.put("%eax",Hex2String((String) Integer.toHexString(register[0])));
        json.put("%ecx",Hex2String((String) Integer.toHexString(register[1])));
        json.put("%edx",Hex2String((String) Integer.toHexString(register[2])));
        json.put("%ebx",Hex2String((String) Integer.toHexString(register[3])));
        json.put("%esp",Hex2String((String) Integer.toHexString(register[4])));
        json.put("%ebp",Hex2String((String) Integer.toHexString(register[5])));
        json.put("%esi",Hex2String((String) Integer.toHexString(register[6])));
        json.put("%edi",Hex2String((String) Integer.toHexString(register[7])));
        
        json.put("F_predPC", F_predPC);

        json.put("D_code", D_code);
        json.put("D_stat",D_stat);
        json.put("D_icode",D_icode.ordinal());
        json.put("D_ifun", D_ifun);
        json.put("D_rA", D_rA);
        json.put("D_rB", D_rB);
        json.put("D_valC", D_valC);
        json.put("D_valP", D_valP);

        json.put("E_code", E_code);
        json.put("E_stat",E_stat);
        json.put("E_icode",E_icode.ordinal());
        json.put("E_ifun", E_ifun);
        json.put("E_valC", E_valC);
        json.put("E_valA", E_valA);
        json.put("E_valB", E_valB);
        json.put("E_dstE", E_dstE);
        json.put("E_dstM", E_dstM);
        json.put("E_srcA", E_srcA);
        json.put("E_srcB", E_srcB);

        json.put("M_code", M_code);
        json.put("M_stat",M_stat);
        json.put("M_icode",M_icode.ordinal());
        json.put("M_Cnd", M_Cnd);
        json.put("zf", zf);
        json.put("sf", sf);
        json.put("of", of);
        json.put("M_valE", M_valE);
        json.put("M_valA", M_valA);
        json.put("M_dstE", M_dstE);
        json.put("M_dstM", M_dstM);

        json.put("W_code", W_code);
        json.put("W_stat",W_stat);
        json.put("W_icode",W_icode.ordinal());
        json.put("W_valE", W_valE);
        json.put("W_valM", W_valM);
        json.put("W_dstE", W_dstE);
        json.put("W_dstM", W_dstM);

        return jsonMapToString(json);
    }

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
