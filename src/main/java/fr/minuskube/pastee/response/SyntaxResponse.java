package fr.minuskube.pastee.response;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.minuskube.pastee.data.Syntax;
import org.json.JSONObject;

public class SyntaxResponse extends Response {

    private Syntax syntax;

    public SyntaxResponse(UnirestException e) { super(e); }

    public SyntaxResponse(JSONObject json) {
        super(json);

        if(this.success)
            this.syntax = new Syntax(json.getJSONObject("syntax"));
    }

    public Syntax getSyntax() { return syntax; }

}
