/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2002-2003  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openscience.cdk.iupac.generator.sectiona;

import org.openscience.cdk.iupac.generator.*;
import org.openscience.cdk.iupac.generator.tools.*;
import org.openscience.cdk.*;
import org.openscience.cdk.exception.*;
import org.openscience.cdk.isomorphism.IsomorphismTester;
import org.openscience.cdk.templates.saturatedhydrocarbons.*;
import org.openscience.cdk.layout.AtomPlacer;

/**
 *  This class implements IUPAC rule 2.1 in Section A.
 *
 * @cdkPackage experimental
 *
 * @author Egon Willighagen
 */
public class Rule2dot1 extends NamingRule {

    NumberingRule numberingRule = null;

    public String getName() {
        return "A-2.1";
    }

    public NumberingRule getNumberingRule() {
        return numberingRule;
    }

    public IUPACNamePart apply(AtomContainer m) {
        IUPACNamePart inp = null;
        /* structure may not have valencies */
        if (m instanceof Molecule) {
            Molecule isobutane = IsoAlkanes.getIsobutane();
            Molecule isopentane = IsoAlkanes.getIsopentane();
            Molecule isohexane = IsoAlkanes.getIsohexane();
            // check if molecule is CH only
            if (((((Integer)m.getProperty(ELEMENT_COUNT)).intValue() == 2) &&
                (((Integer)m.getProperty(CARBON_COUNT)).intValue() > 0) &&
                (((Integer)m.getProperty(HYDROGEN_COUNT)).intValue() > 0)) ||
                ((((Integer)m.getProperty(ELEMENT_COUNT)).intValue() == 1) &&
                (((Integer)m.getProperty(CARBON_COUNT)).intValue() > 0))) {
                try {
                    IsomorphismTester it = new IsomorphismTester(new Molecule(m));
                    m.setProperty(COMPLETED_FLAG, "yes");
                    if (it.isIsomorphic(isobutane)) {
                        inp = new IUPACNamePart(localize("isobutane"), this);
                    } else if (it.isIsomorphic(isopentane)) {
                        inp = new IUPACNamePart(localize("isopentane"), this);
                    } else if (it.isIsomorphic(isohexane)) {
                        inp = new IUPACNamePart(localize("isohexane"), this);
                    } else {
                        m.setProperty(COMPLETED_FLAG, "no");
                    };
                } catch (NoSuchAtomException e) {
                    // do nothing special
                }
            }

            /* Is molecule named? If not, it may be a
            * branched alkane compound
            */
            if (m.getProperty(COMPLETED_FLAG).equals("yes")) {
                return inp;
            } else {
                // then, the main chain needs numbering
                numberingRule = new NumberingRule(this);
            }

            AtomPlacer ap = new AtomPlacer();
            // determine longest C-C chain
            try {
                AtomContainer copy = new AtomContainer();
//                System.err.println(" m: " + m);
                copy.add(m);
//                System.err.println("cp: " + copy);
                AtomContainer longestChain = ap.getInitialLongestChain(
                    new Molecule(deleteNonCarbonAtoms(copy))
                );
//                System.err.println("AC: " + longestChain);

                int length = longestChain.getAtomCount();
                m.setProperty(COMPLETED_FLAG, "no");
                String name = CarbonChainNames.getName(length);
                if (name != null) {
                    inp = new IUPACNamePart(name + localize("ane"), this);
                    // mark named atoms
                    for (int i = 0; i < length; i++) {
                        longestChain.getAtomAt(i).setProperty(ATOM_NAMED_FLAG, "yes");
                        longestChain.getAtomAt(i).setProperty(ATOM_MUST_BE_NUMBERED_FLAG, "yes");
                    }
                }
            } catch (CDKException e) {
                System.err.println(e.toString());
                e.printStackTrace(System.err);
            }
        }
        return inp;
    };

    private AtomContainer deleteNonCarbonAtoms(AtomContainer ac) throws NoSuchAtomException {
        AtomContainer result = (AtomContainer)ac.clone();
//        System.out.println("Deleting non carbon atoms...");
        Atom[] atoms = ac.getAtoms();
        for (int i=0; i < atoms.length; i++) {
            Atom atom = atoms[i];
            if (!"C".equals(atom.getSymbol())) {
//                System.out.println("  deleting: " + atom.getSymbol());
                ac.removeAtomAndConnectedElectronContainers(atom);
            } else {
//                System.out.println("   keeping: " + atom.getSymbol());
            }
        }
        return result;
    }
}
