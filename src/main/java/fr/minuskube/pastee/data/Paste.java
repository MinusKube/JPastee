package fr.minuskube.pastee.data;

import fr.minuskube.pastee.JPastee;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Paste {

    private String id;
    private boolean encrypted;
    private String description;
    private int views;
    private Date creationDate;
    private Date expireDate;
    private List<Section> sections;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Paste(JPastee pastee, JSONObject json) {
        this.id = json.getString("id");
        this.encrypted = json.optBoolean("encrypted");
        this.description = json.optString("description");
        this.views = json.optInt("views");

        try {
            this.creationDate = DATE_FORMAT.parse(json.optString("created_at"));
        } catch(ParseException e) {
            this.creationDate = null;
        }

        try {
            this.expireDate = DATE_FORMAT.parse(json.optString("expires_at"));
        } catch(ParseException e) {
            this.expireDate = null;
        }

        JSONArray sectionsJson = json.optJSONArray("sections");

        this.sections = new ArrayList<>();

        if(sectionsJson != null) {
            for(Object obj : sectionsJson)
                this.sections.add(new Section(pastee, (JSONObject) obj));
        }
    }

    private Paste(Builder builder) {
        encrypted = builder.encrypted;
        description = builder.description;
        sections = builder.sections;
    }

    public String getId() { return id; }
    public boolean isEncrypted() { return encrypted; }
    public String getDescription() { return description; }
    public int getViews() { return views; }
    public Date getCreationDate() { return creationDate; }
    public Date getExpireDate() { return expireDate; }
    public List<Section> getSections() { return sections; }

    public static Builder builder() { return new Builder(); }


    public static final class Builder {

        private boolean encrypted;
        private String description;
        private List<Section> sections = new ArrayList<>();

        private Builder() {}

        public Builder encrypted(boolean encrypted) {
            this.encrypted = encrypted;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder setSections(List<Section> sections) {
            this.sections = sections;
            return this;
        }

        public Builder addSection(Section section) {
            this.sections.add(section);
            return this;
        }

        public Paste build() { return new Paste(this); }

    }
}
