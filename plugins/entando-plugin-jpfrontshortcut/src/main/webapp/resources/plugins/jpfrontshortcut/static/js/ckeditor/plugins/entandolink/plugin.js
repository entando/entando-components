CKEDITOR.plugins.add('entandolink', {
	init: function(editor) {
		if(window.entandoCKEditor === undefined) {
			window.entandoCKEditor  = {};
		}
		var pluginName = 'entandolink';
		var entando_link_path = editor.config.EntandoLinkActionPath;
		editor.addCommand(pluginName, {
			exec: function(editor) {
				var selection = editor.getSelection();
				var ranges = selection.getRanges(true);
				if (!(ranges.length == 1 && ranges[0].collapsed)) {
					var id = 'entando_link_window_' + editor.element.$.id;
					window.entandoCKEditor[id] = editor;
					window.open(entando_link_path, id, 'width=800,location=no,scrollbars=yes,toolbar=no');
				}
			}
		});
        editor.ui.addButton(pluginName, {
            label: 'Link',
			icon: CKEDITOR.plugins.getPath(pluginName) + pluginName + "-icon.gif",
            command: pluginName
		});
    }
});