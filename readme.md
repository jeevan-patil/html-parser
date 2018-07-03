# HTML parsing using Jsoup library.

o Environment
  Runtime               - JDK 8
  Build                 - Maven
  HTML parser framework - JSoup

o How to build and run your solution locally.
  This is a maven project. Some of the commands are as following.
  On command line terminal go to the folder where pom.xml resides and fire commands.
  Build project - ```mvn clean package```
  Run project   - ```mvn spring-boot:run```

o How to run application locally?
  Run this command to run the application   - ```mvn spring-boot:run```

  The application URL on local machine is ```http://localhost:8080/```

  To look at the APIs written for the application ```http://localhost:8080/swagger-ui.html```

o There is an png image file (Html Parser.png) inside the project at root level. Application landing page looks like it.

o The assumptions you made, design decisions you took -
  Hyperlink validation - While reviewing / analysing the hyperlinks on the page, I have considered unique links only.
  Does not make sense to test redirection effect for similar links again and again.

  Deliberatly did not use timeout while checking for hyperlink redirection effect. Because specifying small timeout
  may lead to TimeOutException for many URLs or specifying large timeout may lead an API to run for a long time.

o Logic to check if page has login box -
  Search for text fields with password type. If there is password field,
  there is possibility that it has login box. I could have checked for username field, or submit, login buttons as well.
  But it is not always possible that field names will be similar to what we guessed. Sometimes button text might be in
  regional languages like German, Chinese, so it's better to use input field type to determine this.

o Known constraints or limitations in your solution -
  Performance of the API may degrade in case there are hundreds of hyperlinks on the website.

o Implementation of hyperlink validation -
  I have used CompletableFuture concept which is part of concurrent packages to validate urls in async way.
  Sequencial checks would have been expensive and will take a lot of time to validate all the links.

o Ancient websites with HTML versions less than 5 -
  HTML 4.01   - http://www.dpgraph.com/
  HTML 4.0    - http://www.taco.com
  No Version  - http://www.mcspotlight.org/index.shtml
  HTML 1.0    - http://www.agroweb.com/

