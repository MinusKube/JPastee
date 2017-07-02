package fr.minuskube.pastee.data;

import org.json.JSONObject;

public class Syntax {

    private int id;
    private String shortName;
    private String name;

    public Syntax(JSONObject json) {
        this.id = json.getInt("id");
        this.shortName = json.getString("short");
        this.name = json.getString("name");
    }

    public int getId() { return id; }
    public String getShortName() { return shortName; }
    public String getName() { return name; }

}
