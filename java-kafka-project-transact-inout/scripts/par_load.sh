#!/bin/bash

# array PIDs
pids=()

# 10 procs
for i in {1..100}
do
    ./load_test.sh > "thread_$i.log" 2>&1 &
    pids+=($!) # store PID 
done

sleep 10

echo "timeout, stop"
for pid in "${pids[@]}"
do
    kill $pid 2>/dev/null
done

wait
echo "stopped all. logs in thread_*.log"

