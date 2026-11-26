package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt { // rule S9
    private final IExp expression;
    private final String varName;

    public ReadFileStmt(IExp expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!state.getSymTable().isDefined(varName)) {
            throw new StatementException("readFile: variable " + varName + " is not declared");
        }
        if (!state.getSymTable().lookup(varName).getType().equals(new IntType())) {
            throw new StatementException("readFile: variable " + varName + " is not an int");
        }

        IValue value = expression.eval(state.getSymTable());
        if (!(value instanceof StringValue fileName)) {
            throw new StatementException("readFile: " + expression + " is not a string");
        }
        if (!state.getFileTable().isDefined(fileName)) {
            throw new StatementException("readFile: " + fileName + " is not open");
        }

        BufferedReader reader = state.getFileTable().lookup(fileName);
        try {
            String line = reader.readLine();
            int number = (line == null) ? 0 : Integer.parseInt(line.strip());
            state.getSymTable().update(varName, new IntValue(number));
        } catch (IOException e) {
            throw new StatementException("readFile: cannot read " + fileName
                    + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new StatementException("readFile: a line of " + fileName
                    + " is not an integer");
        }
        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + expression + ", " + varName + ")";
    }
}
