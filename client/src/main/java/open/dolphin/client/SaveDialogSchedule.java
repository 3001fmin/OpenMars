package open.dolphin.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.project.Project;

/**
 * SaveDialog
 * (予定カルテ対応)
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author modified by masuda, Masuda Naika
 */
public final class SaveDialogSchedule extends AbstractSaveDialog {
    
    private final String CHK_TITLE_SEND_WHEN_SCHEDULE;
    private final String TOOLTIP_DEPENDS_ON_CHECK;
    
//s.oh^ 2013/12/12 予定カルテのオーダー対応
    // LabTest 送信
    private JCheckBox sendLabtest;
//s.oh$

    public SaveDialogSchedule() {
        super();
        java.util.ResourceBundle bundle = ClientContext.getMyBundle(SaveDialogSchedule.class);
        CHK_TITLE_SEND_WHEN_SCHEDULE = bundle.getString("chkBoxTitle.sendClaim");
        TOOLTIP_DEPENDS_ON_CHECK = bundle.getString("toolTipText.chkBox");
    }
    
    /**
     * コンポーネントにSaveParamsの値を設定する。
     * @param params
     */
    @Override
    public void setValue(SaveParamsM params) {
        
        enterParams = params;
        
        JPanel contentPanel = createComponent();

        Object[] options = new Object[]{tmpButton, cancelButton};

        JOptionPane jop = new JOptionPane(
                contentPanel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                tmpButton);

        dialog = jop.createDialog(parent, ClientContext.getFrameTitle(TITLE));
        
        // Titleを表示する
//masuda^ 修正元のタイトルもコンボボックスに入れる   
        String[] titles = new String[]{params.getOldTitle(), params.getTitle()};
//masuda$  
        String progress = titles[0];
        for (String str : titles) {
            if (str != null && (!str.equals("") && (!str.equals(progress)))) {
                titleCombo.insertItemAt(str, 0);
            }
        }
        titleCombo.setSelectedIndex(0);
        
        // 診療科を表示する
        // 受付情報からの診療科を設定する
        String val = params.getDepartment();
        if (val != null) {
            String[] depts = val.split("\\s*,\\s*");
            if (depts[0] != null) {
                departmentLabel.setText(depts[0]);
            } else {
                departmentLabel.setText(val);
            }
        }
        
        // 印刷部数選択
        int count = params.getPrintCount();
        if (count != -1) {
            printCombo.setSelectedItem(String.valueOf(count));
            
        } else {
            printCombo.setEnabled(false);
        }

        //--------------------------------
        // CLAIM 送信をチェックする
        //--------------------------------
        boolean sendEnabled = params.isSendEnabled();
        sendClaimAction.setEnabled(sendEnabled);
        if (sendEnabled && params.isSendClaim()) {
            sendClaim.doClick();
        }
        
//s.oh^ 2013/12/12 予定カルテのオーダー対応
        //-------------------------------
        // 検体検査オーダー送信
        //-------------------------------
        sendLabtest.setSelected(params.isSendLabtest() && params.isHasLabtest());
        sendLabtest.setEnabled((sendEnabled && params.isHasLabtest()));
//s.oh$
        
        checkTitle();
        
        controlButton();
    }

