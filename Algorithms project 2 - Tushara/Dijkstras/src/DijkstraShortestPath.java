
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class DijkstraShortestPath {

    static Map<Vertex<String>, Vertex<String>> parent = new HashMap<>();
    static ArrayList<ArrayList<String>> possibleRoutes = new ArrayList<>();
    static Graph<String> graph = new Graph<>(false);


    public String getRoute(String source, String destination)
    {
        Iterator itrRef;
        String cityName, output=destination;

        while (!source.equalsIgnoreCase(destination))
        {
            itrRef = parent.entrySet().iterator();

            while (itrRef.hasNext())
            {
                cityName = itrRef.next().toString();

                if (cityName.startsWith(destination))
                {
                    destination=cityName.substring(cityName.indexOf('=') + 1, cityName.length());
                    output+=","+destination;
                    break;
                }
            }
        }

        return output;
    }

    public Map<Vertex<String>,Integer> shortestPath(Graph<String> graph, Vertex<String> sourceVertex)
    {

        BinaryMinHeap<Vertex<String>> minHeap = new BinaryMinHeap<>();
        Map<Vertex<String>,Integer> distance = new HashMap<>();

        for(Vertex<String> vertex : graph.getAllVertex()){
            minHeap.add(Integer.MAX_VALUE, vertex);
        }

        minHeap.decrease(sourceVertex, 0);
        distance.put(sourceVertex, 0);
        parent.put(sourceVertex, null);

        while(!minHeap.empty())
        {

            BinaryMinHeap<Vertex<String>>.Node heapNode = minHeap.extractMinNode();
            Vertex<String> current = heapNode.key;
            distance.put(current, heapNode.weight);

            for(Edge<String> edge : current.getEdges())
            {
                Vertex<String> adjacent = getVertexForEdge(current, edge);
                if(!minHeap.containsData(adjacent))
                    continue;

                int newDistance = distance.get(current) + edge.getWeight();

                if(minHeap.getWeight(adjacent) > newDistance)
                {
                    minHeap.decrease(adjacent, newDistance);
                    parent.put(adjacent, current);
                }
            }
        }
        return distance;
    }

    private static void generateAllPossibleRoutes(ArrayList<String> str, int index)
    {

        if(index <= 0)
            possibleRoutes.add((ArrayList<String>)str.clone());
        else
        {
            generateAllPossibleRoutes(str, index-1);
            int currPos = str.size()-index;
            for (int i = currPos+1; i < str.size(); i++)
            {
                swap(str,currPos, i);
                generateAllPossibleRoutes(str, index-1);
                swap(str,i, currPos);
            }
        }
    }

    private  static void swap(ArrayList<String> str, int pos1, int pos2)
    {
        String t1 = str.get(pos1);
        str.set(pos1, str.get(pos2));
        str.set(pos2,t1);
    }

    public void calculateSDTrip(Graph<String> graph, ArrayList<String> destinationList, String startingVertex)
    {
        String pathUsed="",returnPath="";
        int newWeight=0,finalWeight=0;

        generateAllPossibleRoutes(destinationList, destinationList.size());

        for (int j = 0; j < possibleRoutes.size(); j++)
        {
            ArrayList<String> route = possibleRoutes.get(j);
            route.add(startingVertex);
            Vertex<String> sourceVertex = graph.getVertex(startingVertex);
            newWeight = 0; pathUsed = ""; returnPath="";

            for(int k=0; k<route.size();k++)
            {
                Map<Vertex<String>, Integer> distance = shortestPath(graph, sourceVertex);
                newWeight+=distance.get(graph.getVertex(route.get(k)));

                if(route.get(k).equalsIgnoreCase(startingVertex))
                    returnPath = getRoute(sourceVertex.toString(),startingVertex);
                else
                    pathUsed= (pathUsed !="") ? (pathUsed+"\n"+ getRoute(sourceVertex.toString(),route.get(k))) : getRoute(sourceVertex.toString(),route.get(k));

                sourceVertex = graph.getVertex(route.get(k));
            }
            finalWeight = (finalWeight==0) ? newWeight : finalWeight;

            if(newWeight<finalWeight)
                finalWeight = newWeight;

            route.remove(startingVertex);
        }

        System.out.println("\nTotal Distance(Shortest): "+ finalWeight);
        System.out.println("\nSource Path(Reverse-Order): \n\n"+pathUsed);
        System.out.println("\nReturn Path(Reverse-Order): "+returnPath);
    }

    public void calculateSPTree(Graph<String> graph, Vertex<String> sourceVertex, ArrayList<String> cityList)
    {
        Map<Vertex<String>, Integer> distance = shortestPath(graph, sourceVertex);

        for(int k=0; k<cityList.size();k++)
        {
            System.out.println(" ");
            System.out.println("From: "+ sourceVertex.toString() +" To: "+cityList.get(k));
            System.out.println("Shortest Distance: "+ distance.get(graph.getVertex(cityList.get(k))));
            System.out.println("Shortest Path(Reverse Order): "+ getRoute(sourceVertex.toString(),cityList.get(k)));
        }
    }

    public static void prepareGraph()
    {
        graph.addEdge("Charlotte", "Wilmington", 203);
        graph.addEdge("Charlotte", "Asheville", 112);
        graph.addEdge("Charlotte", "Atlanta", 240);
        graph.addEdge("Charlotte", "Columbia", 94);

        graph.addEdge("Wilmington", "Raleigh", 124);
        graph.addEdge("Wilmington", "Florence", 119);
        graph.addEdge("Wilmington", "Charleston", 167);

        graph.addEdge("Asheville", "Knoxville", 106);
        graph.addEdge("Asheville", "Chattanooga", 195);
        graph.addEdge("Asheville", "Atlanta", 205);
        graph.addEdge("Asheville", "Augusta", 172);
        graph.addEdge("Asheville", "Columbia", 159);

        graph.addEdge("Atlanta", "Knoxville", 224);
        graph.addEdge("Atlanta", "Chattanooga", 113);
        graph.addEdge("Atlanta", "Birmingham", 150);
        graph.addEdge("Atlanta", "Montgomery", 168);
        graph.addEdge("Atlanta", "Tifton", 182);
        graph.addEdge("Atlanta", "Savannah", 255);
        graph.addEdge("Atlanta", "Augusta", 150);
        graph.addEdge("Atlanta", "Columbia", 214);

        graph.addEdge("Columbia", "Raleigh", 205);
        graph.addEdge("Columbia", "Augusta", 69);
        graph.addEdge("Columbia", "Savannah", 158);
        graph.addEdge("Columbia", "Charleston", 113);
        graph.addEdge("Columbia", "Florence", 80);
        graph.addEdge("Columbia", "Raleigh", 205);

        graph.addEdge("Raleigh", "Florence", 147);

        graph.addEdge("Florence", "Savannah", 172);

        graph.addEdge("Charleston", "Augusta", 139);
        graph.addEdge("Charleston", "Savannah", 106);

        graph.addEdge("Knoxville", "Chattanooga", 111);

        graph.addEdge("Chattanooga", "Birmingham", 145);

        graph.addEdge("Birmingham", "Montgomery", 91);
        graph.addEdge("Birmingham", "Mobile", 240);
        graph.addEdge("Birmingham", "Tifton", 286);

        graph.addEdge("Montgomery", "Tifton", 200);
        graph.addEdge("Montgomery", "Mobile", 172);
        graph.addEdge("Montgomery", "Tallahassee", 202);
        graph.addEdge("Montgomery", "Savannah", 354);

        graph.addEdge("Augusta", "Savannah", 124);
        graph.addEdge("Augusta", "Jacksonville", 260);
        graph.addEdge("Augusta", "Tifton", 222);
        graph.addEdge("Augusta", "Savannah", 124);

        graph.addEdge("Savannah", "Jacksonville", 136);
        graph.addEdge("Savannah", "Tallahassee", 244);

        graph.addEdge("Mobile", "Tallahassee", 244);

        graph.addEdge("Tifton", "Tallahassee", 89);
        graph.addEdge("Tifton", "Jct.Int.75", 185);
        graph.addEdge("Tifton", "Jacksonville", 149);

        graph.addEdge("Tallahassee", "Jacksonville", 166);
        graph.addEdge("Tallahassee", "Jct.Int.75", 170);
        graph.addEdge("Tallahassee", "Tampa", 244);

        graph.addEdge("Jacksonville", "DaytonaBeach", 91);
        graph.addEdge("Jacksonville", "Jct.Int.75", 105);
        graph.addEdge("Jacksonville", "DaytonaBeach", 91);

        graph.addEdge("Jct.Int.75", "Tampa", 94);
        graph.addEdge("Jct.Int.75", "Miami", 302);
        graph.addEdge("Jct.Int.75", "Orlando", 75);
        graph.addEdge("Jct.Int.75", "Tampa", 94);

        graph.addEdge("Orlando", "Cocoa", 44);
        graph.addEdge("Orlando", "DaytonaBeach", 54);
        graph.addEdge("Orlando", "Tampa", 82);

        graph.addEdge("Tampa", "VeroBeach", 137);
        graph.addEdge("Tampa", "Ft.Myers", 125);

        graph.addEdge("Miami", "Keywest", 151);
        graph.addEdge("Miami", "Ft.Myers", 143);
        graph.addEdge("Miami", "VeroBeach", 140);

        graph.addEdge("Cocoa", "VeroBeach", 55);
        graph.addEdge("Cocoa", "DaytonaBeach", 66);

    }

    public ArrayList<String> getCityList()
    {
        ArrayList<String> destinationList = new ArrayList<>();
        destinationList.add("Jacksonville");
        destinationList.add("Tallahassee");
        destinationList.add("Orlando");
        destinationList.add("Tampa");
        destinationList.add("Miami");

        return destinationList;

    }

    private Vertex<String> getVertexForEdge(Vertex<String> v, Edge<String> e){
        return e.getVertex1().equals(v) ? e.getVertex2() : e.getVertex1();
    }

    public static void main(String args[])
    {
        prepareGraph();
        DijkstraShortestPath dsp = new DijkstraShortestPath();
        ArrayList<String> cityList = dsp.getCityList();
        Scanner sc=new Scanner(System.in);

        try
        {
            int inputOption = 0;

            while(inputOption!=3)
            {
                System.out.println ("\n1. Find Shortest Path Tree ? ");
                System.out.println ("2. Find Shortest Distance Trip ?");
                System.out.println ("3. Exit \n");
                System.out.println("Enter one of the options: ");
                inputOption = sc.nextInt();

                switch (inputOption)
                {
                    case 1:
                        System.out.println("Enter source location(case-sensitive): ");
                        Vertex<String> sourceVertex = graph.getVertex(sc.next());
                        dsp.calculateSPTree(graph, sourceVertex, cityList);
                        break;

                    case 2:
                        System.out.println("Enter source location(case-sensitive): ");
                        dsp.calculateSDTrip(graph, cityList, sc.next());
                        break;

                    case 3:
                        break;

                    default:
                        System.out.println("Invalid option");
                }
            }
        }
        catch(NullPointerException e)
        {
            System.out.println("Invalid source location"+e);
        }
    }
}