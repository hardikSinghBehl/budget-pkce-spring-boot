package com.behl.ehrmantraut.utility;

import java.net.URI;

public class RedirectUriBuilder {

    public static URI build(final String redirectUri, final String code, final String state) {
        StringBuilder uri = new StringBuilder(redirectUri + "?code=" + code);
        if (state != null)
            uri.append("&state=" + state);
        return URI.create(uri.toString());
    }

}
