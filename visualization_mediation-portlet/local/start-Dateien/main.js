AUI().ready("liferay-hudcrumbs",function(b){if(Liferay.Browser.isIe()&&Liferay.Browser.getMajorVersion()<7){var a=b.one("#navigation > ul");if(a){a.delegate("mouseenter",function(d){d.currentTarget.addClass("hover")},"> li");a.delegate("mouseleave",function(d){d.currentTarget.removeClass("hover")},"> li")}}var c=b.one(".site-breadcrumbs");if(c){c.plug(b.Hudcrumbs)}});