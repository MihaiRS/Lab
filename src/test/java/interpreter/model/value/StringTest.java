package interpreter.model.value;

import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StringTest {
    @Test
    void stringTypeHasEmptyStringAsDefaultValue() {
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void equalStringValuesAreEqualAndHaveTheSameHashCode() {
        StringValue a = new StringValue("test.in");
        StringValue b = new StringValue("test.in");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentStringValuesAreNotEqual() {
        assertNotEquals(new StringValue("a"), new StringValue("b"));
    }

    @Test
    void stringValueHasStringType() {
        assertEquals(new StringType(), new StringValue("x").getType());
    }

    @Test
    void declaredStringVariableGetsEmptyString() throws Exception {
        PrgState state = PrgState.of(new NopStmt());

        new VarDeclStmt("s", new StringType()).execute(state);

        assertEquals(new StringValue(""), state.getSymTable().lookup("s"));
    }
}
