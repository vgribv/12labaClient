import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.Socket;

class Client {
    private String name;
    private int port;
    private String host;
    private BufferedReader str = new BufferedReader(new InputStreamReader(System.in));
    private BufferedWriter out;

    Client(String host, int port, String name){
        this.host = host;
        this.port = port;
        this.name = name;
    }

    void client() {
        try {
            try {
                try (Socket socket = new Socket(host, port); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    send(name);
                    long millis = System.currentTimeMillis();
                    while (!socket.isClosed()) {
                        if (str.ready()) {
                            String clientCommand = str.readLine();
                            send(clientCommand);
                            millis = System.currentTimeMillis();
                        }
                        if (in.ready()){
                            String str = in.readLine();
                            if(str.startsWith("@connectionCheck")){
                                send("@connectionCheck");
                                millis = System.currentTimeMillis();
                            }
                            else System.out.println(str);
                        }

                        if(millis + 4000 < System.currentTimeMillis()){
                            System.err.println("Сервер не отвечает.");
                            socket.close();
                        }
                    }
                } finally {
                    System.out.println("Завершаем работу клиента...");
                    out.close();
                    str.close();
                }
            }
            finally {
                System.out.println("Клиент закрыт.");
            }
        }
        catch (Exception ex){
            System.out.print("Сервер " + host + ":" + port + " недоступен. Ошибка: ");
            System.err.println(ex);
        }

    }
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}