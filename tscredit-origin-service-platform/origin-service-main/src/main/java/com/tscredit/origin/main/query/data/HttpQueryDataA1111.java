package com.tscredit.origin.main.query.data;


import com.tscredit.tsinterfaces.access.HttpQuery;
import com.tscredit.tsinterfaces.access.QueryData;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 本地企业
 */
@Service("A1111")
public class HttpQueryDataA1111 implements QueryData {

    private final HttpQuery httpQuery;

    public HttpQueryDataA1111(HttpQuery httpQuery) {
        this.httpQuery = httpQuery;
    }

    @Override
    public Map<String, Object> query(Map<String, ?> parameters) {
        return httpQuery.httpQuery("entList", parameters,"ts");
    }

}
