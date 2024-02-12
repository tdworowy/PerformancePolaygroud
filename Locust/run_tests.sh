#!/bin/bash
# add [[ -e ~/.profile ]] && emulate sh -c 'source ~/.profile' to .zshrc
pip3 install -r requirements.txt

run_locust(){
locust -f locust_example3.py  --headless --users $1 --spawn-rate 0.5 -t $2 -H http://192.168.50.241:8080 --csv-full-history --csv report_$1_$2.csv
}

generate_report() {
  python reporter.py report_$1_$2 report_$1_$2.html
}

users=(5 50 300)
durations=("30s" "1m" "5m")

for i in {0..2}
do
  run_locust ${users[$i]} ${durations[$i]} && sleep 10
done

for i in {0..2}
do
  generate_report ${users[$i]} ${durations[$i]}
done