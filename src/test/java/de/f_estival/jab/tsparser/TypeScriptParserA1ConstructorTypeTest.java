package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TypeScriptParser#type()} for Constructor Types
 */
public class TypeScriptParserA1ConstructorTypeTest {
    /**
     * Parameter Lists are tested in {@link TypeScriptParserA1FunctionTypesTest}, so they are blackboxed here.
     */
    @Test
    public void testFunctionTypeMixedParams() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "new <Foo, Bar> (private r: string, public o: number = 42, o2?, ...args: string) => FooBar",
                TypeScriptParser::type
        );

        Assertions.assertEquals("<Foo,Bar>", parsed.constructorType().typeParameters().getText());
        Assertions.assertEquals(
                "private r: string, public o: number = 42, o2?, ...args: string"
                        .replace(" ", ""),
                parsed.constructorType().parameterList().getText()
        );
        Assertions.assertEquals("FooBar", parsed.constructorType().type().getText());
    }

}
