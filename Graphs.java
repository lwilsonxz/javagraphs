import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


public class Graphs
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner keyboard = new Scanner(System.in);
		int input;
		int startNode;  //Variable to hold input for shortestPath starting node.
		int[][] graph;
		int nodeCount = 0; //Variable to hold the number of nodes in the graph.
		int edgeCount = 0; //Variable to hold the number of edges each node has.
		String graphName = "";
		File file;

		System.out.print("Enter the file for the undirected graph to be analyzed: ");
		graphName = keyboard.nextLine();
		file = new File(graphName);
		Scanner fileScan;

		fileScan = new Scanner(file);
		nodeCount = fileScan.nextInt();
		graph = new int[nodeCount][nodeCount];


		while(fileScan.hasNextInt()) //Fill in array with edge weights corresponding to connected nodes.
		{			
			for(int i = 0; i < nodeCount; ++i)
			{
				edgeCount = fileScan.nextInt();

				for(int j = 0; j < edgeCount; ++j)
				{
					graph[i][fileScan.nextInt()] = fileScan.nextInt();

				}
			}
		} //End filling array.

		menu(); //Prints menu.

		do
		{
			input = keyboard.nextInt();  //Get user's menu choices.

			switch(input)
			{
			case 1:  //isConnected()
				if(isConnected(graph))
					System.out.println("\nGraph is connected.\n");
				else
					System.out.println("\nGraph is not connected.\n");

				menu();

				break;

			case 2:  //minSpanningTree()
				if ( isConnected(graph) )
					print(minSpanningTree(graph));
				else
					System.out.println("Error: Graph is not connected.\n");

				menu();

				break;

			case 3:  //shortestPath()
				System.out.print("\nFrom which node would you like to find the shortest paths (0-" + (nodeCount - 1) + "): ");
				startNode = keyboard.nextInt();

				shortestPath(graph, startNode);

				menu();

				break;

			case 4:  //isMetric
				isMetric(graph);

				menu();

				break;

			case 5:  //makeMetric
				makeMetric(graph);
				print(graph);

				menu();

				break;

			case 6:  //tsp()

				if(!isConnected(graph))
					System.out.println("Error: Graph is not connected.\n");
				else
				{
					int[] pathSoFar = new int[graph.length+1];
					int[] bestPath = new int[graph.length+1];
					pathSoFar[graph.length] = Integer.MAX_VALUE;
					for ( int i = 0 ; i < graph.length ; ++i )
						pathSoFar[i] = -1; //load array with -1s, so we can add 0 without accidentally thinking we already did
					bestPath[graph.length] = Integer.MAX_VALUE;
					
					tsp(graph, pathSoFar, 0, bestPath, 0); //start with the current node at 1, and path index at 1, as zero is the beginning of all things cf. Big Boss's speech at the end of Metal Gear Solid 4.
					
					System.out.print(bestPath[graph.length] + ": ");
					for ( int i = 0 ; i < graph.length ; ++i )
						System.out.print(bestPath[i] + " ");
				}
				System.out.println("\n");

				menu();

				break;

			case 7:  //approxTsp()
				approxTsp(graph);

				menu();

				break;

			case 8:  //quit
				break;

			default:
				System.out.println("Invalid input.\n");

				menu();
				break;

			} //End of switch.
		} while(input != 8);

	}

	private static void menu()  //Method to print menu options.
	{
		System.out.println( "1. Is Connected");
		System.out.println( "2. Minimum Spanning Tree");
		System.out.println( "3. Shortest Path");
		System.out.println( "4. Is Metric");
		System.out.println( "5. Make Metric");
		System.out.println( "6. Traveling Salesman Problem");
		System.out.println( "7. Approximate TSP");
		System.out.println( "8. Quit");

		System.out.print("Make your choice (1-8) ");
	}

	private static void print(int[][] graph) //Method to print adjacency matrix.
	{
		String temp = "";
		int count = 0;

		System.out.print("\n");
		System.out.println(graph.length);

		for(int i = 0; i < graph.length; ++i)
		{
			for(int j = 0; j < graph.length; ++j)
			{
				if(graph[i][j] != 0)
				{
					temp += j + " " + graph[i][j] + " ";
					count++;
				}
			}

			System.out.println(count + " " + temp);
			temp = "";
			count = 0;
		}

		System.out.print("\n");
	}

	private static void printShortest(int[] shortest) //Method to print shortest path.
	{
		System.out.print("\n");
		for(int i = 0; i < shortest.length; ++i)
		{
			if(shortest[i] == Integer.MAX_VALUE)
				System.out.println(i + ": Infinity");

			else
				System.out.println(i + ": " + shortest[i]);
		}

		System.out.print("\n");
	}

	private static boolean isConnected(int[][] graph)  //Method to determine if the graph is connected or not.
	{
		int[] temp = new int[graph.length]; //Array to determine if graph is connected.

		dfs(graph, temp, 0, 1);


		for(int i = 0; i < temp.length; ++i)
			if(temp[i] == 0)
				return false;

		return true;
	}

	private static int dfs(int[][] graph, int[] temp, int v, int i)  //Helper method for isConnected. Depth first search.
	{
		temp[v] = i++;
		for ( int k = 0 ; k < graph.length ; ++k )
		{
			if ( graph[v][k] > 0 && temp[k] == 0 )
				i = dfs( graph, temp, k, i );
		}
		
		return i;
	}

	private static int[][] minSpanningTree(int[][] graph)  //Method to find the minimum spanning tree of the graph using Prim's algorithm.
	{ //we only enter this method if the tree is connected
		int closestNode = 0;
		int[][] mst = new int[graph.length][graph.length];
		boolean[] connected = new boolean[graph.length];

		connected[0] = true;

		while(!allTrue(connected))
		{
			int shortest = Integer.MAX_VALUE;
			int connectedNode = -1;
			int notConnectedNode = -1;

			for(int j = 0; j < graph.length; ++j)
				if(connected[j])
					for(int k = 0; k < graph.length; ++k)
						if(!connected[k])
							if(graph[j][k] < shortest && graph[j][k] != 0)
							{
								shortest = graph[j][k];
								connectedNode = j;
								notConnectedNode = k;
							}

			if(shortest != Integer.MAX_VALUE)
			{
				mst[connectedNode][notConnectedNode] = shortest;
				mst[notConnectedNode][connectedNode] = shortest;

				connected[notConnectedNode] = true;
			}

		}

		return mst; //return minimum spanning tree
	}

	private static boolean allTrue(boolean[] connected) //Helper method for minSpanningTree. Checks if all nodes have been connected.
	{
		for(int i = 0; i < connected.length; ++i)
			if(!connected[i])
				return false;

		return true;
	}

	private static void shortestPath(int[][] graph, int start)  //Method to print lengths of shortest paths from given node to all other nodes.
	{
		printShortest(dijkstras(graph, start));
	}

	private static void isMetric(int[][] graph)  //Method to determine is graph is metric or not.
	{
		if(!completeConnect(graph))
		{
			System.out.println("\nGraph is not metric: Graph is not completely connected.\n");
			return;
		}

		for(int i = 0; i < graph.length; ++i)
		{	

			int[] shortest = new int[graph.length];
			shortest = dijkstras(graph, i);

			for(int j = 0; j < graph.length; ++j)
			{				
				if(graph[i][j] > shortest[j])
				{
					System.out.println("\nGraph is not metric: Edges do not obey the triangle inequality.\n");
					return;
				}

			}
		}

		System.out.println("\nGraph is metric.\n");
	}

	private static boolean completeConnect(int[][] graph) //Helper method for isMetric(). Determines if graph is completely connected.
	{
		int count = 0;

		for(int i = 0; i < graph.length; ++i)
		{
			for(int j = 0; j < graph.length; ++j)
			{
				if(graph[i][j] != 0)
				{
					count++;
				}
			}
			if(count != (graph.length - 1))
				return false;

			count = 0;
		}

		return true;

	}

	private static int[] dijkstras(int[][] graph, int firstNode)  //Calculates Dijkstras algorithm.
	{
		int[] shortest = new int[graph.length];
		boolean[] bestKnown = new boolean[graph.length];
		int max = Integer.MAX_VALUE;
		int currentNode = firstNode;

		for(int i = 0; i < graph.length; ++i)
		{
			shortest[i] = max;
			bestKnown[i] = false;
		}

		bestKnown[firstNode] = true;
		shortest[firstNode] = 0;

		for(int i = 1; i < graph.length; ++i)
		{
			//Filling shortest[] with possible distances between current node and all neighbors.
			for(int j = 0; j < graph.length; ++j)
			{
				if(!bestKnown[j] && currentNode != j) //Make sure there was not a best known distance, and node is not on itself.
				{
					if(graph[currentNode][j] != 0) //Make sure there is a connection between the two.
					{
						if(shortest[j] == Integer.MAX_VALUE || shortest[j] > shortest[currentNode] + graph[currentNode][j]) //If shortest distance has not been found, or new distance is better.
							shortest[j] = shortest[currentNode] + graph[currentNode][j]; //Fill in new, better distance.
					}
				}
			}

			//Finding the lowest weighted edge connected to currentNode.
			int temp = -1; 

			for(int j = 0; j < graph.length; ++j)
			{
				if(!bestKnown[j]) //Node that does not  have a closest distance found yet.
				{
					if(shortest[j] != Integer.MAX_VALUE) //If a shortest distance has been found.
					{
						if(temp == -1 || shortest[j] < shortest[temp]) //If haven't found any other shortest distance yet, or if new distance is better.
							temp = j;
					}
				}
			}

			if(temp == -1) //No shortest path was found, so currentNode is shortest.
				bestKnown[currentNode] = true;

			else //If shortest path was found, set to new shortest path.
			{
				currentNode = temp;
				bestKnown[currentNode] = true;
			}
		}

		return shortest;
	}

	private static void makeMetric(int[][] graph)
	{
		if(!isConnected(graph))
		{
			System.out.println("Error: Graph is not connected.\n");
			return;
		}

		else
		{
			for(int i = 0; i < graph.length; ++i)
			{
				int[] shortest = dijkstras(graph, i);

				for(int j = 0; j < graph.length; ++j)
				{
					if(graph[i][j] == 0 && i != j)
						graph[i][j] = shortest[j];
				}
			}
		}

	}

	/**
	 * 
	 * @param graph is the graph
	 * @param pathSoFar contains the order of the nodes we visit, and its final element is the distance so far
	 * @param currentNode represents the node that each call to the tsp is on
	 * @param bestPath records the best path we have found so far, with the distance as the last element
	 * @param pathIndex is where we are in the path
	 */
	private static void tsp(int[][] graph, int[] pathSoFar, int currentNode, int[] bestPath, int pathIndex)
	{
		
		if ( pathIndex >= pathSoFar.length - 1 && currentNode == 0 ) //if we have added every node to this path, AND we are looking at the path between the last node and 0
		{
			//if we're in here, then we have found a tour!
			pathSoFar[pathSoFar.length-1] = getPathLength(graph, pathSoFar); //add the distance between the last node and 0 to the total distance
			if ( pathSoFar[pathSoFar.length-1] < bestPath[bestPath.length-1] ) //if the total distance for this path is less than the best path we've found...
				for ( int i = 0 ; i < pathSoFar.length ; ++i )
					bestPath[i] = pathSoFar[i]; //then copy all the values on over
			return; //no need to keep going if we've found a tour
		}
		
		if ( arrayContains(pathSoFar, currentNode) ) //if we already have the current node, we can't visit it again. Unless it is where we started and is our last step; see above
			return;
		
		//then, after we've checked our base cases, we add this node to the path and loop through everything it's connected to
		pathSoFar[pathIndex] = currentNode;
		for ( int i = 0 ; i < graph.length ; ++i )
			if ( graph[currentNode][i] > 0 ) //if there is a path between the current node and node i
				tsp( graph, pathSoFar, i, bestPath, pathIndex+1 ); //then we wanna head down that path and see how it works out
	}

	private static int getPathLength(int[][] graph, int[] path) //totals up all the distances between elements of a path and saves them to the last index of the path. helper method for tsp
	{
		int sum = 0;

		for (int i = 0; i < path.length-2; ++i)
			sum += graph[path[i]][path[i+1]];

		// add length from final node back to 0
		return sum + graph[path[path.length-2]][0];
	}

	private static boolean arrayContains(int[] a, int b) //determines whether an array contains a specific value. Should only be used on arrays of nodes, since each number in the array should be unique. Helper method for tsp
	{
		for (int i = 0; i < a.length; i++)
			if (a[i] == b)
				return true;

		return false;
	}

	private static void approxTsp(int[][] graph)
	{
		
		if(!returnOfIsMetric(graph))
		{
			System.out.println("Error: Graph is not metric.\n");
			return;
		}

		int[][] mst = minSpanningTree(graph);


		//Find minSpanningTree(graph)
		//Convert tour to Eulerian tour. (Depth-first traversal of minSpanningTree)

		int[] traversal = new int[graph.length]; //array to hold the depth-first traversal

		dfs( mst, traversal, 0, 1 ); //passing 0 into DFS so that the order we visited nodes will appear as 0th through n-1th as opposed to 1st through Nth

		int[] result = new int[graph.length]; //array to hold the nodes in the order we visited them

		for ( int i = 0 ; i < traversal.length ; ++i ) //fill up result
			result[traversal[i]-1] = i; //put the index (the node) into the location of the value to get them in order

		int totalDistance = 0; //variable to hold the total distance of the tour we found

		for ( int i = 0 ; i < result.length - 1 ; ++i ) //find the distance between all the nodes, excluding the cost of getting back where we started
		{
			totalDistance += graph[result[i]][result[i+1]];
		}

		totalDistance += graph[result.length-1][0];

		System.out.print(totalDistance + ": ");
		for ( int i = 0 ; i < result.length ; ++i )
			System.out.print(result[i] + " ");
		System.out.println();
	}

	private static boolean returnOfIsMetric(int[][] graph) //because the first implementation needs to print out why exactly it finds the graph is or is not metric, both a method to return a boolean and a method to print the results are required. Note that method name is a clever pun
	{
		if(!completeConnect(graph))
		{
			return false;
		}

		for(int i = 0; i < graph.length; ++i)
		{	

			int[] shortest = new int[graph.length];
			shortest = dijkstras(graph, i);

			for(int j = 0; j < graph.length; ++j)
			{				
				if(graph[i][j] > shortest[j])
				{
					return false;
				}

			}
		}

		return true;
	}
}