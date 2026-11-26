package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt { // rule S10
    private final IExp expression;

    public CloseRFileStmt(IExp expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = expression.eval(state.getSymTable());
        if (!(value instanceof StringValue fileName)) {
            throw new StatementException("closeRFile: " + expression + " is not a string");
        }
        if (!state.getFileTable().isDefined(fileName)) {
            throw new StatementException("closeRFile: " + fileName + " is not open");
        }

        BufferedReader reader = state.getFileTable().lookup(fileName);
        try {
            reader.close();
        } catch (IOException e) {
            throw new StatementException("closeRFile: cannot close " + fileName
                    + ": " + e.getMessage());
        }
        state.getFileTable().remove(fileName);
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression + ")";
    }
}
