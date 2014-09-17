//version fbf2e3682527cdb025e9928b9b6354cdd6232751 21/06/2013

//taboo start
!function ($) {
	"use strict"; // jshint ;_;
	var sign = 'Entando.Taboo';
	
	var defaultOptions = {
		//morto tabs: "tab",			//classe dei contenitori dei tab
		startTabIndex: null,			//quale tab mostrare per primo (vince su startTab)
		startTab: null,					//come sopra però arriva un ID
		activeTabClass: "activetab",	//class css applicata al toggler attivo
		hideClass: "noscreen",			//classe css che viene applicata in stato "nascosto"
		showClass: "showClass",			//classe css che viene applicata in stato "visibile"
		anchorTab: "_quickmenu",			//backlinks id suffix
		tabTogglers: 'a.tab-toggle',
		togglerTabRetriever: function(toggler) {
			var tabId = toggler.attr('href');
			return $(tabId);
		},
		tabTogglerRetriever: function(tab, root) {
			var id = tab.attr('id');
			if (root) {
				return $('[href="#'+id+'"]', root);
			}
			else {
				return $('[href="#'+id+'"]');
			}
		}
		//startIndex: ""
		//onOpen: function(toggler, submenu) {},
		//onClose: function(toggler, submenu) {},
		//onStart: function() {},
	};

	var Taboo = function (el, opt) {
		var self = this;
		var TabooTogglerRoot = this.TabooTogglerRoot = $(el);
		var opt = this.opt = opt;
		var togglers = $(opt.tabTogglers, el)

		togglers.on('click', function(ev) {
			ev.preventDefault();
			$.each(togglers, function(index, togg) {
				$(togg).removeClass(opt.activeTabClass);
			});
			var currentToggler = $(this);
			var currentTab = opt.togglerTabRetriever(currentToggler);
			$.each(togglers, function(index, togg) {
				var tab = opt.togglerTabRetriever($(togg));
				tab.addClass(opt.hideClass);
				tab.removeClass(opt.showClass);
			})
			currentToggler.addClass(opt.activeTabClass);
			currentTab.addClass(opt.showClass);
			currentTab.removeClass(opt.hideClass);
			$(self).trigger('tab', [currentTab, currentToggler]);
		});

		$.each(togglers, function(index, toggler) {
			toggler = $(toggler);
			var tab = opt.togglerTabRetriever($(toggler));
			tab.addClass(opt.hideClass);
			toggler.removeClass(opt.activeTabClass);
		});

		//init a tab
		var done = false;
		var togglerToActivate;
		var tabToActivate;

		if (opt.startTabIndex && /^\d+$/.test(opt.startTabIndex)) {
			//startTabIndex?
			togglerToActivate = $(togglers[new Number(opt.startTabIndex)]);
			tabToActivate = opt.togglerTabRetriever(togglerToActivate);
			done = true;
		}
		else if (opt.startTab!==undefined && opt.startTab!==null) {
		 	//startTab?
			var startTabRef = opt.startTab;
			var tab;
			if ($.type(startTabRef) == 'string') {
				tab = $('#'+startTabRef);
			}
			else if ($.type(startTabRef) == 'object') {
				tab = $(startTabRef);
			}
			if (tab.length==1) {
				tabToActivate = tab;
				togglerToActivate = opt.tabTogglerRetriever(tab, TabooTogglerRoot);
				done = true;
			}
		}
		else if (document.location.hash.length > 0) {
			//a hash??
			var testString = document.location.hash;
			var tab = $(testString);
			if (tab.length==1) {
				tabToActivate = tab;
				togglerToActivate = opt.tabTogglerRetriever(tab, TabooTogglerRoot);
				done = true;
			}
		}
		if (!done) {
			//ok, start from the first
			togglerToActivate = $(togglers[0]);
			tabToActivate = opt.togglerTabRetriever(togglerToActivate);
		}

		togglerToActivate.addClass(opt.activeTabClass);
		tabToActivate.removeClass(opt.hideClass);
		tabToActivate.addClass(opt.showClass);

		return this;
	};

	$.fn.EntandoTaboo = function (userreqopt) {
		var options = $.extend({},defaultOptions, userreqopt);
		var tabDone = [];
		this.each(function () {
			var $this = $(this);
			var data = $this.data(sign);
			if (!data) {
				data = new Taboo(this, options);
				$this.data(sign, data);
			}
			tabDone.push(data);
		});
		return tabDone;
	};

	$.fn.EntandoTaboo.Constructor = Taboo;
}(window.jQuery);
//taboo end


