package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests {@link TypeScriptParser#type()} for all Primary Types except for ObjectType.
 */
public class TypeScriptParserA1PrimaryTypesSimpleTypesTest {

    @Test
    public void testPredefinedTypes() {
        List<TypeScriptParser.TypeContext> parsed = TsParserTestUtil.test(
                TypeScriptParser::type,
                "any", "number", "boolean", "string", "symbol", "void"
        );
    }

    @Test
    public void testTypeReference() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "de.f_estival.Foobar<Arg1,Arg2>",
                TypeScriptParser::type
        );

        TypeScriptParser.PrimaryTypeContext primaryType = getPrimaryTypeContext(parsed);

        Assertions.assertTrue(primaryType instanceof TypeScriptParser.ReferencePrimTypeContext);
        TypeScriptParser.TypeReferenceContext typeRef =
                ((TypeScriptParser.ReferencePrimTypeContext) primaryType).typeReference();
        TypeScriptParser.TypeNameContext typeName = typeRef.typeName();

        Assertions.assertEquals("de.f_estival", typeName.namespaceName().getText());
        Assertions.assertEquals("Foobar", typeName.identifierReference().getText());
        Assertions.assertEquals("<Arg1,Arg2>", typeRef.typeArguments().getText());
    }

    @Test
    public void testParenthesizedTypes() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "(de.f_estival.Foobar)",
                TypeScriptParser::type
        );

        TypeScriptParser.PrimaryTypeContext primaryType = getPrimaryTypeContext(parsed);

        Assertions.assertTrue(primaryType instanceof TypeScriptParser.ParenthesizedPrimTypeContext);
        TypeScriptParser.TypeContext wrappedType = ((TypeScriptParser.ParenthesizedPrimTypeContext) primaryType).type();

        Assertions.assertEquals("de.f_estival.Foobar", wrappedType.getText());
    }

//    /**
//     * TODO: does not work yet!
//     */
//    @Test
//    public void testTypeReferenceAllowsNoLineTerminatorsBeforeTypeArguments() {
//        TypeScriptParser.TypeContext parsed = TsParserTestUtil.expectParseError(
//                "de.f_estival.Foobar \n <Arg1,Arg2>",
//                TypeScriptParser::type
//        );
//    }

    @Test
    public void testArrayTypes() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "Foobar[][]",
                TypeScriptParser::type
        );

        TypeScriptParser.PrimaryTypeContext outerPrimType = getPrimaryTypeContext(parsed);

        //unwrap first level of array:
        Assertions.assertTrue(outerPrimType instanceof TypeScriptParser.ArrayPrimTypeContext);
        TypeScriptParser.PrimaryTypeContext firstWrap =
                ((TypeScriptParser.ArrayPrimTypeContext) outerPrimType).primaryType();

        //unwrap second level of array:
        Assertions.assertTrue(firstWrap instanceof TypeScriptParser.ArrayPrimTypeContext);
        TypeScriptParser.PrimaryTypeContext secondWrap =
                ((TypeScriptParser.ArrayPrimTypeContext) firstWrap).primaryType();

        Assertions.assertEquals("Foobar", secondWrap.getText());
    }

    @Test
    public void testTupleTypes() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "[number, de.f_estival.Foobar, string | boolean]",
                TypeScriptParser::type
        );

        TypeScriptParser.PrimaryTypeContext outerPrimType = getPrimaryTypeContext(parsed);

        //unwrap first level of array:
        Assertions.assertTrue(outerPrimType instanceof TypeScriptParser.TuplePrimTypeContext);
        TypeScriptParser.TupleElementTypesContext tupleType =
                ((TypeScriptParser.TuplePrimTypeContext) outerPrimType).tupleElementTypes();

        //unwrap second level of array:
        TypeScriptParser.PrimaryTypeContext type0 = getPrimaryTypeContext(tupleType.tupleElementType(0).type());
        TypeScriptParser.PrimaryTypeContext type1 = getPrimaryTypeContext(tupleType.tupleElementType(1).type());
        TypeScriptParser.UnionOrIntersectionOrPrimaryTypeContext type2 =
                tupleType.tupleElementType(2).type().unionOrIntersectionOrPrimaryType(); //Union Type


        Assertions.assertEquals("number", type0.getText());
        Assertions.assertEquals("de.f_estival.Foobar", type1.getText());
        Assertions.assertEquals("string|boolean", type2.getText());
        Assertions.assertTrue(type2 instanceof TypeScriptParser.UnionContext);
    }

    @Test
    public void testTypeQuery() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "typeof Foobar.barfoo.field1",
                TypeScriptParser::type
        );

        TypeScriptParser.PrimaryTypeContext primType = getPrimaryTypeContext(parsed);
        Assertions.assertTrue(primType instanceof TypeScriptParser.QueryPrimTypeContext);
        TypeScriptParser.TypeQueryExpressionContext typeQuery =
                ((TypeScriptParser.QueryPrimTypeContext) primType).typeQuery().typeQueryExpression();

        Assertions.assertEquals("Foobar", typeQuery.identifierName(0).getText());
        Assertions.assertEquals("barfoo", typeQuery.identifierName(1).getText());
        Assertions.assertEquals("field1", typeQuery.identifierName(2).getText());
    }

    @Test
    public void testThisType() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "this",
                TypeScriptParser::type
        );

        TypeScriptParser.PrimaryTypeContext primType = getPrimaryTypeContext(parsed);
        Assertions.assertTrue(primType instanceof TypeScriptParser.ThisPrimTypeContext);
        TypeScriptParser.ThisTypeContext thisType =
                ((TypeScriptParser.ThisPrimTypeContext) primType).thisType();

        Assertions.assertEquals("this", thisType.getText());
    }

    //
    // Helpers:
    //

    private TypeScriptParser.PrimaryTypeContext getPrimaryTypeContext(TypeScriptParser.TypeContext parsed) {
        TypeScriptParser.UnionOrIntersectionOrPrimaryTypeContext uipType = parsed.unionOrIntersectionOrPrimaryType();
        Assertions.assertTrue(uipType instanceof TypeScriptParser.PrimaryContext);
        return ((TypeScriptParser.PrimaryContext) uipType).primaryType();
    }

}
