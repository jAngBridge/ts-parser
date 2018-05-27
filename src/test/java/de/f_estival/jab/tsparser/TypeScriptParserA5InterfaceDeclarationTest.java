package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests Interface Declarations.
 * <p>
 *  The 'objectType' rule (the interface's body) is already tested in
 *  {@link TypeScriptParserA1PrimaryTypesObjectTypeTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserA5InterfaceDeclarationTest {

    @Test
    public void testInterfaceDeclaration() {
        TypeScriptParser.InterfaceDeclarationContext parsed = TsParserTestUtil.test(
                "interface Foo<T1, T2> extends Bar<T1>, Baz<T2> {a: FooBar;}",
                TypeScriptParser::interfaceDeclaration
        );

        Assertions.assertEquals("Foo", parsed.bindingIdentifier().getText());
        Assertions.assertEquals("<T1,T2>", parsed.typeParameters().getText());

        TypeScriptParser.ClassOrInterfaceTypeListContext extendsList =
                parsed.interfaceExtendsClause().classOrInterfaceTypeList();
        Assertions.assertEquals("Bar<T1>", extendsList.classOrInterfaceType(0).typeReference().getText());
        Assertions.assertEquals("Baz<T2>", extendsList.classOrInterfaceType(1).typeReference().getText());

        Assertions.assertEquals("a:FooBar;", parsed.objectType().typeBody().getText());
    }


}










