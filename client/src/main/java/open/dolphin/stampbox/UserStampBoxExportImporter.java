package open.dolphin.stampbox;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import open.dolphin.client.BlockGlass;
import open.dolphin.client.ClientContext;
import open.dolphin.helper.InfiniteProgressBar;
import open.dolphin.infomodel.IInfoModel;

/**
 * StampBox の特別メニュー
 * @author pns
 * modified by masuda
 */
public class UserStampBoxExportImporter {

    private final StampBoxPlugin context;
    private final AbstractStampBox stampBox;
    private InfiniteProgressBar progressBar;

    public UserStampBoxExportImporter(StampBoxPlugin ctx) {
        super();
        context = ctx;
        stampBox = context.getUserStampBox();
    }
    
    private BlockGlass getBlockGlass() {
        BlockGlass blockGlass = context.getBlockGlass();
        blockGlass.setSize(context.getFrame().getSize());
        return blockGlass;
    }

    /**
     * スタンプを xml ファイルに書き出す
     */
    public void exportUserStampBox() {

        // 保存する StampTree の XML データを生成する

//masuda^   blockGlassを入れたりSwingWorkerを入れたり・・・

//masuda    エクスポートデータ作成より前にファイル選択させる
        java.util.ResourceBundle bundle = ClientContext.getMyBundle(UserStampBoxExportImporter.class);
        
        String title = bundle.getString("title.fileChooser.exportStamp");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle(title);
        File current = fileChooser.getCurrentDirectory();
        fileChooser.setSelectedFile(new File(current.getPath(), "DolphinStamp.xml"));
        int selected = fileChooser.showSaveDialog(context.getFrame());

        if (selected == JFileChooser.APPROVE_OPTION) {

            final File file = fileChooser.getSelectedFile();
            if (!file.exists() || overwriteConfirmed(file)) {

                SwingWorker worker = new SwingWorker<String, Void>() {

                    @Override
                    protected String doInBackground() throws Exception {
//masuda    stampBytesを含めたデータを書き出す
                        ExtendedStampTreeXmlBuilder builder = new ExtendedStampTreeXmlBuilder();
                        ExtendedStampTreeXmlDirector director = new ExtendedStampTreeXmlDirector(builder);
                        //BlockGlass blockGlass = getBlockGlass();
                        //blockGlass.setText("スタンプ箱をエクスポート中です。");
                        //blockGlass.block();
                        ArrayList<StampTree> publishList = new ArrayList<>(IInfoModel.STAMP_ENTITIES.length);
                        publishList.addAll(stampBox.getAllTrees());
                        return director.build(publishList);
                    }

                    @Override
                    protected void done() {
                        String xml;
//minagawa^ mac jdk7                        
//                        FileOutputStream fos = null;
//                        OutputStreamWriter writer = null;
//minagawa$
                        try {
                            xml = get();
//minagawa^ mac jdk7                            
//                            fos = new FileOutputStream(file);
//                            writer = new OutputStreamWriter(fos, "UTF-8");
//                            // 書き出す内容
//                            writer.write(xml);
//                            writer.flush();
//minagawa$                            
                            Path destpath = file.toPath();
                            Files.write(destpath, xml.getBytes(StandardCharsets.UTF_8));
                            
                        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                            processException(ex);
                        } catch (InterruptedException | ExecutionException | IOException ex) {
                            processException(ex);
                        }
//                        finally {
//                            try {
//                                writer.close();
//                                fos.close();
//                            } catch (IOException | NullPointerException ex) {
//                                processException(ex);
//                            }
//                        }
                        BlockGlass blockGlass = getBlockGlass();
                        blockGlass.unblock();
                        progressBar.stop();
                        progressBar = null;
                    }

                    private void processException(Exception ex){
                        System.out.println("StampBoxPluginExtraMenu.java: " + ex);
                    }
                };
//minagawa^ 念のため doInbackground の外に出す               
                BlockGlass blockGlass = getBlockGlass();
                blockGlass.block();
                String note = bundle.getString("note.progress.exportingStampBox");
                progressBar = new InfiniteProgressBar("StampBoxExport", note, stampBox);
                progressBar.start();
//minagawa$
                worker.execute();
            }
        }
//masuda$
    }

