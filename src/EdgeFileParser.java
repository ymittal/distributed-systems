import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public final class EdgeFileParser {

    public static List<GossipNode> parseFile(String filepath) {
        List<GossipNode> gossipNodes = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new FileReader(filepath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length != 2) {
                    throw new InputMismatchException(
                            "line must contain exactly one colon");
                } else {
                    int nodeId = Integer.parseInt(parts[0]);
                    List<Integer> edges = new ArrayList<>();
                    String listEdges = parts[1];
                    for (String edgeIdStr : listEdges.split(",")) {
                        edges.add(Integer.parseInt(edgeIdStr));
                    }
                    GossipNode node = new GossipNode(nodeId, edges);
                    gossipNodes.add(node);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gossipNodes;
    }

}
