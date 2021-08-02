//
// Generated by JTB 1.3.2
//

package syntaxtree;

/**
 * Grammar production:
 * f0 -> "public"
 * f1 -> "void"
 * f2 -> "run"
 * f3 -> "("
 * f4 -> ")"
 * f5 -> "{"
 * f6 -> "try"
 * f7 -> "{"
 * f8 -> ( VarDeclaration() )*
 * f9 -> ( QParStatement() )*
 * f10 -> "}"
 * f11 -> "catch"
 * f12 -> "("
 * f13 -> Identifier()
 * f14 -> Identifier()
 * f15 -> ")"
 * f16 -> "{"
 * f17 -> "}"
 * f18 -> "}"
 */
public class MethodDeclaration implements Node {
   public NodeToken f0;
   public NodeToken f1;
   public NodeToken f2;
   public NodeToken f3;
   public NodeToken f4;
   public NodeToken f5;
   public NodeToken f6;
   public NodeToken f7;
   public NodeListOptional f8;
   public NodeListOptional f9;
   public NodeToken f10;
   public NodeToken f11;
   public NodeToken f12;
   public Identifier f13;
   public Identifier f14;
   public NodeToken f15;
   public NodeToken f16;
   public NodeToken f17;
   public NodeToken f18;

   public MethodDeclaration(NodeToken n0, NodeToken n1, NodeToken n2, NodeToken n3, NodeToken n4, NodeToken n5, NodeToken n6, NodeToken n7, NodeListOptional n8, NodeListOptional n9, NodeToken n10, NodeToken n11, NodeToken n12, Identifier n13, Identifier n14, NodeToken n15, NodeToken n16, NodeToken n17, NodeToken n18) {
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
   }

   public MethodDeclaration(NodeListOptional n0, NodeListOptional n1, Identifier n2, Identifier n3) {
      f0 = new NodeToken("public");
      f1 = new NodeToken("void");
      f2 = new NodeToken("run");
      f3 = new NodeToken("(");
      f4 = new NodeToken(")");
      f5 = new NodeToken("{");
      f6 = new NodeToken("try");
      f7 = new NodeToken("{");
      f8 = n0;
      f9 = n1;
      f10 = new NodeToken("}");
      f11 = new NodeToken("catch");
      f12 = new NodeToken("(");
      f13 = n2;
      f14 = n3;
      f15 = new NodeToken(")");
      f16 = new NodeToken("{");
      f17 = new NodeToken("}");
      f18 = new NodeToken("}");
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

