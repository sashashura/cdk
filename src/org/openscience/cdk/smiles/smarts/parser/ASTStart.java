/* Generated By:JJTree: Do not edit this line. ASTStart.java */

package org.openscience.cdk.smiles.smarts.parser;

public class ASTStart extends SimpleNode {
  public ASTStart(int id) {
    super(id);
  }

  public ASTStart(SMARTSParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
