# Build, setup and run

1. Technologies used:
<pre>
   - JSE 7.x+
   - Servlet 3.x+ container such as tomcat 7.x+
   - Maven 3.0.x
</pre>

1. Build, run and use from code:
   - <code>$ mvn tomcat7:run</code>
   - http://localhost:8080/data-engineering/

1. Create war and sources:
   - <code>$ mvn package</code> # will generate target\data-engineering.war and target\data-engineering-sources.jar

1. Install, run and use through war:
<pre>
   - data-engineering.war can be deployed to any servlet 3.x+ container such as tomcat 7.x+.
       - http://stackoverflow.com/questions/5109112/how-to-deploy-war-in-tomcat-7
       - http://tomcat.apache.org/tomcat-7.0-doc/deployer-howto.html
       - http://localhost:8080/data-engineering-1.0/
</pre>

1. Todos:
<pre>
- authentication and authorization
- exception handling
- logging
- file and file content validations
- data and data type validations
- show table output
- use servlet annotations
- use jsp library tag for message
- concurrent upload of more than 1 file
- retry mechanism
</pre>


