package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.AlternativeTestUtil;
import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeScriptParserA1GenericsTest {

    @Test
    public void testTypeParameters() {
        TypeScriptParser.TypeParametersContext parsed = TsParserTestUtil.test(
                "<Foo1, Baz2 extends Bar, Foobar>",
                TypeScriptParser::typeParameters
        );

        TypeScriptParser.TypeParameterContext type0 = parsed.typeParameterList().typeParameter(0);
        TypeScriptParser.TypeParameterContext type1Construct = parsed.typeParameterList().typeParameter(1);
        TypeScriptParser.BindingIdentifierContext type1 = type1Construct.bindingIdentifier();
        TypeScriptParser.TypeContext type1ConstraintType = type1Construct.constraint().type();
        TypeScriptParser.TypeParameterContext type2 = parsed.typeParameterList().typeParameter(2);

        Assertions.assertEquals("Foo1", type0.getText());
        Assertions.assertEquals("Baz2", type1.getText());
        Assertions.assertEquals("Bar", type1ConstraintType.getText());
        Assertions.assertEquals("Foobar", type2.getText());
    }

    @Test
    public void testTypeArguments() {
        TypeScriptParser.TypeArgumentsContext parsed = TsParserTestUtil.test(
                "<Foo, Bar>",
                TypeScriptParser::typeArguments
        );

        TypeScriptParser.TypeArgumentContext type0 = parsed.typeArgumentList().typeArgument(0);
        TypeScriptParser.TypeArgumentContext type1 = parsed.typeArgumentList().typeArgument(1);

        Assertions.assertEquals("Foo", type0.getText());
        Assertions.assertEquals("Bar", type1.getText());
    }

    @Test
    public void testTypeArgumentsDoNotAllowConstraints() {
        TypeScriptParser.TypeArgumentsContext parsed = TsParserTestUtil.expectParseError(
                "<Foo, Baz extends Baz>",
                TypeScriptParser::typeArguments
        );
    }

    @Test
    public void testTypeArrayAsTypeArgument() {
        TypeScriptParser.TypeArgumentsContext parsed = TsParserTestUtil.test(
                "<Foo[]>",
                TypeScriptParser::typeArguments
        );

        TypeScriptParser.UnionOrIntersectionOrPrimaryTypeContext uipType =
                parsed.typeArgumentList().typeArgument(0).type().unionOrIntersectionOrPrimaryType();
        TypeScriptParser.PrimaryTypeContext primaryType =
                AlternativeTestUtil.alt(uipType, TypeScriptParser.PrimaryContext.class).primaryType();
        TypeScriptParser.PrimaryTypeContext wrappedType =
                AlternativeTestUtil.alt(primaryType, TypeScriptParser.ArrayPrimTypeContext.class).primaryType();


        Assertions.assertEquals("Foo", wrappedType.getText());
    }
}
