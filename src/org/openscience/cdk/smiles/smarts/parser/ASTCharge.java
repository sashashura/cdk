/* Generated By:JJTree: Do not edit this line. ASTCharge.java */

package org.openscience.cdk.smiles.smarts.parser;

public class ASTCharge extends SimpleNode {
    private boolean isPositive;

    private int charge;

    public ASTCharge(int id) {
        super(id);
    }

    public ASTCharge(SMARTSParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. * */
    public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }

}
