#!/bin/bash -xv

# Should pass for both Chronicle Queue and JMS

cd "${0%/*}/.."

TARGET_DIR=./target
JAVA=java
WGET=wget

# Start 3 instances of the demo
$JAVA -Dserver.port=8991 -jar demo-0.0.1.war &
$JAVA -Dserver.port=8992 -jar demo-0.0.1.war &
$JAVA -Dserver.port=8993 -jar demo-0.0.1.war &

$WGET http://127.0.0.1:8991 &
$WGET http://127.0.0.1:8992 &
$WGET http://127.0.0.1:8993 &

sleep 30

# Logs should show each instance getting the status messages of the other

