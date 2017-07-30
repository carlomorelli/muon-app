package com.csoft.muon.handler;

import java.io.IOException;
import java.util.Map;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryException;

/**
 * Final class implementing the Get action
 * The process() method does not require a valid Item in input, but a valid params map to retrieve the index
 * @author Carlo Morelli
 *
 */
public final class GetHandler extends AbstractHandler {

    public GetHandler(Repository repo) {
        super(repo);
    }

    @Override
    public Result process(Item item, Map<String, String> params) {
    	int index = Integer.parseInt(params.get(":index"));
        try {
            Item fetchedItem = repo.fetchItemAtIndex(index);
            String body = dumpJson(fetchedItem);
            return new Result(200, body);
        } catch (RepositoryException e) {
            return new Result(404, "ClientError: requested index [" + index + "] not found");
        } catch (IOException e) {
            return new Result(503, "ServerError: unable to provide correctly item");
        }
    }

}
