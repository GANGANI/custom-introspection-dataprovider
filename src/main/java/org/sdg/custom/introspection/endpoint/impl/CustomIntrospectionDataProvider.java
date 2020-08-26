package org.sdg.custom.introspection.endpoint.impl;

import org.wso2.carbon.identity.core.handler.AbstractIdentityHandler;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.IntrospectionDataProvider;
import org.wso2.carbon.identity.oauth2.dto.OAuth2IntrospectionResponseDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;
import org.wso2.carbon.identity.oauth2.model.AccessTokenDO;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

import java.util.HashMap;
import java.util.Map;

public class CustomIntrospectionDataProvider extends AbstractIdentityHandler implements IntrospectionDataProvider {

    private static final String DUMMY_VALUE = "dummy_value";
    private static final String SUB = "sub";
    private static final String ISS = "iss";
    private static final String APPLICATION = "APPLICATION";

    @Override
    public Map<String, Object> getIntrospectionData(OAuth2TokenValidationRequestDTO oAuth2TokenValidationRequestDTO,
                                                    OAuth2IntrospectionResponseDTO oAuth2IntrospectionResponseDTO)
            throws IdentityOAuth2Exception {

        Map<String, Object> introspectionData = new HashMap<>();
        AccessTokenDO accessTokenDO;

        if (isEnabled()) {

            accessTokenDO = OAuth2Util.findAccessToken(oAuth2TokenValidationRequestDTO.getAccessToken().getIdentifier(),
                    false);

            if (accessTokenDO.getTokenType().equals(APPLICATION)) {
                introspectionData.put(ISS, oAuth2IntrospectionResponseDTO.getClientId());
            } else {
                introspectionData.put(ISS, oAuth2IntrospectionResponseDTO.getUsername());
            }

            introspectionData.put(SUB, oAuth2IntrospectionResponseDTO.getUsername());
            // DummyValue
            introspectionData.put(DUMMY_VALUE, "xxx-xxx-xxx");
        }

        return introspectionData;
    }
}
