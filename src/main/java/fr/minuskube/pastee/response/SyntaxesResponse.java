package fr.minuskube.pastee.response;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.minuskube.pastee.data.Paste;
import fr.minuskube.pastee.data.Syntax;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SyntaxesResponse extends Response {

    private List<Syntax> syntaxes;

    public SyntaxesResponse(UnirestException e) { super(e); }

    public SyntaxesResponse(JSONObject json) {
        super(json);

        this.syntaxes = new ArrayList<>();

        if(this.success) {
            JSONArray syntaxesJson = json.getJSONArray("syntaxes");

            for(Object obj : syntaxesJson)
                this.syntaxes.add(new Syntax((JSONObject) obj));
        }
    }

    public List<Syntax> getSyntaxes() { return syntaxes; }

}
