(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer :all]))

(describe "gilded rose"
  (before-all (println "Start testing"))
  
  (after-all (println "Finished testing"))
  
  (it "regular-items are true specialty-items are false"
      (should= (is-regular-item? {:sell-in 2 :quality 48 :name "Backstage passes 88"}) false)
      (should= (is-regular-item? {:sell-in 2 :quality 48 :name "Sulfuras, Hand of Ragnaros"}) false)
      (should= (is-regular-item? {:sell-in 2 :quality 48 :name "Aged Brie"}) false)
      (should= (is-regular-item? {:sell-in 2 :quality 48 :name "Elixir of the Mongoose"}) true))
   
  (it "change-quality-of-item-by number specified but between 0-50"
      (should= (:quality (change-quality-of-item-by {:name "Elixir", :sell-in 0, :quality 49} -2))
               47)
      (should= (:quality (change-quality-of-item-by {:name "Brie", :sell-in 0, :quality 49} 2))
               50)      
      (should= (:quality (change-quality-of-item-by {:name "Elixir", :sell-in 0, :quality 0} -1))
               0))
  )
(run-specs)