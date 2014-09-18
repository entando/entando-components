jQuery(function() {
	var $ = jQuery;
	var imgBasePath = window.entando_widget_sharewith_imgURL;
	if (imgBasePath === undefined) { imgBasePath = '/'; }
	var currentUrl = encodeURIComponent(document.location.href);
	var currentTitle = encodeURIComponent(document.title);
	
	var targets = $('.entando-widget-sharewith-buttons');
	
	$.each(window.entando_showlet_sharewith_buttons,function(index, item) {
		item.link = item.link.replace(/§URL§/g,currentUrl);
		item.link = item.link.replace(/§TITLE§/g,currentTitle);
		
		var tmp = $('<div></div>');
		var a = $('<a class="btn btn-small" href="'+ item.link +'" title="'+ item.title +'"  ></a>')
		var img;
		if (item.image!==undefined) {
			img = $('<img src="'+ imgBasePath + item.image +'" />');
			img.appendTo(a);
		}

		a.appendTo(tmp);
		a = tmp.html();
		$.each(targets, function(index, currenTarget){
			currenTarget = $(currenTarget);
			currenTarget.html(currenTarget.html() + ' ' + a);
		});

		tmp.remove();
	});
});