    /**
     * GUIコンポーネントを初期化する。
     */
    private JPanel createComponent() {
        java.util.ResourceBundle bundle = ClientContext.getMyBundle(SaveDialogSchedule.class);
        String labelTextDocTitle = bundle.getString("labelText.docTitle");
        String labelTextDeptName = bundle.getString("labelText.deptName");
        String labelTextPrintCount = bundle.getString("labelText.printCount");
        String chkBoxTextLabTest = bundle.getString("chkBoxTitle.labTest");
                
        // content
        JPanel content = new JPanel();
        content.setLayout(new GridLayout(0, 1));
        
        // 文書Title
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleCombo = new JComboBox(TITLE_LIST);
        titleCombo.setPreferredSize(new Dimension(220, titleCombo.getPreferredSize().height));
        titleCombo.setMaximumSize(titleCombo.getPreferredSize());
        titleCombo.setEditable(true);
        p.add(new JLabel(labelTextDocTitle));
        p.add(titleCombo);
        content.add(p);
        
        // ComboBox のエディタコンポーネントへリスナを設定する
        titleField = (JTextField)titleCombo.getEditor().getEditorComponent();
        titleField.addFocusListener(AutoKanjiListener.getInstance());
        titleField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkTitle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkTitle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkTitle();
            }
        });
        
        // 診療科、印刷部数を表示するラベルとパネルを生成する
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        departmentLabel = new JLabel();
        p1.add(new JLabel(labelTextDeptName));
        p1.add(departmentLabel);
        
        p1.add(Box.createRigidArea(new Dimension(11, 0)));
        
        // Print
        printCombo = new JComboBox(PRINT_COUNT);
        printCombo.setSelectedIndex(1);
        p1.add(new JLabel(labelTextPrintCount));
        p1.add(printCombo);
        
        content.add(p1);
        
        //---------------------------
        // CLAIM 送信ありなし
        //--------------------------- 
        sendClaimAction = new AbstractAction(CHK_TITLE_SEND_WHEN_SCHEDULE) {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
        sendClaim = new JCheckBox();
        sendClaim.setAction(sendClaimAction);
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5.add(sendClaim);
        content.add(p5);
        
//s.oh^ 2013/12/12 予定カルテのオーダー対応
        //---------------------------
        // 検体検査オーダー送信ありなし
        //---------------------------
        sendLabtest = new JCheckBox(chkBoxTextLabTest);
        if (Project.getBoolean(Project.SEND_LABTEST)) {
            JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p6.add(sendLabtest);
            content.add(p6);
        }
//s.oh$
        
        // Cancel Button
        String buttonText =  (String)UIManager.get("OptionPane.cancelButtonText");
        cancelButton = new JButton(buttonText);
        cancelButton.addActionListener((ActionEvent e) -> {
            value = null;
            close();
        });
        
        // 仮保存 button
        tmpButton = new JButton(TMP_SAVE);
        tmpButton.setToolTipText(TOOLTIP_DEPENDS_ON_CHECK);
        tmpButton.addActionListener((ActionEvent e) -> {
            // 戻り値のSaveparamsを生成する
            value = viewToModel();
            if (value != null) {
                close();
            }
        });
        tmpButton.setEnabled(false);
        
        return content;
    }
    
    private void close() {
        dialog.setVisible(false);
        dialog.dispose();
    }
    
    private void controlButton() {
        tmpButton.setEnabled(true);
    }
    
    /**
     * タイトルフィールドの有効性をチェックする。
     */
    private void checkTitle() {    
        boolean hasTitle = !titleField.getText().trim().isEmpty();
        if (hasTitle) {
            controlButton();
        } else {
            tmpButton.setEnabled(false);
        }
    }

    private SaveParamsM viewToModel() {
        
        // 戻り値のSaveparamsを生成する
        SaveParamsM model = new SaveParamsM();
        
        // 戻り値の整理 仮ボタンが押された時     1
        int returnOption = SaveParamsM.SAVE_AS_TMP;
        
        // 開始時と終了時のオプションでKarteEditorで制御する
        model.setEnterOption(enterParams.getEnterOption());
        model.setReturnOption(returnOption);
        // Title候補
        String titleCand = "";

        if (returnOption == SaveParamsM.SAVE_AS_TMP) {// 仮ボタンが押された時
            model.setTmpSave(true);                                 // 仮保存
            model.setSendClaim(sendClaim.isSelected());             // CLAIM送信->CheckBox
            model.setClaimDate(enterParams.getClaimDate());         // CLAIM送信日
//s.oh^ 2013/12/12 予定カルテのオーダー対応
            //model.setSendLabtest(false);                            // Lab.Test送信->false 互換性を確保..
            model.setSendLabtest(sendLabtest.isSelected());         // Lab.Test送信->CheckBox
//s.oh$
            model.setAllowPatientRef(false);                        // MML->送信しない
            model.setAllowClinicRef(false);                         // MML->送信しない
            model.setSendMML(false);
            titleCand = ClientContext.getMyBundle(SaveDialogSchedule.class).getString("title.candidate.temporalSave");
        }
        
        // 文書タイトルを取得する
        String val = (String)titleCombo.getSelectedItem();
        val = (val.isEmpty()) ? titleCand : val;
        model.setTitle(val);
        
        // Department
        val = departmentLabel.getText();
        model.setDepartment(val);
        
        // 印刷部数を取得する
        int count = Integer.parseInt((String)printCombo.getSelectedItem());
        model.setPrintCount(count);
        
        return model;
    }
}