    /**
     * ファイル上書き確認ダイアログを表示する。
     * @param file 上書き対象ファイル
     * @return 上書きOKが指示されたらtrue
     */
    private boolean overwriteConfirmed(File file){
        java.util.ResourceBundle bundle = ClientContext.getMyBundle(UserStampBoxExportImporter.class);
        String title = bundle.getString("title.optionPane.override");
//        String message = "既存のファイル " + file.toString() + "\n"
//                        +"を上書きしようとしています。続けますか？";
        String fmt = bundle.getString("messageFormat.overriding.existingFile");
        String message = new MessageFormat(fmt).format(new String[]{file.toString()});
        
        int confirm = JOptionPane.showConfirmDialog(
        //int confirm = MyJSheet.showConfirmDialog(
            context.getFrame(), message, title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE );

        return confirm == JOptionPane.OK_OPTION;

    }
    
    /**
     * xml ファイルから新しい userStampBox を作る
     * modified minagawa. doInBackgroundから component へのアクセスを外す。
     */
    public void importUserStampBox() {
        
        final java.util.ResourceBundle bundle = ClientContext.getMyBundle(UserStampBoxExportImporter.class);

        String title = bundle.getString("title.fileChooser.importStamp");
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle(title);
        File current = fileChooser.getCurrentDirectory();
        //fileChooser.setSelectedFile(new File(current.getPath(), "DolphinStamp.xml"));
        //int selected = fileChooser.showSaveDialog(context.getFrame());
        int selected = fileChooser.showOpenDialog(context.getFrame());

        if (selected == JFileChooser.APPROVE_OPTION) {
            final File file = fileChooser.getSelectedFile();

            SwingWorker worker = new SwingWorker<List<StampTree>, Void>(){

                @Override
                protected List<StampTree> doInBackground() throws Exception {
                    //BlockGlass blockGlass = getBlockGlass();
                    //blockGlass.setText("スタンプ箱インポート中です。");
                    //blockGlass.block();
                    
                    // xml ファイルから StampTree 作成
//minagawa^ mac jdk7                    
                    //FileInputStream in = new FileInputStream(file);
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));   
                    BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8"));
//minagawa$                    
//masuda^   stampBytesを含めたデータを読み込む
                    ExtendedStampTreeDirector director
                            = new ExtendedStampTreeDirector(new ExtendedStampTreeBuilder());
//masuda$
                    List<StampTree> userTrees = director.build(reader);
                    reader.close();
                    
                    return userTrees;
                }

                @Override
                protected void done() {
                    boolean imported = false;
                    try {
                        List<StampTree> userTrees = get();
                        int currentTab = stampBox.getSelectedIndex();
                        StampTreeTransferHandler transferHandler = new StampTreeTransferHandler();
                        for (final StampTree stampTree : userTrees) {
                            // ORCA は無視
                            if (stampTree.getEntity().equals(IInfoModel.ENTITY_ORCA)) {
                                continue;
                            }
                            // 読み込んだ stampTree から StampTreePanel を作る
                            stampTree.setUserTree(true);
                            stampTree.setTransferHandler(transferHandler);
                            stampTree.setStampBox(context);
                            StampTreePanel treePanel = new StampTreePanel(stampTree);

                            // 作った StampTreePanel を該当する tab に replace
                            String treeName = stampTree.getTreeName();
                            int index = stampBox.indexOfTab(treeName);
                            stampBox.removeTabAt(index);
                            //stampBox.addTab(treeName, treePanel, index);
                            stampBox.add(treePanel, treeName, index);
                        }
                        stampBox.setSelectedIndex(currentTab);
                        imported = true;
                        
                    } catch (InterruptedException | ExecutionException ex) {
                        processException(ex);
                    }
                    
                    BlockGlass blockGlass = getBlockGlass();
                    blockGlass.unblock();
                    progressBar.stop();
                    progressBar = null;
                    
                    if(imported) {
                        String msg = bundle.getString("message.importDone");
                        String title = bundle.getString("title.optionPane.import");
                        title = ClientContext.getFrameTitle(title);
                        JOptionPane.showMessageDialog(context.getFrame(), msg, title, JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                private void processException(Exception ex) {
                    System.out.println("StampBoxPluginExtraMenu.java: " + ex);
                }
            };
            
            BlockGlass blockGlass = getBlockGlass();
            blockGlass.block();
            String note = bundle.getString("note.progress.importing");
            progressBar = new InfiniteProgressBar("StampBoxImport", note, stampBox);
            progressBar.start();
            
            worker.execute();
        }
    }
}