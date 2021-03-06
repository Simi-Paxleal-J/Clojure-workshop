(ns packt-clj.chapter-6-tests.activity-6-01
  (:require [clojure.string :as string]))


(defn attributes [m]
  (clojure.string/join " "
                       (map (fn [[k v]]
                              (if (string? v)
                                (str (name k) "=\"" v "\"")
                                (name k)))
                            m)))

(defn keyword->opening-tag [kw]
  (str "<" (name kw) ">"))

(defn keyword-attributes->opening-tag [kw attrs]
  (str "<" (name kw) " " (attributes attrs) ">"))

(defn keyword->closing-tag [kw]
  (str "</" (name kw) ">"))

(defn has-attributes? [v]
  (map? (second v)))

(defn singleton? [tree]
  (and (vector? tree)
       (#{:img :meta :link :input :br} (first tree))))

(defn singleton-with-attrs? [tree]
  (and (singleton? tree) (has-attributes? tree)))

(defn element-with-attrs? [tree]
  (and (vector? tree) (has-attributes? tree)))

(defn my-hiccup [tree]
  (cond
    (not tree) tree
    (string? tree) tree

    (singleton-with-attrs? tree)
    (keyword-attributes->opening-tag (first tree) (second tree))

    (singleton? tree)
    (keyword->opening-tag (first tree))
    
    (element-with-attrs? tree)
    (apply str
           (concat
             [(keyword-attributes->opening-tag (first tree) (second tree))]
             (map my-hiccup (next (next tree)))
             [(keyword->closing-tag (first tree))]))
    (vector? tree)
    (apply str
           (concat
             [(keyword->opening-tag (first tree))]
             (map my-hiccup (next tree))
             [(keyword->closing-tag (first tree))]))))


