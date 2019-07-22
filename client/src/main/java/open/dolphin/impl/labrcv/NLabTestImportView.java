/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NLabTestImportView.java
 *
 * Created on 2010/02/24, 19:42:49
 */

package open.dolphin.impl.labrcv;

import java.awt.*;

/**
 *
 * @author kazushi
 */
public class NLabTestImportView extends javax.swing.JPanel {

    /** Creates new form NLabTestImportView */
    public NLabTestImportView() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        countLbl = new javax.swing.JLabel();
        fileBtn = new javax.swing.JButton();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();

        setName("Form"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("open/dolphin/impl/labrcv/resources/NLabTestImportView"); // NOI18N
        table.setToolTipText(bundle.getString("table.tooTipText")); // NOI18N
        jScrollPane1.setViewportView(table);

        countLbl.setFont(new java.awt.Font("Lucida Grande", Font.PLAIN, 12)); // NOI18N
        countLbl.setText(bundle.getString("countLbl.text")); // NOI18N

        fileBtn.setText(bundle.getString("fileBtn.text")); // NOI18N
        fileBtn.setToolTipText(bundle.getString("fileBtn.toolTipText")); // NOI18N

        jLabel1.setText("->"); // NOI18N

        addBtn.setText(bundle.getString("addBtn.text")); // NOI18N
        addBtn.setToolTipText(bundle.getString("addBtn.toolTipText")); // NOI18N

        clearBtn.setText(bundle.getString("clearBtn.text")); // NOI18N
        clearBtn.setToolTipText(bundle.getString("clearBtn.tooTipText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(fileBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                .addComponent(countLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileBtn)
                    .addComponent(jLabel1)
                    .addComponent(addBtn)
                    .addComponent(clearBtn)
                    .addComponent(countLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JLabel countLbl;
    private javax.swing.JButton fileBtn;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the countLbl
     */
    public javax.swing.JLabel getCountLbl() {
        return countLbl;
    }

    /**
     * @return the table
     */
    public javax.swing.JTable getTable() {
        return table;
    }

    /**
     * @return the addBtn
     */
    public javax.swing.JButton getAddBtn() {
        return addBtn;
    }

    /**
     * @return the fileBtn
     */
    public javax.swing.JButton getFileBtn() {
        return fileBtn;
    }

    /**
     * @return the clearBtn
     */
    public javax.swing.JButton getClearBtn() {
        return clearBtn;
    }
}