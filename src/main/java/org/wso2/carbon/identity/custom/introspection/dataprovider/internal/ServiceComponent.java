package org.wso2.carbon.identity.custom.introspection.dataprovider.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.identity.custom.introspection.dataprovider.CustomIntrospectionDataProvider;
import org.wso2.carbon.identity.oauth2.IntrospectionDataProvider;

@Component(name = "org.wso2.carbon.identity.custom.introspection.dataprovider.internal.ServiceComponent",
        immediate = true)
public class ServiceComponent {

    private static final Log log = LogFactory.getLog(ServiceComponent.class);

    @Activate
    protected void activate(BundleContext bundleContext) {

        try {
            bundleContext.registerService(IntrospectionDataProvider.class.getName(),
                    new CustomIntrospectionDataProvider(), null);
            log.info("Custom Introspection Data Provider Bundle activated");
        } catch (Throwable e) {
            log.error("Error while activating ServiceComponent.", e);
        }
    }
}