import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Gui extends JFrame {
    public Gui(final EncryptClass encryptClass, DecryptClass decryptClass) {
        super("Encrypt/Decrypt system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panelEncrypt = new JPanel();
        JPanel panelDecrypt = new JPanel();
        panelEncrypt.setLayout(new BoxLayout(panelEncrypt, BoxLayout.Y_AXIS));
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

        JLabel publicLabel = new JLabel("public key:");
        JTextArea publicTextField = new JTextArea();
        JLabel privateLabel = new JLabel("private key:");
        JTextArea privateTextField = new JTextArea();
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
        panelEncrypt.add(splitTextField);

        JLabel decodedLabel = new JLabel("decoded message:");
        JTextArea decodedTextField = new JTextArea();
        JLabel encodedLabel = new JLabel("encoded message:");
        JTextArea encodedTextField = new JTextArea();

        JPanel innerPanelForMsg = new JPanel();
        innerPanelForMsg.setLayout(gridLayout);
        innerPanelForMsg.add(decodedLabel);
        innerPanelForMsg.add(decodedTextField);
        innerPanelForMsg.add(encodedLabel);
        innerPanelForMsg.add(encodedTextField);

        panelEncrypt.add(innerPanelForMsg);

        JButton processButton = new JButton("Encode message");
        processButton.addActionListener(e-> {
            String method = group.getSelection().getActionCommand();
            Process process = encryptClass.getProcess(Method.valueOf(method));
            Pair<String, String> keys = process.getKeys();
            privateTextField.setText(keys.getLeft());
            publicTextField.setText(keys.getRight());

            String encoded = process.encrypt(decodedTextField.getText(), keys.getRight());
            encodedTextField.setText(encoded);
        });

        panelEncrypt.add(processButton);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Encrypt", null, panelEncrypt,
                "Encrypt messages");
        tabbedPane.addTab("Decrypt", null, panelDecrypt,
                "Decrypt messages");
        add(tabbedPane);
    }

    public static void main(String[] argv) {
        EncryptClass encryptClass = new EncryptClass();
        DecryptClass decryptClass = new DecryptClass();

        Gui sg = new Gui(encryptClass, decryptClass);
        sg.setSize(500, 500);
        sg.setVisible(true);
    }
}
