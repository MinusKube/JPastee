package fr.minuskube.pastee;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.minuskube.pastee.data.Paste;
import fr.minuskube.pastee.data.Syntax;
import fr.minuskube.pastee.response.PasteResponse;
import fr.minuskube.pastee.response.Response;
import fr.minuskube.pastee.response.SubmitResponse;
import fr.minuskube.pastee.response.SyntaxResponse;
import fr.minuskube.pastee.response.SyntaxesResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class JPastee {

    private static final String BASE_URL = "https://api.paste.ee/v1";
    private static final Logger LOGGER = Logger.getLogger("JPastee");

    private List<Syntax> syntaxes = new ArrayList<>();
    private String apiKey;

    /**
     * Instanciate JPastee using an API Key.
     *
     * @see <a href="https://pastee.github.io/docs/">Pastee Docs</a>
     * @param apiKey the api key
     */
    public JPastee(String apiKey) {
        this.apiKey = apiKey;

        SyntaxesResponse resp = listSyntaxes();

        if(!resp.isSuccess())
            LOGGER.severe("Unable to retrieve syntaxes: \n"
                    + resp.getErrorString());

        this.syntaxes = resp.getSyntaxes();
    }

    /**
     * Submit a paste to the server.
     *
     * @see <a href="https://pastee.github.io/docs/#submit-a-new-paste">Pastee Docs: Submit a new paste</a>
     * @param paste the paste to submit
     *
     * @return the response of the submit request
     */
    public SubmitResponse submit(Paste paste) {
        final String route = "/pastes";

        JSONObject json = new JSONObject();

        json.put("encrypted", paste.isEncrypted());
        json.putOpt("description", paste.getDescription());

        JSONArray sectionsJson = new JSONArray();
        paste.getSections().forEach(section -> {
            JSONObject sectionJson = new JSONObject();

            sectionJson.putOpt("name", section.getName());

            if(section.getSyntax() != null)
                sectionJson.putOpt("syntax", section.getSyntax().getShortName());

            sectionJson.put("contents", section.getContents());

            sectionsJson.put(sectionJson);
        });

        json.put("sections", sectionsJson);

        try {
            return new SubmitResponse(post(route, json).getBody().getObject());
        } catch(UnirestException e) {
            return new SubmitResponse(e);
        }
    }

    /**
     * Request a paste from the server.
     *
     * @see <a href="https://pastee.github.io/docs/#get-a-paste">Pastee Docs: Get a paste</a>
     * @param id the id of the paste to retrieve
     *
     * @return the response of the request
     */
    public PasteResponse getPaste(String id) {
        final String route = "/pastes/" + id;

        try {
            return new PasteResponse(this, get(route).getBody().getObject());
        } catch(UnirestException e) {
            return new PasteResponse(e);
        }
    }

    /**
     * Delete a paste of the server.
     *
     * @see <a href="https://pastee.github.io/docs/#remove-a-paste">Pastee Docs: Remove a paste</a>
     * @param id the id of the paste to delete
     *
     * @return the response of the delete request
     */
    public Response deletePaste(String id) {
        final String route = "/pastes/" + id;

        try {
            return new Response(get(route).getBody().getObject());
        } catch(UnirestException e) {
            return new Response(e);
        }
    }

    /**
     * Lists the available syntaxes from the server.
     *
     * @see <a href="https://pastee.github.io/docs/#list-syntaxes">Pastee Docs: List Syntaxes</a>
     * @return the response of the request
     */
    public SyntaxesResponse listSyntaxes() {
        final String route = "/syntaxes";

        try {
            return new SyntaxesResponse(get(route).getBody().getObject());
        } catch(UnirestException e) {
            return new SyntaxesResponse(e);
        }
    }

    /**
     * Get a syntax from the server.
     *
     * @see <a href="https://pastee.github.io/docs/#get-syntax">Pastee Docs: Get syntax</a>
     * @return the response of the request
     */
    public SyntaxResponse getSyntax(int id) {
        final String route = "/syntaxes/" + id;

        try {
            return new SyntaxResponse(get(route).getBody().getObject());
        } catch(UnirestException e) {
            return new SyntaxResponse(e);
        }
    }


    private HttpResponse<JsonNode> get(String route) throws UnirestException {
        return Unirest.get(BASE_URL + route)
                .header("X-Auth-Token", apiKey)
                .asJson();
    }

    private HttpResponse<JsonNode> post(String route, JSONObject json) throws UnirestException {
        return Unirest.post(BASE_URL + route)
                .header("X-Auth-Token", apiKey)
                .header("Content-Type", "application/json")
                .body(json)
                .asJson();
    }

    private HttpResponse<JsonNode> delete(String route) throws UnirestException {
        return Unirest.delete(BASE_URL + route)
                .header("X-Auth-Token", apiKey)
                .asJson();
    }


    /**
     * Get a syntax from its name or its short name.
     *
     * @param name the name or short name of the syntax
     * @return an optional containing the syntax, or null if no syntax was found
     */
    public Optional<Syntax> getSyntaxFromName(String name) {
        return syntaxes.stream()
                .filter(syntax -> syntax.getName().equalsIgnoreCase(name)
                        || syntax.getShortName().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Get a syntax from its id.
     *
     * @param id the id of the syntax
     * @return an optional containing the syntax, or null if no syntax was found
     */
    public Optional<Syntax> getSyntaxFromId(int id) {
        return syntaxes.stream()
                .filter(syntax -> syntax.getId() == id)
                .findAny();
    }

    public List<Syntax> getSyntaxes() { return syntaxes; }
    public String getApiKey() { return apiKey; }

}
