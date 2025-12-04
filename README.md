# FloraX: Alien Genome Analysis 🧬

## About the Project
This is a Java project for the BBM204 Software Laboratory II course. The project simulates the analysis of alien plant genomes found on a new planet.

It reads data from an XML file and uses **Graph Theory** algorithms to solve biological problems.

## Key Features
The project has three main parts:

1.  **Genome Graph (Clustering):**
    * It reads the XML file and connects Genomes (nodes) to create a Graph.
    * It finds connected groups of genomes (Clusters).

2.  **Evolution Analysis:**
    * It calculates the minimum energy required for two different clusters to evolve.
    * It finds the best connection (minimum weight) between groups.

3.  **Adaptation Analysis (Dijkstra):**
    * It finds the shortest adaptation path between two genomes in the same cluster.
    * It uses **Dijkstra's Algorithm** to find the minimum cost.

## Technical Details
* **Language:** Java (OpenJDK 11)
* **Input Format:** XML
* **Libraries:** No external libraries used. Standard Java only.

## How to Run
You can compile and run the project using the terminal.

**1. Compile the code:**
```bash
javac *.java -d .
