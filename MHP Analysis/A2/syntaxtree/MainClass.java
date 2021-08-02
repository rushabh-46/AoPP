//
// Generated by JTB 1.3.2
//

package syntaxtree;

/**
 * Grammar production:
 * f0 -> "class"
 * f1 -> Identifier()
 * f2 -> "{"
 * f3 -> "public"
 * f4 -> "static"
 * f5 -> "void"
 * f6 -> "main"
 * f7 -> "("
 * f8 -> "String"
 * f9 -> "["
 * f10 -> "]"
 * f11 -> Identifier()
 * f12 -> ")"
 * f13 -> "{"
 * f14 -> "try"
 * f15 -> "{"
 * f16 -> ( VarDeclaration() )*
 * f17 -> ( QParStatement() )*
 * f18 -> "}"
 * f19 -> "catch"
 * f20 -> "("
 * f21 -> Identifier()
 * f22 -> Identifier()
 * f23 -> ")"
 * f24 -> "{"
 * f25 -> "}"
 * f26 -> "}"
 * f27 -> "}"
 */
public class MainClass implements Node {
   public NodeToken f0;
   public Identifier f1;
   public NodeToken f2;
   public NodeToken f3;
   public NodeToken f4;
   public NodeToken f5;
   public NodeToken f6;
   public NodeToken f7;
   public NodeToken f8;
   public NodeToken f9;
   public NodeToken f10;
   public Identifier f11;
   public NodeToken f12;
   public NodeToken f13;
   public NodeToken f14;
   public NodeToken f15;
   public NodeListOptional f16;
   public NodeListOptional f17;
   public NodeToken f18;
   public NodeToken f19;
   public NodeToken f20;
   public Identifier f21;
   public Identifier f22;
   public NodeToken f23;
   public NodeToken f24;
   public NodeToken f25;
   public NodeToken f26;
   public NodeToken f27;

   public MainClass(NodeToken n0, Identifier n1, NodeToken n2, NodeToken n3, NodeToken n4, NodeToken n5, NodeToken n6, NodeToken n7, NodeToken n8, NodeToken n9, NodeToken n10, Identifier n11, NodeToken n12, NodeToken n13, NodeToken n14, NodeToken n15, NodeListOptional n16, NodeListOptional n17, NodeToken n18, NodeToken n19, NodeToken n20, Identifier n21, Identifier n22, NodeToken n23, NodeToken n24, NodeToken n25, NodeToken n26, NodeToken n27) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
      f5 = n5;
      f6 = n6;
      f7 = n7;
      f8 = n8;
      f9 = n9;
      f10 = n10;
      f11 = n11;
      f12 = n12;
      f13 = n13;
      f14 = n14;
      f15 = n15;
      f16 = n16;
      f17 = n17;
      f18 = n18;
      f19 = n19;
      f20 = n20;
      f21 = n21;
      f22 = n22;
      f23 = n23;
      f24 = n24;
      f25 = n25;
      f26 = n26;
      f27 = n27;
   }

   public MainClass(Identifier n0, Identifier n1, NodeListOptional n2, NodeListOptional n3, Identifier n4, Identifier n5) {
      f0 = new NodeToken("class");
      f1 = n0;
      f2 = new NodeToken("{");
      f3 = new NodeToken("public");
      f4 = new NodeToken("static");
      f5 = new NodeToken("void");
      f6 = new NodeToken("main");
      f7 = new NodeToken("(");
      f8 = new NodeToken("String");
      f9 = new NodeToken("[");
      f10 = new NodeToken("]");
      f11 = n1;
      f12 = new NodeToken(")");
      f13 = new NodeToken("{");
      f14 = new NodeToken("try");
      f15 = new NodeToken("{");
      f16 = n2;
      f17 = n3;
      f18 = new NodeToken("}");
      f19 = new NodeToken("catch");
      f20 = new NodeToken("(");
      f21 = n4;
      f22 = n5;
      f23 = new NodeToken(")");
      f24 = new NodeToken("{");
      f25 = new NodeToken("}");
      f26 = new NodeToken("}");
      f27 = new NodeToken("}");
   }

   public void accept(visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}
