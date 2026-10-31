$env:JAVA_HOME = "C:\java\OpenJDK21U-jdk_x64_windows_hotspot_21.0.12.1_1\jdk-21.0.12.1+1"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd -Pcli-isolated-output spring-boot:run
