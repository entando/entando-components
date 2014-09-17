<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block"> <a  href="<s:url action="entryPoint" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpstats.header.statistics" />"><s:text name="jpstats.header.statistics" /></a>&#32;/&#32;<s:text name="jpstats.title.stats.trash" /></span>      
</h1>
<div id="main">    
    <s:form action="delete">
		<p class="sr-only">
			<s:hidden name="startDate" ></s:hidden>
			<s:hidden name="endDate" ></s:hidden>
       </p>
       <div class="alert alert-warning">
          <p>
            <s:text name="jpstas.note.delete.are.you.sure" />
          <code><s:date name="startDate" format="dd/MM/yyyy HH:mm" />&#32;&mdash;&#32;<s:date name="endDate" format="dd/MM/yyyy HH:mm" /></code>
			&#32;?&#32;
          </p>         
           <div class="text-center margin-large-top"> 
               <s:submit useTabindexAutoIncrement="true"  type="button" action="delete" cssClass="btn btn-warning btn-lg">
                <span class="icon fa fa-times-circle"></span>&#32;
                <s:text name="label.remove" />
              </s:submit>
           </div>		
       </div>
	</s:form>
</div>