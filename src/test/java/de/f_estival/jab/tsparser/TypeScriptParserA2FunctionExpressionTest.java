package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.AlternativeTestUtil;
import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the Function Expression and Arrow Function Parameters Type.
 * <p>
 *  Call Signatures are already tested in {@link TypeScriptParserA1PrimaryTypesObjectTypeTest}
 *  and {@link TypeScriptParserA1FunctionTypesTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserA2FunctionExpressionTest {

    @Test
    public void testFunctionAsSingleExpression() {
        TypeScriptParser.SingleExpressionContext parsed = TsParserTestUtil.test(
                "function doTheFoo <T, E> (r: T, o?: E, ...args: string): number {" +
                        "   console.log('Foo');" +
                        "   return 42;" +
                        "}",
                TypeScriptParser::singleExpression
        );

        TypeScriptParser.FunctionExpressionContext functionExpression = AlternativeTestUtil.alt(
                parsed,
                TypeScriptParser.FunctionSingleExpressionContext.class
        ).functionExpression();

        Assertions.assertEquals("doTheFoo", functionExpression.bindingIdentifier().getText());
        Assertions.assertEquals(
                "<T, E> (r: T, o?: E, ...args: string): number "
                        .replace(" ", ""),
                functionExpression.callSignature().getText()
        );
        Assertions.assertEquals("console.log('Foo');return42;", functionExpression.functionBody().getText());
    }

    @Test
    public void testArrowFunctionAsSingleExpression() {
        TypeScriptParser.SingleExpressionContext parsed = TsParserTestUtil.test(
                "<T, E> (r: T, o?: E, ...args: string): number => args.length();",
                TypeScriptParser::singleExpression
        );

        TypeScriptParser.ArrowFunctionExpressionContext arrowFunction = AlternativeTestUtil.alt(
                parsed,
                TypeScriptParser.ArrowFunctionExpressionContext.class
        );

        Assertions.assertEquals(
                "<T, E> (r: T, o?: E, ...args: string): number "
                        .replace(" ", ""),
                arrowFunction.arrowFunctionParameters().callSignature().getText()
        );
        Assertions.assertEquals("args.length()",
                arrowFunction.arrowFunctionBody().singleExpression().getText());
    }

}










