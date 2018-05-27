package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.AlternativeTestUtil;
import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests
 * <ul>
 *     <li>the Unary Type Cast Expression.</li>
 *     <li>arguments</li>
 * </ul>
 */
public class TypeScriptParserA2MiscTest {

    @Test
    public void testCastExpression() {
        TypeScriptParser.SingleExpressionContext parsed = TsParserTestUtil.test(
                "<FooBar> myVar;",
                TypeScriptParser::singleExpression
        );

        TypeScriptParser.UnaryExpressionContext unaryExpression = AlternativeTestUtil.alt(
                parsed,
                TypeScriptParser.UnarySingleExpressionContext.class
        ).unaryExpression();
        TypeScriptParser.TypeCastExpressionContext castExpression = AlternativeTestUtil.alt(
                unaryExpression,
                TypeScriptParser.TypeCastExpressionContext.class
        );

        Assertions.assertEquals("FooBar", castExpression.type().getText());
        Assertions.assertEquals("myVar", castExpression.singleExpression().getText());
    }


    @Test
    public void testArguments() {
        TypeScriptParser.ArgumentsContext parsed = TsParserTestUtil.test(
                "<Foo, Bar> ('foo', 42, null);",
                TypeScriptParser::arguments
        );

        Assertions.assertEquals("<Foo,Bar>", parsed.typeArguments().getText());
        Assertions.assertEquals("'foo',42,null", parsed.argumentsList().getText());
    }


}










