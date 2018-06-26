package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class EnvironmentBasedConfigHelper {
    public static KieBpmConfig fromEnvironment(){
        String kieServerUrl = resolve("KIE_SERVER_BASE_URL");
        String kieServerUsername = resolve("KIE_SERVER_USERNAME");
        String kieServerPassword = resolve("KIE_SERVER_PASSWORD");
        if(isNotEmpty(kieServerPassword) && isNotEmpty(kieServerUsername) && isNotEmpty(kieServerUrl)){
            try {
                URL url = new URL(kieServerUrl);
                KieBpmConfig result = new KieBpmConfig();
                result.setActive(true);
                result.setHostname(url.getHost());
                result.setPort(calculatePort(url));
                result.setSchema(url.getProtocol());
                result.setName("defaultBPM");
                result.setPassword(kieServerPassword);
                result.setUsername(kieServerUsername);
                result.setWebapp(url.getPath().startsWith("/")?url.getPath().substring(1):url.getPath());
                result.setTimeoutMsec(5000);
                result.setId("environment-based-kie-config-#0001");
                return result;
            } catch (MalformedURLException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    private static String resolve(String name) {
        return System.getProperty(name,System.getenv(name));
    }

    private static Integer calculatePort(URL url) {
        if(url.getPort()==-1){
            return url.getDefaultPort();
        }
        return url.getPort();
    }
    private static boolean isNotEmpty(String s){
        return s!=null && s.length()>0;
    }

}
