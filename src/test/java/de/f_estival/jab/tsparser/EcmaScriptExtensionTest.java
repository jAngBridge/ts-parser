package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.ESParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link JavaScriptParser} which is generated from JavaScriptParser.g4 grammer.
 * <p>
 *     Tests those parts of the grammar which I had to add to complete the ES2015 spec.
 * </p>
 */
public class EcmaScriptExtensionTest {

    @Test
    public void testImportDeclaration() {
        JavaScriptParser.ImportDeclarationContext parsed = ESParserTestUtil.test(
                "import {Entity1, Entity2 as MyEntity2,} from '../../_jangbridge/model/';",
                JavaScriptParser::importDeclaration
        );

        //Assert Entity1:
        JavaScriptParser.ImportsListContext importsList = parsed.importClause().namedImports().importsList();
        JavaScriptParser.ImportedBindingContext simpleImported = importsList.importSpecifier(0).importedBinding();
        Assertions.assertEquals("Entity1", simpleImported.getText());

        //Assert Entity2 as MyEntity2:
        JavaScriptParser.ImportSpecifierContext renamedImportSpec = importsList.importSpecifier(1);
        Assertions.assertEquals("Entity2", renamedImportSpec.identifierName().getText());
        Assertions.assertEquals("MyEntity2", renamedImportSpec.importedBinding().getText());
    }

    @Test
    public void testExportConstDeclaration() {
        JavaScriptParser.ExportDeclarationContext parsed = ESParserTestUtil.test(
                "export const TestService1_MyVariant = 42",
                JavaScriptParser::exportDeclaration
        );

        Assertions.assertNotNull(parsed.variableStatement().varModifier().Const());
        Assertions.assertNull(parsed.variableStatement().varModifier().Var());
    }

    @Test
    public void testExportClassDeclaration() {
        JavaScriptParser.ExportDeclarationContext parsed = ESParserTestUtil.test(
                "export class FooBar {" +
                        " doTheFoo() { console.log('baz'); }" +
                        " }",
                JavaScriptParser::exportDeclaration
        );

        Assertions.assertNull(parsed.classDeclaration()); //this is the classDeclaration from rule 'export default ...'
        Assertions.assertNotNull(parsed.declaration().classDeclaration()); //and this is the correct declaration
        Assertions.assertEquals("console.log('baz');", parsed.declaration().classDeclaration()
                .classTail().classBody().classElement(0).methodDefinition()
                .functionBody().sourceElements().sourceElement(0).statement().getText());
    }

    @Test
    public void testPropertyDefinition() {
        JavaScriptParser.PropertyAssignmentContext parsed = ESParserTestUtil.test(
                "foo = asdf",
                JavaScriptParser::propertyAssignment
        );

        Assertions.assertTrue(parsed instanceof JavaScriptParser.PropertyExpressionAssignmentContext);
        Assertions.assertEquals("foo",
                ((JavaScriptParser.PropertyExpressionAssignmentContext) parsed).propertyName().getText());
    }
}
