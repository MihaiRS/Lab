package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {
    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        Repository repository = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repository, false).allStep(); // display off
        return state;
    }

    @Test
    void example1PrintsTwo() throws Exception {
        PrgState state = run(Examples.EX1);

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws Exception {
        assertEquals(new IntValue(18), run(Examples.EX2).getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws Exception {
        assertEquals(new IntValue(2), run(Examples.EX3).getOut().get(0));
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertEquals(new IntValue(15), state.getOut().get(0));
        assertEquals(new IntValue(50), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithAnEmptyFilePrintsZeros() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertEquals(new IntValue(0), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getOut().get(1));
    }

    @Test
    void example5PrintsFive() throws Exception {
        PrgState state = run(Examples.EX5);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);

        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    @Test
    void logFileHoldsTheInitialStateAndOneStatePerStep() throws Exception {
        run(Examples.EX1); // 5 statements are executed

        long states = Files.readAllLines(dir.resolve("log.txt")).stream()
                .filter("ExeStack:"::equals).count();
        assertEquals(6L, states);
    }

    @Test
    void oneStepExecutesNextStatement() throws Exception {
        PrgState state = PrgState.of(new VarDeclStmt("x", new IntType()));
        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false);

        controller.oneStep(state);

        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void oneStepOnEmptyStackThrows() throws Exception {
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false);

        assertThrows(StatementException.class, () -> controller.oneStep(state));
    }
}
