(ns gilded-rose.core)

(defn is-regular-item? [item]  ;;is this a regular/non-specialty item?
  (if (or (= "Aged Brie" (:name item))
          (some? (re-matches #"(?i)Sulfuras, Hand of Ragnaros" (:name item)))
          (some? (re-matches #"(?i)Backstage passes.*" (:name item)))
          (some? (re-matches #".*Conjured.*" (:name item)))) 
    false
    true))

(defn backstage-pass? [item]  ;;there could be many different Backstage pass concerts. More regex rules would improve it
  (if (some? (re-matches #"(?i)Backstage passes.*" (:name item)))
    true
    false))

(defn backstage [item] ;;backstage passes have a lot of rules so it's better to isolate it
  (if (backstage-pass? item)
    (cond 
      (< (:sell-in item) 0) (change-quality-of-item-by item (- (:quality item)))
      (<= 0 (:sell-in item) 5) (change-quality-of-item-by item 3)
      (<= 6 (:sell-in item) 10) (change-quality-of-item-by item 2)
      (> (:sell-in item) 10) (change-quality-of-item-by item 1)
      :else item) 
    (throw (Exception. "Not a backstage item"))))

(defn lower-sell-in [item] ;added this to make code more readable
  (if (nil? (re-matches #"(?i)Sulfuras, Hand of Ragnaros" (:name item)));(not= "Sulfuras, Hand of Ragnaros" (:name item))
    (merge item {:sell-in (dec (:sell-in item))})
    item))

(defn change-quality-of-item-by [item quality-delta] ;;this assures quality of items is within 0-50 range 
  (if (some? (re-matches #"(?i)Sulfuras, Hand of Ragnaros" (:name item)))
    (throw (Exception. "Trying to change quality of 'Sulfuras, Hand of Ragnaros'"))    
    (cond
      (<= (+ (:quality item) quality-delta) 0) (merge item {:quality 0})
      (>= (+ (:quality item) quality-delta) 50) (merge item {:quality 50})
      :else (merge item {:quality (+ (:quality item) quality-delta)}))))


(defn update-quality [items] ;;updated function because that was nuts and *nearly impossible to read
  (map
    (fn[item] 
      (cond
        (is-regular-item? item) (change-quality-of-item-by item -1);if conjured item is *anything* update (change-quality-of-item-by item -2)
        (= "Aged Brie" (:name item)) (change-quality-of-item-by item 1)
        (backstage-pass? item) (backstage item)
        (some? (re-matches #".*Conjured.*" (:name item))) (change-quality-of-item-by item -2);Conjured item?
        :else item))
      (map lower-sell-in
          items)))

   
;;didn't change the goblin's code ;-P
    (defn item [item-name, sell-in, quality]
      {:name item-name, :sell-in sell-in, :quality quality})

    (defn update-current-inventory[]
      (let [inventory [ (item "+5 Dexterity Vest" 10 20)
                        (item "Aged Brie" 2 0)
                        (item "Elixir of the Mongoose" 5 7)
                        (item "Sulfuras, Hand Of Ragnaros" 0 80)
                        (item "Backstage passes to a TAFKAL80ETC concert" 15 20)]]
        (println inventory)
    
        (println (update-quality inventory))))
