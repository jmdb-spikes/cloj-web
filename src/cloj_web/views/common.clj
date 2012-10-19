(ns cloj-web.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "cloj-web"]
               (include-css "/css/site.css")]
              [:body
               [:div#wrapper
                content]]))
