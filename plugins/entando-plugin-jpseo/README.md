# SEO

**Code**: ```jpseo```

## Description

The SEO plugin enables some functionality inside the page configuration (new parameters and friendly url), in the content handling (new role attribute to pilot friendly url) and to extract the sitemap.

## Installation

In order to install the SEO plugin, you must insert the following dependency in the pom.xml file of your project:

```  
<dependency>
	<groupId>org.entando.entando.plugins</groupId>
    <artifactId>entando-plugin-jpseo</artifactId>
    <version>${entando.version}</version>
    <type>war</type>
</dependency>
```

## How to use

###### Modify of web.xml

Add, into web.xml file, the following xml section

```
    <servlet-mapping>
        <servlet-name>ControllerServlet</servlet-name>
        <url-pattern>/page/*</url-pattern>
    </servlet-mapping>
```

###### Page Model CUSTOMIZATION

Add the following tld declaration in the page models:

<%@ taglib prefix="jpseo" uri="/jpseo-aps-core" %>

In order to add the metadata description, add the following section in the header: 
```
<jpseo:currentPage param="description" var="metaDescr" />
<c:if test="${metaDescr!=null}" >
<meta name="description" content="<c:out value="${metaDescr}" />" />
</c:if>
```

If you have configured a page with the field "Complex parameters" (as in the example shown):
```
<seoparameters>
	<parameter key="author">Elia Mezzano</parameter>
	<parameter key="keywords">
		<property key="en">aaa,bbb,ccc,ddd,eee,fff</property>
		<property key="it">ggg,hhh,iii,lll,mmm,nnn</property>
	</parameter>
</seoparameters>
```
you can use, into the header of the page models, the following sections:
```
<jpseo:seoMetaTag key="author" var="metaAuthorVar" />
<c:if test="${metaAuthorVar != null}" >
<meta name="author" content="<c:out value="${metaAuthorVar}" />" />
</c:if>
```
```
<jpseo:seoMetaTag key="keywords" var="metaKeywordsVar" />
<c:if test="${metaKeywordsVar != null}" >
<meta name="keywords" content="<c:out value="${metaKeywordsVar}" />" />
</c:if>
```
###### Content CUSTOMIZATION

To ensure that MAIN CONTENT influences the page's description (see appropriate tag in the page templates section)
the jsp of the WIDGET content_viewer must be changed as follows:

**Add the following tld declaration**

<%@ taglib prefix="jpseo" uri="/jpseo-aps-core" %>

**Modify the content tag**

substitute:
```
<jacms:content .... />
```
for 
```
<jpseo:content publishExtraTitle="true" publishExtraDescription="true" />
```
