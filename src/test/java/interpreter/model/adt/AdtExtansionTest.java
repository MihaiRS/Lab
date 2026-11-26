package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdtExtensionsTest {
    @Test
    void removeDeletesTheKey() throws Exception {
        MyDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 1);

        dictionary.remove("x");

        assertFalse(dictionary.isDefined("x"));
    }

    @Test
    void removingMissingKeyThrows() {
        MyDictionary<String, Integer> dictionary = new MyDictionary<>();

        assertThrows(ADTException.class, () -> dictionary.remove("missing"));
    }

    @Test
    void toMapReturnsACopy() {
        MyDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 1);

        Map<String, Integer> copy = dictionary.toMap();
        dictionary.put("y", 2);

        assertEquals(1, copy.size());
        assertEquals(Integer.valueOf(1), copy.get("x"));
    }

    @Test
    void stackToListHasTheTopFirst() {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(List.of(3, 2, 1), stack.toList());
    }

    @Test
    void listToListKeepsInsertionOrder() {
        MyList<Integer> list = new MyList<>();
        list.add(10);
        list.add(20);

        assertEquals(List.of(10, 20), list.toList());
    }
}
