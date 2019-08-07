(ns work-tasks.components.datepicker
  (:require [reagent.core :as reagent :refer [atom]]
            ["moment" :as moment]))

; TODO we can probably use moment to get this and then offer the option for
; March or Mar - something like that
(defonce months ["January" "February" "March" "April" "May"
                 "June" "July" "August" "September" "October"
                 "November" "December"])
; TODO there's a better way to do this but im lazy

(defonce monthMap {:January 1 :February 2 :March 3 :April 4 :May 5
                 :June 6 :July 7 :August 8 :September 9 :October 10
                 :November 11 :December 12})

(defonce defaultStyles {
  :position "fixed"
  :width "100%"
  :height "100%"
  :text-align "center"
  :background-color "rgba(255,255,255,.95)"
  :display "flex"
  :flex-direction "column"
  :justify-content "center"
  :top 0
  :left "-2000px"
  :z-index -999})


(defn generate-date-string [dateMapToFormat]
  "Generates a string that can then be passed to update-passed-store to get the val out"
    (let [test (str ((keyword (:month dateMapToFormat)) monthMap) "/" (:date dateMapToFormat) "/" (:year dateMapToFormat))]
      (.format (moment test) "MM/DD/YYYY")))

(defn update-passed-store [val store]
  "Updates the values of the passed in atom expected {:day :month :year} map"
  (reset! store (generate-date-string val)))

(defn generate-css [styles]
  "simple helper to add the stles to the domElement"
  (str "style=\""(clojure.string/join " " (map (fn [[key val]] (str (name key) ": " val)) styles))"\""))

(defn generate-months []
  (map (fn [month]
           [:option {:value month :key month} month]) months))

(defn generate-years []
  (map (fn [year]
           [:option {:value year :key year} year]) (range (.year (moment)) (+ 50 (.year (moment))))))

(defn generate-days [date]
  (map (fn [day]
           [:option {:value day :key day} day]) (range 1
                                                  (+ 1 (.daysInMonth (moment (str (:year @date) "-" (:month @date)) "YYYY-MMMM"))))))
(defn update-day [val store date]
  (update-passed-store (conj @date {:date val}) store)
  (swap! date conj {:day val}))

(defn update-month [val store date]
  (update-passed-store (conj @date {:month val}) store)
  (swap! date conj {:month val}))

(defn update-year [val store date]
  (update-passed-store (conj @date {:year val}) store)
  (swap! date conj {:year val}))


(defn open-datepicker [randomId]
  (let [bodyElem (.getElementById js/document (str "datepicker-" randomId))]
    (aset  (.-style bodyElem) "z-index" "900")
    (aset  (.-style bodyElem) "left" "0")))

(defn close-datepicker [randomId]
  (let [bodyElem (.getElementById js/document (str "datepicker-" randomId))]
    (aset  (.-style bodyElem) "z-index" "-999")
    (aset  (.-style bodyElem) "left" "-2000px")))

(defn generate-html [store date randomId]
  [:div {:style defaultStyles :id (str "datepicker-" randomId)}
    [:div#datepicker.datepickerinner
      [:select {:default-value (:month @date) :on-change #(update-month (-> % .-target .-value) store date)
                :style {:webkit-appearance "none" :padding "8px" :text-align "center" :margin "0 8px"}}
        (generate-months)]
      [:select {:default-value (:date @date) :on-change #(update-day (-> % .-target .-value) store date)
               :style {:webkit-appearance "none" :padding "8px" :text-align "center" :margin "0 8px"}}
        (generate-days date)]
      [:select {:default-value (:year @date) :on-change #(update-year (-> % .-target .-value) store date)
                :style {:webkit-appearance "none" :padding "8px" :text-align "center" :margin "0 8px"}}
        (generate-years)]
      [:div.buttonWrapper {:style {:width "100%" :margin-top "32px"}}
        [:button.primary  {:on-click #(close-datepicker randomId)} "Update"]]]])

(defn datepicker [store]
  "responsible for rending the datepicker to the screen"
  (let [vals (:end @store)
        currentDate (moment)
        date (atom {:month (.format currentDate "MMMM") :date (.format currentDate "D") :year (.format currentDate "YYYY")}) ; TODO need to automatically st this to todays date
        randomId (rand-int 100000)] ;TODO we might want to roll all this into a single atom
    (fn []
      [:div
        [:input {:type "text"
                 :readOnly true
                 :value (str @store)
                 :on-click #(open-datepicker randomId)}]
        (generate-html store date randomId)])))