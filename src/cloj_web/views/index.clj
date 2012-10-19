(ns cloj-web.views.index
  (:require [cloj-web.views.common :as common])
  (:use [noir.core :only [defpage]]))

(defpage "/" []
         (common/layout
          [:h1 "Welcome to a sweet clojure application"]
          [:ul
           [:li "Click to see a \"" [:a {:href "/guided-answer-demo"} "Smart Answer"] "\" form."]]
          ))


