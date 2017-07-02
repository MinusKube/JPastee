package fr.minuskube.pastee.data;

import fr.minuskube.pastee.JPastee;
import org.json.JSONObject;

public class Section {

    private int id;
    private Syntax syntax;
    private String name;
    private String contents;

    public Section(JPastee pastee, JSONObject json) {
        this.id = json.getInt("id");
        this.syntax = pastee.getSyntaxFromName(json.optString("syntax")).orElse(null);
        this.name = json.optString("name");
        this.contents = json.getString("contents");
    }

    private Section(Builder builder) {
        this.syntax = builder.syntax;
        this.name = builder.name;
        this.contents = builder.contents;
    }

    public int getId() { return id; }
    public Syntax getSyntax() { return syntax; }
    public String getName() { return name; }
    public String getContents() { return contents; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {

        private Syntax syntax;
        private String name;
        private String contents;

        private Builder() {}

        public Builder syntax(Syntax syntax) {
            this.syntax = syntax;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Section build() { return new Section(this); }

    }

}
