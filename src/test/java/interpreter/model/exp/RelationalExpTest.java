package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RelationalExpTest {
    private IValue eval(RelOp op, int a, int b) throws Exception {
        return new RelationalExp(op, new ValueExp(new IntValue(a)), new ValueExp(new IntValue(b)))
                .eval(new MyDictionary<>());
    }

    private void check(RelOp op, int a, int b, boolean expected) throws Exception {
        assertEquals(new BoolValue(expected), eval(op, a, b));
    }

    @Test
    void lessThan() throws Exception {
        check(RelOp.LT, 1, 2, true);
        check(RelOp.LT, 2, 2, false);
    }

    @Test
    void lessOrEqual() throws Exception {
        check(RelOp.LE, 2, 2, true);
        check(RelOp.LE, 3, 2, false);
    }

    @Test
    void equal() throws Exception {
        check(RelOp.EQ, 4, 4, true);
        check(RelOp.EQ, 4, 5, false);
    }

    @Test
    void notEqual() throws Exception {
        check(RelOp.NE, 4, 5, true);
        check(RelOp.NE, 4, 4, false);
    }

    @Test
    void greaterThan() throws Exception {
        check(RelOp.GT, 3, 2, true);
        check(RelOp.GT, 2, 2, false);
    }

    @Test
    void greaterOrEqual() throws Exception {
        check(RelOp.GE, 2, 2, true);
        check(RelOp.GE, 1, 2, false);
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp expression = new RelationalExp(RelOp.LT,
                new ValueExp(new BoolValue(true)), new ValueExp(new IntValue(1)));

        assertThrows(ExpressionException.class, () -> expression.eval(new MyDictionary<>()));
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp expression = new RelationalExp(RelOp.LT,
                new ValueExp(new IntValue(1)), new ValueExp(new StringValue("x")));

        assertThrows(ExpressionException.class, () -> expression.eval(new MyDictionary<>()));
    }
}
