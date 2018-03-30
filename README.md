# Distributed Systems Assignment

You can run the simulator as follows:
```
$ cd Part\ A/
$ ./run.sh compile
$ ./run.sh (q1|q2|q3) <edge list file> <starting node>
```

### Notes
- For q3, I chose to use a command-line graphing utility called `gnuplot` to create the Time versus Message Drop Probability plot. `gnuplot` should be available on all DICE machines.
- I am using Java's `volatile` keyword so certain data elements are available across threads. For example, I am updating `gossip.NetworkThread.recipientQueue` (queue to send gossip to nodes) from `gossip.GNodeThread`.

