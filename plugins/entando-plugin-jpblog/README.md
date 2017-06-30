**Blog**

**Code**: ```jpblog```

**Description**

The _Blog_ plugin lets users create and manage a Blog.
From the configuration page users can configure the root of the blog categories tree.
The Blog includes also a content type for the blog post, 2 content models and 3 widgets to configure a Blog page in a project.
The Blog requires as a prerequisite the Content Feedback (jpcontentfeedback) plugin.

**Installation**

Open the pom.xml of your project: locate the tag toward the end of the file, after the tag; if the dependencies tag does not exist just create a new one just after the closure of the build tag.

Add the following snippet inside the dependencies:

```
<dependency>
    <groupId>org.entando.entando.bundles.app-view</groupId>
    <artifactId>entando-app-view-blog-default</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

**How to use**

By installing the bundle and the blog plugin, the following items are installed in the administration area:

* A configuration page in the Integrations / Components /  Blog section. Here, in fact, only the Category Tree root is set;
* A Blog Content Type in the Content Types List;
* two Content models;
* 3 widgets: (post list, blog archive, category summary)

To verify its behaviour it is necessary:

* Check the existence of the content type, content models, and widgets. For the content type, verify that the page is set for on-the-fly publication;
* Create blog contents;
* Configure a page with widgets and check it out;
