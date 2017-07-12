# Define a variable for classpath
CLASS_PATH = ../bin

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : hello.dll

# $@ matches the target, $< matches the first dependancy
hello.dll : HelloJNI.o
	gcc -Wl,--add-stdcall-alias -shared -o $@ $<

# $@ matches the target, $< matches the first dependancy
HelloJNI.o : HelloJNI.c HelloJNI.h
	gcc -I"D:\bin\jdk1.7\include" -I"D:\bin\jdk1.7\include\win32" -c $< -o $@

# $* matches the target filename without the extension
HelloJNI.h : HelloJNI.class
	javah -classpath $(CLASS_PATH) $*

clean :
	rm HelloJNI.h HelloJNI.o hello.dll