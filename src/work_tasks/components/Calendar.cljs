(ns work-tasks.components.Calendar
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.scripts.taskHelpers :as taskHelpers]
            ["moment" :as moment]))

; TODO DRY this up a bit also need to test on a month in the double digits to make sure it doesn't break
; (defn open-day-view [date app-state]
;   (if (and (not (= (first date) "0")) (= (second date) "/") )
;     (do
;       (swap! app-state conj {:activeDate (str "0" date)})
;       (view_handler/change-view {:day "active"}))
;     (do
;       (swap! app-state conj {:activeDate date})
;       (view_handler/change-view {:day "active"}))))


(defn get-visible-dates [projects]
  "creates a list of discrete dates to highlit on the calendar"
      (let [returnDates (atom [])]
        (doseq [date [projects]]
          (let [currentKey  (first (keys (js->clj date )))
                mappedValue (into (js->clj date ) {})
                dates (keys (get mappedValue currentKey))]
                  (loop [i 0
                    formattedDates []]
                      (if (= i (count dates))
                        formattedDates
                        (do
                          (swap! returnDates conj (str ; TODO there's probably a better way to do this - Just for now ;)
                             (first (nth dates i))(second (nth dates i))
                             "/"
                             (nth (nth dates i) 2)(nth (nth dates i) 3)
                             "/"
                             (nth (nth dates i) 4)
                             (nth (nth dates i) 5)
                             (nth (nth dates i) 6)
                             (nth (nth dates i) 7)
                           ))
                          (recur (inc i) (conj formattedDates (nth dates i))))))))
      @returnDates))


(defn get-current-month-days [currentMonth]
  (.daysInMonth (moment currentMonth "MM") "YYYY-MM"))

(defn get-day-display [offsetAmount numberOfDays currentCount]
  "Determines what to display for a particular table cell"
  (if (<= currentCount offsetAmount)
    ""
    (if (> currentCount (+ offsetAmount numberOfDays))
    ""
    (- currentCount offsetAmount))))

(defn generate-day-count [dayNumber]
  "adds a 0 if needed to a date for comparision stuff"
  (if ( < dayNumber 10)
    (str "0" dayNumber)
    dayNumber))


(defn generate-date-classes [date currentDate tasks]
  "generates the classe ford - active with dates and selected date"
  (let [hasTasks? (not= 0 (count (taskHelpers/get-tasks-due-on-date date tasks)))
        isSelected? (= @currentDate date)]
    (cond
      (and hasTasks? isSelected?) "active selected"
      hasTasks? "active"
      isSelected? "selected"
      :else "")))

(defn generate-table-row [offsetAmount numberOfDays i currentMonth currentYear tasks currentDate]
  "Generates the table HTML"
  (loop [x 1
         row [:tr]]
        (if (= x 8)
          row
          (let [formattedDateString (str currentMonth "/" (generate-day-count (- (+ i x) offsetAmount)) "/" currentYear)]
          (recur (inc x) (conj row [:td {:class (generate-date-classes formattedDateString currentDate tasks)
                                         :on-click #(reset! currentDate formattedDateString)} (get-day-display offsetAmount numberOfDays (+ i x))]))))))

(defn generate-table-html [numberOfDays currentMonth currentYear tasks currentDate]
  (let [offsetAmount (.day (.startOf (moment (str currentMonth "/" currentYear) "MM/YYYY") "month"))
        loopTotal (+ offsetAmount numberOfDays)]
    (loop [i 0
          html [:tbody]]
          (if (>= i loopTotal)
            html ; Our end condition and output
          (recur (+ i 7) (conj html (generate-table-row offsetAmount numberOfDays i currentMonth currentYear tasks currentDate)))))))

(defn increment-year [currentYear]
  (inc (js/parseInt currentYear)))

(defn deincrement-year [currentYear]
  (- (js/parseInt currentYear) 1))

(defn increment-month [current currentYear monthDays]
  (if (= current 12)
    (do
      (reset! monthDays (get-current-month-days 01))
      (swap! currentYear (fn [current] (increment-year current)))
      "01") ; We need a string here or it will strip the '0' and cause the date to not show
    (do
      (reset! monthDays (get-current-month-days (inc (js/parseInt current))))
      (if (< current 9)
        (str "0" (inc (js/parseInt current)))
        (inc (js/parseInt current))))))

(defn deincrement-month [current currentYear monthDays]
  (if (= current "01")
    (do
      (reset! monthDays (get-current-month-days 12))
      (swap! currentYear (fn [current] (deincrement-year current)))
      12)
    (do
      (reset! monthDays (get-current-month-days (- (js/parseInt current) 01)))
      (if (<= current 10)
      (str "0" (- (js/parseInt current) 1))
      (- (js/parseInt current) 1)))))


(defn render [tasks currentDate]
  (let [currentMonth (atom (.format (moment) "MM"))
        currentYear (atom (.format (moment) "YYYY"))
        monthDays (atom (get-current-month-days @currentMonth))]
    (fn [tasks]
      [:div.Calendar-widget
        [:div.Calendar-body
          [:div
            [:div.Calendar-Header
              [:p.Calendar-arrow {:on-click #(swap! currentMonth (fn [current currentYear] (deincrement-month current currentYear monthDays)) currentYear monthDays)} "<"]
              [:p.Calendar-Title (str (.format (moment @currentMonth "MM") "MMMM") " " @currentYear)]
              [:p.Calendar-arrow {:on-click #(swap! currentMonth (fn [current currentYear] (increment-month current currentYear monthDays)) currentYear monthDays)} ">"]]
            [:table.Calendar-wrapper
              [:thead
                [:tr
                  [:th "Sun"]
                  [:th "Mon"]
                  [:th "Tue"]
                  [:th "Wed"]
                  [:th "Thur"]
                  [:th "Fri"]
                  [:th "Sat"]]]
                (generate-table-html @monthDays @currentMonth @currentYear tasks currentDate)]]]])))
