WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW+WWWW
WWWW..FerryLeaks..........WWWWWW++++++WWWW
WWWW..............Beta 1.2WWWWW+++++WWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWW+++WWWWWWWWWW
WWWWWWWWWWWWWWWWWWW  WWWWWWWW+WWWWWWWWWWWW
WWWW   WWWW  W    W    WWW    WWWWWWWWWWWW
WWWWW      WWWWWWWWWWW        WWWWWWWWWWWW
WWWWW          WWWWWWW       WWWWWWWWWWWWW
WWWWW              WWWWWWWW   WWWWWWWWWWWW
WWWWW                   WWWWWWW  WWWWWWWWW
WWWWWWWWWW                  WWWW  WWWWWWWW
WWWWWWWWWWWWWW                    WWWWWWWW
WWWWWWWWWWWWWWWWWW                   WWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW

A Web-Based (Debugging) Editor for the Table-Algebra

Readme
------

1. Requirements
---------------

WebFerry is a web application using Google's WebToolkit (GWT). To run a precompiled WAR
of WebFerry on your server, you need the have Tomcat installed.
The GraphCanvas uses GraphViz' dot to layout the logical plan graphs, so a dot installation
is highly recommended, however not mandatory. Without dot being installed WebFerry falls back
to very simple layouting algorithms.
To compile you own version of WebFerry the GWT-SDK is needed which can be downloaded at
http://code.google.com/intl/de-DE/webtoolkit/download.html
You need at least GWT SDK 2.3.

1. Compilation
--------------

WebFerry comes with an ant script to generate a ready-to-go WAR (WebARchive) which can be easily
uploaded to a live Tomcat server. Compilation can be started be typing

ant war

If you only want to compile WebFerry but not to create the WAR-file, you can do so by typing

ant build

NOTE, however, that you must first change the build.properties.example to fit your system environment.
(More specifically, you have to change the path of GWT-SDK). Otherwise, compilation won't be possible.
The build.properties.example  HAS TO BE RENAMED  to build.properties after modification.

2. Installation
---------------

Your Tomcat-server must allow file-uploads to use WebFerry's upload mechanisms. It must also allow
system calls to communicate with dot and Ferry.

