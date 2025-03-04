package org.openscience.cdk.graph;

import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.TestMoleculeFactory;

import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openscience.cdk.templates.TestMoleculeFactory.makeAnthracene;
import static org.openscience.cdk.templates.TestMoleculeFactory.makeBicycloRings;
import static org.openscience.cdk.templates.TestMoleculeFactory.makeBiphenyl;
import static org.openscience.cdk.templates.TestMoleculeFactory.makeCyclophaneLike;
import static org.openscience.cdk.templates.TestMoleculeFactory.makeGappedCyclophaneLike;
import static org.openscience.cdk.templates.TestMoleculeFactory.makeNaphthalene;

/**
 * Note - these methods are tested in isolation in their respective classes and
 * these are mainly to keep the coverage checker happy.
 *
 * @author John May
 * @cdk.module test-core
 */
public class CyclesTest {

    @Test
    public void all() throws Exception {
        checkSize(Cycles.all(makeBiphenyl()), 2);
        checkSize(Cycles.all(makeBicycloRings()), 3);
        checkSize(Cycles.all(makeNaphthalene()), 3);
        checkSize(Cycles.all(makeAnthracene()), 6);
        checkSize(Cycles.all(makeCyclophaneLike()), 135);
        checkSize(Cycles.all(makeGappedCyclophaneLike()), 135);
    }

    @Test
    public void mcb() throws Exception {
        checkSize(Cycles.mcb(makeBiphenyl()), 2);
        checkSize(Cycles.mcb(makeBicycloRings()), 2);
        checkSize(Cycles.mcb(makeNaphthalene()), 2);
        checkSize(Cycles.mcb(makeAnthracene()), 3);
        checkSize(Cycles.mcb(makeCyclophaneLike()), 8);
        checkSize(Cycles.mcb(makeGappedCyclophaneLike()), 8);
    }

    @Test
    public void relevant() throws Exception {
        checkSize(Cycles.relevant(makeBiphenyl()), 2);
        checkSize(Cycles.relevant(makeBicycloRings()), 3);
        checkSize(Cycles.relevant(makeNaphthalene()), 2);
        checkSize(Cycles.relevant(makeAnthracene()), 3);
        checkSize(Cycles.relevant(makeCyclophaneLike()), 135);
        checkSize(Cycles.relevant(makeGappedCyclophaneLike()), 135);
    }

    @Test
    public void essential() throws Exception {
        checkSize(Cycles.essential(makeBiphenyl()), 2);
        checkSize(Cycles.essential(makeBicycloRings()), 0);
        checkSize(Cycles.essential(makeNaphthalene()), 2);
        checkSize(Cycles.essential(makeAnthracene()), 3);
        checkSize(Cycles.essential(makeCyclophaneLike()), 7);
    }

    @Test
    public void tripletShort() throws Exception {
        checkSize(Cycles.tripletShort(makeBiphenyl()), 2);
        checkSize(Cycles.tripletShort(makeBicycloRings()), 3);
        checkSize(Cycles.tripletShort(makeNaphthalene()), 3);
        checkSize(Cycles.tripletShort(makeAnthracene()), 5);
        checkSize(Cycles.tripletShort(makeCyclophaneLike()), 135);
        checkSize(Cycles.tripletShort(makeGappedCyclophaneLike()), 135);
    }

    @Test
    public void edgeShort() throws Exception {
        checkSize(Cycles.edgeShort(makeBiphenyl()), 2);
        checkSize(Cycles.edgeShort(makeBicycloRings()), 3);
        checkSize(Cycles.edgeShort(makeNaphthalene()), 2);
        checkSize(Cycles.edgeShort(makeAnthracene()), 3);
        checkSize(Cycles.edgeShort(makeCyclophaneLike()), 7);
        checkSize(Cycles.edgeShort(makeGappedCyclophaneLike()), 135);
    }

    @Test
    public void vertexShort() throws Exception {
        checkSize(Cycles.vertexShort(makeBiphenyl()), 2);
        checkSize(Cycles.vertexShort(makeBicycloRings()), 3);
        checkSize(Cycles.vertexShort(makeNaphthalene()), 2);
        checkSize(Cycles.vertexShort(makeAnthracene()), 3);
        checkSize(Cycles.vertexShort(makeCyclophaneLike()), 7);
        checkSize(Cycles.vertexShort(makeGappedCyclophaneLike()), 7);
    }

