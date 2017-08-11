package com.csoft.muon.handler;

import static com.csoft.muon.utils.JsonFunctions.dumpJson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.csoft.muon.domain.Item;
import com.csoft.muon.domain.ItemsDto;
import com.csoft.muon.repository.Repository;

/**
 * Final class implementing the GetList action
 * The process() method does not require either a valid Item in input, nor a valid params map
 * @author Carlo Morelli
 *
 */
public final class GetListHandler extends AbstractHandler {

    public GetListHandler(Repository repo) {
        super(repo);
    }

    @Override
    public Result process(String requestBody, Map<String, String> requestParams) {
        if (requestBody != null && !requestBody.isEmpty()) {
        	return new Result(400, "Forbidded to send body");
        }
    	try {
            List<Item> fetchedItems = repo.fetchAllItems();
            ItemsDto dto = new ItemsDto(fetchedItems.size(), fetchedItems);
            String body = dumpJson(dto);
            return new Result(200, body);
        } catch (IOException e) {
            return new Result(503, "ServerError: unable to process correctly reflected item from database");
        }
    }

}
