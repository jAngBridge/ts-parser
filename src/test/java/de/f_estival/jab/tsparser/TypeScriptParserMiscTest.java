package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.AlternativeTestUtil;
import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeScriptParserMiscTest {

    @Test
    public void testCreateTypeInstance() {
        TypeScriptParser.SingleExpressionContext parsed = TsParserTestUtil.test(
                "new InjectionToken<TestService1>('Contains all fields for AppComponent')",
                TypeScriptParser::singleExpression
        );

        TypeScriptParser.NewExpressionContext newExpression =
                AlternativeTestUtil.alt(parsed, TypeScriptParser.NewSingleExpressionContext.class).newExpression();

        Assertions.assertEquals("InjectionToken", newExpression.identifierReference().getText());
        Assertions.assertEquals("<TestService1>", newExpression.arguments().typeArguments().getText());
        Assertions.assertNotNull(newExpression.arguments());
    }


    @Test
    public void testGenericMethodCall() {
        TypeScriptParser.SingleExpressionContext parsed = TsParserTestUtil.test(
                "foo<Entity1[]>('bar')",
                TypeScriptParser::singleExpression
        );

        TypeScriptParser.CallExpressionContext callExpression =
                AlternativeTestUtil.alt(parsed, TypeScriptParser.CallSingleExpressionContext.class).callExpression();

        Assertions.assertEquals("foo",
                callExpression.memberExpression().primaryExpression().identifierReference().getText());
        TypeScriptParser.TypeContext type = callExpression.arguments().typeArguments().typeArgumentList().typeArgument(0).type();
        Assertions.assertEquals("Entity1[]",
                type.getText());

        TypeScriptParser.PrimaryTypeContext primary =
                AlternativeTestUtil.alt(type.unionOrIntersectionOrPrimaryType(), TypeScriptParser.PrimaryContext.class)
                .primaryType();
        TypeScriptParser.PrimaryTypeContext wrappedType =
                AlternativeTestUtil.alt(primary, TypeScriptParser.ArrayPrimTypeContext.class)
                .primaryType();
        Assertions.assertEquals("Entity1", wrappedType.getText());


        Assertions.assertEquals("'bar'",
                callExpression.arguments().argumentsList().getText());
    }


}
