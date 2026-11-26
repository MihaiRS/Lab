package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileStmtTest {
    @TempDir
    Path dir; // a new empty folder for every test

    private PrgState state;
    private ValueExp file;       // the expression "<dir>/numbers.in"
    private StringValue fileName;

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n");
        fileName = new StringValue(f.toString());
        file = new ValueExp(fileName);
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    // Windows cannot delete a temp file that is still open, so close every reader the test left open.
    @AfterEach
    void closeOpenFiles() throws Exception {
        for (BufferedReader reader : state.getFileTable().toMap().values()) {
            reader.close();
        }
    }

    // ---- openRFile (S8)
    @Test
    void openAddsTheFileToTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertTrue(state.getFileTable().isDefined(fileName));
    }

    @Test
    void openWithNonStringExpressionThrows() {
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(new IntValue(1))).execute(state));
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class, () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void openingAMissingFileThrows() {
        ValueExp missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));

        assertThrows(StatementException.class, () -> new OpenRFileStmt(missing).execute(state));
    }

    // ---- readFile (S9)
    @Test
    void readGivesTheNextNumberThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readIntoUndeclaredVariableThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "y").execute(state));
    }

    @Test
    void readIntoNonIntVariableThrows() throws MyException {
        new VarDeclStmt("b", new BoolType()).execute(state);
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "b").execute(state));
    }

    @Test
    void readFromAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void readWithNonStringExpressionThrows() {
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(new ValueExp(new IntValue(1)), "x").execute(state));
    }

    @Test
    void readOfANonIntegerLineThrows() throws Exception {
        Path bad = dir.resolve("bad.in");
        Files.writeString(bad, "abc\n");
        ValueExp badFile = new ValueExp(new StringValue(bad.toString()));
        new OpenRFileStmt(badFile).execute(state);

        assertThrows(StatementException.class, () -> new ReadFileStmt(badFile, "x").execute(state));
    }

    // ---- closeRFile (S10)
    @Test
    void closeRemovesTheFileFromTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);

        new CloseRFileStmt(file).execute(state);

        assertFalse(state.getFileTable().isDefined(fileName));
    }

    @Test
    void closingAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class, () -> new CloseRFileStmt(file).execute(state));
    }

    @Test
    void closeWithNonStringExpressionThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(new ValueExp(new IntValue(1))).execute(state));
    }

    @Test
    void readAfterCloseThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);

        assertThrows(StatementException.class, () -> new ReadFileStmt(file, "x").execute(state));
    }
}
