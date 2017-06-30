**Collaboration**

**Code**: ```jpcollaboration```

**Description**

The _Social Collaboration_ plugin lets administrators create dedicated area where users may post and vote ideas. Ideas are created by registered -and logged- users: these ideas are then shared among all the portal visitors. Each idea can be rated (with a Like or Don't Like button) or commented. From the Configuration interface administrators decide whether ideas or comment must be approved before publication. From the Ideas interface administrators can manage the ideas submitted by users.

**Installation**

Open the pom.xml of your project: locate the tag toward the end of the file, after the tag; if the dependencies tag does not exist just create a new one just after the closure of the build tag.

Add the following snippet inside the dependencies:

```
<dependency>
    <groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpcollaboration</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

The page model where widgets will be published must contain the following snippet in the <head> tag:

```
<%-- JS_JQUERY: (mandatory) it's used to load the necessary coming from the plugin. It also set the flag "outputHeadInfo_JS_JQUERY_isHere" --%>
<wp:outputHeadInfo type="JS_JQUERY">
	<c:set var="outputHeadInfo_JS_JQUERY_isHere" value="${true}" />
	<wp:printHeadInfo />    
</wp:outputHeadInfo>
```

**How to use**

By installing the Collaboration plugin, the following items are installed in the administration area:

* A page in the Integrations / Components / Collaboration section. Here, in fact, we can see 4 tabs:

    * Instances: an instance is the topic of some ideas;

    * Ideas: an idea is a proposal on a particular topic that a user shares on the portal;

    * Comments: list of comments on posted ideas;

    * Configure: when moderation is selected for Ideas or Comments, administration approval will be required before publication.

* A category root for collaboration ideas;
* 8 widgets: (ideas list, new idea, category summary, publish idea, quick entry idea, search ideas, search result and statistics)

To verify its behaviour it is necessary:

* Create almost one instance;
* Configure a page with widgets and check it out;
* Approve or don't approve ideas or comments on the admin area.
