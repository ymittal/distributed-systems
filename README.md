# Distributed Systems Assignment

You can run the simulator as follows:
```
$ cd Part\ A/
$ ./run.sh compile
$ ./run.sh (q1|q2|q3) <edge list file> <starting node>
```

### Notes
- For q3, I chose to use a command-line graphing utility called `gnuplot` to create the Time versus Message Drop Probability plot. `gnuplot` should be available on all DICE machines.
- I am using Java's `ConcurrentLinkedQueue` under `gossip.NetworkThread` to store nodes to send gossip to up next.

You can also check a couple sample plots generated for q3 under `plot/` directory.
