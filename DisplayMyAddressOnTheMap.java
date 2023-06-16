import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class DisplayMyAddressOnTheMap {
    public static void main(String[] args) {
        String inputFile = "C:\\CST7284\\input\\InputAddresses.txt";
        String outputFile = "C:\\CST7284\\output\\OutputAddresses.csv";

        try {
            parseAddresses(inputFile, outputFile);
            System.out.println("Output file generated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void parseAddresses(String inputFile, String outputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        // Write the header row
        writer.write("First Name,Last Name,Spouse First Name,Spouse Last Name,Street Number,Street Name,Street Type,Street Orientation,City,Province");
        writer.newLine();

        String line;
        Person person = new Person();
        Address address = new Address();
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                // Empty line denotes the end of a record
                writeRecord(writer, person, address);
                // Reset person and address for the next record
                person = new Person();
                address = new Address();
            } else if (person.getFirstName() == null) {
                // First line contains the names
                String[] names = line.replace(",", "").split("\\s+(and\\s+)?");
                System.out.println("Split names: " + Arrays.toString(names));

                // First name
                person.setFirstName(names[0]);

                // Last name
                if (line.contains("and")) {
                    person.setLastName(names[names.length - 1]);
                } else {
                    person.setLastName(names[1]);
                }

                // Spouse first name
                if (names.length > 2) {
                    person.setSpouseFirstName(names[names.length - 2]);
                } else {
                    person.setSpouseFirstName("");
                }

                // Spouse last name
                if (names.length > 2) {
                    person.setSpouseLastName(names[names.length - 1]);
                } else {
                    person.setSpouseLastName("");
                }
            } else if (address.getStreetNumber() == null) {
                // Second line contains the address
            	String[] addresses = line.trim().split("\\s+(?<!St\\s)");
                System.out.println("Split addresses: " + Arrays.toString(addresses));

                // Street number
                address.setStreetNumber(addresses[0].replaceAll("^.*-", ""));
                
                // Street name
                address.setStreetName(addresses[1]);
                
                // Street type
                address.setStreetType(addresses[2]);
                
                // Street Orientation
                if (addresses.length > 3) {
                    address.setStreetOrientation(addresses[3].substring(0, 1));
                } else {
                	address.setStreetOrientation(" ");
                }
            } else if (address.getCity() == null) {
                    // Third line contains the city and province
                    String[] cityAndProvince = line.split(",\\s*");
                    System.out.println("Split city and province: " + Arrays.toString(cityAndProvince));

                    // City
                    address.setCity(cityAndProvince[0]);

                    // Province
                    address.setProvince(cityAndProvince[1].substring(0, 2));
                }     
            }
        
        // Write the last record
        writeRecord(writer, person, address);

        reader.close();
        writer.close();
    }

    private static void writeRecord(BufferedWriter writer, Person person, Address address) throws IOException {
        writer.write(formatValue(person.getFirstName())); // First name
        writer.write("," + formatValue(person.getLastName())); // Last name
        writer.write("," + formatValue(person.getSpouseFirstName())); // Spouse first name
        writer.write("," + formatValue(person.getSpouseLastName())); // Spouse last name
        writer.write("," + formatValue(address.getStreetNumber())); // Street number
        writer.write("," + formatValue(address.getStreetName())); // Street name
        writer.write("," + formatValue(address.getStreetType())); // Street type
        writer.write("," + formatValue(address.getStreetOrientation())); // Street orientation
        writer.write("," + formatValue(address.getCity())); // City
        writer.write("," + formatValue(address.getProvince())); // Province
        writer.newLine();
    }

    private static String formatValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        } else {
            return value;
        }
    }
}
