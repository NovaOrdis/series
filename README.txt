

Test debug

mvn -Dmaven.surefire.debug="-Xrunjdwp:transport=dt_shmem,server=y,suspend=y,address=mvn" clean test -Dtest=ConfigurationTest