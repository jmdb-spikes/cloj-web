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

(defn yes-no-question [name value]
  [:ul
   [:li
    [:label {:for name} "Yes"
     [:input {:type "radio" :name name :value "yes" :checked (= "yes" value)}]]]
   [:li
    [:label {:for name} "No"
     [:input {:type "radio" :name name :value "no" :checked (= "no" value)}]]]])


(defn named-submit-button [name value]
  [:input {:type "submit" :value value :name name}])

(defn section-button [questionId]
  (named-submit-button (format "active-%s" questionId) "Change Answer"))



(defpartial question-1 [{:keys [firstname lastname]} active?]
  [:fieldset#question-1
   [:legend "Your name"]
   (if active?
     (label "active" "I AM ACTIVE !!!!!!!!!"))
   (text-box "firstname", "First name: ", firstname)
   (text-box "lastname", "Last name: ", lastname)
   (section-button "1")])

(defpartial question-2 [{:keys [age]} active?]
  [:fieldset#question-2
   [:legend "Your age"]
   (if active?
     (label "active" "I AM ACTIVE !!!!!!!!!"))
   (text-box "age", "Age: ", age)
   (:input "Edit")
   (section-button "2")])




(defpartial question-3 [{:keys [likesFish]} active?]
  [:fieldset#question-3
   [:legend "Do you like fish?"]
   (if active?
     (label "active" "I AM ACTIVE !!!!!!"))
   (yes-no-question "likesFish" likesFish)
   (section-button "3")])

(defn increment-question-number
  "Increments the question number upto a maximum of number of questions"
  [currentQ numberOfQuestions]
  (min
   (+ numberOfQuestions 1)
   (+ currentQ 1)))

(defn calculate-question [applicationForm numberOfQuestions]
  (let [currentQ (:question applicationForm)]
    (if (and
         (not (= nil currentQ))
         (contains? applicationForm :next))
      (increment-question-number (Integer/parseInt currentQ) numberOfQuestions)
      (if (= nil currentQ)
        2
        currentQ))))


(defn key-starts-with [prefix mapEntry]
    (.startsWith (str (key mapEntry)) prefix))

(defn str-key [mapEntry]
  (str (key mapEntry)))

(defn keys-starting-with [prefix aMap]
  (map str-key (filter #(key-starts-with prefix %1) aMap)))

(defn question-id-from [keyName]
  (last (seq (.split keyName "-"))))

(defn question-ids-from [keys]
  (set (map question-id-from keys)))



(defn is-question-active?
  "Determines wether the formdata indicates that this question is 'active'
    i.e. it should have the key :active-<questionId> in it.
    If its not there, it might be the default so we also check that."
  [formData questionId defaultQuestion]

  (let [activeIds (question-ids-from (keys-starting-with ":active" formData))]
    (or
     (contains? activeIds questionId)
     (and
      (empty? activeIds)
      (or
       (= questionId (:question formData))
       (and
        (nil? (:question formData))
        (= questionId defaultQuestion)))))))


;; (def formData-1
;;   {:question "3"
;;    :active-2 "Change+Answer"})

;; (def formData-2
;;   {:question "2"})

;; (def formData-3
;;   {})

;; (is-question-active? formData-1 "3" "1")
;; (is-question-active? formData-2 "1" "1")
;; (is-question-active? formData-3 "1" "1")
;; (def keys (keys-starting-with ":active" formData))


(def default-question "1")

(defpage [:get "/guided-answer-demo"] {:as formData}
  (common/layout
   [:h1 "Please fill out this form:"]
   [:div.guide
    (form-to [:post "/guided-answer-demo" {:class "form-top"}]
             (label "foo" "Next question is:")
             (text-field "question", (calculate-question formData 3))
             (question-1 formData (is-question-active? formData "1" default-question))
             (question-2 formData (is-question-active? formData "2" default-question))
             (question-3 formData (is-question-active? formData "3" default-question))
             (named-submit-button "next" "Next->"))]))

(defpage [:post "/guided-answer-demo"] {:as formData}
  (redirect (url "/guided-answer-demo" formData)))

