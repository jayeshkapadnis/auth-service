package com.hashmapinc.haf.jwt.extractors;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    public String extract(HttpServletRequest request);
}
