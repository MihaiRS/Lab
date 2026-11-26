package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private final IStack<IStmt> exeStack;
    private final IDictionary<String, IValue> symTable;
    private final IList<IValue> out;
    private final IDictionary<StringValue, BufferedReader> fileTable;
    private final IStmt originalProgram;

    public PrgState(IStack<IStmt> exeStack,
                    IDictionary<String, IValue> symTable,
                    IList<IValue> out,
                    IDictionary<StringValue, BufferedReader> fileTable,
                    IStmt originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = originalProgram;
        this.exeStack.push(originalProgram);
    }

    // A state with empty structures: only this method changes when a new structure is added.
    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(), program);
    }

    public IStack<IStmt> getExeStack() { return exeStack; }

    public IDictionary<String, IValue> getSymTable() { return symTable; }

    public IList<IValue> getOut() { return out; }

    public IDictionary<StringValue, BufferedReader> getFileTable() { return fileTable; }

    public IStmt getOriginalProgram() { return originalProgram; }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n"
                + "SymTable: " + symTable + "\n"
                + "Out: " + out + "\n"
                + "FileTable: " + fileTable.toMap().keySet() + "\n";
    }

    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExeStack:\n");
        exeStack.toList().forEach(stmt -> sb.append(stmt).append('\n'));
        sb.append("SymTable:\n");
        symTable.toMap().forEach((name, value) ->
                sb.append(name).append(" --> ").append(value).append('\n'));
        sb.append("Out:\n");
        out.toList().forEach(value -> sb.append(value).append('\n'));
        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(file -> sb.append(file.getVal()).append('\n'));
        return sb.toString();
    }
}
