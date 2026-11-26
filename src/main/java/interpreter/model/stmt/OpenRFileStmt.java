package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStmt implements IStmt { // rule S8
    private final IExp expression;

    public OpenRFileStmt(IExp expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = expression.eval(state.getSymTable());
        if (!(value instanceof StringValue fileName)) {
            throw new StatementException("openRFile: " + expression + " is not a string");
        }

        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        if (fileTable.isDefined(fileName)) {
            throw new StatementException("openRFile: " + fileName + " is already open");
        }

        try {
            // no try-with-resources: the file must stay open until closeRFile
            BufferedReader reader = new BufferedReader(new FileReader(fileName.getVal()));
            fileTable.put(fileName, reader);
        } catch (IOException e) {
            throw new StatementException("openRFile: cannot open " + fileName
                    + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }
}
