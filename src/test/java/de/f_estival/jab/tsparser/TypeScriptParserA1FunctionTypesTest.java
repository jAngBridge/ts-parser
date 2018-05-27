package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TypeScriptParser#type()} for function types. This includes testing Parameter Lists.
 */
public class TypeScriptParserA1FunctionTypesTest {

    @Test
    public void testFunctionTypeWithoutParams() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "<T1, T2> () => Foobar",
                TypeScriptParser::type
        );

        Assertions.assertEquals("<T1,T2>", parsed.functionType().typeParameters().getText());
        Assertions.assertNull(parsed.functionType().parameterList());
        Assertions.assertEquals("Foobar", parsed.functionType().type().getText());
    }

    @Test
    public void testFunctionTypeOnlyRequiredParams() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "(r1: number, private r2: string, noType, litType: \"Foobar\") => void",
                TypeScriptParser::type
        );

        Assertions.assertNull(parsed.functionType().typeParameters());
        Assertions.assertEquals("void", parsed.functionType().type().getText());

        Assertions.assertNull(parsed.functionType().parameterList().optionalParameterList());
        Assertions.assertNull(parsed.functionType().parameterList().restParameter());

        //Params:
        TypeScriptParser.RequiredParameterListContext requiredParams = parsed.functionType().parameterList().requiredParameterList();

        Assertions.assertNull(requiredParams.requiredParameter(0).accessibilityModifier());
        Assertions.assertEquals("r1:number", requiredParams.requiredParameter(0).getText());

        TypeScriptParser.RequiredParameterContext r2 = requiredParams.requiredParameter(1);
        Assertions.assertEquals("private", r2.accessibilityModifier().getText());
        Assertions.assertEquals("r2", r2.bindingIdentifierOrPattern().getText());
        Assertions.assertEquals("string", r2.typeAnnotation().type().getText());

        TypeScriptParser.RequiredParameterContext noType = requiredParams.requiredParameter(2);
        Assertions.assertNull(noType.accessibilityModifier());
        Assertions.assertEquals("noType", noType.bindingIdentifierOrPattern().getText());
        Assertions.assertNull(noType.typeAnnotation());

        TypeScriptParser.RequiredParameterContext litType = requiredParams.requiredParameter(3);
        Assertions.assertEquals("\"Foobar\"", litType.StringLiteral().getText());
    }

    @Test
    public void testFunctionTypeOnlyOptionalParams() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "(o1?, private o2: number = 42, o3 = 'foobar') => void",
                TypeScriptParser::type
        );

        Assertions.assertNull(parsed.functionType().typeParameters());
        Assertions.assertEquals("void", parsed.functionType().type().getText());

        Assertions.assertNull(parsed.functionType().parameterList().requiredParameterList());
        Assertions.assertNull(parsed.functionType().parameterList().restParameter());

        //Params:
        TypeScriptParser.OptionalParameterListContext optionalParams =
                parsed.functionType().parameterList().optionalParameterList();

        Assertions.assertNull(optionalParams.optionalParameter(0).accessibilityModifier());
        Assertions.assertEquals("o1",
                optionalParams.optionalParameter(0).bindingIdentifierOrPattern().getText());

        TypeScriptParser.OptionalParameterContext o2 = optionalParams.optionalParameter(1);
        Assertions.assertEquals("private", o2.accessibilityModifier().getText());
        Assertions.assertEquals("o2", o2.bindingIdentifierOrPattern().getText());
        Assertions.assertEquals("number", o2.typeAnnotation().type().getText());
        Assertions.assertEquals("42", o2.initializer().singleExpression().getText());

        TypeScriptParser.OptionalParameterContext o3 = optionalParams.optionalParameter(2);
        Assertions.assertEquals("o3", o3.bindingIdentifierOrPattern().getText());
        Assertions.assertEquals("'foobar'", o3.initializer().singleExpression().getText());
    }

    @Test
    public void testFunctionTypeOnlyRestParams() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "(... args: Foobar) => void",
                TypeScriptParser::type
        );

        Assertions.assertNull(parsed.functionType().typeParameters());
        Assertions.assertEquals("void", parsed.functionType().type().getText());

        Assertions.assertNull(parsed.functionType().parameterList().requiredParameterList());
        Assertions.assertNull(parsed.functionType().parameterList().optionalParameterList());

        //Params:
        TypeScriptParser.RestParameterContext restParam =
                parsed.functionType().parameterList().restParameter();

        Assertions.assertEquals("args", restParam.bindingIdentifier().getText());
        Assertions.assertEquals("Foobar", restParam.typeAnnotation().type().getText());
    }

    @Test
    public void testFunctionTypeMixedParams() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "(private r: number, public o: string = 'foobar', ... args: Foobar) => void",
                TypeScriptParser::type
        );

        Assertions.assertNull(parsed.functionType().typeParameters());
        Assertions.assertEquals("void", parsed.functionType().type().getText());


        //Requried Params:
        TypeScriptParser.RequiredParameterListContext requiredParams =
                parsed.functionType().parameterList().requiredParameterList();

        Assertions.assertEquals(
                "private r: number"
                        .replace(" ", ""),
                requiredParams.requiredParameter(0).getText()
        );

        //Optional Params:
        TypeScriptParser.OptionalParameterListContext optionalParams =
                parsed.functionType().parameterList().optionalParameterList();

        Assertions.assertEquals(
                "public o: string = 'foobar'"
                        .replace(" ", ""),
                optionalParams.optionalParameter(0).getText()
        );

        //Rest Params:
        TypeScriptParser.RestParameterContext restParam =
                parsed.functionType().parameterList().restParameter();

        Assertions.assertEquals("args", restParam.bindingIdentifier().getText());
        Assertions.assertEquals("Foobar", restParam.typeAnnotation().type().getText());
    }


}
