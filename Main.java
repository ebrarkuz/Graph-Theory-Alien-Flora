import java.io.File;

public class Main {
    public static void main(String[] args) {
        // TODO: Create an instance of AlienFlora and run readGenomes. After
        AlienFlora alienFlora=new AlienFlora( new File(args[0]));

        System.out.println("##Start Reading Flora Genomes##");
        alienFlora.readGenomes();


        System.out.println("##Start Evaluating Possible Evolutions##");
        alienFlora.evaluateEvolutions();


        System.out.println("##Start Evaluating Possible Adaptations##");
        alienFlora.evaluateAdaptations();


    }
}
