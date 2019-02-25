import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Gui extends JFrame {
    public Gui(final EncryptClass encryptClass, DecryptClass decryptClass) {
        super("Encrypt/Decrypt system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panelEncrypt = new JPanel();
        JPanel panelDecrypt = new JPanel();
        setupEncryptPanel(encryptClass, panelEncrypt);
        setupDecryptPanel(decryptClass, panelDecrypt);

        ScrollPane scrollPaneEn = new ScrollPane();
        ScrollPane scrollPaneDe = new ScrollPane();

        scrollPaneEn.add(panelEncrypt);
        scrollPaneDe.add(panelDecrypt);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encrypt", null, scrollPaneEn,
                "Encrypt messages");
        tabbedPane.addTab("Decrypt", null, scrollPaneDe,
                "Decrypt messages");
        add(tabbedPane);
    }

    private void setupDecryptPanel(DecryptClass decryptClass, JPanel panelDecrypt)
    {
        panelDecrypt.setLayout(new BoxLayout(panelDecrypt, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        JRadioButton rsaButton = new JRadioButton(Method.RSA.getMethod());
        rsaButton.setActionCommand(Method.RSA.getMethod());
        rsaButton.setSelected(true);
        JRadioButton ecButton = new JRadioButton(Method.EC.getMethod());
        ecButton.setActionCommand(Method.EC.getMethod());
        JRadioButton ncryptButton = new JRadioButton(Method.NCRYPT.getMethod());
        ncryptButton.setActionCommand(Method.NCRYPT.getMethod());
        group.add(rsaButton);
        group.add(ecButton);
        group.add(ncryptButton);

        JPanel innerPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(0,2);
        innerPanel.setLayout(gridLayout);

        JLabel publicLabel = new JLabel("public key:");
        JTextArea publicTextField = new JTextArea();
        setUpTestArea(publicTextField);

        innerPanel.add(publicLabel);
        innerPanel.add(publicTextField);

        JLabel privateLabel = new JLabel("private key/s:");
        JTextArea privateTextField = new JTextArea();
        setUpTestArea(privateTextField);

        innerPanel.add(privateLabel);
        innerPanel.add(privateTextField);

        JLabel encryptedTextLabel = new JLabel("Encrypted message:");
        JTextArea encryptedTextField = new JTextArea();
        setUpTestArea(encryptedTextField);

        innerPanel.add(encryptedTextLabel);
        innerPanel.add(encryptedTextField);

        JLabel decryptedTextLabel = new JLabel("Decrypted message:");
        JTextArea decryptedTextField = new JTextArea();
        setUpTestArea(decryptedTextField);

        innerPanel.add(decryptedTextLabel);
        innerPanel.add(decryptedTextField);

        panelDecrypt.add(rsaButton);
        panelDecrypt.add(ecButton);
        panelDecrypt.add(ncryptButton);

        panelDecrypt.add(innerPanel);
        JButton processButton = new JButton("Decode message");
        processButton.addActionListener(e-> {
            String method = group.getSelection().getActionCommand();
            Process process = decryptClass.getProcess(Method.valueOf(method));
            String privateKey = privateTextField.getText();
            String publicKey = publicTextField.getText();

            String decoded = process.decrypt(encryptedTextField.getText(), privateKey, publicKey);
            decryptedTextField.setText(decoded);
        });
        panelDecrypt.add(processButton);
    }

    private void setupEncryptPanel(EncryptClass encryptClass, JPanel panelEncrypt)
    {
        panelEncrypt.setLayout(new BoxLayout(panelEncrypt, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        JRadioButton rsaButton = new JRadioButton(Method.RSA.getMethod());
        rsaButton.setActionCommand(Method.RSA.getMethod());
        rsaButton.setSelected(true);
        JRadioButton ecButton = new JRadioButton(Method.EC.getMethod());
        ecButton.setActionCommand(Method.EC.getMethod());
        JRadioButton ncryptButton = new JRadioButton(Method.NCRYPT.getMethod());
        ncryptButton.setActionCommand(Method.NCRYPT.getMethod());
        group.add(rsaButton);
        group.add(ecButton);
        group.add(ncryptButton);

        JLabel publicLabel = new JLabel("public key:");
        JTextArea publicTextField = new JTextArea();
        setUpTestArea(publicTextField);
        JLabel privateLabel = new JLabel("private key:");
        JTextArea privateTextField = new JTextArea();
        setUpTestArea(privateTextField);

        JLabel splitLabel = new JLabel("split private key on parts:");
        SpinnerModel splitSize = new SpinnerNumberModel(1,1,10,1);

        JPanel innerPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(0,2);
        innerPanel.setLayout(gridLayout);

        innerPanel.add(publicLabel);
        innerPanel.add(publicTextField);
        innerPanel.add(privateLabel);
        innerPanel.add(privateTextField);
        innerPanel.add(splitLabel);
        innerPanel.add(new JSpinner(splitSize));

        panelEncrypt.add(rsaButton);
        panelEncrypt.add(ecButton);
        panelEncrypt.add(ncryptButton);

        panelEncrypt.add(innerPanel);

        JTextArea splitTextField = new JTextArea();
        setUpTestArea(splitTextField);
        panelEncrypt.add(splitTextField);

        JLabel decodedLabel = new JLabel("decoded message:");
        JTextArea decodedTextField = new JTextArea();
        setUpTestArea(decodedTextField);

        JLabel encodedLabel = new JLabel("encoded message:");
        JTextArea encodedTextField = new JTextArea();
        setUpTestArea(encodedTextField);

        JPanel innerPanelForMsg = new JPanel();
        innerPanelForMsg.setLayout(gridLayout);
        innerPanelForMsg.add(decodedLabel);
        innerPanelForMsg.add(decodedTextField);
        innerPanelForMsg.add(encodedLabel);
        innerPanelForMsg.add(encodedTextField);


        JButton genButton = new JButton("Generate new keys");
        JButton processButton = new JButton("Encode message");
        genButton.addActionListener(e-> {
            String method = group.getSelection().getActionCommand();
            Process process = encryptClass.getProcess(Method.valueOf(method));
            Pair<String, String> keys = process.getKeys();
            privateTextField.setText(keys.getLeft());
            publicTextField.setText(keys.getRight());
        });
        processButton.addActionListener(e-> {
            String method = group.getSelection().getActionCommand();
            Process process = encryptClass.getProcess(Method.valueOf(method));
            String encoded = process.encrypt(decodedTextField.getText(), publicTextField.getText());
            encodedTextField.setText(encoded);
        });

        innerPanelForMsg.add(genButton);
        innerPanelForMsg.add(processButton);
        panelEncrypt.add(innerPanelForMsg);
    }

    private void setUpTestArea(JTextArea textArea){
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        textArea.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    public static void main(String[] argv) {
        EncryptClass encryptClass = new EncryptClass();
        DecryptClass decryptClass = new DecryptClass();

        Gui sg = new Gui(encryptClass, decryptClass);
        sg.setSize(500, 500);
        sg.setVisible(true);
    }
}
