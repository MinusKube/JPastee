# JPastee
Java Wrapper for the https://paste.ee/ API

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
