package fr.minuskube.pastee;

import fr.minuskube.pastee.data.Paste;
import fr.minuskube.pastee.data.Section;
import fr.minuskube.pastee.response.PasteResponse;
import fr.minuskube.pastee.response.SubmitResponse;
import fr.minuskube.pastee.response.SyntaxesResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
            logger.severe("Errors: \n" + resp.getErrorString());

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
        assertEquals(paste.getDescription(), "Test for JPastee API");
        assertTrue(paste.getSections().size() == 1);

        Section section = paste.getSections().get(0);

        assertEquals(section.getName(), "JPastee Section Test");
        assertEquals(section.getContents(), "This is a test for the JPastee API.");
        assertEquals(section.getSyntax(), this.pastee.getSyntaxes().get(0));

        SubmitResponse resp = this.pastee.submit(paste);

        if(!resp.isSuccess())
            logger.severe("Errors: \n" + resp.getErrorString());

        assertTrue(resp.isSuccess());
        assertTrue(resp.getErrors().isEmpty());

        assertNotNull(resp.getId());
        assertNotNull(resp.getLink());

        logger.info("Paste created: " + resp.getLink());

        getPaste(resp.getId());
    }

    public void getPaste(String createdPaste) {
        PasteResponse resp = pastee.getPaste(createdPaste);

        if(!resp.isSuccess())
            logger.severe("Errors: \n" + resp.getErrorString());

        assertTrue(resp.isSuccess());
        assertTrue(resp.getErrors().isEmpty());

        assertNotNull(resp.getPaste());

        Paste paste = resp.getPaste();

        assertEquals(paste.getId(), createdPaste);

        assertTrue(paste.isEncrypted());
        assertEquals(paste.getDescription(), "Test for JPastee API");
        assertTrue(paste.getSections().size() == 1);

        Section section = paste.getSections().get(0);

        assertEquals(section.getName(), "JPastee Section Test");
        assertEquals(section.getContents(), "This is a test for the JPastee API.");
        assertEquals(section.getSyntax(), this.pastee.getSyntaxes().get(0));

        assertNotNull(paste.getCreationDate());
    }

}
