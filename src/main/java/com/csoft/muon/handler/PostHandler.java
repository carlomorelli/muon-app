package com.csoft.muon.handler;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;
import static com.csoft.muon.utils.JsonFunctions.parseJson;

import java.io.IOException;
import java.util.Map;

import com.csoft.muon.domain.Item;
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
            return new Result(403, "ClientError: malformed / unparsable input body");
        }
        if (!item.isValid()) {
            return new Result(403, "ClientError: required fields not available in body");
        }
        try {
            repo.insertItem(item);
            return new Result(200, dumpJson(item));
        } catch (RepositoryException e) {
            return new Result(403, "ClientError: invalid / null index or already used index in input item");
        } catch (IOException e) {
            return new Result(503, "ServerError: unable to process correctly reflected item from database");
        }
    }

}