    @Test
    public void cdkAromaticSet() throws Exception {
        checkSize(Cycles.cdkAromaticSet().find(makeBiphenyl()), 2);
        checkSize(Cycles.cdkAromaticSet().find(makeBicycloRings()), 3);
        checkSize(Cycles.cdkAromaticSet().find(makeNaphthalene()), 3);
        checkSize(Cycles.cdkAromaticSet().find(makeAnthracene()), 6);
        checkSize(Cycles.cdkAromaticSet().find(makeCyclophaneLike()), 8);
        checkSize(Cycles.cdkAromaticSet().find(makeGappedCyclophaneLike()), 8);
    }

    @Test
    public void allOrVertexShort() throws Exception {
        checkSize(Cycles.allOrVertexShort().find(makeBiphenyl()), 2);
        checkSize(Cycles.allOrVertexShort().find(makeBicycloRings()), 3);
        checkSize(Cycles.allOrVertexShort().find(makeNaphthalene()), 3);
        checkSize(Cycles.allOrVertexShort().find(makeAnthracene()), 6);
        checkSize(Cycles.allOrVertexShort().find(makeCyclophaneLike()), 135);
        checkSize(Cycles.allOrVertexShort().find(makeGappedCyclophaneLike()), 135);
        checkSize(Cycles.allOrVertexShort().find(fullerene()), 120);
    }

    @Test
    public void cdkAromaticSet_withGraph() throws Exception {
        checkSize(Cycles.cdkAromaticSet().find(makeBiphenyl(), GraphUtil.toAdjList(makeBiphenyl()), Integer.MAX_VALUE),
                2);
        checkSize(
                Cycles.cdkAromaticSet().find(makeBicycloRings(), GraphUtil.toAdjList(makeBicycloRings()),
                        Integer.MAX_VALUE), 3);
        checkSize(
                Cycles.cdkAromaticSet().find(makeNaphthalene(), GraphUtil.toAdjList(makeNaphthalene()),
                        Integer.MAX_VALUE), 3);
        checkSize(
                Cycles.cdkAromaticSet()
                        .find(makeAnthracene(), GraphUtil.toAdjList(makeAnthracene()), Integer.MAX_VALUE), 6);
        checkSize(
                Cycles.cdkAromaticSet().find(makeCyclophaneLike(), GraphUtil.toAdjList(makeCyclophaneLike()),
                        Integer.MAX_VALUE), 8);
        checkSize(
                Cycles.cdkAromaticSet().find(makeGappedCyclophaneLike(),
                        GraphUtil.toAdjList(makeGappedCyclophaneLike()), Integer.MAX_VALUE), 8);
    }

    @Test
    public void allOrVertexShort_withGraph() throws Exception {
        checkSize(Cycles.allOrVertexShort()
                .find(makeBiphenyl(), GraphUtil.toAdjList(makeBiphenyl()), Integer.MAX_VALUE), 2);
        checkSize(
                Cycles.allOrVertexShort().find(makeBicycloRings(), GraphUtil.toAdjList(makeBicycloRings()),
                        Integer.MAX_VALUE), 3);
        checkSize(
                Cycles.allOrVertexShort().find(makeNaphthalene(), GraphUtil.toAdjList(makeNaphthalene()),
                        Integer.MAX_VALUE), 3);
        checkSize(
                Cycles.allOrVertexShort().find(makeAnthracene(), GraphUtil.toAdjList(makeAnthracene()),
                        Integer.MAX_VALUE), 6);
        checkSize(
                Cycles.allOrVertexShort().find(makeCyclophaneLike(), GraphUtil.toAdjList(makeCyclophaneLike()),
                        Integer.MAX_VALUE), 135);
        checkSize(
                Cycles.allOrVertexShort().find(makeGappedCyclophaneLike(),
                        GraphUtil.toAdjList(makeGappedCyclophaneLike()), Integer.MAX_VALUE), 135);
        checkSize(Cycles.allOrVertexShort().find(fullerene(), GraphUtil.toAdjList(fullerene()), Integer.MAX_VALUE), 120);
    }

    @Test
    public void allUpToLength() throws Exception {
        checkSize(Cycles.all(6).find(makeBiphenyl(), GraphUtil.toAdjList(makeBiphenyl()), Integer.MAX_VALUE), 2);
        checkSize(Cycles.all(6).find(makeBicycloRings(), GraphUtil.toAdjList(makeBicycloRings()), Integer.MAX_VALUE), 3);
        checkSize(Cycles.all(6).find(makeNaphthalene(), GraphUtil.toAdjList(makeNaphthalene()), Integer.MAX_VALUE), 2);
        checkSize(Cycles.all(6).find(makeAnthracene(), GraphUtil.toAdjList(makeAnthracene()), Integer.MAX_VALUE), 3);
    }

    @Test
    public void pathsAreCopy() throws Exception {
        Cycles cs = Cycles.all(makeAnthracene());
        int[][] org = cs.paths();
        org[0][0] = -203; // modify
        assertThat(org, is(not(cs.paths()))); // internal is unchanged
    }

