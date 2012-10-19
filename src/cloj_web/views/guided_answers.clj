(ns cloj-web.views.guided-answers
  (:require [cloj-web.views.common :as common])
  (:use noir.core
        [noir.response :only [redirect]]
        hiccup.core
        hiccup.page
        hiccup.form
        hiccup.util))

(defn text-box [id label-text data]
  [:p.group
    (label id label-text)
    (text-field id data)])


(defpartial question-1 [{:keys [firstname lastname]}]
  [:fieldset#question-1
   [:legend "Your name"]
   (text-box "firstname", "First name: ", firstname)
   (text-box "lastname", "Last name: ", lastname)])

(defpartial question-2 [{:keys [age]}]
  [:fieldset#question-1
   [:legend "Your age"]
   (text-box "age", "Age: ", age)])



(defpage [:get "/guided-answer-demo"] {:as applicationForm}
  (common/layout
   [:h1 "Please fill out this form:"]
   [:div.guide
    (form-to [:post "/guided-answer-demo" {:class "form-top"}]
             (question-1 applicationForm)
             (question-2 applicationForm)
             (submit-button "Next->"))]))

(defpage [:post "/guided-answer-demo"] {:as applicationForm}
  (redirect (url "/guided-answer-demo" applicationForm)))

