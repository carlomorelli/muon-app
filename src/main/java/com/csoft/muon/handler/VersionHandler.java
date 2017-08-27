package com.csoft.muon.handler;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.csoft.muon.domain.Version;
import com.csoft.muon.events.HttpErrorEvent;

/**
 * Final class for Version page
 * 
 * @author Carlo Morelli
 *
 */
public final class VersionHandler extends AbstractHandler {

    public VersionHandler() {
        super(null);
    }

    @Override
    public Result process(String requestBody, Map<String, String> requestParams) {
        if (requestBody != null && !requestBody.isEmpty()) {
            return HttpErrorEvent.SC_400_FORBIDDEN_BODY.asResult();
        }
        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            String projectName = props.getProperty("project.name");
            String projectVersion = props.getProperty("project.version");
            String versionPage = dumpJson(new Version(projectName, projectVersion));
            return new Result(200, versionPage);
        } catch (IOException e) {
            return HttpErrorEvent.SC_503_ERROR_CREATING_RETURN_BODY.asResult();
        }
    }


}
