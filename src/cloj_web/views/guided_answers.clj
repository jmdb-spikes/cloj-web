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

(defn section-button [questionId]
  [:input {:type "submit" :value "Change Answer" :name (format "active-%s" questionId)}])


(defpartial question-1 [{:keys [firstname lastname]} active?]
  [:fieldset#question-1
   [:legend "Your name"]
   (if active?
     (label "active" "I AM ACTIVE !!!!!!!!!"))
   (text-box "firstname", "First name: ", firstname)
   (text-box "lastname", "Last name: ", lastname)
   (section-button "1")])

(defpartial question-2 [{:keys [age]} active?]
  [:fieldset#question-1
   [:legend "Your age"]
   (if active?
     (label "active" "I AM ACTIVE !!!!!!!!!"))
   (text-box "age", "Age: ", age)
   (:input "Edit")
   (section-button "2")])


(defn calculate-question [applicationForm]
  (let [currentQ (:question applicationForm)]
       (if (= nil currentQ)
         1
         (+ (Integer/parseInt currentQ) 1))))

(def demoForm
  {:question "1"
   :active-1 "Change+Answer"})

(defn is-active [formData]
  )


(defn key-starts-with [prefix mapEntry]
    (.startsWith (str (key mapEntry)) prefix))

(defn str-key [mapEntry]
  (str (key mapEntry)))


(defn active-keys [aMap]
  (map str-key (filter #(key-starts-with ":active" %1) demoForm)))

(defn question-id-from [keyName]
  (last (seq (.split keyName "-"))))



(map question-id-from (active-keys demoForm))





(seq (.split "a-1" "-"))





(defpage [:get "/guided-answer-demo"] {:as formData}
  (common/layout
   [:h1 "Please fill out this form:"]
   [:div.guide
    (form-to [:post "/guided-answer-demo" {:class "form-top"}]
             (text-field "question", (calculate-question formData))
             (question-1 formData (is-active? formData "1"))
             (question-2 formData (is-active? formData "2"))
             (submit-button "Next->"))]))

(defpage [:post "/guided-answer-demo"] {:as formData}
  (redirect (url "/guided-answer-demo" formData)))

