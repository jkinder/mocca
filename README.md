Mocca v0.1
==========

Copyright 2004,2005 Johannes Kinder <jk@jakstab.org>

Overview
--------

Mocca (_Model Checker for CTPL on Assembly code_) is a prototype 
implementation of a CTPL model checker for verifying CTPL 
specifications on assembly files as produced by IDA Pro. 

Mocca is intended for detecting malicious behavior in executables.
The example input files consist of several disassembled e-mail 
worms, and the example specifications describe using Windows API 
calls to copy the image of the running process to another 
directory (usually `windows/system32`).

Getting started
---------------

Use `./compile.sh` to compile.

Run `./mocca.sh` for a simple GUI, or `./moccac.sh` for a command 
line interface.

Caveats
-------

This is really old code I wrote for my Master's thesis in 2005, and
I haven't looked at it for years. It is probably full of bugs, but I'm
no longer maintaining it nor do I provide any support. I make it 
available purely for documenting the algorithm and experimental 
setup used in our [DIMVA 2005][2] and [TDSC 2010][1] papers.

Citing
------

If you use this code or parts of it in your own papers, please cite 
either:

* Kinder, J., S. Katzenbeisser, C. Schallhart, and H. Veith (Oct. 2010). 
Proactive Detection of Computer Worms Using Model Checking. 
In: IEEE Trans. Dependable Sec. Comput. 7(4), 424–438. [DOI][1]

and/or

* J. Kinder, S. Katzenbeisser, C. Schallhart, and H. Veith (2005). 
Detecting Malicious Code by Model Checking. In: Second Int. Conf. on 
Detection of Intrusions and Malware & Vulnerability Assessment 
(DIMVA 2005). Vol. 3548. LNCS. Springer, pp.174–187. [DOI][2]

[1]: http://dx.doi.org/10.1109/TDSC.2008.74 "TDSC'10 7(4), 424-438"
[2]: http://dx.doi.org/10.1007/11506881_11 "DIMVA'05, 174-187"