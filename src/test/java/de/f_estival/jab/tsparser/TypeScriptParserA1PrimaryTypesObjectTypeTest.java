package de.f_estival.jab.tsparser;

import de.f_estival.jab.tsparser.testutil.TsParserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TypeScriptParser#type()} for the Primary Type "ObjectType".<br/>
 * <b>Basically this is also a test for INTERFACE BODIES</b>, as interfaces are kind of a "named objecttype".
 * Thus, ObjectTypes can get rather complex and the test fills its own class.
 */
public class TypeScriptParserA1PrimaryTypesObjectTypeTest {

    @Test
    public void testEmtpyObjectType() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{}",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);

        Assertions.assertNull(objectType.typeBody());
    }

    @Test
    public void testPropertySignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{field1: number, field2?: string; field3: Foobar<Baz>}",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.TypeMemberListContext members = objectType.typeBody().typeMemberList();

        TypeScriptParser.PropertySignaturContext member0 = members.typeMember(0).propertySignatur();
        Assertions.assertEquals("field1", member0.propertyName().getText());
        Assertions.assertEquals("number", member0.typeAnnotation().type().getText());

        TypeScriptParser.PropertySignaturContext member1 = members.typeMember(1).propertySignatur();
        Assertions.assertEquals("field2", member1.propertyName().getText());
        Assertions.assertEquals("string", member1.typeAnnotation().type().getText());

        TypeScriptParser.PropertySignaturContext member2 = members.typeMember(2).propertySignatur();
        Assertions.assertEquals("field3", member2.propertyName().getText());
        Assertions.assertEquals("Foobar<Baz>", member2.typeAnnotation().type().getText());
    }

    @Test
    public void testEmptyCallSignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{()}",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.CallSignatureContext callSignature =
                objectType.typeBody().typeMemberList().typeMember(0).callSignature();

        Assertions.assertNull(callSignature.typeParameters());
        Assertions.assertNull(callSignature.parameterList());
        Assertions.assertNull(callSignature.typeAnnotation());
    }

    /**
     * parameterlists are tested in detail in {@link TypeScriptParserA1FunctionTypesTest}.
     * So they are blackboxed in this test.
     */
    @Test
    public void testCallSignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{<Foo, Bar> (str: string, foo: Foo): Bar}",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.CallSignatureContext callSignature =
                objectType.typeBody().typeMemberList().typeMember(0).callSignature();

        Assertions.assertEquals("<Foo,Bar>", callSignature.typeParameters().getText());
        Assertions.assertEquals(
                "str: string, foo: Foo"
                        .replace(" ", ""),
                callSignature.parameterList().getText());
        Assertions.assertEquals("Bar", callSignature.typeAnnotation().type().getText());
    }

    @Test
    public void testEmptyConstructSignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{new ()}",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.ConstructSignatureContext callSignature =
                objectType.typeBody().typeMemberList().typeMember(0).constructSignature();

        Assertions.assertNull(callSignature.typeParameters());
        Assertions.assertNull(callSignature.parameterList());
        Assertions.assertNull(callSignature.typeAnnotation());
    }

    /**
     * parameterlists are tested in detail in {@link TypeScriptParserA1FunctionTypesTest}.
     * So they are blackboxed in this test.
     */
    @Test
    public void testConstructSignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{new <Foo, Bar> (str: string, foo: Foo): Bar}",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.ConstructSignatureContext callSignature =
                objectType.typeBody().typeMemberList().typeMember(0).constructSignature();

        Assertions.assertEquals("<Foo,Bar>", callSignature.typeParameters().getText());
        Assertions.assertEquals(
                "str: string, foo: Foo"
                        .replace(" ", ""),
                callSignature.parameterList().getText());
        Assertions.assertEquals("Bar", callSignature.typeAnnotation().type().getText());
    }

    /**
     * parameterlists are tested in detail in {@link TypeScriptParserA1FunctionTypesTest}.
     * So they are blackboxed in this test.
     */
    @Test
    public void testIndexSignatureString() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{ [key: string]: {x: number, y: Foobar} }",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.IndexSignatureContext indexSignature =
                objectType.typeBody().typeMemberList().typeMember(0).indexSignature();

        Assertions.assertEquals("key", indexSignature.bindingIdentifier().getText());
        Assertions.assertEquals(
                "{x: number, y: Foobar}"
                        .replace(" ", ""),
                indexSignature.typeAnnotation().type().getText());
    }

    /**
     * A Method Signature is like a "named Call Signature".<br/>
     * Call Signatures are already tested in {@link #testCallSignatures()}, so they are blackboxed here.
     */
    @Test
    public void testMethodSignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{ doTheFoo ? <Foo, Bar> (str: string, foo: Foo): Bar }",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.MethodSignatureContext methodSignature =
                objectType.typeBody().typeMemberList().typeMember(0).methodSignature();

        Assertions.assertEquals("doTheFoo", methodSignature.propertyName().getText());
        Assertions.assertEquals(
                "<Foo, Bar> (str: string, foo: Foo): Bar"
                        .replace(" ", ""),
                methodSignature.callSignature().getText());
    }

    /**
     * Example from the TypeScript 1.8 Language Spec, p. 52
     */
    @Test
    public void testDifferentFunctionSignatures() {
        TypeScriptParser.TypeContext parsed = TsParserTestUtil.test(
                "{ " +
                        "   func1(x: number): number;" + //Method signature
                        "   func2: (x: number) => number;" + //Function type literal
                        "   func3: { (x: number): number };" + //object type literal
                        " }",
                TypeScriptParser::type
        );

        TypeScriptParser.ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeScriptParser.TypeMemberListContext members = objectType.typeBody().typeMemberList();

        Assertions.assertEquals(
                "func1(x: number): number"
                        .replace(" ", ""),
                members.typeMember(0).methodSignature().getText()
        );

        Assertions.assertEquals(
                "(x: number) => number"
                        .replace(" ", ""),
                members.typeMember(1).propertySignatur().typeAnnotation().type().functionType().getText()
        );

        TypeScriptParser.ObjectTypeContext nestedObjectType =
                getObjectTypeContext(members.typeMember(2).propertySignatur().typeAnnotation().type());
        Assertions.assertEquals(
                "(x: number): number"
                        .replace(" ", ""),
                nestedObjectType.typeBody().typeMemberList().typeMember(0).callSignature().getText()
        );


    }

    //
    // Helpers:
    //

    private TypeScriptParser.ObjectTypeContext getObjectTypeContext(TypeScriptParser.TypeContext parsed) {
        TypeScriptParser.UnionOrIntersectionOrPrimaryTypeContext uipType = parsed.unionOrIntersectionOrPrimaryType();

        Assertions.assertTrue(uipType instanceof TypeScriptParser.PrimaryContext);
        TypeScriptParser.PrimaryTypeContext primaryType = ((TypeScriptParser.PrimaryContext) uipType).primaryType();

        Assertions.assertTrue(primaryType instanceof TypeScriptParser.ObjectPrimTypeContext);
        return ((TypeScriptParser.ObjectPrimTypeContext) primaryType).objectType();
    }

}
