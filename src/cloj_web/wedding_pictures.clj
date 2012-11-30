(ns cloj-web.wedding_pictures
  (:require [net.cgrand.enlive-html :as html]
            [clojure.set :as set]
            clojure.java.io
            [clojure.string :as string]))



(def jim-selected [5,7,12,13,15,17,19,22,23,24,28,30,33,35,38,39,44,49,50,54,55,56,59,61,62,63,64,67,68,69,71,74,76,77,78,79,80,81,82,85,91,93,95,97,98,99,101,106,107,108,110,112,113,114,116,118,120,122,125,127,130,131,132,135,137,138,139,140,142,143,144,145,146,147,151,153,154,155,156,157,159,160,162,,164,166,174,176,177,180,182,184,186,187,188,189,192,195,196,198,201,206,209,211,214,215,217,220,222,224,225,226,227,229,230,231,232,233,234,235,237,238,239,240,241,242,243,244,246,247,250,253,254,255,258,259,261,262,263,264,266,268,269,270,271,272,273,274,276,278,279,280,282,284,285,287,291,292,293,294,295,296,299,300,301,303,304,305,307,308,311,312,314,316,317,319,324,327,331,333,335,337,339,341,344,346,349,350,351,353,354,355,356,357,358,359,360,361,363,366,367,368,370,372,373,375])

(def romina-selected [8, 12,16,54,56,57,59,63,66,79,97,98,106,110,112,113,114,115,117,118,123,125,126,127,128,129,130,131,132,136,138,140,142,151,152,156,157,159,166,189,191,192,198,206,214,215,217,218,220,222,225,233,234,237,238,244,253,254,271,276,300,311,313,315,316,321,328,340,315,316,321,338,340,343,357,358,362,363,366,367,368,373,375])

(def jim-and-romina (set/intersection (set jim-selected) (set romina-selected)))


(defn img-url [index]
  (format "https://www.dropbox.com/sh/ok8x0rvwu0i3eox/hu1HVNed_w#f:jr%03d.jpg" index))


(defn fetch-url [url]
  (html/html-resource (io/as-url url)))

(defn extract-list-of-imgs [resource]
  (let [resource resource]
    (html/select resource [:div#gallery-view-container :img])))

(defn get-img-url [link-tag]
  (:data-src (:attrs link-tag)))


(defn get-image-id-from-url [img-url]
  (-> (re-find #".*(jr)(\d\d\d)(\.jpg).*" img-url)       
      (get 2)
      Integer/parseInt))

(defn get-list-of-img-urls [resource]
  (let [list-of-img-urls (map get-img-url (extract-list-of-imgs resource))]
    [(map get-image-id-from-url list-of-img-urls)
     list-of-img-urls]))

(defn make-map [two-arrays]
  (apply hash-map (flatten (map vector (first two-arrays) (second two-arrays)))))

(defn list-item [index map-of-img-urls]
  (format "<li><img src='%s' width='100px' /></li>" (get map-of-img-urls index)))

(defn process-img-map [selected map-of-img-urls]
  (map #(list-item % map-of-img-urls) (sort (seq selected))))

(defn list-of-images [resource selected]
  (let [map-of-img-urls (make-map (get-list-of-img-urls resource))]
    (process-img-map selected map-of-img-urls)))


(defn web-page [resource selected]
  (format "<html><body><ul>%s</ul></body></html>"  (string/join (list-of-images resource selected))))

(defn replace-home-dir [path]
  (string/replace path "~" (System/getProperty "user.home")))

(defn expand-path [path]
  (-> (io/file (replace-home-dir path)) .getAbsolutePath))

(defn create-page [content]
  (let [filename (io/file (expand-path "~/Desktop") "selected-images.html")]
    ;;(io/delete-file filename :silent true)
    (with-open [wrtr (io/writer filename :append false)]
      (.write wrtr content))))

(def *gallery-url* "https://www.dropbox.com/sh/ok8x0rvwu0i3eox/hu1HVNed_w#/")
;; (create-page (web-page (fetch-url *gallery-url*) jim-and-romina))




;; (defn extract-raw-img-from [resource item-url]
;;   (:data-src (:attrs (first (html/select resource [:div#gallery-view-container (html/attr= :href item-url) :img])))))

;;(:data-src (:attrs (first (:content link-tag))))
;;(def link-tag (first (html/select resource [:div#gallery-view-container :img])))
;;(extract-raw-img-from "https://www.dropbox.com/sh/ok8x0rvwu0i3eox/hu1HVNed_w#/" "https://www.dropbox.com/sh/ok8x0rvwu0i3eox/IdapoWpzSs/jr375.jpg")
;;"https://photos-6.dropbox.com/t/0/AABkkQtYaANLULGxDMwZ9mXcb3F-x23ZnVFm5ZdR1tyCQw/10/35623490/jpeg/178x178/1/1354222800/0/2/jr001.jpg/-6Q8N7JsYumGhHHN-CRNeDK0LuML_R9s1DQypLyuRnQ"

;;(def img-url "https://photos-6.dropbox.com/t/0/AABkkQtYaANLULGxDMwZ9mXcb3F-x23ZnVFm5ZdR1tyCQw/10/35623490/jpeg/178x178/1/1354222800/0/2/jr001.jpg/-6Q8N7JsYumGhHHN-CRNeDK0LuML_R9s1DQypLyuRnQ")
;;(def matcher (re-matcher #".*(jr)(\d\d\d)(\.jpg).*" img-url))
;;(re-find matcher)
;;(re-groups matcher)

;;(def resource (fetch-url "https://www.dropbox.com/sh/ok8x0rvwu0i3eox/hu1HVNed_w#/"))

