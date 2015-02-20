<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<div id="main"> 
  


  <s:form id="newDomain" name="NewDomain" action="saveDomain" namespace="/do/Mailgun" method="post" cssClass="form-horizontal">  
		<fieldset class="margin-base-vertical">
			<div class="accordion_element">			
						<div>
							<label for="jpmailgun_nameDomain" class="basic-mint-label">Name</label>
							<input type="text" name="_domainId" value="" id="jpmailgun_nameDomain" class="form-control"><br>
						</div>
						<div>
							<label for="jpmailgun_passDomain" class="basic-mint-label">Password (SMTP)</label>
							<input type="text" name="_domainPassSMTP" value="" id="jpmailgun_passDomain" class="form-control"><br>
						</div>
			</div>
		</fieldset>	
    <div class="form-group">
      <div class="col-xs-12 col-sm-4 col-md-3">
        <button type="submit" name="saveDomain" value="Search" class="btn btn-primary" tabindex="8">
        <span class="icon fa fa-save"></span> 
                  Save
        </button>
     </div> 
    </div>
	</s:form>


	</div>
</div>

