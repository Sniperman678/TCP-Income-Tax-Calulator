// Pravin Cherian - Fernandez
//c3306899
//SENG4500
package pravin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;


public class TaxClient {
    public static void main(String[] args) throws IOException {

        String defaultHostname = "127.0.0.1";
        int defaultPortNumber = 8080;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the host IP(Enter \"N\" for default) ");
        String hostName = scanner.nextLine();
        if(hostName.equals("N")) {
            hostName = defaultHostname;
        }

        System.out.println("Enter the port number(Enter \"N\" for default) ");
        String portString = scanner.nextLine();
        int portNumber;
        if(portString.equals("N")) {
            portNumber = defaultPortNumber;
        } else {
            portNumber =Integer.parseInt(portString);
        }

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);//Writing or Send to Server
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));//Reading from Server
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))// Reading from Keyboard
        ) {
            String userInput;//User Inputs on ClientSide BYE, END, QUERY, STORE each case.
            while (true) {
                userInput = stdIn.readLine();
                switch (userInput) {
                    case "BYE":
                    case "END":
                        out.println(userInput);
                        System.out.println(in.readLine());
                        stdIn.close();
                        in.close();
                        out.close();
                        System.exit(0);
                    case "QUERY":
                        out.println(userInput);
                        String temp;
                        do {
                            temp = in.readLine();
                            System.out.println(temp);
                        } while(!temp.equals("QUERY: OK"));
                        break;
                    case "STORE":
                        out.println(userInput);
                        for (int i = 0; i <= 3; i++) {
                            out.println(stdIn.readLine());
                        }
                        System.out.println(in.readLine());
                        break;
                    default:        //Handling calculate tax
                        out.println(userInput);
                        System.out.println(in.readLine());
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}