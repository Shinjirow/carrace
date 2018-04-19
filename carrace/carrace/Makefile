JC = javac
J = java

AIPROGRAMD = simplerace/e/AIController.java
TESTPROGRAMD = simplerace/e/TestSensor.java
AIPROGRAM = simplerace.e.AIController
TESTPROGRAM = simplerace.e.TestSensor

MAINPROGRAM = simplerace.Play


cai:
	${JC} ${AIPROGRAMD}

ct:
	${JC} ${TESTPROGRAMD}

do:
	${J} ${MAINPROGRAM} ${AIPROGRAM}

test:
	${J} ${MAINPROGRAM} ${TESTPROGRAM}

help:
	"cai : compile AI Program"
	"ct  : compile TEST Program"
	"do  : execute AI Program"
	"test: execute TEST Program"

stats:
	${J} simplerace.Stats ${AIPROGRAM}

bigstats:
	sh loop.sh
