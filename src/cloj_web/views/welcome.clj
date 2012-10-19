(ns cloj-web.views.welcome
  (:require [cloj-web.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to cloj-web"]))
