package fr.minuskube.pastee.response;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {

    protected boolean success;
    protected List<ResponseError> errors;

    public Response(UnirestException e) {
        this.success = false;
        this.errors = Collections.singletonList(new ResponseError(0,
                "Internal Error: " + e.getMessage(), null));
    }

    public Response(JSONObject json) {
        this.success = json.getBoolean("success");
        this.errors = new ArrayList<>();

        if(!this.success) {
            for(Object obj : json.getJSONArray("errors")) {
                JSONObject error = (JSONObject) obj;

                this.errors.add(new ResponseError(
                        error.getInt("code"),
                        error.getString("message"),
                        error.optString("field")
                ));
            }
        }
    }

    public boolean isSuccess() { return success; }
    public List<ResponseError> getErrors() { return errors; }

    public String getErrorString() {
        StringBuilder sb = new StringBuilder();

        if(errors.size() == 1) {
            ResponseError error = errors.get(0);

            sb.append(error.getCode())
                    .append(" -> ")
                    .append(error.getMessage());
        }
        else {
            sb.append("[");

            for(int i = 0; i < errors.size(); i++) {
                ResponseError error = errors.get(i);

                if(i != 0)
                    sb.append(",\n");

                sb.append(error.getCode())
                        .append(" -> ")
                        .append(error.getMessage());
            }

            sb.append("]");
        }

        return sb.toString();
    }

}
