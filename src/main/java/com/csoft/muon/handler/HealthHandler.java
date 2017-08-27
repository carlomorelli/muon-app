package com.csoft.muon.handler;


import static com.csoft.muon.utils.JsonFunctions.dumpJson;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.csoft.muon.domain.Health;
import com.csoft.muon.domain.HealthsDto;
import com.csoft.muon.events.HttpErrorEvent;
import com.csoft.muon.repository.Repository;

public final class HealthHandler extends AbstractHandler {

    public HealthHandler(Repository repo) {
        super(repo);
    }

    @Override
    public Result process(String requestBody, Map<String, String> requestParams) {
        if (requestBody != null && !requestBody.isEmpty()) {
            return HttpErrorEvent.SC_400_FORBIDDEN_BODY.asResult();
        }
        try {
            String status = repo.isHealthy() ? "active" : "unreachable";
            String body = dumpJson(new HealthsDto(
                    Collections.singletonList(
                            new Health("database", status)
                    )
            ));
            return new Result(200, body);
        } catch (IOException e) {
            return HttpErrorEvent.SC_503_ERROR_CREATING_RETURN_BODY.asResult();
        }
    }

}
