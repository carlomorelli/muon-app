package com.csoft.muon.handler;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;

import java.io.IOException;
import java.util.Map;

import com.csoft.muon.domain.Item;
import com.csoft.muon.events.HttpErrorEvent;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryException;

/**
 * Final class implementing the Get action
 * 
 * @author Carlo Morelli
 *
 */
public final class GetHandler extends AbstractHandler {

    public GetHandler(Repository repo) {
        super(repo);
    }

    @Override
    public Result process(String requestBody, Map<String, String> requestParams) {
        if (requestBody != null && !requestBody.isEmpty()) {
            return HttpErrorEvent.SC_400_FORBIDDEN_BODY.asResult();
        }
        int index = Integer.parseInt(requestParams.get(":index"));
        try {
            Item fetchedItem = repo.fetchItemAtIndex(index);
            String body = dumpJson(fetchedItem);
            return new Result(200, body);
        } catch (RepositoryException e) {
            return HttpErrorEvent.SC_404_NOT_FOUND.asResult();
        } catch (IOException e) {
            return HttpErrorEvent.SC_503_ERROR_CREATING_RETURN_BODY.asResult();
        }
    }

}
