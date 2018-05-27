package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.AlternativeTestUtil;
import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests Property Definition Types to work within {@link TypeScriptParser#objectLiteral() object literals}
 */
public class TypeScriptParserA2PropertyDefinitionTest {

    @Test
    public void testPropertyShorthands() {
        TypeScriptParser.ObjectLiteralContext parsed = TsParserTestUtil.test(
                "{ a, b }",
                TypeScriptParser::objectLiteral
        );

        TypeScriptParser.PropertyShorthandContext prop0 = AlternativeTestUtil.alt(
                parsed.propertyAssignment(0),
                TypeScriptParser.PropertyShorthandContext.class
        );
        TypeScriptParser.PropertyShorthandContext prop1 = AlternativeTestUtil.alt(
                parsed.propertyAssignment(1),
                TypeScriptParser.PropertyShorthandContext.class
        );

        Assertions.assertEquals("a", prop0.getText());
        Assertions.assertEquals("b", prop1.getText());
    }

    @Test
    public void testCoverInitializedName() {
        TypeScriptParser.ObjectLiteralContext parsed = TsParserTestUtil.test(
                "{ a = 42 }",
                TypeScriptParser::objectLiteral
        );

        TypeScriptParser.CoverInitializedNameContext prop0 = AlternativeTestUtil.alt(
                parsed.propertyAssignment(0),
                TypeScriptParser.CoverInitializedNameContext.class
        );

        //Test left side of "="
        Assertions.assertEquals("a", prop0.propertyName().getText());

        //Test right side of "="
        TypeScriptParser.LiteralContext assignementLiteral = AlternativeTestUtil.alt(
                prop0.singleExpression(),
                TypeScriptParser.LiteralExpressionContext.class
        ).literal();
        Assertions.assertEquals("42", assignementLiteral.getText());
    }

    //TODO: PropertyExpressionAssignment, MethodProperty

    @Test
    public void testGetterProperty() {
        TypeScriptParser.ObjectLiteralContext parsed = TsParserTestUtil.test(
                "{ get fooBar(): FooBarType { return this.fooBar; } }",
                TypeScriptParser::objectLiteral
        );

        TypeScriptParser.GetAccessorContext getter = AlternativeTestUtil.alt(
                parsed.propertyAssignment(0),
                TypeScriptParser.PropertyGetterContext.class
        ).getAccessor();
        Assertions.assertEquals("fooBar", getter.propertyName().getText());
        Assertions.assertEquals("FooBarType", getter.typeAnnotation().type().getText());
        Assertions.assertEquals("returnthis.fooBar;", getter.functionBody().getText());
    }

    @Test
    public void testSetterProperty() {
        TypeScriptParser.ObjectLiteralContext parsed = TsParserTestUtil.test(
                "{ set fooBar(fooBarParam: FooBarType) { this.fooBar = fooBarParam; } }",
                TypeScriptParser::objectLiteral
        );

        TypeScriptParser.SetAccessorContext setter = AlternativeTestUtil.alt(
                parsed.propertyAssignment(0),
                TypeScriptParser.PropertySetterContext.class
        ).setAccessor();
        Assertions.assertEquals("fooBar", setter.propertyName().getText());
        Assertions.assertEquals("fooBarParam", setter.bindingIdentifierOrPattern().getText());
        Assertions.assertEquals("FooBarType", setter.typeAnnotation().type().getText());
        Assertions.assertEquals("this.fooBar=fooBarParam;", setter.functionBody().getText());
    }
}
