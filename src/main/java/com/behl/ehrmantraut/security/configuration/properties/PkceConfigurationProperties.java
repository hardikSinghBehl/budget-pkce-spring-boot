package com.behl.ehrmantraut.security.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "com.behl.ehrmantraut")
@Data
public class PkceConfigurationProperties {

    private Security security = new Security();

    @Data
    public class Security {

        private String clientId;
        private String redirectUri;
        private String responseType;
        private String codeChallengeMethod;
        private String grantType;
        private Integer codeExpirationMinutes;
        private Refresh refresh = new Refresh();

        @Data
        public class Refresh {
            private String grantType;
        }

    }

}