// Pravin Cherian - Fernandez
//c3306899
//SENG4500
package pravin;

import java.net.*;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TaxServer {
    public static void main(String[] args) throws IOException {
        int defaultPortNumber = 8080;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the port number(Enter \"N\" for default) ");
        String portString = scanner.nextLine();
        int portNumber;
        if (portString.equals("N")) {
            portNumber = defaultPortNumber;
        } else {
            portNumber = Integer.parseInt(portString);
        }
        ServerSocket serverSocket = null;
        Socket socket = null;
        RangeGroup ranges = new RangeGroup();

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (Exception ex) {
            System.out.println("IUnable to create a server");
        }


        while (true) {
            try {
                String inputLine;
                socket = serverSocket.accept();
                while (true) {
                    BufferedReader in;
                    PrintWriter out;
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    inputLine = in.readLine();// Outputs on ServerSide

                    switch (inputLine) {
                        case "TAX":
                            out.println("TAX: OK");
                            break;
                        case "STORE":
                            int start = Integer.parseInt(in.readLine());
                            String endString = in.readLine();
                            int end = endString.equals("~") ? Integer.MAX_VALUE : Integer.parseInt(endString);// Store function allowing user to enter Start and End income, Base tax and tax in cents
                            int baseTax = Integer.parseInt(in.readLine());
                            int tax = Integer.parseInt(in.readLine());

                            ranges.ranges = ranges.addRange(new Range(start, end, baseTax, tax));
                            out.println("STORE: OK");
                            break;
                        case "QUERY":
                            for (Range range : ranges.ranges) {
                                out.println(range.start + " " + ((range.end == Integer.MAX_VALUE)? "~":range.end) + " " + range.baseTax + " " + range.tax);
                            }
                            out.println("QUERY: OK");
                            break;
                        case "BYE":
                            out.println("BYE: OK");
                            out.close();
                            in.close();
                            socket.close();
                            break;
                        case "END":
                            out.println("END: OK");
                            out.close();
                            in.close();
                            System.exit(0);
                            break;
                        default:
                            // code block for calculating tax
                            double calculatedTax = calculateTax(ranges.ranges, Integer.parseInt(inputLine));
                            if (calculatedTax == -1) {
                                out.println("I DON'T KNOW " + inputLine);
                            } else {
                                out.println(calculatedTax);
                            }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Client disconnected.");
            }
        }
    }

    private static double calculateTax(List<Range> ranges, int salary) {
        ranges.sort(Comparator.comparingInt(o -> o.start));
        double tax = -1;
        for (int index = 0; index < ranges.size(); index++) {
            if (salary >= ranges.get(index).end) {
                if (index == ranges.size() - 1) {
                    return -1;
                }
                if (tax == -1) tax = 0;
                tax += ranges.get(index).baseTax + (ranges.get(index).end - ranges.get(index).start + 1) * ranges.get(index).tax / 100.0;
            } else if (salary >= ranges.get(index).start && salary <= ranges.get(index).end) {
                if (tax == -1) tax = 0;
                tax += ranges.get(index).baseTax + (salary - ranges.get(index).start + 1) * ranges.get(index).tax / 100.0;// Displaying
            } else {
                break;
            }
        }
        return tax;
    }
}