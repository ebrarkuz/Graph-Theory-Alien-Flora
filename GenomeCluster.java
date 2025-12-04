import java.util.*;

public class GenomeCluster {
    public Map<String, Genome> genomeMap = new HashMap<>();

    public void addGenome(Genome genome) {
        genomeMap.put(genome.id, genome);
        // TODO: Add genome to the cluster
    }

    public boolean contains(String genomeId) {
        return genomeMap.containsKey(genomeId);
        // TODO: Return true if the genome is in the cluster

    }

    public Genome getMinEvolutionGenome() {
        // TODO: Return the genome with minimum evolutionFactor
        Genome minGenome = null;
        for (Genome genome : genomeMap.values()) {
            if (minGenome == null || genome.evolutionFactor < minGenome.evolutionFactor) {
                minGenome = genome;
            }
        }
        return minGenome;
    }
    private static class Node {
        String id;
        int dist;
        Node(String id, int dist) {
            this.id = id;
            this.dist = dist;
        }
    }
    public int dijkstra(String startId, String endId) {
        // TODO: Implement Dijkstra's algorithm to return shortest path

        // make all distances infinite
        Map<String, Integer> dist = new HashMap<>();
        for (String id : genomeMap.keySet()) {
            dist.put(id, Integer.MAX_VALUE);
        }
        // make startId 0
        dist.put(startId, 0);

        // create a priority queue
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
        pq.add(new Node(startId, 0));


        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (curr.id.equals(endId)) {
                return curr.dist;
            }

            if (curr.dist > dist.get(curr.id)) continue;


            Genome g = genomeMap.get(curr.id);
            for (Genome.Link link : g.links) {
                String nei = link.target;
                int weight = link.adaptationFactor;
                int alt = curr.dist + weight;
                if (alt < dist.get(nei)) {
                    dist.put(nei, alt);
                    pq.add(new Node(nei, alt));
                }
            }
        }

        return -1;
    }
}

