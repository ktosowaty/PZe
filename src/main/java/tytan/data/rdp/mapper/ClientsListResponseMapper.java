package tytan.data.rdp.mapper;

import org.json.JSONArray;
import org.json.JSONObject;
import tytan.data.rdp.entity.Client;

import java.util.ArrayList;

public class ClientsListResponseMapper {

    public ArrayList<Client> mapResponseToClientsList(JSONObject response) {
        ArrayList<Client> clientsList = new ArrayList<>();
        JSONArray clientsListArray = response.getJSONArray("clientsList");
        for(Object item: clientsListArray){
            JSONObject client = (JSONObject) item;
            clientsList.add(new Client(
                    client.getString("clientName"),
                    client.getInt("id"),
                    client.getLong("connectedFrom")
            ));
        }
        return clientsList;
    }

}
