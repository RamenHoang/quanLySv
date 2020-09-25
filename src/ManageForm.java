import javax.net.ssl.ExtendedSSLSession;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ManageForm extends JFrame implements ActionListener, ListSelectionListener {
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel classLabel;
    private JLabel gpaLabel;
    private JTextField idText;
    private JTextField nameText;
    private JTextField ageText;
    private JTextField classText;
    private JTextField gpaText;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JScrollPane scrollPane;
    private JTable table;
    private SpringLayout layout;
    private JPanel panel;

    private String left = "West";
    private String right = "East";
    private String top = "North";
    private String bottom = "South";

    private String[] columnNames = { "ID", "Name", "Age", "Class", "GPA" };
    
    private String hostname = "localhost";
    private int port = 8083;

    public ManageForm() {
        this.idLabel = new JLabel("ID");
        this.nameLabel = new JLabel("Name");
        this.ageLabel = new JLabel("Age");
        this.classLabel = new JLabel("Class");
        this.gpaLabel = new JLabel("GPA");

        this.idText = new JTextField(4);
        this.nameText = new JTextField(15);
        this.ageText = new JTextField(4);
        this.classText = new JTextField(5);
        this.gpaText = new JTextField(5);

        this.addButton = new JButton("Add");
        this.editButton = new JButton("Edit");
        this.deleteButton = new JButton("Delete");
        this.clearButton = new JButton("Clear");

        this.scrollPane = new JScrollPane();
        this.table = new JTable();
        this.layout = new SpringLayout();
        this.panel = new JPanel();

        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.scrollPane.setViewportView(this.table);
        this.scrollPane.setSize(600, 500);
        this.panel.setSize(700, 600);
        this.panel.setLayout(this.layout);
        this.panel.add(this.idLabel);
        this.panel.add(this.nameLabel);
        this.panel.add(this.ageLabel);
        this.panel.add(this.classLabel);
        this.panel.add(this.gpaLabel);
        this.panel.add(this.idText);
        this.panel.add(this.nameText);
        this.panel.add(this.ageText);
        this.panel.add(this.classText);
        this.panel.add(this.gpaText);

        this.panel.add(this.addButton);
        this.panel.add(this.editButton);
        this.panel.add(this.deleteButton);
        this.panel.add(this.clearButton);
        this.panel.add(this.scrollPane);

        this.layout.putConstraint(this.left, this.idLabel, 30, this.top, this.panel);
        this.layout.putConstraint(this.top, this.idLabel, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.idText, 5, this.right, this.idLabel);
        this.layout.putConstraint(this.top, this.idText, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.nameLabel, 5, this.right, this.idText);
        this.layout.putConstraint(this.top, this.nameLabel, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.nameText, 5, this.right, this.nameLabel);
        this.layout.putConstraint(this.top, this.nameText, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.ageLabel, 5, this.right, this.nameText);
        this.layout.putConstraint(this.top, this.ageLabel, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.ageText, 5, this.right, this.ageLabel);
        this.layout.putConstraint(this.top, this.ageText, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.classLabel, 5, this.right, this.ageText);
        this.layout.putConstraint(this.top, this.classLabel, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.classText, 5, this.right, this.classLabel);
        this.layout.putConstraint(this.top, this.classText, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.gpaLabel, 5, this.right, this.classText);
        this.layout.putConstraint(this.top, this.gpaLabel, 30, this.top, this.panel);
        this.layout.putConstraint(this.left, this.gpaText, 5, this.right, this.gpaLabel);
        this.layout.putConstraint(this.top, this.gpaText, 30, this.top, this.panel);

        this.layout.putConstraint(this.left, this.addButton, 100, this.left, this.panel);
        this.layout.putConstraint(this.top, this.addButton, 80, this.top, this.panel);
        this.layout.putConstraint(this.left, this.editButton, 60, this.right, this.addButton);
        this.layout.putConstraint(this.top, this.editButton, 80, this.top, this.panel);
        this.layout.putConstraint(this.left, this.deleteButton, 60, this.right, this.editButton);
        this.layout.putConstraint(this.top, this.deleteButton, 80, this.top, this.panel);
        this.layout.putConstraint(this.left, this.clearButton, 60, this.right, this.deleteButton);
        this.layout.putConstraint(this.top, this.clearButton, 80, this.top, this.panel);

        this.layout.putConstraint(this.left, this.scrollPane, 120, this.left, this.panel);
        this.layout.putConstraint(this.bottom, this.scrollPane, -30, this.bottom, this.panel);

        this.add(this.panel);
        this.setTitle("Student Manage Form");
        this.setBounds(400, 300, 700, 600);
        this.setVisible(true);
        this.editButton.setEnabled(false);
        this.deleteButton.setEnabled(false);
        this.clearButton.setEnabled(false);
        this.idText.setEnabled(false);

        this.addButton.addActionListener(this);
        this.editButton.addActionListener(this);
        this.deleteButton.addActionListener(this);
        this.clearButton.addActionListener(this);

        this.table.getSelectionModel().addListSelectionListener(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.doFillTable();
    }

    public void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void doFillTable() {
        try {
            String req = "svGet/";
            String response = HTTP.doRequest(req, hostname, port);
            String[] students = response.split("/");
            	if(students[0].equals("200")) {
            		DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(this.columnNames);
                for (int i = 1; i < students.length; i++) {
                    String[] row = students[i].split(";");
                    model.addRow(row);
                }
                this.table.setModel(model);
            	} else {
            		alert(students[0] + " ERR: " + students[1]);
            	}
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    private boolean isValidated() {
    	try {
    		Integer.parseInt(this.idText.getText());
    		String name = this.nameText.getText().replaceAll(" ", "");
    		String age = this.ageText.getText();
    		String classString = this.classText.getText().replaceAll(" ", "");
    		String gpa = this.gpaText.getText();
    		Integer.parseInt(age);
    		Double.parseDouble(gpa);
    		if (name.matches("[a-zA-Z]+") && classString.matches("\\w+")) 
    			return true;
    		this.alert("Some fields is not valid");
    		return false;
    	} catch(Exception e) {
    		this.alert(e.getMessage());
    		return false;
    	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.clearButton) {
        	this.idText.setText("");
            this.nameText.setText("");
            this.ageText.setText("");
            this.classText.setText("");
            this.gpaText.setText("");
            this.editButton.setEnabled(false);
            this.deleteButton.setEnabled(false);
        } else if (e.getSource() == this.addButton) {
        	if (this.isValidated()) {
        		String req = "svNew/";
	            	req += this.nameText.getText() + ";";
	            	req += this.ageText.getText() + ";";
	            	req += this.classText.getText() + ";";
            		req += this.gpaText.getText();
            		try {
            			String response = HTTP.doRequest(req, hostname, port);
            			String[] responseArr = response.split("/");
            			if (responseArr[0].equals("200")) {
            				alert("200 OK: Added this student");
            				doFillTable();
            				this.clearButton.doClick();
            			} else {
            				alert(responseArr[0] + " ERR: " + responseArr[1]);
            			}
            		} catch(Exception _e) {
            			alert(_e.getMessage());
            		}	
        	}
        } else if (e.getSource() == this.editButton) {
        	if (this.isValidated()) {
        		String req = "svUpdate/";
        		req += this.idText.getText() + ";";
	            	req += this.nameText.getText() + ";";
	            	req += this.ageText.getText() + ";";
	            	req += this.classText.getText() + ";";
            		req += this.gpaText.getText();
            		try {
            			String response = HTTP.doRequest(req, hostname, port);
            			String[] responseArr = response.split("/");
            			if (responseArr[0].equals("200")) {
            				alert("200 OK: Updated this student");
            				doFillTable();
            				this.clearButton.doClick();
            			} else {
            				alert(responseArr[0] + " ERR: " + responseArr[1]);
            			}
            		} catch(Exception _e) {
            			alert(_e.getMessage());
            		}	
        	}
        } else if (e.getSource() == this.deleteButton) {
        	String req = "svDelete/" + this.idText;
        	try {
        		String response = HTTP.doRequest(req, hostname, port);
        		String[] responseArr = response.split("/");
    			if (responseArr[0].equals("200")) {
    				alert("200 OK: Deleted this student");
    				doFillTable();
    				this.clearButton.doClick();
    			} else {
    				alert(responseArr[0] + " ERR: " + responseArr[1]);
    			}
        	} catch (Exception _e) {
        		alert(_e.getMessage());
        	}
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = this.table.getSelectedRow();
        if (selectedRow >= 0) {
            this.idText.setText(this.table.getModel().getValueAt(selectedRow, 0).toString());
            this.nameText.setText(this.table.getModel().getValueAt(selectedRow, 1).toString());
            this.ageText.setText(this.table.getModel().getValueAt(selectedRow, 2).toString());
            this.classText.setText(this.table.getModel().getValueAt(selectedRow, 3).toString());
            this.gpaText.setText(this.table.getModel().getValueAt(selectedRow, 4).toString());
            
            	this.editButton.setEnabled(true);
            	this.deleteButton.setEnabled(true);
            	this.clearButton.setEnabled(true);
        }
    }
}
