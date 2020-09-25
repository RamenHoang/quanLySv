import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<MyTask> tasks;
    private ServerSocket server;
    private String url;
    private String username;
    private String password;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server _server = new Server();
        _server.waitingConnection();
    }

    public Server() throws IOException {
        this.server = new ServerSocket(8083);
        this.tasks = new ArrayList<>();
        this.url = "jdbc:mysql://34.126.120.35:3306/ltm";
        this.username = "root";
        this.password = "root1999";
        log("Server is running ...");
    }

    private void waitingConnection() throws IOException {
        while (true) {
            Socket socket = server.accept();
            try {
                this.addTask(new MyTask(socket));
            } catch (Exception e) {
                socket.close();
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }

    private synchronized void addTask(MyTask task) {
        this.tasks.add(task);
    }

    private synchronized void removeTask(MyTask task) {
        this.tasks.remove(task);
    }

    private void log(Object s) {
        System.out.println(s);
    }

    public class MyTask extends Thread {
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private Connection connection;
        private String response;

        // Name of service; svLogin = Service Login :D
        private static final String LOGIN = "svLogin";
        private static final String GET_STUDENT = "svGet";
        private static final String UPDATE_STUDENT = "svUpdate";
        private static final String NEW_STUDENT = "svNew";
        private static final String DELETE_STUDENT = "svDelete";

        public MyTask(Socket _socket) throws IOException {
            this.socket = _socket;
            this.in = new Scanner(this.socket.getInputStream());
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.response = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection(url, username, password);
                if (this.connection != null) {
                    log("Connect db success - " + this.socket.getLocalPort());
                }
            } catch (Exception e) {
                log(e.getMessage());
                log(e.getStackTrace());
            }
            this.start();
        }

        @Override
        public void run() {
            this.doMission();
        }

        private void doMission() {
            try {
                System.out.println("Connected: " + this.socket);
                String request = in.nextLine();
                log(request);
                this.doResponse(request);
            } catch (Exception e) {
                log(e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    this.close();
                    System.out.println("Closed socket" + this.socket);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                }
            }
        }
        
        /**
         * @param request -  Example: "statusCode/data1/data2/data3/..."
         */
        private void doResponse(String request) {
            String[] requestArr = request.split("/");
            if (requestArr[0].equals(MyTask.LOGIN)) {
                this.doSvLogin(requestArr);
            }
            if (requestArr[0].equals(MyTask.GET_STUDENT)) {
                this.doSvGetStudent();
                return;
            }
            if (requestArr[0].equals(MyTask.NEW_STUDENT)) {
                this.doSvNewStudent(requestArr);
                return;
            }
            if (requestArr[0].equals(MyTask.UPDATE_STUDENT)) {
                	this.doSvUpdateStudent(requestArr);
                return;
            }
            if (requestArr[0].equals(MyTask.DELETE_STUDENT)) {
                	this.doSvDeleteStudent(requestArr);
                return;
            }
        }

        /**
         * @param requestArr - ["statusCode", "data1", "data2", "data3", ...]
         */
        private void doSvLogin(String[] requestArr) {
            String query = "SELECT * from AutUser where username='" + requestArr[1] + "' and password='" + requestArr[2]
                    + "'";
            try {
                Statement statement = this.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    this.response = "200/" + resultSet.getInt(1);
                    this.out.println(this.response);
                }
                this.connection.close();
            } catch (SQLException e) {
                try {
                    this.connection.close();
                } catch (Exception _e) {
                    log(_e.getMessage());
                    _e.printStackTrace();
                }
                log(e.getMessage());
                e.printStackTrace();
            }
        }

        private void doSvGetStudent() {
            String query = "SELECT * from Student";
            try {
                Statement statement = this.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                StringBuilder _res = new StringBuilder();
                _res.append("200/");
                while (resultSet.next()) {
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        _res.append(resultSet.getString(i) + ";");
                    }
                    _res.deleteCharAt(_res.length() - 1);
                    _res.append("/");
                }
                this.response = _res.deleteCharAt(_res.length() - 1).toString();
                this.out.println(this.response);
                this.connection.close();
            } catch (SQLException e) {
            		this.out.println("400/" + e.getMessage());
                try {
                    this.connection.close();
                } catch (Exception _e) {
                    log(_e.getMessage());
                    _e.printStackTrace();
                }
                log(e.getMessage());
                e.printStackTrace();
            }
        }

        private void doSvNewStudent(String[] requestArr) {
        	try {
        		String[] studentStrings = requestArr[1].split(";");
        		String query = String.format("INSERT INTO Student (Name, Age, Class, GPA) VALUES ('%s', %s, '%s', %s)", 
        				studentStrings[0], studentStrings[1], studentStrings[2], studentStrings[3]);
        		Statement statement = this.connection.createStatement();
        		int rowEffected = statement.executeUpdate(query);
        		if (rowEffected > 0) {
        			this.out.println("200/Updated " + rowEffected + " rows");
        		} else {
        			this.out.println("400/Some errors occurs");
        		}
        		this.connection.close();
        	} catch(Exception e) {
        		log(e.getMessage());
        		log(e.getStackTrace());
        		this.out.println("400/" + e.getMessage());
        	}
        }
        
        private void doSvUpdateStudent(String[] requestArr) {
        	try {
        		String[] studentStrings = requestArr[1].split(";");
        		String query = String.format("UPDATE Student SET Name='%s', Age=%s, Class='%s', GPA=%s WHERE id=%s", 
        				studentStrings[1], studentStrings[2], studentStrings[3], studentStrings[4], studentStrings[0]);
        		Statement statement = this.connection.createStatement();
        		int rowEffected = statement.executeUpdate(query);
        		if (rowEffected > 0) {
        			this.out.println("200/Updated " + rowEffected + " rows");
        		} else {
        			this.out.println("400/Some errors occurs");
        		}
        		this.connection.close();
        	} catch(Exception e) {
        		log(e.getMessage());
        		log(e.getStackTrace());
        		this.out.println("400/" + e.getMessage());
        	}
        }
        
        private void doSvDeleteStudent(String[] requestArr) {
        	try {
        		String[] studentStrings = requestArr[1].split(";");
        		String query = String.format("DELETE FROM Student WHERE id=%s", 
        				studentStrings[1]);
        		Statement statement = this.connection.createStatement();
        		int rowEffected = statement.executeUpdate(query);
        		if (rowEffected > 0) {
        			this.out.println("200/Deleted " + rowEffected + " rows");
        		} else {
        			this.out.println("400/Some errors occurs");
        		}
        		this.connection.close();
        	} catch(Exception e) {
        		log(e.getMessage());
        		log(e.getStackTrace());
        		this.out.println("400/" + e.getMessage());
        	}
        }

        private void close() {
            try {
                this.socket.close();
                removeTask(this);
            } catch (IOException e) {
                log(e.getMessage());
                log(e.getStackTrace());
            }
        }
    }
}
