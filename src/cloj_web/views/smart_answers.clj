(ns cloj-web.views.smart-answers
  (:require [cloj-web.views.common :as common])
  (:use noir.core
        hiccup.core
        hiccup.page
        hiccup.form))


(defpartial about-you-section [{:keys [firstname lastname]}]
  [:fieldset#about-you
   [:legend "About you"]
   [:p.group
    (label "firstname" "First name: ")
    (text-field "firstname" firstname)]
   [:p.group
    (label "lastname" "Last name: ")
    (text-field "lastname" lastname)]])

(defpage "/smart-answer-demo" []
  (common/layout
   [:h1 "Please fill out this form:"]
   [:div.guide
    (form-to [:post "/smart-answer-demo" {:class "form-top"}]
             (about-you-section {})
             (submit-button "Next->"))]))
