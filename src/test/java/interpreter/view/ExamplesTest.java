package interpreter.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExamplesTest {
    @Test
    void providesFiveExamplePrograms() {
        assertNotNull(Examples.EX1);
        assertNotNull(Examples.EX2);
        assertNotNull(Examples.EX3);
        assertNotNull(Examples.EX4);
        assertNotNull(Examples.EX5);
    }

    @Test
    void fileExampleUsesTheFileStatementsAndTheGivenName() {
        String text = Examples.fileExample("data.in").toString();

        assertTrue(text.contains("openRFile"));
        assertTrue(text.contains("readFile"));
        assertTrue(text.contains("closeRFile"));
        assertTrue(text.contains("\"data.in\""));
    }

    @Test
    void relationalExampleShowsTheComparisonInInfixForm() {
        assertTrue(Examples.EX5.toString().contains("(a < b)"));
    }
}
