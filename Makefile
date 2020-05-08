test:
	mkdir -p "$(CURDIR)/build"

	javac -d "$(CURDIR)/build" \
	-cp "$(CURDIR)/lib/junit-4.13.jar" \
	"$(CURDIR)/nio_reactor_pattern/Acceptor.java" "$(CURDIR)/nio_reactor_pattern/Reactor.java" \
	"$(CURDIR)/nio_reactor_pattern/MyTest.java"

	java -cp "$(CURDIR)/lib/junit-4.13.jar:$(CURDIR)/lib/hamcrest-core-1.3.jar:$(CURDIR)/build" \
	org.junit.runner.JUnitCore \
	nio_reactor_pattern.MyTest
