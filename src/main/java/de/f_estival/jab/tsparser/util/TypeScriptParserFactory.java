package de.f_estival.jab.tsparser.util;

import de.f_estival.jab.tsparser.TypeScriptLexer;
import de.f_estival.jab.tsparser.TypeScriptParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class TypeScriptParserFactory {

    public static TypeScriptParser parse(CharStream charStream) {
        /* Always parsing TS code as 'strict' mode.
         * This means: the JavaScript Lexer parts that we inherit in TypeScript Parser
         * will treat TS code like strict JavaScript Code.
         * Keywords like 'let', 'private' and many more are currently only considered keywords in strict mode by
         * the JavaScript Lexer.
         */
        TypeScriptLexer lexer = new TypeScriptLexer(charStream);
        lexer.setUseStrictDefault(true);

//        TsDebugHelper.printTokens(lexer);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TypeScriptParser parser = new TypeScriptParser(tokens);

        return parser;
    }

    public static TypeScriptParser parse(InputStream input) throws IOException {
        return parse(CharStreams.fromStream(input));
    }
}
