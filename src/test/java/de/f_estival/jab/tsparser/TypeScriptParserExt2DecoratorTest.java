package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.AlternativeTestUtil;
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
public class TypeScriptParserExt2DecoratorTest {

    @Test
    public void testDecoratorList() {
        TypeScriptParser.DecoratorListContext parsed = TsParserTestUtil.test(
                "@Component({selector: 'app-root',\n" +
                        "  templateUrl: './app.component.html',\n" +
                        "  styleUrls: ['./app.component.css']})",
                TypeScriptParser::decoratorList
        );
        TypeScriptParser.DecoratorCallExpressionContext decoratorCall = parsed.decorator(0).decoratorCallExpression();

        Assertions.assertEquals("Component",
                decoratorCall.decoratorMemberExpression().identifierReference().getText());

        //assert first argument is parsed as object literal:
        AlternativeTestUtil.alt(decoratorCall.arguments().argumentsList().singleExpression(0),
                TypeScriptParser.ObjectLiteralExpressionContext.class);
    }

    @Test
    public void testClassDecorator() {
        TypeScriptParser.ClassDeclarationContext parsed = TsParserTestUtil.test(
                "@Component(42)" +
                        "class FooBar {}",
                TypeScriptParser::classDeclaration
        );

        TypeScriptParser.ClassDeclarationContext classDeclaration = parsed;//parsed.classDeclaration();
        TypeScriptParser.DecoratorCallExpressionContext decorator =
                classDeclaration.decoratorList().decorator(0).decoratorCallExpression();

        Assertions.assertEquals("Component",
                decorator.decoratorMemberExpression().identifierReference().getText());
    }

    @Test
    public void testExportClassDecorator() {
        TypeScriptParser.ExportImplementationElementContext parsed = TsParserTestUtil.test(
                "@Component(42)" +
                        "export class FooBar {}",
                TypeScriptParser::exportImplementationElement
        );

//        TypeScriptParser.ClassDeclarationContext classDeclaration = parsed.classDeclaration();
        //watch out, decorator does not belong to parsed.classDeclaration(), but to the exportImplElement itself:
        TypeScriptParser.DecoratorCallExpressionContext decorator =
                parsed.decoratorList().decorator(0).decoratorCallExpression();

        Assertions.assertEquals("Component",
                decorator.decoratorMemberExpression().identifierReference().getText());
    }

}










