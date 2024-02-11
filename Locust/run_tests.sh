#!/bin/sh
# add [[ -e ~/.profile ]] && emulate sh -c 'source ~/.profile' to .zshrc
pip3 install -r requirements.txt

fun_locust(){
locust -f locust_example3.py  --headless --users $1 --spawn-rate 0.5 -t $2 -H http://192.168.50.241:8080 --csv-full-history --csv report_$1_$2.csv && python reporter.py report_$1_$2 report_$1_$2.html
}

users1=50
time1="1m"
fun_locust $users1 $time1

users2=100
time2="5m"
fun_locust $users2 $time2

users3=500
time3="3m"
fun_locust $users3 $time3
