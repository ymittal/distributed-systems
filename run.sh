#!/usr/bin/env bash

OUT_DIR="./build"

TASK=$1

if [ "$TASK" == "compile" ]
then
    rm -r $OUT_DIR && mkdir -p $OUT_DIR
    javac $(find ./src/* | grep .java) -d $OUT_DIR -cp ".:./dependency/*"
elif [ "$TASK" == "q1" ] || [ "$TASK" == "q2" ] || [ "$TASK" == "q3" ]
then
    EDGE_FILE=$2
    NODE_ID=$3

    if [ ! -f "$EDGE_FILE" ]
    then
        echo "edge file \"$EDGE_FILE\" not found"
        exit 1
    elif [ ! "$NODE_ID" -ge 0 ]
    then
        echo "starting node id must be greater than or equal to zero"
        exit 1
    fi

    # get full path of graph edge file
    EDGE_FILE=$(realpath $EDGE_FILE)

    if [ "$TASK" == "q1" ]
    then
        mkdir -p log
        java -cp "$OUT_DIR" simulator.ProbSimulator "$EDGE_FILE" "$NODE_ID" 0.0 | tee log/q1.out
        echo "output file log/q1.out"
    elif [ "$TASK" == "q2" ]
    then
        java -cp "$OUT_DIR:./dependency/*" simulator.GraphSimulator "$EDGE_FILE" "$NODE_ID"
    elif [ "$TASK" == "q3" ]
    then
        for p in `seq 0.0 0.05 0.95`
        do
            echo "using p=$p"
            java -cp "$OUT_DIR:./dependency/*" simulator.ProbSimulator "$EDGE_FILE" "$NODE_ID" "$p"
        done
    fi
else
    echo "usage: ./run.sh (compile|q1|q2|q3)"
    exit 1
fi