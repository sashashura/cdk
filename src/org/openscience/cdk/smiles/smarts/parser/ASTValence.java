/* Generated By:JJTree: Do not edit this line. ASTValence.java */

package org.openscience.cdk.smiles.smarts.parser;

public class ASTValence extends SimpleNode {
    private int order;

    public ASTValence(int id) {
        super(id);
    }

    public ASTValence(SMARTSParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. * */
    public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
