import org.xml.sax.SAXException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AlienFlora {
    private File xmlFile;
    List<GenomeCluster> clusters = new ArrayList<>();

    public AlienFlora(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void readGenomes() {
        try {
            // read genomes from xml
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Map<String, Genome> genomes = new HashMap<>();
            NodeList genomeList = doc.getElementsByTagName("genome");
            for (int i = 0; i < genomeList.getLength(); i++) {
                Element gElem = (Element) genomeList.item(i);
                String id = gElem.getElementsByTagName("id").item(0).getTextContent();
                int evo = Integer.parseInt(
                        gElem.getElementsByTagName("evolutionFactor").item(0).getTextContent()
                );
                Genome g = new Genome(id, evo);

                NodeList linkList = gElem.getElementsByTagName("link");
                for (int j = 0; j < linkList.getLength(); j++) {
                    Element lElem = (Element) linkList.item(j);
                    String target = lElem.getElementsByTagName("target").item(0).getTextContent();
                    int adapt = Integer.parseInt(
                            lElem.getElementsByTagName("adaptationFactor").item(0).getTextContent()
                    );
                    g.addLink(target, adapt);
                }
                genomes.put(id, g);
            }

            // create clusters

            Set<String> visited = new HashSet<>();
            for (String id : genomes.keySet()) {
                if (!visited.contains(id)) {
                    GenomeCluster cluster = new GenomeCluster();
                    dfs(id, genomes, visited, cluster);
                    this.clusters.add(cluster);
                }
            }



            System.out.println("Number of Genome Clusters: " + clusters.size());

            List<String> out = new ArrayList<>();
            for (GenomeCluster c : clusters) {
                List<String> ids = new ArrayList<>(c.genomeMap.keySet());
                out.add(ids.toString());
            }
            System.out.println("For the Genomes: [" + String.join(", ", out) + "]");
            System.out.println("##Reading Flora Genomes Completed##");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    //helper method for dfs algorithm
    private void dfs(
            String currentId,
            Map<String, Genome> genomes,
            Set<String> visited,
            GenomeCluster cluster
    ) {
        visited.add(currentId);
        cluster.addGenome(genomes.get(currentId));


        for (Genome.Link link : genomes.get(currentId).links) {
            if (!visited.contains(link.target)) {
                dfs(link.target, genomes, visited, cluster);
            }
        }

        for (Map.Entry<String, Genome> entry : genomes.entrySet()) {
            String otherId = entry.getKey();
            if (visited.contains(otherId)) continue;
            for (Genome.Link link : entry.getValue().links) {
                if (link.target.equals(currentId)) {
                    dfs(otherId, genomes, visited, cluster);
                    break;
                }
            }
        }
    }

    public void evaluateEvolutions() {
        // TODO:
        try {

            DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = fabrika.newDocumentBuilder();
            Document file = builder.parse(xmlFile);
            file.getDocumentElement().normalize();
            Element evoContainer = (Element) file
                    .getElementsByTagName("possibleEvolutionPairs")
                    .item(0);


            NodeList pairList = evoContainer
                    .getElementsByTagName("pair");
            List<Double> evolutionPairs = new ArrayList<>();
            int certifiedCount=0;
            for (int i = 0; i < pairList.getLength(); i++) {
                Element pairElem = (Element) pairList.item(i);
                String firstId = pairElem
                        .getElementsByTagName("firstId")
                        .item(0)
                        .getTextContent();
                String secondId = pairElem
                        .getElementsByTagName("secondId")
                        .item(0)
                        .getTextContent();
                GenomeCluster c1 = null, c2 = null;
                for (GenomeCluster c : clusters) {
                    if (c.contains(firstId)) c1 = c;
                    if (c.contains(secondId)) c2 = c;
                    if (c1 != null && c2 != null) break;
                }

                if(c1==c2){
                    evolutionPairs.add(-1.0);

                }else{
                    Genome minG1 = c1.getMinEvolutionGenome();
                    Genome minG2 = c2.getMinEvolutionGenome();

                    int minEvo1= minG1.evolutionFactor;
                    int minEvo2= minG2.evolutionFactor;

                    double result= (minEvo1+minEvo2)/ 2.0;
                    evolutionPairs.add(result);
                    certifiedCount++;


                }


            }
            System.out.println("Number of Possible Evolutions: " + evolutionPairs.size());
            System.out.println("Number of Certified Evolution: " + certifiedCount);
            System.out.println("Evolution Factor for Each Evolution Pair: " + evolutionPairs);
            System.out.println("##Evaluated Possible Evolutions##");


        }catch (Exception e) {
            e.printStackTrace();
        }


        // - Parse and process possibleEvolutionPairs
        // - Find min evolution genome in each cluster
        // - Calculate and print evolution factors
    }

    public void evaluateAdaptations() {
        // TODO:
        try {
            DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = fabrika.newDocumentBuilder();
            Document file = builder.parse(xmlFile);
            file.getDocumentElement().normalize();
            Element adaptContainer = (Element) file
                    .getElementsByTagName("possibleAdaptationPairs")
                    .item(0);
            NodeList pairList = adaptContainer.getElementsByTagName("pair");

            List<Integer> adaptationFactors = new ArrayList<>();
            int certifiedCount = 0;

            for (int i = 0; i < pairList.getLength(); i++) {
                Element pairElem = (Element) pairList.item(i);
                String firstId  = pairElem.getElementsByTagName("firstId").item(0).getTextContent();
                String secondId = pairElem.getElementsByTagName("secondId").item(0).getTextContent();
                GenomeCluster c1 = null, c2 = null;
                for (GenomeCluster c : clusters) {
                    if (c.contains(firstId))  c1 = c;
                    if (c.contains(secondId)) c2 = c;
                    if (c1 != null && c2 != null) break;
                }
                if (c1 != null && c1 == c2) {

                    int dist = c1.dijkstra(firstId, secondId);
                    adaptationFactors.add(dist);

                    if (dist != -1) certifiedCount++;
                } else {

                    adaptationFactors.add(-1);
                }

            }
            System.out.println("Number of Possible Adaptations: " + adaptationFactors.size());
            System.out.println("Number of Certified Adaptations: " + certifiedCount);
            System.out.println("Adaptation Factor for Each Adaptation Pair: " + adaptationFactors);
            System.out.println("##Evaluated Possible Adaptations##");



        }catch (Exception e) {
            e.printStackTrace();
        }
        // - Parse and process possibleAdaptationPairs
        // - If genomes in same cluster, use Dijkstra to calculate min path
        // - Print adaptation factors
    }
}
