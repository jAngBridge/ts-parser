package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the Function Declarations.
 * <p>
 *  Call Signatures are already tested in {@link TypeScriptParserA1PrimaryTypesObjectTypeTest}
 *  and {@link TypeScriptParserA1FunctionTypesTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserA4FunctionDeclarationTest {

    @Test
    public void testEmptyFunctionBody() {
        TypeScriptParser.FunctionDeclarationContext parsed = TsParserTestUtil.test(
                "function doTheFoo <T, E> (r: T, o?: E, ...args: string): number;",
                TypeScriptParser::functionDeclaration
        );

        Assertions.assertEquals("doTheFoo", parsed.bindingIdentifier().getText());
        Assertions.assertEquals(
                "<T, E> (r: T, o?: E, ...args: string): number"
                .replace(" ", ""),
                parsed.callSignature().getText()
        );
        Assertions.assertNull(parsed.functionBody());
    }

    @Test
    public void testFunctionDeclaration() {
        TypeScriptParser.FunctionDeclarationContext parsed = TsParserTestUtil.test(
                "function doTheFoo <T, E> (r: T, o?: E, ...args: string): number {" +
                        "   console.log(r);" +
                        "}",
                TypeScriptParser::functionDeclaration
        );

        Assertions.assertEquals("doTheFoo", parsed.bindingIdentifier().getText());
        Assertions.assertEquals(
                "<T, E> (r: T, o?: E, ...args: string): number"
                .replace(" ", ""),
                parsed.callSignature().getText()
        );
        Assertions.assertEquals("console.log(r);", parsed.functionBody().getText());
    }

}










