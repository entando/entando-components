/*
 * File Name: fckplugin.js
 * Plugin to insert "japslink" in the editor.
 * File Author: Eugenio Santoboni
 */
var applicationBasePath=FCKConfig.AppBaseUrl;

var jAPSLinkCommand=function(){
//create our own command, we dont want to use the FCKDialogCommand because it uses the default fck layout and not our own
};
jAPSLinkCommand.GetState=function() {
return FCK_TRISTATE_OFF; //we dont want the button to be toggled
}
jAPSLinkCommand.Execute=function() {
//open a popup window when the button is clicked
window.open(applicationBasePath+'do/jpfastcontentedit/Content/Hypertext/configInternalLink.action','','width=800,location=no,scrollbars=yes,toolbar=no');
}

FCKCommands.RegisterCommand('fastContentEdit_japslink', jAPSLinkCommand ); //otherwise our command will not be found

var oJapslinkItem = new FCKToolbarButton('fastContentEdit_japslink',FCKLang.JapslinkBtn,FCKLang.JapslinkBtn,null,false,true);

oJapslinkItem.IconPath = FCKPlugins.Items['fastContentEdit_japslink'].Path + 'link.gif' ;
FCKToolbarItems.RegisterItem( 'fastContentEdit_japslink', oJapslinkItem ) ;