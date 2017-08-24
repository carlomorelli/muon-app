package com.csoft.muon.handler;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;

import java.io.IOException;
import java.util.Properties;

import com.csoft.muon.domain.Version;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Final class for Version page
 * 
 * @author Carlo Morelli
 *
 */
public final class VersionHandler implements Route {

    @Override
    public Object handle(Request request, Response response) {
        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            String projectName = props.getProperty("project.name");
            String projectVersion = props.getProperty("project.version");
            String versionPage = dumpJson(new Version(projectName, projectVersion));
            response.status(200);
            response.type("application/json");
            response.body(versionPage);
            return versionPage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
