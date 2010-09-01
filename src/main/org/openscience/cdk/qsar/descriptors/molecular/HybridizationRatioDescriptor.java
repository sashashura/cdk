/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2004-2007  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.qsar.descriptors.molecular;

import org.openscience.cdk.AtomType;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * IDescriptor that reports the fraction of sp3 carbons to sp2 carbons.
 * <p/>
 * Note that it only considers carbon atoms and rather than use a simple ratio
 * it reports the value of N<sub>sp3</sub>/ (N<sub>sp3</sub> + N<sub>sp2</sub>).
 * The original form of the descriptor (i.e., simple ratio) has been used to
 * characterize molecular complexity, especially in the are of natural products
 * , which usually have a high value of the sp3 to sp2 ratio.
 *
 * @author Rajarshi Guha
 * @cdk.module qsarmolecular
 * @cdk.githash
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:hybratio
 */
@TestClass("org.openscience.cdk.qsar.descriptors.molecular.HybridizationRatioDescriptorTest")
public class HybridizationRatioDescriptor implements IMolecularDescriptor {

    /**
     * Constructor for the WeightDescriptor object.
     */
    public HybridizationRatioDescriptor() {
    }

    /**
     * Returns a <code>Map</code> which specifies which descriptor is implemented by this class.
     * <p/>
     * These fields are used in the map:
     * <ul>
     * <li>Specification-Reference: refers to an entry in a unique dictionary
     * <li>Implementation-Title: anything
     * <li>Implementation-Identifier: a unique identifier for this version of
     * this class
     * <li>Implementation-Vendor: CDK, JOELib, or anything else
     * </ul>
     *
     * @return An object containing the descriptor specification
     */
    @TestMethod("testGetSpecification")
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#hybratio",
                this.getClass().getName(),
                "$Id$",
                "The Chemistry Development Kit");
    }

    /**
     * Sets the parameters attribute of the WeightDescriptor object.
     *
     * @param params The new parameters value
     * @throws org.openscience.cdk.exception.CDKException
     *          if more than 1 parameter is specified or if the parameter
     *          is not of type String
     * @see #getParameters
     */
    @TestMethod("testSetParameters_arrayObject")
    public void setParameters(Object[] params) throws CDKException {
    }


    /**
     * Gets the parameters attribute of the WeightDescriptor object.
     *
     * @return The parameters value
     * @see #setParameters
     */
    @TestMethod("testGetParameters")
    public Object[] getParameters() {
        return new Object[0];
    }

    @TestMethod(value = "testNamesConsistency")
    public String[] getDescriptorNames() {
        return new String[]{"HybRatio"};
    }

    private DescriptorValue getDummyDescriptorValue(Exception e) {
        return new DescriptorValue(getSpecification(), getParameterNames(),
                getParameters(), new DoubleResult(Double.NaN), getDescriptorNames(), e);
    }


    /**
     * Calculate the weight of specified element type in the supplied {@link org.openscience.cdk.interfaces.IAtomContainer}.
     *
     * @param container The AtomContainer for which this descriptor is to be calculated.
     * @return The total weight of atoms of the specified element type
     */
    @TestMethod("testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtomContainer container) {
        try {
            IAtomContainer clone = (IAtomContainer) container.clone();
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(clone);
            int nsp2 = 0;
            int nsp3 = 0;
            for (IAtom atom : clone.atoms()) {
                if (!atom.getSymbol().equals("C")) continue;
                if (atom.getHybridization() == AtomType.Hybridization.SP2) nsp2++;
                else if (atom.getHybridization() == AtomType.Hybridization.SP3) nsp3++;
            }
            double ratio = nsp3 / (double) (nsp2 + nsp3);
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                new DoubleResult(ratio), getDescriptorNames());
        } catch (CloneNotSupportedException e) {
            return getDummyDescriptorValue(e);
        } catch (CDKException e) {
            return getDummyDescriptorValue(e);
        }       
    }

    /**
     * Returns the specific type of the DescriptorResult object.
     * <p/>
     * The return value from this method really indicates what type of result will
     * be obtained from the {@link org.openscience.cdk.qsar.DescriptorValue} object. Note that the same result
     * can be achieved by interrogating the {@link org.openscience.cdk.qsar.DescriptorValue} object; this method
     * allows you to do the same thing, without actually calculating the descriptor.
     *
     * @return an object that implements the {@link org.openscience.cdk.qsar.result.IDescriptorResult} interface indicating
     *         the actual type of values returned by the descriptor in the {@link org.openscience.cdk.qsar.DescriptorValue} object
     */
    @TestMethod("testGetDescriptorResultType")
    public IDescriptorResult getDescriptorResultType() {
        return new DoubleResult(0.0);
    }


    /**
     * Gets the parameterNames attribute of the WeightDescriptor object.
     *
     * @return The parameterNames value
     */
    @TestMethod("testGetParameterNames")
    public String[] getParameterNames() {
        return new String[0];
    }


    /**
     * Gets the parameterType attribute of the HybridizationRatioDescriptor object.
     *
     * @param name Description of the Parameter
     * @return An Object whose class is that of the parameter requested
     */
    @TestMethod("testGetParameterType_String")
    public Object getParameterType(String name) {
        return "";
    }
}