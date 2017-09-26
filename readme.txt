This was a project I did for my algorithms and data structures course. The project is focused on graphs and executing various algorithms on the data structure (obviously).

The program loads a text file representing a graph and allows users to perform various operations via a text-based menu. The program can:
-check if the graph is connected
-fine a minimum spanning tree
-find the shortest path between two points using Dikstra's algorithm
-check if the graph is metric
-make the graph metric-approximate the traveling salesperson problem
-brute force the traveling salesperson problem.

It's not perfect. Notably, the implementation of Dikstra's algorithm is broken. It does not properly scan through best known distances.
Moreover, the brute force traveling salesperson is also broken. Nodes are not unmarked as visited properly, so the algorithm as I implemented it is close but inaccurate.

In spite of these flaws, I think this java project shows my understanding and interest in algorithms and data structures, and I had a lot of fun working on it.