//wood start
!function ($) {
	"use strict"; // jshint ;_;
	var sign = 'Entando.Wood.Menu';
	
	var defaultOptions = {
		menuToggler: "toggler", //togglers css class
		hideClass: "noscreen", //css class for hidden menu
		showClass: "undoNoscreen", //css class for visible menu
		openClass: "openmenu", //css class used when the branch is open
		closedClass: "closedmenu", //css class used when the branch is closed
		showTools: false, //show the toolbar open/close all?
		expandAllLabel: "+", //label "expand all"
		collapseAllLabel: "-", //label "collapse all"
		toolClass: "toolClass", //css class for the toolbox
		toolTextIntro: 'You can expand the tree structure, or collapse it, using the appropriate links.', //intro text before toolbar links
		toolexpandAllLabelTitle: 'Expand All Tree',	//title for expand  all
		toolcollapseLabelTitle: 'Collapse All Tree', //title for close all
		menuRetriever: function(toggler) { return $("#"+toggler.attr("rel")); }
		//startIndex: "" //optional: {String|Number} choose which open at start
		//onOpen: function(toggler, submenu) {}, //optional: called on open
		//onClose: function(toggler, submenu) {}, //optional: called on close
		//onStart: function() {}, //optional: called on start
	};

	var Wood = function (el, opt) {
		var WoodRoot = this.WoodRoot = $(el);
		var opt = this.opt = opt;
		var getSubMenu = this.getSubMenu = opt.menuRetriever;
		var expandAll = this.expandAll = function() {
			//console.log('all', $("."+opt.menuToggler, WoodRoot), $("."+opt.menuToggler, WoodRoot).length);
			$("."+opt.menuToggler, WoodRoot).each(function(index, toggler){
				openWood($(toggler));
			});
		};
		var collapseAll = this.collapseAll = function() {
			$("."+opt.menuToggler, WoodRoot).each(function(index, toggler){
				closeWood($(toggler));
			});
			$(":checked", el).prop('checked',false);
		};
		var openWood = this.openWood = function(toggler) {
			var submenu = getSubMenu(toggler);
			var togglerParent = toggler.parent();
			submenu.addClass(opt.showClass);
			submenu.removeClass(opt.hideClass);
			togglerParent.removeClass(opt.closedClass);
			togglerParent.addClass(opt.openClass);
			if (opt.onOpen) {
				opt.onOpen(toggler, submenu);
			}
		};
		var closeWood = this.closeWood = function(toggler) {
			var submenu = getSubMenu(toggler);
			var togglerParent = toggler.parent();
			submenu.removeClass(opt.showClass);
			submenu.addClass(opt.hideClass);
			togglerParent.addClass(opt.closedClass);
			togglerParent.removeClass(opt.openClass);
			if (opt.onClose) {
				opt.onClose(toggler, submenu);
			}
		};
		var toggleWood = this.toggleWood = function(toggler) {
			var submenu = getSubMenu(toggler);
			var togglerParent = toggler.parent();
			submenu.toggleClass(opt.hideClass);
			if (submenu.hasClass(opt.hideClass)) {
				closeWood(toggler);
			}
			else {
				openWood(toggler);
			}
		};
		var openAllWoodFromRootTo = this.openWood = function(toggler) {
			$(toggler).parentsUntil(WoodRoot).children('.'+opt.menuToggler).each(function(index, togg){
				openWood($(togg));
			});
		};

		if (opt.showTools.toString()=='true') {
			var toolbar = $('<p class="'+opt.toolClass+'"><span class="'+opt.hideClass+'">'+opt.toolTextIntro+'</span> <a href="#" rel="expand" title="'+opt.toolexpandAllLabelTitle+'">'+opt.expandAllLabel+'</a> <a href="#" rel="collapse" title="'+opt.toolcollapseLabelTitle+'">'+opt.collapseAllLabel+'</a></p>');
			$(el).before(toolbar);
			$(toolbar).on('click', 'a[rel="expand"]', function(ev) {
				ev.preventDefault();
				expandAll();
			});

			$(toolbar).on('click', 'a[rel="collapse"]', function(ev) {
				ev.preventDefault();
				collapseAll();
			});

		}
		$(el).on('click', "."+opt.menuToggler, function(ev) {
			var toggler = $(ev.target);
			toggleWood(toggler);
		});
		if (opt.onStart) {
			opt.onStart.apply(this);
		}

		if (opt.startIndex!==undefined) {
			openAllWoodFromRootTo($('#'+opt.startIndex, this.WoodRoot));
		}
		return this;
	};

	$.fn.EntandoWoodMenu = function (userreqopt) {
		var options = $.extend({},defaultOptions, userreqopt);
		var woodDone = [];
		this.each(function () {
			var $this = $(this);
			var data = $this.data(sign);
			if (!data) {
				data = new Wood(this, options);
				$this.data(sign, data);
			}
			woodDone.push(data);
		});
		return woodDone;
	};

	$.fn.EntandoWoodMenu.Constructor = Wood;
}(window.jQuery);
//wood end