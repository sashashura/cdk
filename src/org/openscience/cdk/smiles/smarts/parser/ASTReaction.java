/* Generated By:JJTree: Do not edit this line. ASTReaction.java */

package org.openscience.cdk.smiles.smarts.parser;

public class ASTReaction extends SimpleNode {
  public ASTReaction(int id) {
    super(id);
  }

  public ASTReaction(SMARTSParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
