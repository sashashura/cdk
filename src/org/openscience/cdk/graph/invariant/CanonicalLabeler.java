/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2001-2004  The Chemistry Development Kit (CDK) Project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  */
package org.openscience.cdk.graph.invariant;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.smiles.InvPair;

/**
 * Canonically lables an atom container implementing
 * the algorithm published in David Weininger et.al. [WEI89].
 * The Collections.sort() method uses a merge sort which is 
 * stable and runs in n log(n).
 *
 * <p>References:
 *   <a href="http://cdk.sf.net/biblio.html#WEI89">WEI89</a>
 *
 * @cdkPackage standard
 *
 * @author   Oliver Horlacher <oliver.horlacher@therastrat.com>
 * @created  2002-02-26
 *
 * @keyword canonicalization
 */
public class CanonicalLabeler {

  public CanonicalLabeler() {
  }

  /**
   * Canonicaly label the fragment.
   * This is an implementation of the algorithm published in
   * David Weininger et.al. (see WEI89).
   * 
   * <p>The Collections.sort() method uses a merge sort which is stable and runs in n log(n).
   * 
   * <p>It is assumed that a chemicaly valid AtomContainer is provided: this method does not check
   * the correctness of the AtomContainer.  Negative H counts will cause a NumberFormatException
   * to be thrown.
   *
   * References:
   *   <a href="http://cdk.sf.net/biblio.html#WEI89">WEI89</a>
   */
  public synchronized void canonLabel(AtomContainer atomContainer) {
    if (atomContainer.getAtomCount() == 0)
      return;
    Vector vect = createInvarLabel(atomContainer);
    step3(vect, atomContainer);
  }

  /**
   * @param v the invariance pair vector
   */
  private void step2(Vector v, AtomContainer atoms) {
    primeProduct(v, atoms);
    step3(v, atoms);
  }

  /**
   * @param v the invariance pair vector
   */
  private void step3(Vector v, AtomContainer atoms) {
    sortVector(v);
    step4(v, atoms);
  }

  /**
   * @param v the invariance pair vector
   */
  private void step4(Vector v, AtomContainer atoms) {
    rankVector(v);
    step5(v, atoms);
  }

  /**
   * @param v the invariance pair vector
   */
  private void step5(Vector v, AtomContainer atoms) {
    if (!isInvPart(v))
      step2(v, atoms);
    else
      step6(v, atoms);
  }

  /**
   * @param v the invariance pair vector
   */
  private void step6(Vector v, AtomContainer atoms) {
    //On first pass save, partitioning as symmetry classes.
    step7(v, atoms);
  }

  /**
   * @param v the invariance pair vector
   */
  private void step7(Vector v, AtomContainer atoms) {
    if (((InvPair) v.lastElement()).getCurr() < v.size()) {
      breakTies(v);
      step2(v, atoms);
    }
    Iterator it = v.iterator();
    while (it.hasNext()) {
      ((InvPair) it.next()).comit();
    }
  }

  /**
   * Create initial invariant labeling corresponds to step 1
   *
   * @return Vector containting the
   */
  private Vector createInvarLabel(AtomContainer atomContainer) {
    Atom[]  atoms = atomContainer.getAtoms();
    Atom a;
    StringBuffer inv;
    Vector vect = new Vector();
    for(int x = 0; x < atoms.length; x++) {
      a = atoms[x];
      inv = new StringBuffer();
      inv.append(atomContainer.getConnectedAtoms(a).length + a.getHydrogenCount()); //Num connections
      inv.append(atomContainer.getConnectedAtoms(a).length);                        //Num of non H bonds
      inv.append(a.getAtomicNumber());                                              //Atomic number
      if (a.getCharge() < 0)                                                        //Sign of charge
        inv.append(1);
      else
        inv.append(0);                                                              //Absolute charge
      inv.append((int)Math.abs(a.getCharge()));                                     //Hydrogen count
      inv.append(a.getHydrogenCount());
      vect.add(new InvPair(Long.parseLong(inv.toString()), a));
    }
    return vect;
  }

  /**
   * Calculates the product of the neighbouring primes.
   *
   * @param v the invariance pair vector
   */
  private void primeProduct(Vector v, AtomContainer atomContainer) {
    Iterator it = v.iterator();
    Iterator n;
    InvPair inv, curr;
    Atom a;
    long summ;
    while (it.hasNext()) {
      inv = (InvPair) it.next();
      Vector neighbour = atomContainer.getConnectedAtomsVector(inv.getAtom());
      n = neighbour.iterator();
      summ = 1;
      while (n.hasNext()) {
        a = (Atom) n.next();
        int next = ((InvPair)a.getProperty(InvPair.INVARIANCE_PAIR)).getPrime();
        summ = summ * next;
      }
      inv.setLast(inv.getCurr());
      inv.setCurr(summ);
    }
  }

  /**
   * Sorts the vector according to the current invariance, corresponds to step 3
   *
   * @param v the invariance pair vector
   * @todo    can this be done in one loop?
   */
  private void sortVector(Vector v) {
    Collections.sort(v, new Comparator() {
      public int compare(Object o1, Object o2) {
        return (int) (((InvPair) o1).getCurr() - ((InvPair) o2).getCurr());
      }
    });
    Collections.sort(v, new Comparator() {
      public int compare(Object o1, Object o2) {
        return (int) (((InvPair) o1).getLast() - ((InvPair) o2).getLast());
      }
    });
  }

  /**
   * Rank atomic vector, corresponds to step 4.
   *
   *  @param v the invariance pair vector
   */
  private void rankVector(Vector v) {
    int num = 1;
    int[] temp = new int[v.size()];
    InvPair last = (InvPair) v.firstElement();
    Iterator it = v.iterator();
    InvPair curr;
    for (int x = 0; it.hasNext(); x++) {
      curr = (InvPair) it.next();
      if (!last.equals(curr)) {
        num++;
      }
      temp[x] = num;
      last = curr;
    }
    it = v.iterator();
    for (int x = 0; it.hasNext(); x++) {
      curr = (InvPair) it.next();
      curr.setCurr(temp[x]);
      curr.setPrime();
    }
  }

  /**
   * Checks to see if the vector is invariantely partitioned
   *
   * @param v the invariance pair vector
   * @return true if the vector is invariantely partitioned, false otherwise
   */
  private boolean isInvPart(Vector v) {
    if (((InvPair) v.lastElement()).getCurr() == v.size())
      return true;
    Iterator it = v.iterator();
    InvPair curr;
    while (it.hasNext()) {
      curr = (InvPair) it.next();
      if (curr.getCurr() != curr.getLast())
        return false;
    }
    return true;
  }

  /**
   * Break ties. Corresponds to step 7
   *
   * @param v the invariance pair vector
   */
  private void breakTies(Vector v) {
    Iterator it = v.iterator();
    InvPair curr;
    InvPair last = null;
    int tie = 0;
    boolean found = false;
    for (int x = 0; it.hasNext(); x++) {
      curr = (InvPair) it.next();
      curr.setCurr(curr.getCurr() * 2);
      curr.setPrime();
      if (x != 0 && !found && curr.getCurr() == last.getCurr()) {
        tie = x - 1;
        found = true;
      }
      last = curr;
    }
    curr = (InvPair) v.elementAt(tie);
    curr.setCurr(curr.getCurr() - 1);
    curr.setPrime();
  }
}