    @Test
    public void toRingSet() throws Exception {
        IAtomContainer biphenyl = makeBiphenyl();
        IRingSet rs = Cycles.vertexShort(biphenyl).toRingSet();
        Iterator<IAtomContainer> it = rs.atomContainers().iterator();
        assertTrue(it.hasNext());
        IAtomContainer r1 = it.next();

        assertThat(r1.getAtom(0), is(biphenyl.getAtom(0)));
        assertThat(r1.getAtom(1), is(biphenyl.getAtom(1)));
        assertThat(r1.getAtom(2), is(biphenyl.getAtom(2)));
        assertThat(r1.getAtom(3), is(biphenyl.getAtom(3)));
        assertThat(r1.getAtom(4), is(biphenyl.getAtom(4)));
        assertThat(r1.getAtom(5), is(biphenyl.getAtom(5)));

        assertThat(r1.getBond(0), is(biphenyl.getBond(0)));
        assertThat(r1.getBond(1), is(biphenyl.getBond(1)));
        assertThat(r1.getBond(2), is(biphenyl.getBond(2)));
        assertThat(r1.getBond(3), is(biphenyl.getBond(3)));
        assertThat(r1.getBond(4), is(biphenyl.getBond(4)));
        assertThat(r1.getBond(5), is(biphenyl.getBond(5)));

        assertTrue(it.hasNext());
        IAtomContainer r2 = it.next();

        assertThat(r2.getAtom(0), is(biphenyl.getAtom(6)));
        assertThat(r2.getAtom(1), is(biphenyl.getAtom(7)));
        assertThat(r2.getAtom(2), is(biphenyl.getAtom(8)));
        assertThat(r2.getAtom(3), is(biphenyl.getAtom(9)));
        assertThat(r2.getAtom(4), is(biphenyl.getAtom(10)));
        assertThat(r2.getAtom(5), is(biphenyl.getAtom(11)));

        assertThat(r2.getBond(0), is(biphenyl.getBond(7)));
        assertThat(r2.getBond(1), is(biphenyl.getBond(8)));
        assertThat(r2.getBond(2), is(biphenyl.getBond(9)));
        assertThat(r2.getBond(3), is(biphenyl.getBond(10)));
        assertThat(r2.getBond(4), is(biphenyl.getBond(11)));
        assertThat(r2.getBond(5), is(biphenyl.getBond(12)));
    }

    @Test
    public void markAtomsAndBonds() throws Exception {
        IAtomContainer biphenyl = makeBiphenyl();
        Cycles.markRingAtomsAndBonds(biphenyl);
        int cyclicAtoms = 0;
        int cyclicBonds = 0;
        for (IAtom atom : biphenyl.atoms()) {
            if (atom.isInRing())
                cyclicAtoms++;
        }
        for (IBond bond : biphenyl.bonds()) {
            if (bond.isInRing())
                cyclicBonds++;
        }
        assertThat(cyclicAtoms, is(biphenyl.getAtomCount()));
        assertThat(cyclicBonds, is(biphenyl.getBondCount()-1));
    }

    @Test
    public void or() throws Exception {
        CycleFinder cf = Cycles.or(Cycles.all(), Cycles.all(3));
        IAtomContainer fullerene = fullerene();
        checkSize(cf.find(fullerene, fullerene.getAtomCount()), 120);
    }

    @Test
    public void unchorded() throws Exception {
        IAtomContainer container = TestMoleculeFactory.makeAnthracene();
        checkSize(Cycles.unchorded(Cycles.all()).find(container), 3);
    }

    // load a boron fullerene
    private IAtomContainer fullerene() throws Exception {
        String path = "boronBuckyBall.mol";
        MDLV2000Reader mdl = new MDLV2000Reader(getClass().getResourceAsStream(path));
        try {
            return mdl.read(new AtomContainer());
        } finally {
            mdl.close();
        }
    }

    static void checkSize(Cycles cs, int nCycles) {
        assertThat(cs.numberOfCycles(), is(nCycles));
    }

    private static IAtomContainer loadSmiles(String smi) throws InvalidSmilesException {
        SmilesParser smipar = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer mol = smipar.parseSmiles(smi);
        return mol;
    }

    private static void assertRingSizes(String smi, int ... expected) throws InvalidSmilesException {
        SmilesParser smipar = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer mol = smipar.parseSmiles(smi);
        Cycles.markRingAtomsAndBonds(mol);
        int[] actual = new int[mol.getAtomCount()];
        Cycles.smallRingSizes(mol, actual);
        assertThat(Arrays.toString(actual), actual, is(expected));
    }

