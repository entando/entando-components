**Content feedback**

**Code**: ```jpcontentfeedback```

**Description**

The CONTENT FEEDBACK plugin lets users manage comments associated with specific content types.
From the Configuration interface users can configure some options for comments and contents.
From the comment list interface users can access the list of comments and approve or not if they required approval.

**Installation**

Open the pom.xml of your project: locate the tag toward the end of the file, after the tag; if the dependencies tag does not exist just create a new one just after the closure of the build tag.

Add the following snippet inside the dependencies:

```  
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpcontentfeedback</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**How to use**

Configure a page with widget "Content Feedback - Publish Content", publish a content and then users can add comments and/or rating to the content.
