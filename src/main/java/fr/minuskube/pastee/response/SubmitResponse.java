package fr.minuskube.pastee.response;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class SubmitResponse extends Response {

    private String id;
    private String link;

    public SubmitResponse(UnirestException e) { super(e); }

    public SubmitResponse(JSONObject json) {
        super(json);

        if(this.success) {
            this.id = json.getString("id");
            this.link = json.getString("link");
        }
    }

    public String getId() { return id; }
    public String getLink() { return link; }

}
