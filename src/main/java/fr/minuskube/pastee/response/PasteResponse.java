package fr.minuskube.pastee.response;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.minuskube.pastee.JPastee;
import fr.minuskube.pastee.data.Paste;
import org.json.JSONObject;

public class PasteResponse extends Response {

    private Paste paste;

    public PasteResponse(UnirestException e) { super(e); }

    public PasteResponse(JPastee pastee, JSONObject json) {
        super(json);

        if(this.success)
            this.paste = new Paste(pastee, json.getJSONObject("paste"));
    }

    public Paste getPaste() { return paste; }

}
