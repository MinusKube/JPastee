package fr.minuskube.pastee;

import fr.minuskube.pastee.data.Paste;
import fr.minuskube.pastee.data.Section;
import fr.minuskube.pastee.response.PasteResponse;
import fr.minuskube.pastee.response.SubmitResponse;
import fr.minuskube.pastee.response.SyntaxesResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class RequestTest {

    private final Logger logger = Logger.getLogger("JPastee-Test");
    private JPastee pastee;

    @Before
    public void init() {
        String apiKey = System.getenv("JPASTEE_KEY");
        assertNotNull(apiKey);

        this.pastee = new JPastee(apiKey);

        assertEquals(this.pastee.getApiKey(), apiKey);
        assertFalse(this.pastee.getSyntaxes().isEmpty());
    }

    @Test
    public void listSyntaxes() {
        SyntaxesResponse resp = this.pastee.listSyntaxes();

        if(!resp.isSuccess())
            this.logger.severe("Errors: \n" + resp.getErrorString());

        assertTrue(resp.isSuccess());
        assertTrue(resp.getErrors().isEmpty());

        assertFalse(resp.getSyntaxes().isEmpty());
    }

    @Test
    public void submitPaste() {
        Paste paste = Paste.builder()
                .encrypted(true)
                .description("Test for JPastee API")
                .addSection(Section.builder()
                        .name("JPastee Section Test")
                        .contents("This is a test for the JPastee API.")
                        .syntax(this.pastee.getSyntaxes().get(0))
                        .build())
                .build();

        assertTrue(paste.isEncrypted());
        assertEquals("Test for JPastee API", paste.getDescription());
        assertEquals(1, paste.getSections().size());

        Section section = paste.getSections().get(0);

        assertEquals("JPastee Section Test", section.getName());
        assertEquals("This is a test for the JPastee API.", section.getContents());
        assertEquals(this.pastee.getSyntaxes().get(0), section.getSyntax());

        SubmitResponse resp = this.pastee.submit(paste);

        if(!resp.isSuccess())
            this.logger.severe("Errors: \n" + resp.getErrorString());

        assertTrue(resp.isSuccess());
        assertTrue(resp.getErrors().isEmpty());

        assertNotNull(resp.getId());
        assertNotNull(resp.getLink());

        this.logger.info("Paste created: " + resp.getLink());

        this.getPaste(resp.getId());
    }

    public void getPaste(String createdPaste) {
        PasteResponse resp = this.pastee.getPaste(createdPaste);

        if(!resp.isSuccess())
            this.logger.severe("Errors: \n" + resp.getErrorString());

        assertTrue(resp.isSuccess());
        assertTrue(resp.getErrors().isEmpty());

        assertNotNull(resp.getPaste());

        Paste paste = resp.getPaste();

        assertEquals(createdPaste, paste.getId());

        assertTrue(paste.isEncrypted());
        assertEquals("Test for JPastee API", paste.getDescription());
        assertEquals(1, paste.getSections().size());

        Section section = paste.getSections().get(0);

        assertEquals("JPastee Section Test", section.getName());
        assertEquals("This is a test for the JPastee API.", section.getContents());
        assertEquals(this.pastee.getSyntaxes().get(0), section.getSyntax());

        assertNotNull(paste.getCreationDate());
    }

}
