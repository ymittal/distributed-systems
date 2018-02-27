import util.EdgeFileParser;

public class Simulator {

    public static void main(String[] args) {
        int count = args.length;
        if (count < 2) {
            throw new IllegalArgumentException("At least two args required");
        }

        String edgeFilename = args[0];
        EdgeFileParser.parseFile(edgeFilename);
        int startNodeId = Integer.parseInt(args[1]);
    }
}
