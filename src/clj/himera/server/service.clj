; Copyright (c) 2012 Fogus and Relevance Inc. All rights reserved.  The
; use and distribution terms for this software are covered by the Eclipse
; Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
; which can be found in the file COPYING the root of this
; distribution.  By using this software in any fashion, you are
; agreeing to be bound by the terms of this license.  You must not
; remove this notice, or any other, from this software.

(ns himera.server.service
  (:use compojure.core)
  (:use ring.middleware.clj-params)
  (:require [clojure.string :as string])
  (:require [himera.server.cljs :as cljs]
            [compojure.route :as route]
            [ring.util.response :as resp]))

;; (str "{\"js\" : " (string/trim-newline (if-let [d (:js data)] (pr-str d) "null")) "}")

(defn generate-response [data & [status]]
  (let [ret-val (pr-str {:js (string/trim-newline (:js data))})]
    (println ret-val)
    {:status (or status 200)
     :headers {"Content-Type" "application/clojure; charset=utf-8"}
     :body ret-val}))

(defroutes handler
  (GET "/" [] (resp/redirect "/index.html"))

  (PUT "/" [name]
       (generate-response {:hello name}))

  (POST "/compile" [expr]
        (generate-response (cljs/compilation expr :simple false)))

  (route/resources "/"))

(def app
  (-> handler
      wrap-clj-params))

