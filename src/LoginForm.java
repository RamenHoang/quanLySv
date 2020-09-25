import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import java.awt.event.*;

public class LoginForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private JLabel userNameLabel;
    private JLabel passWordLabel;
    private JTextField userNameText;
    private JTextField passWordText;
    private JButton loginButton;
    private JPanel panel;
    private SpringLayout layout;
    
    private String left = "West";
    private String right = "East";
    private String top = "North";
    private String bottom = "South";

    public LoginForm() {
        this.userNameLabel = new JLabel("Username");
        this.passWordLabel = new JLabel("Password");
        this.userNameText = new JTextField(10);
        this.passWordText = new JTextField(10);
        this.loginButton = new JButton("Login");
        this.panel = new JPanel();
        this.layout = new SpringLayout();

        this.panel.setSize(300, 200);
        this.panel.setLayout(this.layout);
        this.panel.add(this.userNameLabel);
        this.panel.add(this.userNameText);
        this.panel.add(this.passWordLabel);
        this.panel.add(this.passWordText);
        this.panel.add(this.loginButton);

        // set constraint for userNameLabel
        this.layout.putConstraint(this.left, this.userNameLabel, 30, this.left, this.panel);
        this.layout.putConstraint(this.top, this.userNameLabel, 30, this.top, this.panel);
        // set constraint for userNameText
        this.layout.putConstraint(this.right, this.userNameText, -30, this.right, this.panel);
        this.layout.putConstraint(this.top, this.userNameText, 30, this.top, this.panel);
        // set constraint for passWordLabel
        this.layout.putConstraint(this.left, this.passWordLabel, 30, this.left, this.panel);
        this.layout.putConstraint(this.top, this.passWordLabel, 70, this.top, this.panel);
        // set constraint for passWordText
        this.layout.putConstraint(this.right, this.passWordText, -30, this.right, this.panel);
        this.layout.putConstraint(this.top, this.passWordText, 70, this.top, this.panel);
        // set constraint for loginButton
        this.layout.putConstraint(this.left, this.loginButton, 110, this.left, this.panel);
        this.layout.putConstraint(this.bottom, this.loginButton, -30, this.bottom, this.panel);

        this.add(this.panel);
        this.setTitle("Login - Student Management");
        this.setVisible(true);
        this.setBounds(500, 400, 300, 200);
        this.setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public String getUserName() {
        return this.userNameText.getText();
    }

    public String getPassWord() {
        return this.passWordText.getText();
    }

    public void doRequestFocus() {
        String userName = this.getUserName();
        String passWord = this.getPassWord();
        if(userName.equals(""))
            this.userNameText.requestFocus();
        else if (passWord.equals(""))
            this.passWordText.requestFocus();
    }

    public void doBindEvent(ActionListener listener) {
        this.loginButton.addActionListener(listener);
    }

    public void doFade() {
        this.setVisible(false);
    }
}