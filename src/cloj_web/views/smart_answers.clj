(ns cloj-web.views.smart-answers
  (:require [cloj-web.views.common :as common])
  (:use [noir.core :only [defpage]]))


(defpage "/smart-answer-demo" []
         (common/layout
          [:h1 "Please fill out this form"]
           ))
