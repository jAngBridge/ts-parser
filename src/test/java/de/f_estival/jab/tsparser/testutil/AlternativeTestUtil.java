package de.f_estival.jab.tsparser.testutil;

import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Assertions;

public class AlternativeTestUtil {


    /**
     * Asserts, that the given RuleContext "parsed" is of type "targetClass" and also returns it properly casted.
     */
    public static <T extends ParserRuleContext> T alt(ParserRuleContext parsed,
                                                      Class<T> targetClass) {
        Assertions.assertEquals(targetClass, parsed.getClass());
        return ((T) parsed);
    }
}
