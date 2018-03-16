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
        java -cp "$OUT_DIR" Simulator "$EDGE_FILE" "$NODE_ID" | tee q1.out
        echo "output file q1.out"
    elif [ "$TASK" == "q2" ]
    then
        java -cp "$OUT_DIR:./dependency/*" GraphSimulator "$EDGE_FILE" "$NODE_ID"
    elif [ "$TASK" == "q3" ]
    then
        java Simulator "$EDGE_FILE" "$NODE_ID"
    fi
else
    echo "usage: ./run.sh (compile|q1|q2|q3)"
    exit 1
fi