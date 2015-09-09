# topological-sort
Given a text file with a digraph, it will topological sort it if and only if the digraph is a (DAG) or more specifically does not contain a cycle.

## input.txt

3

010

000

010

## output.txt

No Cycle found!

Adjecency List: 

0| 1

1| 

2| 1

Topological Sort: 

2 0 1
