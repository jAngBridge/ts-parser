package de.f_estival.jab.tsparser.testutil;

import de.f_estival.jab.tsparser.JavaScriptLexer;
import de.f_estival.jab.tsparser.JavaScriptParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Assertions;

import java.util.function.Function;

public class ESParserTestUtil {

    public static JavaScriptParser parse(String input) {
        CharStream charStream = CharStreams.fromString(input);
        JavaScriptLexer lexer = new JavaScriptLexer(charStream);
        lexer.setUseStrictDefault(true);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaScriptParser parser = new JavaScriptParser(tokens);

        return parser;
    }

    public static <T extends ParserRuleContext> T test(String input, Function<JavaScriptParser, T> ruleInvoker) {
        JavaScriptParser parser = parse(input);
        T result = ruleInvoker.apply(parser);

        Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors(),
                String.format("UNEXPECTED PARSE ERROR: The input '%s' should be parsed without syntax errors.", input));

        return result;
    }
}
