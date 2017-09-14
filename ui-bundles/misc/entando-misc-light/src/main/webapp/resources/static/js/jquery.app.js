/* Theme Name: Zodkoo - Responsive Landing page template
   Author: Coderthemes
   Author e-mail: coderthemes@gmail.com
   Version: 1.2.0
   Created: May 2016
   File Description:Main JS file of the template
*/


!function($) {
    "use strict";


    var ContactForm = function() {
        this.$contactForm = $("#contact-form"),
        this.$errorMessages = $("#err-form"),
        this.$confirmMessage = $("#success-form")
    };
    //contact form submit handler
    ContactForm.prototype.submitForm = function(e) {
        var $this = this;
        $this.$errorMessages.fadeOut('slow'); // reset the error messages (hides them)

        var data_string = $this.$contactForm.serialize(); // Collect data from form
        $.ajax({
            type: "POST",
            url: $this.$contactForm.attr('action'),
            data: data_string,
            timeout: 6000,
            cache: false,
            crossDomain: false,
            error: function (request, error) {
                $this.$errorMessages.html('An error occurred: ' + error + '');
            },
            success: function () {
                $this.$confirmMessage.show(500).delay(4000).animate({
                    height: 'toggle'
                    }, 500, function () {
                    }
                );
            }
        });
        return false;
    },

    ContactForm.prototype.init = function () {
        var $this = this;
        //initializing the contact form
        this.$contactForm.parsley().on('form:submit', function() {
            $this.submitForm();
            return false;
        });
    },
    $.ContactForm = new ContactForm, $.ContactForm.Constructor = ContactForm

}(window.jQuery),


function($) {
    "use strict";

    var SubscribeForm = function () {
        this.$subscribeForm = $("#subscribe-form")
    };
    SubscribeForm.prototype.init = function () {
        var $this = this;
        //initializing the form using ajaxChimp
        this.$subscribeForm.ajaxChimp({});
    },
    $.SubscribeForm = new SubscribeForm, $.SubscribeForm.Constructor = SubscribeForm
}(window.jQuery),

function($) {
    "use strict";

    var Page = function() {
        this.$topSection = $('#home-fullscreen'),
        this.$topNavbar = $("#navbar-menu"),
        this.$stickyElem = $("#sticky-nav"),
        this.$backToTop = $('#back-to-top'),
        this.$contactForm = $("#contact-form"),
        this.$subscribeForm = $("#subscribe-form")
    };

    //
    Page.prototype.init = function () {
        var $this = this;
        //window related event

        //Handling load event
        $(window).on('load', function() {
            var windowHeight = $(window).height();
            // adding height attr to top section
            $this.$topSection.css('height', windowHeight);

            //init sticky
            $this.$stickyElem.sticky({topSpacing: 0});
        });

        //Handling the resize event
        $(window).on('resize', function() {
            var windowHeight = $(window).height();
            $this.$topSection.css('height', windowHeight);
        });

        //Handling the scroll event
        $(window).scroll(function(){
            if ($(this).scrollTop() > 100) {
                $this.$backToTop.fadeIn();
            } else {
                $this.$backToTop.fadeOut();
            }
        }); 

        //on click on navbar - Smooth Scroll To Anchor (requires jQuery Easing plugin)
        this.$topNavbar.on('click', function(event) {
            var $anchor = $(event.target);
            if ($($anchor.attr('href')).length > 0 && $anchor.is('a.nav-link')) {
                $('html, body').stop().animate({
                    scrollTop: $($anchor.attr('href')).offset().top - 0
                }, 1500, 'easeInOutExpo');
                event.preventDefault();    
            }
        });

        //back-to-top button
        $this.$backToTop.click(function(){
            $("html, body").animate({ scrollTop: 0 }, 1000);
            return false;
        });


        //init contact app if contact form added in a page
        if(this.$contactForm.length>0)
            $.ContactForm.init();

        //init subscribe app if form is added in a page
        if(this.$subscribeForm.length>0)
            $.SubscribeForm.init();
    },
    //init
    $.Page = new Page, $.Page.Constructor = Page
}(window.jQuery),

//initializing
function($) {
    "use strict";
    $.Page.init()
}(window.jQuery);