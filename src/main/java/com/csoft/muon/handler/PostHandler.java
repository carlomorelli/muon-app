package com.csoft.muon.handler;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;
import static com.csoft.muon.utils.JsonFunctions.parseJson;

import java.io.IOException;
import java.util.Map;

import com.csoft.muon.domain.Item;
import com.csoft.muon.events.HttpErrorEvent;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryException;

/**
 * Final class implementing the Post action
 * The process() method requires a valid Item in input
 * @author Carlo Morelli
 *
 */
public final class PostHandler extends AbstractHandler {

    public PostHandler(Repository repo) {
        super(repo);
    }

    @Override
    public Result process(String requestBody, Map<String, String> requestParams) {
        Item item;
       	try {
    		item = parseJson(requestBody, Item.class);
    	} catch (IOException e) {
            return HttpErrorEvent.SC_403_MALFORMED_BODY.asResult();
        }
        if (!item.isValid()) {
            return HttpErrorEvent.SC_403_MISSING_FIELDS_IN_BODY.asResult();
        }
        try {
            repo.insertItem(item);
            return new Result(200, dumpJson(item));
        } catch (RepositoryException e) {
            return HttpErrorEvent.SC_403_INVALID_INDEX_IN_BODY.asResult();
        } catch (IOException e) {
            return HttpErrorEvent.SC_503_ERROR_CREATING_RETURN_BODY.asResult();
        }
    }

}
