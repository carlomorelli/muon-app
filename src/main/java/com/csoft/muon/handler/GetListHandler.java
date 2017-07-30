package com.csoft.muon.handler;

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
    public Result process(Item item, Map<String, String> params) {
        try {
            List<Item> fetchedItems = repo.fetchAllItems();
            ItemsDto dto = new ItemsDto(fetchedItems.size(), fetchedItems);
            String body = dumpJson(dto);
            return new Result(200, body);
        } catch (IOException e) {
            return new Result(503, "ServerError: unable to provide correctly item");
        }
    }

}
