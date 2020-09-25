import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.*;

public class Client {
    private LoginForm loginForm;
    private ManageForm manageForm;
    private String response;
    private String hostname;
    private int port;

    public Client() {
        try {
            this.response = "";
            	this.hostname = "localhost";
            	this.port = 8083;
            this.initLayout();
        } catch (Exception e) {
            log(e.getStackTrace());
        }
    }

    public static void main(String[] args) {
        new Client();
    }
    
    private void initLayout() {
        this.loginForm = new LoginForm();
        loginForm.doBindEvent(new LoginListener());
    }

    public static void log(Object s) {
        System.out.println(s);
    }

    class LoginListener implements ActionListener {
        public LoginListener() {}
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String username = loginForm.getUserName();
                String passWord = loginForm.getPassWord();

                if (username.equals("") || passWord.equals("")) {
                    loginForm.alert("Please complete all fields");
                    loginForm.doRequestFocus();
                } else {
                    String req = "";
                    req += "svLogin/";
                    req += loginForm.getUserName() + "/";
                    req += loginForm.getPassWord();
                    	response = HTTP.doRequest(req, hostname, port);
                    if (response.length() > 0) {
                        if (response.split("/")[0].equals("200")) {
                            loginForm.alert("200 OK: Login success");
                            manageForm = new ManageForm();
                            loginForm.doFade();
                        } else {
                            loginForm.alert(response.split("/")[0] + " ERR: " + response.split("/")[1]);
                        }
                    } else {
                        loginForm.alert("Some errors occurs");
                    }
                }
            } catch (IOException _e) {
                loginForm.alert(_e.getMessage()+ "!");
            }
        }
    }
}