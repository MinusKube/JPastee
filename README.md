# JPastee
Java Wrapper for the https://paste.ee/ API

### Installation

#### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'fr.minuskube:jpastee:1.0.2'
}
```

#### Maven
```xml
<dependency>
  <groupId>fr.minuskube</groupId>
  <artifactId>jpastee</artifactId>
  <version>1.0.2</version>
</dependency>
```

#### Manually
You can download the latest version on the [Releases page](https://github.com/MinusKube/JPastee/releases) on Github.

### Example
```java
JPastee pastee = new JPastee("YOUR_API_KEY");

Paste paste = Paste.builder()
        .description("My super paste!")
        .addSection(Section.builder()
                .name("An awesome section.")
                .contents("This is a test made using the JPastee API.")
                .syntax(pastee.getSyntaxFromName("java").get())
                .build())
        .build();
        
SubmitResponse resp = pastee.submit(paste);

if(resp.isSuccess())
    System.out.println("The paste was successfully submitted!");
else
    System.out.println("The paste could not be submitted... " + resp.getErrorString());
```
