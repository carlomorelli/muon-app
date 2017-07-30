package com.csoft.muon.handler;

import java.io.IOException;
import java.util.Map;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryException;

/**
 * Final class implementing the Post action
 * The process() method requires a valid Item in input
 * @author Carlo
 *
 */
public final class PostHandler extends AbstractHandler {

    public PostHandler(Repository repo) {
        super(repo);
    }

    @Override
    public Result process(Item item, Map<String, String> params) {
    	if (!item.isValid()) {
            return new Result(403, "ClientError: required fields not available in body");
        }
        try {
            repo.insertItem(item);
            return new Result(200, dumpJson(item));
        } catch (RepositoryException e) {
            return new Result(403, "ClientError: invalid / null index or already used index in input item");
        } catch (IOException e) {
            return new Result(503, "ServerError: unable to process correctly input item");
        }
    }

}
