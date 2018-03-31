#!/usr/bin/env bash

OUT_DIR="./build"
TASK=$1

if [ "$TASK" == "compile" ]
then
    rm -rf $OUT_DIR && mkdir -p $OUT_DIR
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
        # create log directory if doesn't exist
        mkdir -p log
        OUT_FILE="log/q1.out"
        java -cp "$OUT_DIR" simulator.ProbSimulator "$EDGE_FILE" "$NODE_ID" 0.0 | tee $OUT_FILE
        echo "output file $OUT_FILE"
    elif [ "$TASK" == "q2" ]
    then
        # add graph dependencies to classpath
        java -cp "$OUT_DIR:./dependency/*" simulator.GraphSimulator "$EDGE_FILE" "$NODE_ID"
    elif [ "$TASK" == "q3" ]
    then
        data=""
        # loop over values of drop probability
        for p in `seq 0 0.05 0.95`
        do
            echo "using p=$p"
            # retrieve time elapsed from last line of stdout
            time=$(java -cp "$OUT_DIR" simulator.ProbSimulator "$EDGE_FILE" "$NODE_ID" "$p" | tee /dev/tty | awk 'END {print $4}')
            data+=$p$'\t'$time$'\n'
        done

        TEMP_FILE="delete.me"
        echo "$data" > "$TEMP_FILE" # store plot data
        # plot scatter plot
        gnuplot -p -e "set xlabel 'Message Drop Probability';
                       set ylabel 'Time (milliseconds)';
                       set xrange [0:1];
                       set yrange [0:];
                       set title 'Time vs Message Drop Probability';
                       plot '$TEMP_FILE' notitle with points pt 7 ps 2"
        rm $TEMP_FILE   # delete temporary data file
    fi
else
    echo "usage: ./run.sh (compile|q1|q2|q3)"
    exit 1
fi