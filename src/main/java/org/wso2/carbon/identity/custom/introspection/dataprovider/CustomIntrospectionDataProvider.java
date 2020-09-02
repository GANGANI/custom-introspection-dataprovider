package org.wso2.carbon.identity.custom.introspection.dataprovider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.core.handler.AbstractIdentityHandler;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.common.exception.InvalidOAuthClientException;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.IntrospectionDataProvider;
import org.wso2.carbon.identity.oauth2.dto.OAuth2IntrospectionResponseDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;
import org.wso2.carbon.identity.oauth2.model.AccessTokenDO;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.HashMap;
import java.util.Map;

public class CustomIntrospectionDataProvider extends AbstractIdentityHandler implements IntrospectionDataProvider {

    private static final String SUB = "sub";
    private static final String ISS = "iss";
    private static final String DUMMY_VALUE = "dummy_value";

    private static final Log log = LogFactory.getLog(CustomIntrospectionDataProvider.class);

    @Override
    public Map<String, Object> getIntrospectionData(OAuth2TokenValidationRequestDTO oAuth2TokenValidationRequestDTO,
                                                    OAuth2IntrospectionResponseDTO oAuth2IntrospectionResponseDTO)
            throws IdentityOAuth2Exception {

        Map<String, Object> introspectionData = new HashMap<>();
        AccessTokenDO accessTokenDO;
        AbstractUserStoreManager userStoreManager;

        if (isEnabled()) {

            try {
                userStoreManager = (AbstractUserStoreManager) CarbonContext.
                        getThreadLocalCarbonContext().getUserRealm().getUserStoreManager();

                accessTokenDO = OAuth2Util.findAccessToken(oAuth2TokenValidationRequestDTO.
                        getAccessToken().getIdentifier(), false);

                if (OAuthConstants.UserType.APPLICATION.equals(accessTokenDO.getTokenType())) {
                    introspectionData.put(SUB, oAuth2IntrospectionResponseDTO.getClientId());
                } else {
                    introspectionData.put(SUB, userStoreManager.getUserIDFromUserName(MultitenantUtils.
                            getTenantAwareUsername(oAuth2IntrospectionResponseDTO.getUsername())));
                }

                String tenantDomain = OAuth2Util.getTenantDomainOfOauthApp(oAuth2IntrospectionResponseDTO.getClientId());
                introspectionData.put(ISS, OAuth2Util.getIdTokenIssuer(tenantDomain));

                // Dummy Value
                introspectionData.put(DUMMY_VALUE, "xxx-xxx-xxx");

            } catch (UserStoreException | InvalidOAuthClientException e) {
                log.error("Error retrieving the user store manager");
            }
        }

        return introspectionData;
    }
}
