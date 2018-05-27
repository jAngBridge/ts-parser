package de.f_estival.jab.tsparser;


import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link TypeScriptParser}, which is generated from the TypeScriptParser.g4 grammar.
 *
 * <p>
 *     Asserts that the TypeScriptParser also parses some basic JavaScript inputs.
 *     This is basically a regression test to ensure that the TypeScriptParser is not completely broken or overwrites
 *     JavaScript behaviour.
 * </p>
 */
public class TypeScriptParserInheritsEcmaScriptTest {

    @Test
    public void testVariableStatements() {
        testVariableStatement("var");
        testVariableStatement("let");
        testVariableStatement("const");
    }

    private void testVariableStatement(String varModifier) {
        TypeScriptParser.VariableStatementContext parsed =
                TsParserTestUtil.test(varModifier + " foobar = 42;", TypeScriptParser::variableStatement);

        TypeScriptParser.VariableDeclarationContext varDecl =
                parsed.variableDeclarationList().variableDeclaration(0);
        TypeScriptParser.SimpleVariableDeclarationContext simpleVarDec = varDecl.simpleVariableDeclaration();
        Assertions.assertEquals("foobar", simpleVarDec.bindingIdentifier().getText());
        Assertions.assertEquals("42", simpleVarDec.initializer().singleExpression().getText());
    }


}