    @Test
    public void testSmallRings() throws InvalidSmilesException {
        assertRingSizes("C1CCCCC1", 6, 6, 6, 6, 6, 6);
        assertRingSizes("C1CCCC1", 5, 5, 5, 5, 5);
        // spiro
        assertRingSizes("C1CCCCC11CCCC1", 6, 6, 6, 6, 6, 5, 5, 5, 5, 5);
        // fused
        assertRingSizes("CCC12CCC2CCCC1", 0, 0, 4, 4, 4, 4, 6, 6, 6, 6);
        assertRingSizes("[nH]1ccc2c1cccc2", 5, 5, 5, 5, 5, 6, 6, 6, 6);
        // compound
        assertRingSizes("Clc4cccc(N3CCN(CCCCOc2ccc1c(NC(=O)CC1)c2)CC3)c4Cl",
                0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0);
    }

    @Test
    public void testSmallRing_atom_simple() throws InvalidSmilesException {
        IAtomContainer mol = loadSmiles("C1CCCCC1");
        Cycles.markRingAtomsAndBonds(mol); // required
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(0)));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(2)));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(5)));

        assertEquals(0,Cycles.smallRingSize(mol.getAtom(0), 4));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(2), 4));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(5), 4));

        assertEquals(6,Cycles.smallRingSize(mol.getAtom(0), 6));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(2), 6));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(5), 6));

        assertEquals(6,Cycles.smallRingSize(mol.getAtom(0), 7));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(2), 7));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(5), 7));
    }

    @Test
    public void testSmallRing_atom_acyclic() throws InvalidSmilesException {
        IAtomContainer mol = loadSmiles("CCCC");
        Cycles.markRingAtomsAndBonds(mol); // required
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(0)));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(1)));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(2)));
    }

    @Test
    public void testSmallRing_atom_indole() throws InvalidSmilesException {
        IAtomContainer mol = loadSmiles("[nH]1ccc2c1cccc2");
        Cycles.markRingAtomsAndBonds(mol); // required
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(0)));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(1)));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(2)));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(3)));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(4)));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(5)));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(6)));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(7)));
        assertEquals(6,Cycles.smallRingSize(mol.getAtom(8)));
    }

    @Test
    public void testSmallRing_atom_indole_lim() throws InvalidSmilesException {
        IAtomContainer mol = loadSmiles("[nH]1ccc2c1cccc2");
        Cycles.markRingAtomsAndBonds(mol); // required
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(0), 5));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(1), 5));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(2), 5));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(3), 5));
        assertEquals(5,Cycles.smallRingSize(mol.getAtom(4), 5));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(5), 5));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(6), 5));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(7), 5));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(8), 5));
        // limit = 4
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(0), 4));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(1), 4));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(2), 4));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(3), 4));
        assertEquals(0,Cycles.smallRingSize(mol.getAtom(4), 4));
    }

    @Test
    public void testSmallRing_bond_indole() throws InvalidSmilesException {
        IAtomContainer mol = loadSmiles("[nH]1ccc2c1cccc2");
        Cycles.markRingAtomsAndBonds(mol); // required
        assertEquals(5,Cycles.smallRingSize(mol.getBond(0)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(1)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(2)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(3)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(4)));
        assertEquals(6,Cycles.smallRingSize(mol.getBond(5)));
        assertEquals(6,Cycles.smallRingSize(mol.getBond(6)));
        assertEquals(6,Cycles.smallRingSize(mol.getBond(7)));
        assertEquals(6,Cycles.smallRingSize(mol.getBond(8)));
    }

    @Test
    public void testSmallRing_bond_spiro() throws InvalidSmilesException {
        IAtomContainer mol = loadSmiles("C1CCCC11CCC1");
        Cycles.markRingAtomsAndBonds(mol); // required
        assertEquals(5,Cycles.smallRingSize(mol.getBond(0)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(1)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(2)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(3)));
        assertEquals(5,Cycles.smallRingSize(mol.getBond(4)));
        assertEquals(4,Cycles.smallRingSize(mol.getBond(5)));
        assertEquals(4,Cycles.smallRingSize(mol.getBond(6)));
        assertEquals(4,Cycles.smallRingSize(mol.getBond(7)));
        assertEquals(4,Cycles.smallRingSize(mol.getBond(8)));
        // limit=4
        assertEquals(0,Cycles.smallRingSize(mol.getBond(3),4));
        assertEquals(4,Cycles.smallRingSize(mol.getBond(7),4));
        // limit=3
        assertEquals(0,Cycles.smallRingSize(mol.getBond(3),3));
        assertEquals(0,Cycles.smallRingSize(mol.getBond(7),3));
    }
}
