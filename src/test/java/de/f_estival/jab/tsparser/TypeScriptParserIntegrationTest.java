package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import de.f_estival.jab.tsparser.util.TsDebugHelper;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * Test real source files, where all the grammar rules must work together.
 */
public class TypeScriptParserIntegrationTest {
    @Test
    public void testAngularComponent() {
        InputStream testData =
                TypeScriptParserIntegrationTest.class.getResourceAsStream("/parser/examples/app.component.ts.test");

        TypeScriptParser.SourceFileContext parsed = TsParserTestUtil.test(
                testData,
                TypeScriptParser::sourceFile
        );

        TsDebugHelper.printTsTree(parsed);
    }

    @Test
    public void testAngularModule() {
        InputStream testData =
                TypeScriptParserIntegrationTest.class.getResourceAsStream("/parser/examples/app.module.ts.test");

        TypeScriptParser.SourceFileContext parsed = TsParserTestUtil.test(
                testData,
                TypeScriptParser::sourceFile
        );

//        TsDebugHelper.printTsTree(parsed);
    }

    @Test
    public void testAngularServiceHierarchy() {
        InputStream testData = TypeScriptParserIntegrationTest.class.getResourceAsStream(
                "/parser/examples/test-service1.service.ts.test"
        );

        TypeScriptParser.SourceFileContext parsed = TsParserTestUtil.test(
                testData,
                TypeScriptParser::sourceFile
        );

        TsDebugHelper.printTsTree(parsed);
    }
}
