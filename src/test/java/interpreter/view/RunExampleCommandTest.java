package interpreter.view;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RunExampleCommandTest {
    @TempDir
    Path dir;

    private long states(Path log) throws Exception {
        return Files.readAllLines(log).stream().filter("ExeStack:"::equals).count();
    }

    @Test
    void anExampleRunsOnlyOnce() throws Exception {
        Path log = dir.resolve("log.txt");
        Controller controller = new Controller(
                new Repository(PrgState.of(Examples.EX1), log.toString()), false);
        RunExampleCommand command = new RunExampleCommand("1", "ex1", controller);

        command.execute();
        command.execute();

        assertEquals(6L, states(log));
    }

    @Test
    void anInterpreterErrorIsReportedAndNotThrown() throws Exception {
        Path log = dir.resolve("log.txt");
        PrgState state = PrgState.of(new AssignStmt("x", new ValueExp(new IntValue(1))));
        Controller controller = new Controller(
                new Repository(state, log.toString()), false);

        new RunExampleCommand("1", "bad", controller).execute(); // must not throw

        assertTrue(Files.exists(log));
    }

    @Test
    void commandKeepsItsKeyAndDescription() {
        Command command = new ExitCommand("0", "exit");

        assertEquals("0", command.getKey());
        assertEquals("exit", command.getDescription());
    }
}
