/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003-2005  The JChemPaint project
 *
 * Contact: jchempaint-devel@lists.sourceforge.net
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
 */
package org.openscience.cdk.applications.jchempaint.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.openscience.jchempaint.application.JChemPaint;
import org.openscience.jchempaint.JChemPaintModel;

/**
 * Internal frame to allow for changing the propterties.
 *
 * @cdk.module jchempaint
 */
public class JChemPaintModelPropsEditor extends JFrame {
    
    Properties props;
    JChemPaint jcp;
    JChemPaintModel jcpm;

    JTextField author;
    JTextField software;
    JTextField date;
    
    public JChemPaintModelPropsEditor(JChemPaint jcp) {
        super("Edit Model Properties...");
        this.jcp = jcp;
        jcpm = jcp.getCurrentModel();

        getContentPane().setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        JButton openButton = new JButton("OK");
        openButton.addActionListener(new UpdateAction());
        cancelButton.addActionListener(new CancelAction());
        southPanel.add(openButton);
        southPanel.add(cancelButton);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(5,2));
        
        JLabel valueLabel2 = new JLabel("Author:");
        author = new JTextField(jcpm.getAuthor(), 20);
        author.addActionListener(new EditAction("author"));
        centerPanel.add(valueLabel2);
        centerPanel.add(author);
        
        JLabel valueLabel3 = new JLabel("Software:");
        software = new JTextField(jcpm.getSoftware(), 20);
        software.addActionListener(new EditAction("software"));
        centerPanel.add(valueLabel3);
        centerPanel.add(software);
        
        JLabel valueLabel4 = new JLabel("Generated on:");
        date = new JTextField(jcpm.getGendate(), 20);
        date.addActionListener(new EditAction("date"));
        centerPanel.add(valueLabel4);
        centerPanel.add(date);
        
        setSize(300, 500);
	
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", centerPanel);
        getContentPane().add("South", southPanel);
    }
    
    public void closeFrame(){
    	dispose();	
    }

    class UpdateAction extends AbstractAction {
        UpdateAction() {
            super("Update");
        }
				
        public void actionPerformed(ActionEvent e) {
            jcpm.setAuthor(author.getText());
            jcpm.setSoftware(software.getText());
            jcpm.setGendate(date.getText());
            closeFrame();
        }
    }

    class CancelAction extends AbstractAction {
        CancelAction() {
            super("Cancel");
        }
				
        public void actionPerformed(ActionEvent e) {
            closeFrame();
        }
    }

    class EditAction extends AbstractAction {
        private String prop = "";
   
        EditAction(String prop) {
            super("Edit");
            this.prop = prop;
        }

        public void actionPerformed(ActionEvent e) {
            // do not validate content
        }
    }
 }
