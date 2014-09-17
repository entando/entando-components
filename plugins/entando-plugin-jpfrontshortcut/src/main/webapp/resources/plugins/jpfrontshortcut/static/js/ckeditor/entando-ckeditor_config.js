CKEDITOR.editorConfig = function(config) {
	//config.language = 'fr';
	config.extraPlugins = 'entandolink,onchange';
	//config.uiColor = '#AADC6E';
	config.toolbar = [['Bold', 'Italic', '-', 'NumberedList','BulletedList', '-', 'entandolink', 'Unlink', '-', 'Undo','Redo', '-', 'Table', '-', 'Source']];
	config.height = 250;
	config.forcePasteAsPlainText = true;
	config.docType = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">';
	config.pasteFromWordNumberedHeadingToList = true;
	config.pasteFromWordPromptCleanup = true;
};