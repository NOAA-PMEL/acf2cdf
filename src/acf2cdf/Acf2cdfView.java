/*
 * Acf2cdfView.java
 */
package acf2cdf;

import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileView;
import org.jdesktop.application.Application.ExitListener;

/**
 * The application's main frame.
 */
public class Acf2cdfView extends FrameView {

    public Acf2cdfView(SingleFrameApplication app) {
        super(app);

        appProps = new Properties();
        appProps.setProperty(inputPathKey, homeDir);
        appProps.setProperty(maskPathKey, homeDir);
        appProps.setProperty(useMaskKey, useMask.toString()); // default: do not use mask
//        appProps = new Properties();
//        appProps.setProperty(inputPathKey, homeDir);
//        appProps.setProperty(xrefPathKey, homeDir);
//        appProps.setProperty(outputPathKey, homeDir);
//
        try {
            File config;
            appDir = homeDir + fileSep + ".ACGApps" + fileSep + "acf2cdf" +
                    fileSep + "config";
            config = new File(appDir);
            config.mkdirs();

            propertyFileName = appDir + fileSep + "acf2cdf.config";

            FileInputStream in = new FileInputStream(propertyFileName);
            appProps.loadFromXML(in);
            in.close();
            //setInputPath(appProps.getProperty(inputPathKey));
            //setXrefPath(appProps.getProperty(xrefPathKey));

            // set useMask from string
            useMask = Boolean.parseBoolean(appProps.getProperty(useMaskKey));
            //System.out.println(appProps.getProperty(useMaskKey));

        } catch (IOException e) {
            System.out.println("error loading properties " + e);
            //setInputPath(null);
            //setXrefPath(null);
        }

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        // add ExitListener
        app.addExitListener(new SaveConfigOnExit());

        // enable/disable mask fields based on saved settings
        maskFileLabel.setEnabled(false);
        maskFileField.setEnabled(false);
        maskBrowseButton.setEnabled(false);

//        useMaskCheckBox.firePropertyChange("enabled", false, useMask);
        useMaskCheckBox.setSelected(useMask);
        useMaskFile();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = Acf2cdfApp.getApplication().getMainFrame();
            aboutBox = new Acf2cdfAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Acf2cdfApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        inputFileField = new javax.swing.JTextField();
        inputBrowseButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        useMaskCheckBox = new javax.swing.JCheckBox();
        maskFileField = new javax.swing.JTextField();
        maskFileLabel = new javax.swing.JLabel();
        maskBrowseButton = new javax.swing.JButton();
        evenTimesYesButton = new javax.swing.JRadioButton();
        evenTimesNoButton = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        quitButton = new javax.swing.JButton();
        runButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        evenTimeGroup = new javax.swing.ButtonGroup();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(acf2cdf.Acf2cdfApp.class).getContext().getResourceMap(Acf2cdfView.class);
        inputFileField.setText(resourceMap.getString("inputFileField.text")); // NOI18N
        inputFileField.setName("inputFileField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(acf2cdf.Acf2cdfApp.class).getContext().getActionMap(Acf2cdfView.class, this);
        inputBrowseButton.setAction(actionMap.get("showFileBrowser")); // NOI18N
        inputBrowseButton.setText(resourceMap.getString("inputBrowseButton.text")); // NOI18N
        inputBrowseButton.setName("inputBrowseButton"); // NOI18N
        inputBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputBrowseButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        useMaskCheckBox.setAction(actionMap.get("useMaskFile")); // NOI18N
        useMaskCheckBox.setName("useMaskCheckBox"); // NOI18N

        maskFileField.setName("maskFileField"); // NOI18N

        maskFileLabel.setText(resourceMap.getString("maskFileLabel.text")); // NOI18N
        maskFileLabel.setName("maskFileLabel"); // NOI18N

        maskBrowseButton.setAction(actionMap.get("showFileBrowser")); // NOI18N
        maskBrowseButton.setText(resourceMap.getString("maskBrowseButton.text")); // NOI18N
        maskBrowseButton.setName("maskBrowseButton"); // NOI18N
        maskBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskBrowseButtonActionPerformed(evt);
            }
        });

        evenTimeGroup.add(evenTimesYesButton);
        evenTimesYesButton.setText(resourceMap.getString("evenTimesYesButton.text")); // NOI18N
        evenTimesYesButton.setName("evenTimesYesButton"); // NOI18N

        evenTimeGroup.add(evenTimesNoButton);
        evenTimesNoButton.setSelected(true);
        evenTimesNoButton.setText(resourceMap.getString("evenTimesNoButton.text")); // NOI18N
        evenTimesNoButton.setName("evenTimesNoButton"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        quitButton.setAction(actionMap.get("quit")); // NOI18N
        quitButton.setText(resourceMap.getString("quitButton.text")); // NOI18N
        quitButton.setName("quitButton"); // NOI18N

        runButton.setText(resourceMap.getString("runButton.text")); // NOI18N
        runButton.setName("runButton"); // NOI18N
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(useMaskCheckBox)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(inputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(inputBrowseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(maskFileLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(maskFileField, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(evenTimesYesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                                .addComponent(evenTimesNoButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addComponent(runButton)
                                .addGap(18, 18, 18)
                                .addComponent(quitButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(maskBrowseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputBrowseButton)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(useMaskCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maskFileField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maskBrowseButton)
                    .addComponent(maskFileLabel))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(evenTimesNoButton)
                    .addComponent(jLabel2)
                    .addComponent(evenTimesYesButton))
                .addGap(32, 32, 32)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quitButton)
                    .addComponent(runButton))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setAction(actionMap.get("quit")); // NOI18N
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void inputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputBrowseButtonActionPerformed
        JFileChooser fc = new JFileChooser();

        String val;
        if ((val = appProps.getProperty(inputPathKey)) != null) {
            fc.setCurrentDirectory(new File(val));
        }
        int retval = fc.showOpenDialog(Acf2cdfApp.getApplication().getMainFrame());
        if (retval == JFileChooser.APPROVE_OPTION) {
            File input = fc.getSelectedFile();
            inputFileField.setText(input.getAbsolutePath());
            appProps.setProperty(inputPathKey, fc.getCurrentDirectory().toString());
        }

    }//GEN-LAST:event_inputBrowseButtonActionPerformed

    private void maskBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maskBrowseButtonActionPerformed
        JFileChooser fc = new JFileChooser();

        String val;
        if ((val = appProps.getProperty(maskPathKey)) != null) {
            fc.setCurrentDirectory(new File(val));
        }
        int retval = fc.showOpenDialog(Acf2cdfApp.getApplication().getMainFrame());
        if (retval == JFileChooser.APPROVE_OPTION) {
            File mask = fc.getSelectedFile();
            maskFileField.setText(mask.getAbsolutePath());
            appProps.setProperty(maskPathKey, fc.getCurrentDirectory().toString());
        }


    }//GEN-LAST:event_maskBrowseButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed

        try {
            ACFFileReader acfReader = new ACFFileReader(inputFileField.getText());
//            if (useMaskCheckBox.isSelected()) {
//                MaskFileReader mfReader = new MaskFileReader(maskFileField.getText());
//                acfReader.setMaskFile(mfReader);
//            }
//            acfReader.setEvenTimeStep(evenTimesYesButton.isSelected());

//            TaskMonitor tm = getApplication().getContext().getTaskMonitor();
//            //busyIconTimer.setActionCommand("started");
//            if (!busyIconTimer.isRunning()) {
//                statusAnimationLabel.setIcon(busyIcons[0]);
//                busyIconIndex = 0;
//                busyIconTimer.start();
//            }
//            progressBar.setVisible(true);
//            progressBar.setIndeterminate(true);

            ACFDataset dataset = acfReader.parse();
            Boolean evenTimeStep = evenTimesYesButton.isSelected();
            dataset.putMetadata("evenTimeStep", evenTimeStep.toString());
            dataset.verify();
            if (useMaskCheckBox.isSelected()) {
                MaskFileReader mfReader = new MaskFileReader(maskFileField.getText());
                //acfReader.setMaskFile(mfReader);
                dataset.maskData(mfReader);
            }
            //String[] name = inputFileField.getText().split(".");
            int lastDot = inputFileField.getText().lastIndexOf(".");
            //String cdfName = inputFileField.getText()
            String inputName = inputFileField.getText();
            String cdfName = inputName.substring(0, lastDot) + ".cdf";

            dataset.writeToEpicCDF(cdfName);
            //acfReader.process();
            //acfReader.writeToNetCDF();

//            busyIconTimer.stop();
//            statusAnimationLabel.setIcon(idleIcon);
//            progressBar.setVisible(false);
//            progressBar.setValue(0);


        } catch (IOException ex) {
            Logger.getLogger(Acf2cdfView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_runButtonActionPerformed

    @Action()
    public void useMaskFile() {
        useMask = useMaskCheckBox.isSelected();
        maskFileField.setEnabled(useMask);
        maskFileLabel.setEnabled(useMask);
        maskBrowseButton.setEnabled(useMask);
        appProps.setProperty(useMaskKey, Boolean.toString(useMask));
    }

    public class SaveConfigOnExit implements ExitListener {

        public boolean canExit(EventObject event) {
            return true;
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void willExit(EventObject event) {
//            System.out.println("in will exit!");
            try {
                FileOutputStream out = new FileOutputStream(propertyFileName);
                appProps.storeToXML(out, "--acf2cdf Runtime Properties--");
                out.close();
            } catch (IOException e) {
                System.out.println("error writing properties to file " + e);
            }

//            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup evenTimeGroup;
    private javax.swing.JRadioButton evenTimesNoButton;
    private javax.swing.JRadioButton evenTimesYesButton;
    private javax.swing.JButton inputBrowseButton;
    private javax.swing.JTextField inputFileField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton maskBrowseButton;
    private javax.swing.JTextField maskFileField;
    private javax.swing.JLabel maskFileLabel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton runButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JCheckBox useMaskCheckBox;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    String fileSep = System.getProperty("file.separator");
    String homeDir = System.getProperty("user.home");
    String appDir;
    Properties appProps;
    String propertyFileName;
    private String inputPathKey = "inputPath";
    private String maskPathKey = "maskPath";
//    private String outputPathKey = "outputPath";
    private String useMaskKey = "useMask";
    private String inputPath;
    private String maskPath;
    private String useMaskStr;
    private Boolean useMask = false;
}
