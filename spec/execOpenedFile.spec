[version]
Mocca 0.1

[name]
Execute opened file

[description]
Execute a previously opened file.

[clues]
call(CreateProcessA)

[formula]
(EF( exists $r0(lea($r0, $pfname) & EX(E %noassign($r0) U (push($r0) & EX(E %nostack U (call(CreateFileA) | call(fopen)))))))
& E %noassign($pfname) U EF(%syscall(CreateProcessA, $pfname, 0)))
|
(EF( exists $r0(lea($r0, $pfname) & EX(E %noassign($r0) U (push($r0) & EX(E %nostack U (call(CreateFileA) | call(fopen)))))))
& E %noassign($pfname) U EF(%syscall(CreateProcessA, $pfname)))
