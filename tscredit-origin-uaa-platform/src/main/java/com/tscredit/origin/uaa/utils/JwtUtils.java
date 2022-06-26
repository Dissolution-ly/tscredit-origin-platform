package com.tscredit.origin.uaa.utils;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;

import java.text.ParseException;
import java.util.Map;


public class JwtUtils {

    public static Map<String, Object> decodeJwt(String jwt)
    {
        JWSObject jwsObject;
        Map<String, Object>  jsonObject = null;
        try {
            jwsObject = JWSObject.parse(jwt);
            Payload payload = jwsObject.getPayload();
            jsonObject = payload.toJSONObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
