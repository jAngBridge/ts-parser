package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the different kinds of declaration rules that have been overriden by TypeScript grammar.
 *
 * <ul>
 *     <li>
 *         Tests the added Declarations alternatives: Interface(yet to do), Type Alias and Enum(yet to do) Declarations.
 *     </li>
 *     <li>
 *         Tests variable declarations.
 *     </li>
 * </ul>
 */
public class TypeScriptParserA3DeclarationTest {

    @Test
    public void testTypeAliasDeclaration() {
        TypeScriptParser.StatementListItemContext parsed = TsParserTestUtil.test(
                "type foo<Bar, Baz> = {bar: Bar; baz: Baz};",
                TypeScriptParser::statementListItem
        );

        TypeScriptParser.TypeAliasDeclarationContext aliasDeclaration = parsed.declaration().typeAliasDeclaration();
        Assertions.assertEquals("foo", aliasDeclaration.bindingIdentifier().getText());
        Assertions.assertEquals("<Bar,Baz>", aliasDeclaration.typeParameters().getText());
        Assertions.assertEquals(
                "{bar: Bar; baz: Baz}"
                        .replace(" ", ""),
                aliasDeclaration.type().getText())
        ;
    }

    /**
     * Only testing destructutingVariableDeclaration, simpleVariableDeclaration is tested
     * in {@link TypeScriptParserInheritsEcmaScriptTest#testVariableStatement(String)}
     * <br/><br/>
     * This method tests destructuring for arrays.
     * <br/>
     */
    @Test
    public void testArrayDestructVarDeclaration() {
        TypeScriptParser.VariableDeclarationContext parsed = TsParserTestUtil.test(
                "[foo, bar] = ['foobar', 42]",
                TypeScriptParser::variableDeclaration
        );
        Assertions.assertEquals("[foo,bar]",
                parsed.destructuringVariableDeclaration().bindingPattern().arrayBindingPattern().getText());
        Assertions.assertEquals("['foobar',42]",
                parsed.destructuringVariableDeclaration().initializer().singleExpression().getText());
    }

    /**
     * Only testing destructutingVariableDeclaration, simpleVariableDeclaration is tested
     * in {@link TypeScriptParserInheritsEcmaScriptTest#testVariableStatement(String)}
     * <br/><br/>
     * This method tests destructuring for objects.
     * <br/>
     */
    @Test
    public void testObjectDestructVarDeclaration() {
        TypeScriptParser.VariableDeclarationContext parsed = TsParserTestUtil.test(
                "{foo, bar = 4711} = {foo: 'foobar', bar: 42}", //4711 is default value
                TypeScriptParser::variableDeclaration
        );
        Assertions.assertEquals("{foo,bar=4711}",
                parsed.destructuringVariableDeclaration().bindingPattern().objectBindingPattern().getText());
        Assertions.assertEquals("{foo:'foobar',bar:42}",
                parsed.destructuringVariableDeclaration().initializer().singleExpression().getText());
    }


}










