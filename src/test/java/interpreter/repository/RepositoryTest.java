package interpreter.repository;

import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositoryTest {
    @TempDir
    Path dir;

    @Test
    void returnsCurrentProgramState() {
        PrgState state = PrgState.of(new NopStmt());

        Repository repository = new Repository(state, dir.resolve("log.txt").toString());

        assertSame(state, repository.getCrtPrg());
    }

    @Test
    void logWritesTheFourSectionsAndTheStack() throws Exception {
        Path log = dir.resolve("log.txt");
        Repository repository = new Repository(PrgState.of(new NopStmt()), log.toString());

        repository.logPrgStateExec();

        List<String> lines = Files.readAllLines(log);
        assertTrue(lines.indexOf("ExeStack:") < lines.indexOf("nop"));
        assertTrue(lines.indexOf("nop") < lines.indexOf("SymTable:"));
        assertTrue(lines.indexOf("SymTable:") < lines.indexOf("Out:"));
        assertTrue(lines.indexOf("Out:") < lines.indexOf("FileTable:"));
    }

    @Test
    void logAppendsInsteadOfOverwriting() throws Exception {
        Path log = dir.resolve("log.txt");
        Repository repository = new Repository(PrgState.of(new NopStmt()), log.toString());

        repository.logPrgStateExec();
        repository.logPrgStateExec();

        long sections = Files.readAllLines(log).stream().filter("ExeStack:"::equals).count();
        assertEquals(2L, sections);
    }

    @Test
    void unwritableLogPathThrowsRepositoryException() {
        String path = dir.resolve("no-such-folder").resolve("log.txt").toString();
        Repository repository = new Repository(PrgState.of(new NopStmt()), path);

        assertThrows(RepositoryException.class, repository::logPrgStateExec);
    }
}
